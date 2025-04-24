package com.example.travalms.data.remote

import android.util.Log
import com.example.travalms.data.repository.GroupChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 群聊管理器，负责管理群聊的加入和离开
 */
@Singleton
class GroupChatManager @Inject constructor(
    private val groupChatRepository: GroupChatRepository,
    private val xmppManager: XMPPManager
) {
    private val TAG = "GroupChatManager"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    // 群聊加入完成回调
    private var onGroupChatsJoinedCallback: (() -> Unit)? = null
    
    /**
     * 设置群聊加入完成回调
     */
    fun setOnGroupChatsJoinedCallback(callback: () -> Unit) {
        onGroupChatsJoinedCallback = callback
    }
    
    /**
     * 在用户登录后自动加入本地数据库存储的所有群聊
     */
    fun joinSavedGroupChats() {
        coroutineScope.launch {
            try {
                if (!xmppManager.isAuthenticated()) {
                    Log.d(TAG, "用户未登录，无法加入群聊")
                    return@launch
                }
                
                Log.d(TAG, "开始加入保存的群聊")
                val groupRooms = groupChatRepository.getAllGroupChats().first()
                
                if (groupRooms.isEmpty()) {
                    Log.d(TAG, "没有保存的群聊")
                    onGroupChatsJoinedCallback?.invoke()
                    return@launch
                }
                
                // 获取连接和MUC管理器
                val connection = xmppManager.getConnection() ?: run {
                    Log.e(TAG, "XMPP连接为空，无法加入群聊")
                    return@launch
                }
                
                val mucManager = MultiUserChatManager.getInstanceFor(connection)
                
                // 获取当前用户的基本JID(不包含资源标识符)
                val currentUserJidBase = connection.user.asEntityBareJidString()
                Log.d(TAG, "当前用户基本JID: $currentUserJidBase")
                
                // 加入每个群聊
                for (room in groupRooms) {
                    try {
                        // 将String类型的roomJid转换为EntityBareJid
                        val entityBareJid = JidCreate.entityBareFrom(room.roomJid)
                        val muc = mucManager.getMultiUserChat(entityBareJid)
                        
                        // 获取当前房间内的所有人
                        try {
                            // 先尝试获取房间内的所有成员
                            val occupants = muc.occupants
                            
                            // 过滤出属于当前用户的所有连接
                            val myConnections = occupants.filter { occupantJid ->
                                val occupantStr = occupantJid.toString()
                                // 检查是否是当前用户的连接(用户名部分匹配)
                                occupantStr.startsWith(currentUserJidBase) || 
                                occupantStr.contains("/${connection.user.localpart}")
                            }
                            
                            if (myConnections.size > 1) {
                                Log.d(TAG, "发现当前用户在群聊 ${room.name} 中有 ${myConnections.size} 个连接")
                                
                                // 如果发现同一用户有多个(>1)连接，只退出当前连接，稍后会用新连接重新加入
                                if (muc.isJoined) {
                                    try {
                                        Log.d(TAG, "尝试退出房间 ${room.name} 中的当前连接")
                                        muc.leave()
                                        Log.d(TAG, "成功退出房间 ${room.name}")
                                        // 等待一小段时间，确保退出操作完成
                                        delay(1000)
                                    } catch (e: Exception) {
                                        Log.e(TAG, "退出房间 ${room.name} 时出错: ${e.message}", e)
                                    }
                                }
                            } else {
                                Log.d(TAG, "用户在群聊 ${room.name} 中只有一个或没有连接，无需清理")
                                
                                // 如果已经在房间中且只有一个连接，则无需重新加入
                                if (muc.isJoined) {
                                    Log.d(TAG, "已经在群聊中: ${room.name}")
                                    continue
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "获取房间 ${room.name} 成员列表失败: ${e.message}", e)
                        }
                        
                        // 检查是否已经在房间中，避免重复加入
                        if (muc.isJoined) {
                            Log.d(TAG, "已经在群聊中: ${room.name}")
                            continue
                        }
                        
                        // 加入群聊，使用用户昵称
                        val nickname = xmppManager.getUserNickname() ?: run {
                            val jid = connection.user
                            jid.localpart.toString()  // 使用JID的本地部分作为昵称
                        }
                        
                        withContext(Dispatchers.IO) {
                            // 使用Resourcepart类型作为参数
                            muc.join(Resourcepart.from(nickname))
                        }
                        Log.d(TAG, "成功加入群聊: ${room.name}")
                    } catch (e: XMPPException.XMPPErrorException) {
                        Log.e(TAG, "加入群聊 ${room.name} 时出错: ${e.message}", e)
                    } catch (e: SmackException) {
                        Log.e(TAG, "加入群聊 ${room.name} 时出错: ${e.message}", e)
                    } catch (e: Exception) {
                        Log.e(TAG, "加入群聊 ${room.name} 时出现未知错误: ${e.message}", e)
                    }
                }
                
                Log.d(TAG, "群聊加入过程完成")
                
                // 调用回调通知群聊加入完成
                onGroupChatsJoinedCallback?.invoke()
            } catch (e: Exception) {
                Log.e(TAG, "加入保存的群聊过程中出错: ${e.message}", e)
                
                // 即使出错也调用回调
                onGroupChatsJoinedCallback?.invoke()
            }
        }
    }
} 