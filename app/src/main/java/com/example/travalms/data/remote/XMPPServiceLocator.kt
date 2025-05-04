package com.example.travalms.data.remote

import android.util.Log
import javax.inject.Inject

/**
 * XMPP服务定位器，用于提供对XMPP相关服务的全局访问
 * 这是一个简单的服务定位器模式实现，用于在非依赖注入环境中获取服务实例
 */
class XMPPServiceLocator private constructor() {
    companion object {
        private const val TAG = "XMPPServiceLocator"
        
        @Volatile
        private var INSTANCE: XMPPServiceLocator? = null
        
        fun getInstance(): XMPPServiceLocator {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: XMPPServiceLocator().also { INSTANCE = it }
            }
        }
    }
    
    // 群聊加入处理器实例
    var groupChatJoinHandler: GroupChatJoinHandler? = null
        private set
    
    /**
     * 设置群聊加入处理器
     * 应在应用启动时通过依赖注入设置
     */
    fun setGroupChatJoinHandler(handler: GroupChatJoinHandler) {
        Log.d(TAG, "设置GroupChatJoinHandler")
        this.groupChatJoinHandler = handler
    }
} 