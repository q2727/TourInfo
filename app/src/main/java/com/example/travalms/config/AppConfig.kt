package com.example.travalms.config

/**
 * 应用程序配置常量
 * 集中管理所有硬编码的IP地址和服务器配置
 */
object AppConfig {
    // API服务器地址 - 确保使用开发机器在局域网中的实际IP地址
    const val API_SERVER_ADDRESS = "192.168.1.3" // 使用你的开发机器实际IP
    const val API_SERVER_PORT = "8080"
    const val API_BASE_URL = "http://$API_SERVER_ADDRESS:$API_SERVER_PORT/"
    
    // XMPP服务器地址
    const val XMPP_SERVER_HOST = "120.46.26.49"
    
    // 头像服务器地址（替换localhost使用）
    const val AVATAR_SERVER_ADDRESS = "192.168.1.3" // 使用相同的IP
}