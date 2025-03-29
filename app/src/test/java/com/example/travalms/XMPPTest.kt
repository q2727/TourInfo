package com.example.travalms

import kotlinx.coroutines.runBlocking
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart
import java.util.HashMap

fun main() = runBlocking {
    println("开始测试 XMPP 注册...")
    
    // 测试参数
    val username = "testuser123"
    val password = "testpass123"
    val nickname = "Test User"
    val email = "test@example.com"
    
    // 服务器配置
    val SERVER_DOMAIN = "120.46.26.49"
    val SERVER_HOST = "120.46.26.49"
    val SERVER_PORT = 5222
    val RESOURCE = "TestClient"
    
    println("用户名: $username")
    println("密码: $password")
    println("昵称: $nickname")
    println("邮箱: $email")
    
    // 确保 Smack 配置正确初始化
    try {
        // 手动设置解析器
        System.setProperty("smack.debugEnabled", "true")
        
        var connection: XMPPTCPConnection? = null
        
        try {
            // 创建连接配置
            val config = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(SERVER_DOMAIN)
                .setHost(SERVER_HOST)
                .setPort(SERVER_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setResource(RESOURCE)
                .setConnectTimeout(300000)
                .setSendPresence(false)
                .build()
            
            // 创建连接
            connection = XMPPTCPConnection(config)
            
            println("正在连接到XMPP服务器...")
            connection.connect()
            println("已成功连接到XMPP服务器")
            
            // 使用已有账户登录，而不是匿名登录
            connection.login("qinchong", "qc@BIT")  // 使用你的 XMPPManager 中的登录凭据
            println("登录成功")
            
            // 获取账户管理器
            val accountManager = AccountManager.getInstance(connection)
            accountManager.sensitiveOperationOverInsecureConnection(true)
            
            // 准备注册属性
            val attributes = HashMap<String, String>()
            attributes["name"] = nickname
            attributes["email"] = email
            
            // 创建账户
            println("开始创建账户...")
            accountManager.createAccount(Localpart.from(username), password, attributes)
            println("XMPP注册成功: 用户名=$username")
            
        } catch (e: Exception) {
            println("注册失败: ${e.message}")
            e.printStackTrace()
        } finally {
            try {
                connection?.disconnect()
                println("XMPP连接已断开")
            } catch (e: Exception) {
                println("断开连接时出错: ${e.message}")
            }
        }
    } catch (e: Exception) {
        println("Smack 初始化失败: ${e.message}")
        e.printStackTrace()
    }
} 