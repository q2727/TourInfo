package com.example.travalms.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.ChatSession
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * 聊天ViewModel，管理聊天消息和会话
 */
class ChatViewModel : ViewModel() {
    
    private val TAG = "ChatViewModel"
    private val xmppManager = XMPPManager.getInstance()
    
    // 存储所有聊天消息
    private val messageStore = mutableMapOf<String, MutableList<ChatMessage>>()
    
    // 存储最近的会话列表
    private val _recentSessions = MutableStateFlow<List<ChatSession>>(emptyList())
    val recentSessions: StateFlow<List<ChatSession>> = _recentSessions.asStateFlow()

    // 初始化，开始监听新消息
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
    }

    /**
     * 处理新接收到的消息
     */
    private fun processNewMessage(message: ChatMessage) {
        Log.d(TAG, "处理新消息: ${message.content} 从 ${message.senderId} 到 ${message.recipientId}")
        
        // 确定会话ID（根据发送者或接收者）
        val currentUserJid = getCurrentUserJid()
        
        // 记录日志，方便调试
        Log.d(TAG, "当前用户JID: $currentUserJid, 消息发送者: ${message.senderId}")
        
        val sessionId = if (message.senderId == currentUserJid) {
            // 当前用户发送的消息，使用接收者作为会话ID
            message.recipientId ?: return
        } else {
            // 收到的消息，使用发送者作为会话ID
            message.senderId
        }
        
        // 获取会话的消息列表
        val sessionMessages = messageStore.getOrPut(sessionId) { mutableListOf() }
        
        // 检查消息是否已存在（去重）
        val isDuplicate = sessionMessages.any { existingMsg -> 
            existingMsg.id == message.id ||
            (existingMsg.content == message.content && 
             existingMsg.senderId == message.senderId &&
             existingMsg.timestamp.isEqual(message.timestamp))
        }
        
        if (!isDuplicate) {
            // 保存消息到对应会话的消息列表
            sessionMessages.add(message)
            Log.d(TAG, "添加消息到会话 $sessionId: ${message.id}, 发送者=${message.senderId}")
            
            // 更新最近会话列表
            updateRecentSessions()
        } else {
            Log.d(TAG, "忽略重复消息: ${message.id}")
        }
    }
    
    /**
     * 更新最近会话列表
     */
    private fun updateRecentSessions() {
        viewModelScope.launch {
            val sessions = mutableListOf<ChatSession>()
            
            messageStore.forEach { (jid, messages) ->
                if (messages.isNotEmpty()) {
                    // 获取最后一条消息
                    val lastMessage = messages.maxByOrNull { it.timestamp }
                    
                    // 获取未读消息数量
                    val unreadCount = messages.count { 
                        !it.isRead && it.senderId != getCurrentUserJid() 
                    }
                    
                    // 获取好友名称
                    val name = getSenderName(jid)
                    
                    // 创建会话
                    val session = ChatSession(
                        id = jid,
                        targetId = jid,
                        targetName = name,
                        targetType = "person", // 假设都是个人会话
                        lastMessage = lastMessage,
                        unreadCount = unreadCount
                    )
                    
                    sessions.add(session)
                }
            }
            
            // 按最后消息时间排序
            sessions.sortByDescending { it.lastMessage?.timestamp }
            
            // 更新状态流
            _recentSessions.update { sessions }
            Log.d(TAG, "已更新 ${sessions.size} 个最近会话")
        }
    }
    
    /**
     * 获取指定会话的所有消息
     */
    fun getMessagesForSession(sessionId: String): List<ChatMessage> {
        return messageStore[sessionId] ?: emptyList()
    }
    
    /**
     * 将会话标记为已读
     */
    fun markSessionAsRead(sessionId: String) {
        messageStore[sessionId]?.let { messages ->
            // 将所有接收到的消息标记为已读
            for (i in messages.indices) {
                if (messages[i].senderId == sessionId && !messages[i].isRead) {
                    messages[i] = messages[i].copy(isRead = true)
                }
            }
            // 更新会话列表
            updateRecentSessions()
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
     * 发送新消息
     */
    fun sendMessage(recipientJid: String, content: String) {
        viewModelScope.launch {
            try {
                val result = xmppManager.sendMessage(recipientJid, content)
                if (result.isSuccess) {
                    Log.d(TAG, "消息发送成功")
                    // 消息应该已经通过messageFlow被处理了
                } else {
                    Log.e(TAG, "发送消息失败", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e(TAG, "发送消息异常", e)
            }
        }
    }
} 