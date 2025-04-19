package com.example.travalms.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.db.ChatDatabase  // 取消注释
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.ChatSession
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.repository.MessageRepository  // 取消注释
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

/**
 * 聊天ViewModel，管理聊天消息和会话
 */
class ChatViewModel(application: Application) : AndroidViewModel(application) {
    
    private val TAG = "ChatViewModel"
    private val xmppManager = XMPPManager.getInstance()
    
    // 使用Room数据库
    private val database = ChatDatabase.getDatabase(application)
    private val messageDao = database.messageDao()
    private val messageRepository = MessageRepository(messageDao, xmppManager)
    
    // 存储最近的会话列表
    private val _recentSessions = MutableStateFlow<List<ChatSession>>(emptyList())
    val recentSessions: StateFlow<List<ChatSession>> = _recentSessions.asStateFlow()

    // 存储联系人状态映射
    private val _contactsStatusMap = MutableStateFlow<Map<String, String>>(emptyMap())
    val contactsStatusMap: StateFlow<Map<String, String>> = _contactsStatusMap.asStateFlow()

    // 保存消息屏幕当前选中的选项卡
    private val _selectedMessageTab = MutableStateFlow(0) // 默认选中"全部消息"选项卡
    val selectedMessageTab: StateFlow<Int> = _selectedMessageTab.asStateFlow()

    // 添加用于更新消息联系人状态的方法
    fun updateContactsStatus(updatedContacts: List<com.example.travalms.data.model.ContactItem>) {
        // 提取JID和状态映射
        val statusMap = updatedContacts
            .filter { it.jid != null }
            .associate { contact ->
                contact.jid.toString() to contact.status 
            }
        
        // 更新状态映射
        if (statusMap.isNotEmpty()) {
            _contactsStatusMap.update { currentMap ->
                currentMap.toMutableMap().apply {
                    putAll(statusMap)
                }
            }
            Log.d(TAG, "更新了 ${statusMap.size} 个联系人的状态")
        }
    }

    // 初始化，开始监听新消息和加载会话
    init {
        // 订阅消息流
        viewModelScope.launch {
            try {
                xmppManager.messageFlow.collect { message ->
                    processNewMessage(message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "监听消息流出错", e)
            }
        }
        
        // 订阅 Presence 更新流
        viewModelScope.launch {
            try {
                XMPPManager.getInstance().presenceUpdateFlow.collect { (jidString, status) ->
                    Log.d(TAG, "收到 Presence 更新: $jidString -> $status")
                    updateSingleContactStatus(jidString, status)
                }
            } catch (e: Exception) {
                Log.e(TAG, "监听 Presence 更新流出错", e)
            }
        }
        
        // 如果已经登录，加载会话列表
        if (xmppManager.currentConnection?.isAuthenticated == true) {
            updateRecentSessions()
        }
    }

    /**
     * 处理新接收到的消息
     */
    private fun processNewMessage(message: ChatMessage) {
        Log.d(TAG, "处理新消息: ${message.content} 从 ${message.senderId} 到 ${message.recipientId}")
        
        // 确定会话ID（根据发送者或接收者）
        val currentUserJid = getCurrentUserJid()
        val sessionId = determineSessionId(message, currentUserJid)
        
        if (sessionId.isEmpty()) {
            Log.w(TAG, "无法确定会话ID，跳过处理消息")
            return
        }
        
        // 保存消息到数据库
        viewModelScope.launch {
            try {
                messageRepository.saveMessage(message, sessionId)
                // 更新最近会话列表
                updateRecentSessions()
            } catch (e: Exception) {
                Log.e(TAG, "保存消息到数据库失败", e)
            }
        }
    }
    
    /**
     * 确定消息的会话ID
     */
    private fun determineSessionId(message: ChatMessage, currentUserJid: String): String {
        return if (message.senderId == currentUserJid) {
            // 自己发送的消息，使用接收者作为会话ID
            message.recipientId ?: ""
        } else {
            // 收到的消息，使用发送者作为会话ID
            message.senderId
        }
    }
    
    /**
     * 更新最近会话列表
     */
    private fun updateRecentSessions() {
        viewModelScope.launch {
            try {
                val currentUserJid = getCurrentUserJid()
                if (currentUserJid.isNotEmpty()) {
                    val sessions = messageRepository.getAllSessions(currentUserJid)
                    _recentSessions.update { sessions }
                    Log.d(TAG, "已更新 ${sessions.size} 个最近会话")
                }
            } catch (e: Exception) {
                Log.e(TAG, "更新会话列表失败", e)
            }
        }
    }
    
    /**
     * 获取指定会话的所有消息
     */
    suspend fun getMessagesForSession(sessionId: String): List<ChatMessage> {
        return try {
            messageRepository.getMessagesForSessionSync(sessionId)
        } catch (e: Exception) {
            Log.e(TAG, "获取会话消息失败: $sessionId", e)
            emptyList()
        }
    }
    
    /**
     * 将会话标记为已读
     */
    fun markSessionAsRead(sessionId: String) {
        viewModelScope.launch {
            try {
                messageRepository.markSessionAsRead(sessionId, sessionId)
                // 更新会话列表
                updateRecentSessions()
            } catch (e: Exception) {
                Log.e(TAG, "标记会话已读失败", e)
            }
        }
    }
    
    /**
     * 获取当前用户的JID
     */
    private fun getCurrentUserJid(): String {
        return xmppManager.currentConnection?.user?.asEntityBareJidString() ?: ""
    }
    
    /**
     * 根据JID获取发送者名称
     */
    private fun getSenderName(jid: String): String {
        // 这里可以添加更复杂的逻辑来获取好友名称
        // 简单实现：提取JID的username部分
        return if (jid.contains("@")) {
            jid.substringBefore("@")
        } else {
            jid
        }
    }
    
    /**
     * 从服务器加载历史消息
     */
    suspend fun loadHistoryFromServer(otherJid: String): Result<List<ChatMessage>> {
        return try {
            messageRepository.loadHistoryFromServer(otherJid)
        } catch (e: Exception) {
            Log.e(TAG, "加载服务器历史消息失败", e)
            Result.failure(e)
        }
    }
    
    /**
     * 发送新消息
     */
    fun sendMessage(recipientJid: String, content: String) {
        viewModelScope.launch {
            try {
                val result = messageRepository.sendMessage(recipientJid, content)
                if (result.isSuccess) {
                    Log.d(TAG, "消息发送成功")
                    // 消息已由MessageRepository保存到数据库
                    // 更新会话列表
                    updateRecentSessions()
                } else {
                    Log.e(TAG, "发送消息失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "发送消息异常", e)
            }
        }
    }
    
    /**
     * 清除会话历史
     */
    fun clearSessionHistory(sessionId: String) {
        viewModelScope.launch {
            try {
                messageRepository.clearSessionHistory(sessionId)
                // 更新会话列表
                updateRecentSessions()
                Log.d(TAG, "已清除会话 $sessionId 的历史记录")
            } catch (e: Exception) {
                Log.e(TAG, "清除会话历史失败", e)
            }
        }
    }

    // 更新选中的选项卡
    fun updateSelectedMessageTab(tabIndex: Int) {
        _selectedMessageTab.value = tabIndex
        Log.d(TAG, "更新选中消息标签页: $tabIndex")
    }

    // 添加用于更新单个联系人状态的方法
    fun updateSingleContactStatus(jidString: String, status: String) {
        _contactsStatusMap.update { currentMap ->
            currentMap.toMutableMap().apply {
                this[jidString] = status
            }
        }
        Log.d(TAG, "已更新单个联系人状态: $jidString -> $status")
    }
} 