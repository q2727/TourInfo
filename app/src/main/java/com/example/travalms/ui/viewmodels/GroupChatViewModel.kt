package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.model.GroupChatMessage
import com.example.travalms.data.model.GroupMember
import com.example.travalms.data.model.GroupRoom
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.repository.GroupChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import java.util.concurrent.ConcurrentHashMap

/**
 * 群聊ViewModel，负责管理群聊数据和操作
 */
@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val xmppManager: XMPPManager,
    private val groupChatRepository: GroupChatRepository
) : ViewModel() {
    private val TAG = "GroupChatViewModel"

    // 当前房间
    private val _currentRoom = MutableStateFlow<GroupRoom?>(null)
    val currentRoom: StateFlow<GroupRoom?> = _currentRoom.asStateFlow()

    // 加载状态
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    // 错误信息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 房间消息缓存 (Use the correct GroupChatMessage model)
    private val _roomMessages = MutableStateFlow<Map<String, List<GroupChatMessage>>>(emptyMap())
    // 只读的 StateFlow 用于外部观察
    val roomMessagesState: StateFlow<Map<String, List<GroupChatMessage>>> = _roomMessages.asStateFlow()

    // 房间成员缓存 (保持 ConcurrentHashMap)
    private val roomMembers = ConcurrentHashMap<String, MutableList<GroupMember>>()
    
    // 跟踪已发送的消息指纹，防止重复
    private val sentMessageFingerprints = mutableSetOf<String>()

    init {
        // 监听群聊消息 (Now also saves)
        viewModelScope.launch {
            xmppManager.groupChatManager.groupMessageFlow.collect { message ->
                processIncomingMessage(message, true)
            }
        }
    }

    /**
     * 加入聊天室
     */
    fun joinRoom(roomJid: String, nickname: String) {
        if (_currentRoom.value?.roomJid == roomJid) return

        _loading.value = true
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.joinRoom(roomJid, nickname)
                if (result.isSuccess) {
                    _currentRoom.value = result.getOrNull()
                    loadInitialMessages(roomJid)
                } else {
                    _errorMessage.value =
                        "加入房间失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "加入房间异常", e)
                _errorMessage.value = "加入房间异常: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * 加载房间成员列表
     */
    fun loadRoomMembers(roomJid: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.getRoomMembers(roomJid)
                if (result.isSuccess) {
                    val members = result.getOrDefault(emptyList())
                    roomMembers[roomJid] = members.toMutableList()
                } else {
                    Log.e(TAG, "获取房间成员失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "加载房间成员异常", e)
            }
        }
    }

    /**
     * 发送消息
     */
    fun sendMessage(roomJid: String, message: String) {
        viewModelScope.launch {
            try {
                // 获取完整的当前用户信息
                val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString()
                val currentUserName = currentUserJid?.substringBefore('@') ?: "未知用户"
                
                // 创建一个更稳定、唯一的消息ID
                val timestamp = LocalDateTime.now()
                val timeString = timestamp.toString()
                val stableId = "outgoing_${timeString}_${currentUserName}_${message.hashCode()}"
                
                // 创建消息指纹，用于后续去重
                val messageFingerprint = "${roomJid}_${currentUserName}_${message.trim()}"
                
                // 记录发送的消息指纹
                sentMessageFingerprints.add(messageFingerprint)
                
                // 清理超过100条的指纹缓存
                if (sentMessageFingerprints.size > 100) {
                    sentMessageFingerprints.clear()
                }
                
                Log.d(TAG, "开始发送消息 roomJid=$roomJid, ID=$stableId, 指纹=$messageFingerprint")
                
                // 创建临时消息用于UI显示，确保senderNickname就是当前用户名
                val tempMessage = GroupChatMessage(
                    id = stableId,
                    roomJid = roomJid,
                    roomName = _currentRoom.value?.name ?: roomJid,
                    senderJid = currentUserJid,
                    senderNickname = currentUserName,  // 确保使用真实用户名
                    content = message,
                    timestamp = timestamp,
                    isFromMe = true,  // 这个字段保留但不再依赖它
                    messageType = GroupChatMessage.MessageType.TEXT
                )
                
                // 先在UI中显示临时消息
                processIncomingMessage(tempMessage, false)  // 不保存临时消息到数据库
                
                // 发送消息到服务器
                val result = xmppManager.groupChatManager.sendGroupMessage(roomJid, message)
                
                if (!result.isSuccess) {
                    _errorMessage.value = "发送消息失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                    Log.e(TAG, "发送消息失败", result.exceptionOrNull())
                } else {
                    Log.d(TAG, "消息发送成功: $stableId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "发送消息异常", e)
                _errorMessage.value = "发送消息异常: ${e.message}"
            }
        }
    }

    /**
     * 邀请用户加入群聊
     */
    fun inviteUser(roomJid: String, userJid: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.groupChatManager.inviteUserToRoom(roomJid, userJid)
                if (result.isSuccess) {
                    _errorMessage.value = "邀请已发送"
                } else {
                    _errorMessage.value =
                        "邀请失败: ${result.exceptionOrNull()?.message ?: "未知错误"}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "邀请用户异常", e)
                _errorMessage.value = "邀请用户异常: ${e.message}"
            }
        }
    }

    /**
     * 获取特定房间的消息 Flow
     */
    fun getRoomMessagesFlow(roomJid: String): StateFlow<List<GroupChatMessage>> {
        return roomMessagesState.map { map ->
            map[roomJid] ?: emptyList()
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())
    }

    /**
     * 获取房间消息 (保留旧方法，可能用于非响应式场景，但主要使用 Flow)
     */
    fun getRoomMessages(roomJid: String): List<GroupChatMessage> {
        return _roomMessages.value[roomJid] ?: emptyList()
    }

    /**
     * 获取房间成员
     */
    fun getRoomMembers(roomJid: String): List<GroupMember> {
        return roomMembers[roomJid] ?: mutableListOf()
    }

    /**
     * Function to load initial messages
     */
    private fun loadInitialMessages(roomJid: String) {
        viewModelScope.launch {
            try {
                val roomName = _currentRoom.value?.name ?: roomJid
                val history = groupChatRepository.getMessagesForRoom(roomJid, roomName)
                Log.d(TAG, "Loaded ${history.size} historical messages for room $roomJid")
                
                if (history.isNotEmpty()) {
                    // 获取当前用户JID用于判断消息来源
                    val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString()
                    
                    // 处理每条消息，判断是否来自当前用户
                    val processedMessages = history.map { message ->
                        // 从数据库加载的消息默认isFromMe=false，需要根据当前用户JID动态判断
                        val cleanSenderJid = message.senderJid?.substringBefore("/") ?: ""
                        val cleanCurrentJid = currentUserJid?.substringBefore("/") ?: ""
                        
                        // 判断消息是否来自当前用户
                        val isFromCurrentUser = when {
                            // 明确标记为当前用户的临时消息
                            message.id.startsWith("outgoing_") -> true
                            // JID完全匹配
                            cleanSenderJid.isNotEmpty() && cleanCurrentJid.isNotEmpty() && 
                            cleanSenderJid == cleanCurrentJid -> true
                            // 用户名匹配
                            currentUserJid != null && message.senderNickname == currentUserJid.substringBefore("@") -> true
                            // 其他情况视为非当前用户消息
                            else -> false
                        }
                        
                        // 根据判断结果更新消息的isFromMe属性
                        if (isFromCurrentUser != message.isFromMe) {
                            Log.d(TAG, "修正历史消息的isFromMe属性: ${message.id}, ${message.content}, " +
                                    "原值=${message.isFromMe}, 新值=$isFromCurrentUser")
                            message.copy(isFromMe = isFromCurrentUser)
                        } else {
                            message
                        }
                    }
                    
                    _roomMessages.value = _roomMessages.value.toMutableMap().apply {
                        val currentList = getOrDefault(roomJid, emptyList()).toMutableList()
                        val existingIds = currentList.map { it.id }.toSet()
                        val newHistoryMessages = processedMessages.filter { it.id !in existingIds }
                        currentList.addAll(newHistoryMessages)
                        this[roomJid] = currentList.sortedBy { it.timestamp }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading message history for $roomJid", e)
                _errorMessage.value = "加载消息记录失败: ${e.message}"
            }
        }
    }

    /**
     * 处理收到的消息
     * @param saveToDb Whether to save this message to the database
     */
    private fun processIncomingMessage(message: GroupChatMessage, saveToDb: Boolean = false) {
        val roomJid = message.roomJid

        // 获取当前用户JID，用于核对消息是否来自当前用户
        val currentUserJid = xmppManager.currentConnection?.user?.asEntityBareJidString()
        val cleanCurrentJid = currentUserJid?.substringBefore("/") ?: ""
        val cleanSenderJid = message.senderJid?.substringBefore("/") ?: ""

        // 确保isFromMe值是正确的
        val correctedMessage = if (message.id.startsWith("outgoing_")) {
            // 临时消息一定是自己发送的
            if (!message.isFromMe) {
                Log.d(TAG, "修正临时消息的isFromMe值为true: ${message.id}")
                message.copy(isFromMe = true)
            } else {
                message
            }
        } else if (cleanCurrentJid.isNotEmpty() && cleanSenderJid.isNotEmpty()) {
            // 根据JID判断消息来源
            val isFromCurrentUser = cleanSenderJid == cleanCurrentJid
            if (isFromCurrentUser != message.isFromMe) {
                Log.d(TAG, "修正消息的isFromMe值: ${message.id}, ${message.content}, " +
                        "原值=${message.isFromMe}, 新值=$isFromCurrentUser")
                message.copy(isFromMe = isFromCurrentUser)
            } else {
                message
            }
        } else {
            message
        }
        
        Log.d(TAG, "处理消息: room=$roomJid, id=${correctedMessage.id}, 内容='${correctedMessage.content}', 来源=${if (correctedMessage.isFromMe) "自己" else "他人"}")
        
        // 创建当前消息的指纹用于检查重复
        val senderName = correctedMessage.senderJid?.substringBefore('@') ?: correctedMessage.senderNickname
        val contentFingerprint = "${roomJid}_${senderName}_${correctedMessage.content.trim()}"
        
        // 检查是否是自己刚刚发送的消息被服务器返回
        val isEchoOfSentMessage = sentMessageFingerprints.contains(contentFingerprint)
        if (isEchoOfSentMessage && !correctedMessage.id.startsWith("outgoing_")) {
            Log.d(TAG, "检测到服务器回显消息，已忽略: $contentFingerprint")
            return  // 完全跳过处理
        }

        _roomMessages.value = _roomMessages.value.toMutableMap().apply {
            val currentList = getOrPut(roomJid) { mutableListOf() }.toMutableList()
            
            // 增强的重复消息检测逻辑
            val isDuplicate = currentList.any { existingMsg -> 
                // 1. 基于ID的严格匹配
                if (existingMsg.id == correctedMessage.id) {
                    Log.d(TAG, "消息ID重复: ${correctedMessage.id}")
                    return@any true
                }
                
                // 2. 基于内容+发送者的更严格匹配
                val contentMatch = existingMsg.content.trim() == correctedMessage.content.trim()
                val senderMatch = existingMsg.senderJid == correctedMessage.senderJid || 
                                 existingMsg.senderNickname == correctedMessage.senderNickname
                
                // 检查时间是否非常接近 (5秒内)
                val timeMatch = existingMsg.timestamp.plusSeconds(5).isAfter(correctedMessage.timestamp) &&
                               existingMsg.timestamp.minusSeconds(5).isBefore(correctedMessage.timestamp)
                
                // 检查是否有一个是临时消息，另一个是服务器消息
                val oneIsTemporary = existingMsg.id.startsWith("outgoing_") != correctedMessage.id.startsWith("outgoing_")
                              
                val compositeMatch = contentMatch && senderMatch && (timeMatch || oneIsTemporary)
                
                if (compositeMatch) {
                    if (oneIsTemporary && !existingMsg.id.startsWith("outgoing_")) {
                        // 当已有服务器确认的消息，而新消息是临时消息时，保留服务器消息
                        Log.d(TAG, "已有服务器消息，忽略临时消息: ${correctedMessage.content}")
                        return@any true
                    } else if (oneIsTemporary && !correctedMessage.id.startsWith("outgoing_")) {
                        // 当已有临时消息，而新消息是服务器消息时，替换临时消息为服务器消息
                        Log.d(TAG, "替换临时消息为服务器消息: ${correctedMessage.content}")
                        currentList.remove(existingMsg)
                        return@any false  // 不视为重复，而是替换
                    } else {
                        Log.d(TAG, "消息内容、发送者和时间匹配，认为是重复: ${correctedMessage.content}")
                        return@any true
                    }
                }
                
                compositeMatch
            }
            
            if (!isDuplicate) {
                currentList.add(correctedMessage)
                this[roomJid] = currentList.sortedBy { it.timestamp }
                Log.d(TAG, "消息已添加到StateFlow, 房间=$roomJid, 当前消息数量: ${this[roomJid]?.size}")
                
                // 如果需要保存到数据库且不是临时消息
                if (saveToDb && !correctedMessage.id.startsWith("outgoing_")) {
                    viewModelScope.launch {
                        try {
                            groupChatRepository.saveGroupMessage(correctedMessage)
                            Log.d(TAG, "消息已保存到数据库: ${correctedMessage.id}")
                        } catch (e: Exception) {
                            Log.e(TAG, "保存消息到数据库失败: ${correctedMessage.id}", e)
                        }
                    }
                }
            } else {
                Log.d(TAG, "检测到重复消息，已忽略: id=${correctedMessage.id}, 内容=${correctedMessage.content}")
            }
        }
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _errorMessage.value = null
    }
}