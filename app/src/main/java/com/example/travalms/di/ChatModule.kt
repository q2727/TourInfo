package com.example.travalms.di

import com.example.travalms.data.api.GroupChatApiClient
import com.example.travalms.data.remote.GroupChatJoinHandler
import com.example.travalms.data.remote.GroupChatManager
import com.example.travalms.data.remote.XMPPGroupChatManager
import com.example.travalms.data.remote.XMPPManager
import com.example.travalms.data.remote.XMPPServiceLocator
import com.example.travalms.data.repository.GroupChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 聊天模块 - 提供与聊天相关的依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    
    /**
     * 提供XMPPManager实例
     */
    @Provides
    @Singleton
    fun provideXMPPManager(): XMPPManager {
        return XMPPManager.getInstance()
    }
    
    /**
     * 提供XMPPGroupChatManager实例
     * 注意：由于XMPPGroupChatManager是由XMPPManager创建的，
     * 这个方法目前不会被使用，保留仅为参考
     */
    @Provides
    @Singleton
    fun provideXMPPGroupChatManager(xmppManager: XMPPManager): XMPPGroupChatManager {
        return xmppManager.groupChatManager
    }
    
    /**
     * 提供GroupChatManager实例
     */
    @Provides
    @Singleton
    fun provideGroupChatManager(
        groupChatRepository: GroupChatRepository,
        xmppManager: XMPPManager
    ): GroupChatManager {
        return GroupChatManager(groupChatRepository, xmppManager)
    }
    
    /**
     * 提供XMPPServiceLocator实例
     */
    @Provides
    @Singleton
    fun provideXMPPServiceLocator(): XMPPServiceLocator {
        return XMPPServiceLocator.getInstance()
    }
    
    /**
     * 提供GroupChatJoinHandler实例
     */
    @Provides
    @Singleton
    fun provideGroupChatJoinHandler(
        groupChatRepository: GroupChatRepository,
        xmppManager: XMPPManager,
        groupChatApiClient: GroupChatApiClient  // 添加API客户端依赖
    ): GroupChatJoinHandler {
        val handler = GroupChatJoinHandler(groupChatRepository, xmppManager, groupChatApiClient)
        // 将GroupChatJoinHandler实例设置到XMPPServiceLocator中，以便在非注入环境中访问
        XMPPServiceLocator.getInstance().setGroupChatJoinHandler(handler)
        return handler
    }
    
    /**
     * 提供GroupChatApiClient实例
     */
    @Provides
    @Singleton
    fun provideGroupChatApiClient(): GroupChatApiClient {
        return GroupChatApiClient()
    }
} 