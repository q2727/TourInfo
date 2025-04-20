package com.example.travalms.data.remote

import android.util.Log
import com.example.travalms.data.model.GroupChatMessage
import com.example.travalms.data.model.GroupMember
import com.example.travalms.data.model.GroupRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.xdata.form.FillableForm
import org.jivesoftware.smackx.xdata.form.Form
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart
import java.time.LocalDateTime
import java.util.Collections
import org.jivesoftware.smack.SmackConfiguration
import com.example.travalms.data.model.ChatInvitation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jivesoftware.smack.packet.Presence

/**
 * XMPPGroupChatManager - 专门负责XMPP群聊(MUC)功能的类
 * 
 * 将XMPPManager中的群聊功能解构出来，实现更清晰的代码组织
 */
class XMPPGroupChatManager(private val xmppManager: XMPPManager) {
    private val TAG = "XMPPGroupChatManager"

    // 配置常量
    private val CONFERENCE_DOMAIN = "conference.${XMPPManager.SERVER_DOMAIN}"

    // MultiUserChat管理器
    private var _mucManager: MultiUserChatManager? = null
    val mucManager: MultiUserChatManager? get() = _mucManager

    // 群聊消息Flow
    private val _groupMessageFlow = MutableSharedFlow<GroupChatMessage>(extraBufferCapacity = 64)
    val groupMessageFlow: SharedFlow<GroupChatMessage> = _groupMessageFlow

    // 添加群聊邀请管理
    private val _invitations = MutableStateFlow<List<ChatInvitation>>(emptyList())
    val invitations: StateFlow<List<ChatInvitation>> = _invitations.asStateFlow()

    private val _unreadInvitationCount = MutableStateFlow(0)
    val unreadInvitationCount: StateFlow<Int> = _unreadInvitationCount.asStateFlow()

    init {
        // 添加连接状态监听
        updateConnectionListener()
    }

    /**
     * 更新连接监听器
     */
    fun updateConnectionListener() {
        xmppManager.currentConnection?.removeConnectionListener(connectionListener)
        xmppManager.currentConnection?.addConnectionListener(connectionListener)
    }

