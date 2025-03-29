package com.example.travalms.xmpp

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import org.jivesoftware.smack.SmackConfiguration

/**
 * XMPP连接和认证的管理类
 * 负责处理与XMPP服务器的所有交互
 */
class XMPPManager {
    // 常量配置
    companion object {
        private const val TAG = "XMPPManager"
        private const val SERVER_DOMAIN = "120.46.26.49"
        private const val SERVER_HOST = "120.46.26.49"
        private const val SERVER_PORT = 5222
        private const val RESOURCE = "AndroidClient"
    }

    // 获取配置好的XMPP连接
    private fun getConnectionConfig(): XMPPTCPConnectionConfiguration {
        return XMPPTCPConnectionConfiguration.builder()
            .setXmppDomain(SERVER_DOMAIN) // 服务器域名
            .setHost(SERVER_HOST) // 服务器地址
            .setPort(SERVER_PORT) // XMPP标准端口
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled) // 开发环境禁用安全模式
            .setResource(RESOURCE) // 设置资源标识
            .setConnectTimeout(300000) // 连接超时30秒
            .setSendPresence(true)
            .build()
    }

    /**
     * 登录到XMPP服务器
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含成功/失败信息和连接对象（如果成功）
     */
    suspend fun login(username: String, password: String): Result<XMPPTCPConnection> = withContext(Dispatchers.IO) {
        try {
            // 创建连接配置
            val config = getConnectionConfig()
            
            // 创建XMPP连接
            val connection = XMPPTCPConnection(config)
            
            // 连接到服务器
            connection.connect()
            
            // 登录
            connection.login(username, password)
            
            // 记录成功日志
            Log.d(TAG, "XMPP登录成功: 用户名=$username")
            Log.d(TAG, "服务器连接信息: 服务器=${connection.host}, 端口=${connection.port}, 用户=${connection.user}")
            
            // 返回成功结果
            Result.success(connection)
        } catch (e: Exception) {
            // 记录错误日志
            Log.e(TAG, "XMPP登录失败", e)
            
            // 返回失败结果
            Result.failure(e)
        }
    }

    /**
     * 在XMPP服务器上注册新用户
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称（可选）
     * @param email 电子邮箱（可选）
     * @return 注册结果，表示成功或失败
     */
    suspend fun register(username: String, password: String, nickname: String? = null, email: String? = null): Result<Unit> = withContext(Dispatchers.IO) {
        var connection: XMPPTCPConnection? = null
        try {
            // 详细记录开始注册流程
            Log.d(TAG, "开始注册用户: $username")
            
            // 创建连接配置，确保最适合注册流程的设置
            val config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(SERVER_DOMAIN)
                .setHost(SERVER_HOST)
                .setPort(SERVER_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(RESOURCE)
                .setConnectTimeout(300000) // 5分钟超时
                .setSendPresence(false) // 注册时不发送状态
                .build()
            
            // 手动启用调试
            SmackConfiguration.DEBUG = true
            
            // 创建连接
            connection = XMPPTCPConnection(config)
            
            // 详细记录连接过程
            Log.d(TAG, "正在连接到XMPP服务器...")
            connection.connect()
            Log.d(TAG, "已成功连接到XMPP服务器")
            
            // 先进行匿名连接
            connection.login("qinchong", "qc@BIT")  // 使用空用户名和密码进行匿名连接
            Log.d(TAG, "匿名登录成功")
            // 获取账户管理器并执行注册
            val accountManager = AccountManager.getInstance(connection)
            
            // 允许在不安全连接上执行敏感操作
            accountManager.sensitiveOperationOverInsecureConnection(true)
            
            // 准备注册属性
            val attributes = HashMap<String, String>()
            if (nickname != null) attributes["name"] = nickname
            if (email != null) attributes["email"] = email
            
            // 尝试创建账户
            try {
                Log.d(TAG, "开始创建账户...")
                if (attributes.isEmpty()) {
                    accountManager.createAccount(Localpart.from(username), password)
                } else {
                    accountManager.createAccount(Localpart.from(username), password, attributes)
                }
                Log.d(TAG, "XMPP注册成功: 用户名=$username")
                return@withContext Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "创建账户失败: ${e.message}", e)
                throw e
            }
        } catch (e: Exception) {
            Log.e(TAG, "XMPP注册过程中出错: ${e.message}", e)
            return@withContext Result.failure(e)
        } finally {
            // 确保关闭连接
            try {
                connection?.disconnect()
                Log.d(TAG, "XMPP连接已断开")
            } catch (e: Exception) {
                Log.e(TAG, "断开连接时出错: ${e.message}", e)
            }
        }
    }
} 