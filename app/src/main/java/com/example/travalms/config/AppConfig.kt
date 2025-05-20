package com.example.travalms.config

/**
 * 应用程序配置常量
 * 集中管理所有硬编码的IP地址和服务器配置
 */
object AppConfig {
    // 局域网开发服务器
    const val API_SERVER_ADDRESS = "10.63.152.125" // 局域网开发机器
    const val API_SERVER_PORT = "8080"
    const val API_BASE_URL = "http://$API_SERVER_ADDRESS:$API_SERVER_PORT/"

    // 正式产品服务器
    const val PROD_API_BASE_URL = "http://42.193.112.197/"
    const val PROD_IMAGE_BASE_URL = "http://42.193.112.197/static/"

    // XMPP服务器地址
    const val XMPP_SERVER_HOST = "120.46.26.49"

    // 头像服务器地址（替换localhost使用）
    const val AVATAR_SERVER_ADDRESS = "10.63.152.125" // 使用相同的IP
}