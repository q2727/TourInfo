package com.example.travalms.data.remote

import android.util.Log
import org.jxmpp.jid.BareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException

/**
 * 将字符串转换为BareJid的扩展方法
 * 如果转换失败，返回null
 */
fun String.toBareJidOrNull(): BareJid? {
    return try {
        // 字符串必须是有效的JID格式 (user@domain)
        if (this.contains('@')) {
            JidCreate.bareFrom(this)
        } else {
            null
        }
    } catch (e: XmppStringprepException) {
        Log.e("JidExtensions", "JID格式转换失败: $this", e)
        null
    } catch (e: Exception) {
        Log.e("JidExtensions", "转换JID时发生未知错误: $this", e)
        null
    }
} 