    private val connectionListener = object : org.jivesoftware.smack.ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            Log.d(TAG, "XMPP连接已建立")
        }

        override fun authenticated(connection: XMPPConnection, resumed: Boolean) {
            Log.d(TAG, "XMPP认证成功，是否为恢复会话: $resumed")
            // 在认证成功后，重新初始化MUC管理器
            initMucManager()
        }

        override fun connectionClosed() {
            Log.d(TAG, "XMPP连接已关闭")
        }

        override fun connectionClosedOnError(e: Exception) {
            Log.e(TAG, "XMPP连接异常关闭", e)
        }
    }

    /**
     * 初始化MUC管理器
     */
    fun initMucManager() {
        val connection = xmppManager.currentConnection ?: return
        try {
            // 获取新的MUC管理器实例，而不是重用旧的
            _mucManager = MultiUserChatManager.getInstanceFor(connection).apply {
                // 设置自动加入模式，这样在重连时会自动重新加入之前的房间
                setAutoJoinOnReconnect(true)
            }
            
            // 设置群聊消息监听器
            setupGroupChatMessageListener()

            // 设置群聊邀请监听器
            setupGroupChatInvitationListener()

            Log.d(TAG, "MultiUserChatManager初始化完成")
        } catch (e: Exception) {
            Log.e(TAG, "初始化MUC管理器失败", e)
        }
    }

    /**
     * 设置群聊消息监听器
     */
    fun setupGroupChatMessageListener() {
        val connection = xmppManager.currentConnection ?: return

        // 添加消息监听器
        connection.addAsyncStanzaListener({ stanza ->
            if (stanza is Message) {
                val from = stanza.from.toString()

                // 只处理群聊消息（来自conference域的消息）
                if (from.contains("conference.")) {
                    processGroupChatMessage(stanza)
                }
            }
        }, null)

        Log.d(TAG, "群聊消息监听器设置完成")
    }

    /**
     * 处理群聊消息
     */
    private fun processGroupChatMessage(message: Message) {
        try {
            val fromJid = message.from
            val mucRoom = fromJid.toString().substringBefore("/")
            val senderNickname = fromJid.toString().substringAfter("/", "")

            // 判断是否是自己发送的消息
            val currentUserNick = xmppManager.currentConnection?.user?.localpart?.toString() ?: ""
            val isFromMe = senderNickname == currentUserNick

            // 创建群聊消息对象
            val groupMessage = GroupChatMessage(
                roomJid = mucRoom,
                senderJid = fromJid.toString(),
                senderNickname = senderNickname,
                content = message.body ?: "",
                timestamp = LocalDateTime.now(),
                isFromMe = isFromMe
            )

            // 发送到Flow
            _groupMessageFlow.tryEmit(groupMessage)

            Log.d(TAG, "处理群聊消息: room=$mucRoom, sender=$senderNickname, body=${message.body}")
        } catch (e: Exception) {
            Log.e(TAG, "处理群聊消息异常", e)
        }
    }

    /**
     * 设置群聊邀请监听器
     */
    private fun setupGroupChatInvitationListener() {
        try {
            val connection = xmppManager.currentConnection ?: return
            val mucMgr = _mucManager ?: return

            // 注册MUC邀请监听器
            mucMgr.addInvitationListener { conn, room, inviter, reason, password, message, invitation ->
                Log.d(TAG, "收到群聊邀请: room=$room, inviter=$inviter, reason=$reason")

                try {
                    // 清理JID格式，确保没有非法字符
                    val cleanRoomJid = room.toString().replace(Regex("\\(.+\\)$"), "").trim()
                    
                    // 移除可能存在的"MUC:"前缀
                    val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                        cleanRoomJid.substring(4).trim()
                    } else {
                        cleanRoomJid
                    }
                    
                    Log.d(TAG, "处理邀请: 原始房间JID=$room, 清理后=$finalRoomJid")

                    // 获取房间信息，尝试获取房间名称
                    var roomName = finalRoomJid.substringBefore('@')
                    try {
                        val roomInfo = mucMgr.getRoomInfo(JidCreate.entityBareFrom(finalRoomJid))
                        if (roomInfo != null && roomInfo.name.isNotEmpty()) {
                            roomName = roomInfo.name
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "获取房间信息失败: ${e.message}")
                    }

                    // 创建邀请对象
                    val chatInvitation = ChatInvitation(
                        roomJid = finalRoomJid,
                        roomName = roomName,
                        senderJid = inviter.toString(),
                        reason = reason ?: "邀请你加入群聊"
                    )

                    // 添加到邀请列表
                    addInvitation(chatInvitation)

                    Log.d(TAG, "已添加群聊邀请: ${chatInvitation.shortDescription}")
                } catch (e: Exception) {
                    Log.e(TAG, "处理群聊邀请时出错", e)
                }
            }

            Log.d(TAG, "群聊邀请监听器设置成功")
        } catch (e: Exception) {
            Log.e(TAG, "设置群聊邀请监听器失败", e)
        }
    }

    /**
     * 辅助方法: 清理JID字符串，移除不合法字符
     */
    private fun cleanJidString(jidString: String): String {
        // 1. 移除末尾的括号部分，如 'user@domain(sender@domain/resource)'
        val withoutParentheses = jidString.replace(Regex("\\(.+\\)$"), "").trim()

        // 2. 确保只保留JID的user@domain部分
        return if (withoutParentheses.contains("@")) {
            val parts = withoutParentheses.split("@")
            if (parts.size >= 2) {
                // 只保留user@domain，去掉可能的resource部分
                "${parts[0]}@${parts[1].split("/")[0]}"
            } else {
                withoutParentheses
            }
        } else {
            // 如果不含@，可能不是有效JID
            withoutParentheses
        }
    }

    /**
     * 添加新的邀请
     */
    fun addInvitation(invitation: ChatInvitation) {
        _invitations.update { currentList ->
            // 检查是否已存在相同roomJid的邀请，避免重复
            val exists = currentList.any { it.roomJid == invitation.roomJid }
            if (exists) {
                Log.d(TAG, "已存在相同房间的邀请: ${invitation.roomJid}")
                // 替换已有邀请
                currentList.map {
                    if (it.roomJid == invitation.roomJid) invitation else it
                }
            } else {
                Log.d(TAG, "添加新邀请: ${invitation.roomJid}")
                // 添加到列表前面
                listOf(invitation) + currentList
            }
        }
        updateUnreadInvitationCount()
    }

    /**
     * 删除邀请
     */
    fun removeInvitation(invitationId: String) {
        _invitations.update { currentList ->
            currentList.filter { it.id != invitationId }
        }
        updateUnreadInvitationCount()
    }

    /**
     * 更新未读邀请数量
     */
    private fun updateUnreadInvitationCount() {
        val count = _invitations.value.size
        Log.d(TAG, "更新未读邀请数量: $count")
        
        // 如果数量有变化，才更新
        if (_unreadInvitationCount.value != count) {
            _unreadInvitationCount.value = count
            Log.d(TAG, "未读邀请数量已更新为: $count")
        }
    }

    /**
     * 标记所有邀请为已读
     */
    fun markAllInvitationsAsRead() {
        Log.d(TAG, "标记所有邀请为已读")
        // 标记为已读只是将未读计数清零，不影响邀请列表
        if (_unreadInvitationCount.value > 0) {
            _unreadInvitationCount.value = 0
            Log.d(TAG, "未读邀请计数已清零")
        }
    }

    /**
     * 接受邀请
     */
    suspend fun acceptInvitation(invitation: ChatInvitation): Result<Unit> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "【接受群聊邀请】开始处理: ${invitation.roomJid}")

            try {
                // 检查连接状态
                val connection = xmppManager.currentConnection
                if (connection == null || !connection.isConnected || !connection.isAuthenticated) {
                    Log.e(TAG, "【接受群聊邀请】连接无效或未认证")
                    return@withContext Result.failure(Exception("连接状态异常，请重新登录"))
                }

                // 确保MUC管理器已初始化
                if (_mucManager == null) {
                    Log.d(TAG, "【接受群聊邀请】MUC管理器未初始化，尝试初始化")
                    initMucManager()
                    // 给一点时间让初始化完成
                    kotlinx.coroutines.delay(500)
                }

                if (_mucManager == null) {
                    Log.e(TAG, "【接受群聊邀请】MUC管理器初始化失败")
                    return@withContext Result.failure(Exception("无法初始化群聊组件"))
                }

                // 清理JID
                val cleanRoomJid = cleanJidString(invitation.roomJid)
                // 进一步检查并移除"MUC:"前缀
                val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                    cleanRoomJid.substring(4).trim()
                } else {
                    cleanRoomJid
                }
                
                Log.d(TAG, "【接受群聊邀请】处理后的房间JID: $finalRoomJid (原始: ${invitation.roomJid})")

                // 获取当前用户昵称
                val nickname = connection.user?.localpart?.toString() ?: "user"
                Log.d(TAG, "【接受群聊邀请】当前用户昵称: $nickname")

                // 尝试加入房间，最多重试3次
                var joinResult: Result<GroupRoom>? = null
                var retryCount = 0
                val maxRetries = 3

                while (retryCount < maxRetries) {
                    try {
                        Log.d(TAG, "【接受群聊邀请】尝试加入房间 (尝试 ${retryCount + 1}/$maxRetries)")
                        // 调用加入房间方法
                        joinResult = joinRoom(roomJid = finalRoomJid, nickname = nickname)

                        if (joinResult.isSuccess) {
                            Log.d(TAG, "【接受群聊邀请】成功加入房间: $finalRoomJid")
                            break
                        } else {
                            val error = joinResult.exceptionOrNull()
                            Log.w(TAG, "【接受群聊邀请】加入房间失败 (尝试 ${retryCount + 1}/$maxRetries): ${error?.message}")
                            retryCount++
                            // 在重试前等待一段时间
                            kotlinx.coroutines.delay(1000)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "【接受群聊邀请】加入房间异常 (尝试 ${retryCount + 1}/$maxRetries): ${e.message}", e)
                        retryCount++
                        // 在重试前等待一段时间
                        kotlinx.coroutines.delay(1000)
                    }
                }

                // 验证加入结果
                if (joinResult?.isSuccess == true) {
                    // 额外检查是否真的加入了房间
                    try {
                        val jid = JidCreate.entityBareFrom(finalRoomJid)
                        val muc = _mucManager?.getMultiUserChat(jid)

                        if (muc != null && muc.isJoined) {
                            Log.d(TAG, "【接受群聊邀请】已验证成功加入房间 $finalRoomJid")

                            // 从列表中移除邀请
                            removeInvitation(invitation.id)

                            // 刷新房间列表
                            val rooms = _mucManager?.getJoinedRooms()
                            Log.d(TAG, "【接受群聊邀请】当前加入的房间数: ${rooms?.size ?: 0}")

                            // 创建并发送一条加入消息
                            try {
                                val joinMessage = org.jivesoftware.smack.packet.Message()
                                joinMessage.setTo(jid)
                                joinMessage.setType(org.jivesoftware.smack.packet.Message.Type.groupchat)
                                joinMessage.setBody("大家好，我已加入群聊")
                                connection.sendStanza(joinMessage)
                                Log.d(TAG, "【接受群聊邀请】已发送加入通知")
                            } catch (e: Exception) {
                                Log.w(TAG, "【接受群聊邀请】发送加入通知失败: ${e.message}")
                            }

                            return@withContext Result.success(Unit)
                        } else {
                            Log.e(TAG, "【接受群聊邀请】加入房间后验证失败，isJoined=${muc?.isJoined}")
                            return@withContext Result.failure(Exception("加入房间验证失败"))
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "【接受群聊邀请】验证房间加入状态时出错: ${e.message}", e)
                        return@withContext Result.failure(e)
                    }
                } else {
                    Log.e(TAG, "【接受群聊邀请】加入房间失败: ${joinResult?.exceptionOrNull()?.message}")
                    return@withContext Result.failure(joinResult?.exceptionOrNull() ?: Exception("加入房间失败"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "【接受群聊邀请】处理邀请异常: ${e.message}", e)
                return@withContext Result.failure(e)
            }
        }

    /**
     * 拒绝邀请
     */
    fun rejectInvitation(invitation: ChatInvitation) {
        Log.d(TAG, "拒绝邀请: ${invitation.roomJid}")
        
        try {
            // 判断房间是否实际存在
            val connection = xmppManager.currentConnection
            if (connection != null && connection.isConnected && connection.isAuthenticated) {
                // 清理JID
                val cleanRoomJid = cleanJidString(invitation.roomJid)
                val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                    cleanRoomJid.substring(4).trim()
                } else {
                    cleanRoomJid
                }
                
                Log.d(TAG, "拒绝邀请，清理后的房间JID: $finalRoomJid")
                
                // 如果用户已经在房间中，尝试离开该房间
                if (_mucManager?.getMultiUserChat(JidCreate.entityBareFrom(finalRoomJid))?.isJoined == true) {
                    try {
                        val jid = JidCreate.entityBareFrom(finalRoomJid)
                        val muc = _mucManager?.getMultiUserChat(jid)
                        if (muc != null && muc.isJoined) {
                            // 退出房间
                            muc.leave()
                            Log.d(TAG, "用户拒绝邀请并已退出房间: $finalRoomJid")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "尝试退出被拒绝的房间时出错: $finalRoomJid", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "拒绝邀请过程中出错", e)
        } finally {
            // 从列表中移除邀请（无论上面的操作是否成功）
            removeInvitation(invitation.id)
        }
    }

    /**
     * 获取可用的聊天室列表
     */
    suspend fun getAvailableRooms(): Result<List<GroupRoom>> = withContext(Dispatchers.IO) {
        Log.d(TAG, "开始获取房间列表")
        try {
            val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                Exception("未连接到服务器")
            )

            // 初始化MUC管理器
            if (_mucManager == null) {
                initMucManager()
            }
            val mucMgr =
                _mucManager ?: return@withContext Result.failure(Exception("MUC管理器初始化失败"))

            // 使用服务发现获取会议服务列表
            val serviceManager = ServiceDiscoveryManager.getInstanceFor(connection)
            val services = serviceManager.findServices(
                "http://jabber.org/protocol/muc",
                true,
                true
            )

            val conferenceService = services.firstOrNull {
                it.toString().startsWith("conference.")
            }?.toString() ?: CONFERENCE_DOMAIN

            Log.d(TAG, "发现会议服务: $conferenceService")

            // 获取房间列表
            val items = serviceManager.discoverItems(JidCreate.from(conferenceService))
            val rooms = items.items.mapNotNull { item ->
                try {
                    val entityJid = item.entityID.asEntityBareJidIfPossible()
                    if (entityJid != null) {
                        // 获取房间信息
                        val info = mucMgr.getRoomInfo(entityJid)
                        GroupRoom(
                            roomJid = entityJid.toString(),
                            name = info.name ?: item.name ?: entityJid.toString()
                                .substringBefore('@'),
                            description = info.description ?: "",
                            memberCount = info.occupantsCount,
                            isPrivate = info.isMembersOnly,
                            canEdit = false
                        )
                    } else null
                } catch (e: Exception) {
                    Log.e(TAG, "获取房间 ${item.entityID} 信息失败", e)
                    null
                }
            }

            Log.d(TAG, "找到 ${rooms.size} 个房间")
            Result.success(rooms)
        } catch (e: Exception) {
            Log.e(TAG, "获取房间列表失败", e)
            Result.failure(e)
        }
    }

    /**
     * 创建新聊天室 - 增强版本，处理多种异常情况
     */
    suspend fun createGroupRoomEnhanced(
        roomName: String,
        nickname: String,
        description: String = "",
        membersOnly: Boolean = false,
        persistent: Boolean = true
    ): Result<GroupRoom> = withContext(Dispatchers.IO) {
        Log.d(TAG, "尝试创建新房间 (增强版): $roomName")

        // 不再检查MUC支持状态，直接创建房间

        try {
            val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                Exception("未连接到服务器")
            )

            // 清理房间名称，只保留英文字母、数字和下划线
            val safeRoomName = roomName.replace(Regex("[^a-zA-Z0-9_]"), "")
            if (safeRoomName.isEmpty()) {
                return@withContext Result.failure(Exception("房间名称无效"))
            }

            // 创建房间JID
            val roomJid = "$safeRoomName@$CONFERENCE_DOMAIN"
            val jid = JidCreate.entityBareFrom(roomJid)

            // 获取MUC管理器
            val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                _mucManager = it
            }

            // 创建多用户聊天实例
            val muc = mucMgr.getMultiUserChat(jid)

            // 检查是否已加入该房间，如果已加入则先离开
            if (muc.isJoined) {
                Log.d(TAG, "已经加入房间 $roomJid，先尝试离开")
                try {
                    muc.leave()
                    Log.d(TAG, "成功离开房间 $roomJid")
                } catch (e: Exception) {
                    Log.e(TAG, "离开房间 $roomJid 时出错", e)
                }
            }

            // 修改超时设置
            val previousTimeout = SmackConfiguration.getDefaultReplyTimeout()
            try {
                // 增加超时时间到30秒
                SmackConfiguration.setDefaultReplyTimeout(30000)

                // 尝试创建房间
                Log.d(TAG, "开始创建房间 $roomJid, 昵称: $nickname")
                val nickPart = Resourcepart.from(nickname)
                muc.create(nickPart)

                // 配置房间
                Log.d(TAG, "房间创建成功，开始配置房间")
                val form = muc.configurationForm
                val submitForm = form.fillableForm

                // 设置房间属性
                submitForm.setAnswer("muc#roomconfig_roomname", roomName)
                submitForm.setAnswer("muc#roomconfig_roomdesc", description)

                // 房间选项
                submitForm.setAnswer("muc#roomconfig_membersonly", membersOnly)
                submitForm.setAnswer("muc#roomconfig_persistentroom", persistent)

                // 提交配置
                muc.sendConfigurationForm(submitForm)

                Log.d(TAG, "房间配置完成，房间创建成功: $roomJid")

                // 返回创建的房间
                return@withContext Result.success(
                    GroupRoom(
                        roomJid = roomJid,
                        name = roomName,
                        description = description,
                        memberCount = 1,
                        isPrivate = membersOnly,
                        canEdit = true
                    )
                )
            } finally {
                // 恢复原来的超时设置
                SmackConfiguration.setDefaultReplyTimeout(previousTimeout)
            }
        } catch (e: org.jivesoftware.smackx.muc.MultiUserChatException.MucAlreadyJoinedException) {
            // 特别处理MucAlreadyJoinedException
            Log.w(TAG, "创建房间时发现已经加入了该房间", e)

            // 清理房间名称，只保留英文字母、数字和下划线
            val safeRoomName = roomName.replace(Regex("[^a-zA-Z0-9_]"), "")
            val roomJid = "$safeRoomName@$CONFERENCE_DOMAIN"
            val jid = JidCreate.entityBareFrom(roomJid)

            // 尝试获取房间信息并返回
            try {
                val mucMgr = _mucManager
                    ?: MultiUserChatManager.getInstanceFor(xmppManager.currentConnection!!)
                val info = mucMgr.getRoomInfo(jid)
                Log.d(TAG, "返回已存在的房间信息")
                return@withContext Result.success(
                    GroupRoom(
                        roomJid = roomJid,
                        name = roomName,
                        description = info.description ?: description,
                        memberCount = info.occupantsCount,
                        isPrivate = info.isMembersOnly,
                        canEdit = true
                    )
                )
            } catch (infoEx: Exception) {
                Log.e(TAG, "获取已存在房间信息失败", infoEx)
                return@withContext Result.failure(e)
            }
        } catch (e: org.jivesoftware.smackx.muc.MultiUserChatException.MissingMucCreationAcknowledgeException) {
            // 特别处理MissingMucCreationAcknowledgeException
            Log.w(TAG, "创建房间时服务器没有确认", e)

            // 检查房间是否已经创建，尝试加入
            try {
                Log.d(TAG, "尝试加入可能已创建的房间")
                // 清理房间名称，只保留英文字母、数字和下划线
                val safeRoomName = roomName.replace(Regex("[^a-zA-Z0-9_]"), "")
                val roomJid = "$safeRoomName@$CONFERENCE_DOMAIN"
                return@withContext joinRoom(roomJid, nickname)
            } catch (joinEx: Exception) {
                Log.e(TAG, "无法加入可能已创建的房间", joinEx)
                return@withContext Result.failure(e) // 返回原始异常
            }
        } catch (e: Exception) {
            Log.e(TAG, "创建房间失败", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * 加入聊天室
     */
    suspend fun joinRoom(roomJid: String, nickname: String): Result<GroupRoom> =
        withContext(Dispatchers.IO) {
            try {
                val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                    Exception("未连接到服务器")
                )

                // 确保MUC管理器已初始化
                val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                    _mucManager = it
                }

                // 创建房间JID
                val jid = JidCreate.entityBareFrom(roomJid)
                val muc = mucMgr.getMultiUserChat(jid)
                var info = mucMgr.getRoomInfo(jid) // 先尝试获取信息

                // 如果已经在房间中，直接返回成功
                if (muc.isJoined) {
                    if (info == null) {
                        info = mucMgr.getRoomInfo(jid) // 重新获取信息
                    }
                    return@withContext Result.success(
                        GroupRoom(
                            roomJid = jid.toString(),
                            name = info?.name ?: roomJid.substringBefore('@'),
                            description = info?.description ?: "",
                            memberCount = info?.occupantsCount ?: 1,
                            isPrivate = info?.isMembersOnly ?: false,
                            canEdit = true // 假设加入者可以编辑，或者需要更复杂的逻辑
                        )
                    )
                }

                // 创建加入配置
                val mucConfig = muc.getEnterConfigurationBuilder(Resourcepart.from(nickname))
                    .requestMaxStanzasHistory(20)  // 请求最近的20条消息历史
                    .build()

                // 加入房间
                muc.join(mucConfig)
                
                // 加入后重新获取信息
                info = mucMgr.getRoomInfo(jid)

                Result.success(
                    GroupRoom(
                        roomJid = jid.toString(),
                        name = info?.name ?: roomJid.substringBefore('@'),
                        description = info?.description ?: "",
                        memberCount = info?.occupantsCount ?: muc.occupantsCount, // 使用occupantsCount作为备选
                        isPrivate = info?.isMembersOnly ?: false,
                        canEdit = true // 假设加入者可以编辑
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "加入房间失败", e)
                Result.failure(e)
            }
        }

    /**
     * 主动离开房间
     */
    suspend fun leaveRoom(roomJid: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val jid = JidCreate.entityBareFrom(roomJid)
            val muc = _mucManager?.getMultiUserChat(jid)
            
            if (muc?.isJoined == true) {
                // 直接调用 leave 方法，不需要参数
                muc.leave()
                Result.success(Unit)
            } else {
                Result.failure(Exception("未加入该房间"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 获取已加入的房间JID列表
     */
    fun getJoinedRoomJids(): List<String> {
        return _mucManager?.getJoinedRooms()?.map { it.toString() } ?: emptyList()
    }

    /**
     * 检查MUC管理器是否已初始化
     */
    private fun isMucManagerInitialized(): Boolean {
        return _mucManager != null
    }

    /**
     * 获取房间成员列表
     */
    suspend fun getRoomMembers(roomJid: String): Result<List<GroupMember>> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "获取房间成员: $roomJid")
            try {
                val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                    Exception("未连接到服务器")
                )

                // 获取MUC管理器
                val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                    _mucManager = it
                }

                // 清理房间JID，移除MUC前缀
                val cleanRoomJid = cleanJidString(roomJid)
                val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                    cleanRoomJid.substring(4).trim()
                } else {
                    cleanRoomJid
                }
                
                Log.d(TAG, "处理后的房间JID: $finalRoomJid (原始: $roomJid)")

                // 获取房间
                val jid = JidCreate.entityBareFrom(finalRoomJid)
                val muc = mucMgr.getMultiUserChat(jid)

                // 判断是否已经加入
                if (!muc.isJoined) {
                    Log.d(TAG, "未加入房间 $finalRoomJid，无法获取成员列表")
                    return@withContext Result.failure(Exception("未加入房间"))
                }

                // 获取房间成员
                val occupants = muc.occupants

                // 用于存储已处理的用户，按照真实JID或昵称去重
                val processedMembers = HashMap<String, GroupMember>()

                occupants.forEach { occupantJid ->
                    try {
                        val nickname = occupantJid.resourceOrEmpty.toString()

                        // 如果昵称为空，则跳过
                        if (nickname.isEmpty()) {
                            return@forEach
                        }

                        // 获取当前用户在房间中的从属关系
                        val currentUserJid = connection.user?.asEntityBareJidString()
                        val affiliation = try {
                            // 使用正确的方法获取用户的从属关系
                            val occupantJids = muc.occupants
                            val userOccupant = occupantJids.find { occupant ->
                                occupant.asBareJid().toString() == currentUserJid
                            }
                            if (userOccupant != null) {
                                // 使用 Form 获取用户信息
                                val form = muc.configurationForm
                                val affiliations = form.getField("muc#roomconfig_affiliations")
                                val affiliationValue = affiliations?.values?.firstOrNull { value ->
                                    value.contains(currentUserJid ?: "")
                                }
                                when {
                                    affiliationValue?.contains("owner") == true -> "owner"
                                    affiliationValue?.contains("admin") == true -> "admin"
                                    affiliationValue?.contains("member") == true -> "member"
                                    else -> "none"
                                }
                            } else {
                                "none"
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "获取用户从属关系失败: ${e.message}")
                            null
                        }

                        val role = try {
                            // 不同版本Smack库中此方法可能会有不同实现
                            val mucRole = when {
                                // 尝试多种获取方式
                                muc::class.java.methods.any { it.name == "getOccupantRole" } ->
                                    try {
                                        muc.javaClass.getMethod(
                                            "getOccupantRole",
                                            occupantJid.javaClass
                                        )
                                            .invoke(muc, occupantJid)?.toString()
                                    } catch (e: Exception) {
                                        null
                                    }

                                muc::class.java.methods.any { it.name == "getRole" } ->
                                    try {
                                        muc.javaClass.getMethod("getRole", occupantJid.javaClass)
                                            .invoke(muc, occupantJid)?.toString()
                                    } catch (e: Exception) {
                                        null
                                    }

                                else -> null
                            }
                            mucRole ?: "none"
                        } catch (e: Exception) {
                            Log.w(TAG, "获取用户角色失败: $occupantJid", e)
                            "none"
                        }

                        // 获取真实JID（如果有权限）
                        val realJid = try {
                            // 尝试获取占用者的真实JID
                            val jidObj = when {
                                muc::class.java.methods.any { it.name == "getOccupantJid" } ->
                                    try {
                                        muc.javaClass.getMethod(
                                            "getOccupantJid",
                                            occupantJid.javaClass
                                        )
                                            .invoke(muc, occupantJid)?.toString()
                                    } catch (e: Exception) {
                                        null
                                    }

                                muc::class.java.methods.any { it.name == "getUserJid" } ->
                                    try {
                                        muc.javaClass.getMethod("getUserJid", occupantJid.javaClass)
                                            .invoke(muc, occupantJid)?.toString()
                                    } catch (e: Exception) {
                                        null
                                    }

                                else -> null
                            }
                            jidObj ?: occupantJid.toString()
                        } catch (e: Exception) {
                            Log.w(TAG, "获取用户真实JID失败: $occupantJid", e)
                            occupantJid.toString()
                        }

                        // 获取去除资源部分的bare JID
                        val bareJid = if (realJid.contains("/")) {
                            realJid.substringBefore("/")
                        } else if (realJid.contains("@")) {
                            realJid.substringBefore("@")
                        } else {
                            // 如果无法提取有效JID，使用昵称作为键
                            nickname
                        }

                        // 只有当我们之前没有处理过这个用户时，才添加它
                        if (!processedMembers.containsKey(bareJid)) {
                            val member = GroupMember(
                                jid = realJid,
                                nickname = nickname,
                                role = when (role.uppercase()) {
                                    "MODERATOR" -> GroupMember.ROLE_MODERATOR
                                    "PARTICIPANT" -> GroupMember.ROLE_PARTICIPANT
                                    "VISITOR" -> GroupMember.ROLE_VISITOR
                                    else -> GroupMember.ROLE_NONE
                                },
                                affiliation = when (affiliation?.uppercase()) {
                                    "OWNER" -> GroupMember.AFFILIATION_OWNER
                                    "ADMIN" -> GroupMember.AFFILIATION_ADMIN
                                    "MEMBER" -> GroupMember.AFFILIATION_MEMBER
                                    "OUTCAST" -> GroupMember.AFFILIATION_NONE
                                    else -> GroupMember.AFFILIATION_NONE
                                }
                            )
                            processedMembers[bareJid] = member
                            Log.d(TAG, "添加用户到成员列表: bareJid=$bareJid, nickname=$nickname")
                        } else {
                            Log.d(TAG, "跳过重复用户: bareJid=$bareJid, nickname=$nickname")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "获取成员信息失败: $occupantJid", e)
                    }
                }

                val uniqueMembers = processedMembers.values.toList()
                Log.d(TAG, "获取到 ${occupants.size} 个原始成员，去重后剩余 ${uniqueMembers.size} 个成员")
                Result.success(uniqueMembers)
            } catch (e: Exception) {
                Log.e(TAG, "获取房间成员失败", e)
                Result.failure(e)
            }
        }

    /**
     * 发送群聊消息
     */
    suspend fun sendGroupMessage(roomJid: String, message: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "发送群聊消息: room=$roomJid, message=$message")
            try {
                val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                    Exception("未连接到服务器")
                )

                // 获取MUC管理器
                val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                    _mucManager = it
                }

                // 清理房间JID，移除MUC前缀
                val cleanRoomJid = cleanJidString(roomJid)
                val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                    cleanRoomJid.substring(4).trim()
                } else {
                    cleanRoomJid
                }
                
                Log.d(TAG, "处理后的房间JID: $finalRoomJid (原始: $roomJid)")

                // 获取房间
                val jid = JidCreate.entityBareFrom(finalRoomJid)
                val muc = mucMgr.getMultiUserChat(jid)

                // 判断是否已经加入
                if (!muc.isJoined) {
                    Log.d(TAG, "未加入房间 $finalRoomJid，无法发送消息")
                    return@withContext Result.failure(Exception("未加入房间"))
                }

                // 发送消息
                muc.sendMessage(message)

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "发送群聊消息失败", e)
                Result.failure(e)
            }
        }

    /**
     * 邀请用户加入群聊
     */
    suspend fun inviteUserToRoom(
        roomJid: String,
        userJid: String,
        reason: String = "邀请你加入群聊"
    ): Result<Unit> = withContext(Dispatchers.IO) {
        Log.d(TAG, "发送群聊邀请: room=$roomJid, user=$userJid")
        try {
            val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                Exception("未连接到服务器")
            )

            // 获取MUC管理器
            val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                _mucManager = it
            }

            // 创建房间JID对象
            val jid = JidCreate.entityBareFrom(roomJid)
            val muc = mucMgr.getMultiUserChat(jid)

            // 创建用户JID对象
            val inviteeJid = JidCreate.entityBareFrom(userJid)

            // 发送邀请
            muc.invite(inviteeJid, reason)
            Log.d(TAG, "邀请发送成功")
            
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "发送邀请失败", e)
            Result.failure(e)
        }
    }

    /**
     * 销毁/删除聊天室
     */
    suspend fun destroyRoom(roomJid: String, reason: String = "房间已被销毁"): Result<Unit> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "销毁房间: $roomJid")
            try {
                val connection = xmppManager.currentConnection ?: return@withContext Result.failure(
                    Exception("未连接到服务器")
                )

                // 获取MUC管理器
                val mucMgr = _mucManager ?: MultiUserChatManager.getInstanceFor(connection).also {
                    _mucManager = it
                }

                // 清理房间JID，移除MUC前缀
                val cleanRoomJid = cleanJidString(roomJid)
                val finalRoomJid = if (cleanRoomJid.startsWith("MUC:", ignoreCase = true)) {
                    cleanRoomJid.substring(4).trim()
                } else {
                    cleanRoomJid
                }
                
                Log.d(TAG, "处理后的房间JID: $finalRoomJid (原始: $roomJid)")

                // 获取房间
                val jid = JidCreate.entityBareFrom(finalRoomJid)
                val muc = mucMgr.getMultiUserChat(jid)

                // 销毁房间
                muc.destroy(reason, null)

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "销毁房间失败", e)
                Result.failure(e)
            }
        }

    /**
     * 创建新聊天室
     * 为了向后兼容，内部调用增强版本
     */
    suspend fun createGroupRoom(
        roomName: String,
        nickname: String,
        description: String = "",
        membersOnly: Boolean = false,
        persistent: Boolean = true
    ): Result<GroupRoom> = createGroupRoomEnhanced(
        roomName = roomName,
        nickname = nickname,
        description = description,
        membersOnly = membersOnly,
        persistent = persistent
    )

    /**
     * Retrieves a list of rooms the user has joined
     * @return List of GroupRoom objects representing joined rooms
     */
    suspend fun getJoinedRooms(): List<GroupRoom> {
        // 确保MUC管理器已初始化
        if (_mucManager == null) {
            Log.d(TAG, "getJoinedRooms: MUC manager not initialized, attempting to initialize it")
            initMucManager()
            kotlinx.coroutines.delay(300)
        }

        if (!isMucManagerInitialized()) {
            Log.e(TAG, "getJoinedRooms: MUC manager initialization failed")
            return emptyList()
        }

        try {
            val connection = xmppManager.currentConnection
            if (connection == null || !connection.isConnected || !connection.isAuthenticated) {
                Log.e(TAG, "getJoinedRooms: 连接无效或未认证，无法获取群聊列表")
                return emptyList()
            }

            // 直接从服务器获取已加入的房间
            val joinedRooms = _mucManager?.getJoinedRooms()
            Log.d(TAG, "getJoinedRooms: 从服务器获取到 ${joinedRooms?.size ?: 0} 个已加入的房间")

            // 转换为GroupRoom对象列表
            return joinedRooms?.mapNotNull { roomJid ->
                try {
                    val muc = _mucManager?.getMultiUserChat(roomJid)
                    val info = _mucManager?.getRoomInfo(roomJid)
                    
                    GroupRoom(
                        roomJid = roomJid.toString(),
                        name = info?.name ?: roomJid.localpart.toString(),
                        description = info?.description ?: "Group Chat",
                        memberCount = info?.occupantsCount ?: 1,
                        isPrivate = info?.isMembersOnly ?: false,
                        canEdit = true
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "处理房间信息失败: $roomJid", e)
                    null
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "获取已加入房间列表失败", e)
            return emptyList()
        }
    }

}