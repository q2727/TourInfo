package com.example.travalms.data.repository

import android.util.Log
import com.example.travalms.data.db.MessageDao
import com.example.travalms.data.db.MessageEntity
import com.example.travalms.data.model.ChatMessage
import com.example.travalms.data.model.ChatSession
import com.example.travalms.data.remote.XMPPManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 消息仓库类 - 协调本地数据库和XMPP消息操作
 */
class MessageRepository(
    private val messageDao: MessageDao,
    private val xmppManager: XMPPManager
) {
    private val TAG = "MessageRepository"
    
    /**
     * 保存消息到本地数据库
     */
    suspend fun saveMessage(message: ChatMessage, sessionId: String) {
        Log.d(TAG, "保存消息到数据库: ID=${message.id}, sessionId=$sessionId")
        val messageEntity = MessageEntity.fromChatMessage(message, sessionId)
        messageDao.insertMessage(messageEntity)
    }
    
    /**
     * 批量保存消息到本地数据库
     */
    suspend fun saveMessages(messages: List<ChatMessage>, sessionId: String) {
        if (messages.isEmpty()) return
        
        Log.d(TAG, "批量保存 ${messages.size} 条消息到数据库, sessionId=$sessionId")
        val entities = messages.map { MessageEntity.fromChatMessage(it, sessionId) }
        messageDao.insertMessages(entities)
    }
    
    /**
     * 获取指定会话的所有消息
     */
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessage>> {
        return messageDao.getMessagesForSession(sessionId)
            .map { entities -> entities.map { it.toChatMessage() } }
    }
    
    /**
     * 获取指定会话的所有消息（非Flow版本）
     */
    suspend fun getMessagesForSessionSync(sessionId: String): List<ChatMessage> {
        return messageDao.getMessagesForSessionSync(sessionId)
            .map { it.toChatMessage() }
    }
    
    /**
     * 获取会话的未读消息数量
     */
    suspend fun getUnreadCount(sessionId: String, currentUserJid: String): Int {
        return messageDao.getUnreadCountForSession(sessionId, currentUserJid)
    }
    
    /**
     * 标记会话消息为已读
     */
    suspend fun markSessionAsRead(sessionId: String, senderId: String) {
        messageDao.updateReadStatus(sessionId, senderId, true)
    }
    
    /**
     * 获取所有会话及其最后一条消息
     */
    suspend fun getAllSessions(currentUserJid: String): List<ChatSession> {
        // 1. 获取所有会话ID
        val sessionIds = messageDao.getAllSessionIds()
        if (sessionIds.isEmpty()) return emptyList()
        
        // 2. 获取每个会话的最后一条消息
        val lastMessages = messageDao.getLastMessagesForAllSessions()
        val sessionsMap = mutableMapOf<String, ChatSession>()
        
        for (sessionId in sessionIds) {
            // 找到这个会话的最后一条消息
            val lastMessage = lastMessages.find { it.sessionId == sessionId }?.toChatMessage()
            
            // 获取未读消息数量
            val unreadCount = messageDao.getUnreadCountForSession(sessionId, currentUserJid)
            
            // 创建会话对象
            val targetName = if (sessionId.contains('@')) {
                sessionId.substringBefore('@')
            } else {
                sessionId
            }
            
            val session = ChatSession(
                id = sessionId,
                targetId = sessionId,
                targetName = targetName,
                targetType = "person", // 假设都是个人会话
                lastMessage = lastMessage,
                unreadCount = unreadCount
            )
            
            sessionsMap[sessionId] = session
        }
        
        // 按最后消息时间排序
        return sessionsMap.values.sortedByDescending { it.lastMessage?.timestamp }
    }
    
    /**
     * 从XMPP服务器加载历史消息
     */
    suspend fun loadHistoryFromServer(otherJid: String): Result<List<ChatMessage>> {
        Log.d(TAG, "从服务器加载与 $otherJid 的历史消息")
        val result = xmppManager.getChatHistory(otherJid)
        
        if (result.isSuccess) {
            val messages = result.getOrNull() ?: emptyList()
            if (messages.isNotEmpty()) {
                Log.d(TAG, "从服务器加载了 ${messages.size} 条消息，保存到数据库")
                saveMessages(messages, otherJid)
            }
        }
        
        return result
    }
    
    /**
     * 发送消息
     */
    suspend fun sendMessage(recipientJid: String, content: String): Result<ChatMessage> {
        val result = xmppManager.sendMessage(recipientJid, content)
        
        if (result.isSuccess) {
            val message = result.getOrNull()
            if (message != null) {
                Log.d(TAG, "消息发送成功，保存到数据库: ${message.id}")
                saveMessage(message, recipientJid)
            }
        }
        
        return result
    }
    
    /**
     * 清除会话历史消息
     */
    suspend fun clearSessionHistory(sessionId: String) {
        Log.d(TAG, "清除会话 $sessionId 的历史消息")
        messageDao.deleteSessionMessages(sessionId)
    }
    
    /**
     * 清除所有历史消息
     */
    suspend fun clearAllHistory() {
        Log.d(TAG, "清除所有历史消息")
        messageDao.deleteAllMessages()
    }
} 