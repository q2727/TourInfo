package com.example.travalms.data.remote;

import android.content.Context;
import android.util.Log;
import com.example.travalms.data.model.ChatInvitation;
import com.example.travalms.data.model.ChatMessage;
import com.example.travalms.data.model.ContactItem;
import com.example.travalms.data.model.Message;
import com.example.travalms.config.AppConfig;
import kotlinx.coroutines.*;
import kotlinx.coroutines.flow.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StandardExtensionElement;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.StanzaError;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.pubsub.*;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.jivesoftware.smackx.pubsub.packet.PubSub;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.pubsub.form.FillableConfigureForm;
import java.time.LocalDateTime;
import java.util.*;
import org.jxmpp.jid.*;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.StanzaListener;
import org.jxmpp.jid.EntityBareJid;

/**
 * PubSub通知数据类
 * 用于表示从XMPP PubSub系统接收到的通知
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/example/travalms/data/remote/PubSubNotification;", "", "nodeId", "", "itemId", "payload", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getItemId", "()Ljava/lang/String;", "getNodeId", "getPayload", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class PubSubNotification {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String nodeId = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String itemId = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String payload = null;
    
    /**
     * PubSub通知数据类
     * 用于表示从XMPP PubSub系统接收到的通知
     */
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.remote.PubSubNotification copy(@org.jetbrains.annotations.NotNull
    java.lang.String nodeId, @org.jetbrains.annotations.NotNull
    java.lang.String itemId, @org.jetbrains.annotations.NotNull
    java.lang.String payload) {
        return null;
    }
    
    /**
     * PubSub通知数据类
     * 用于表示从XMPP PubSub系统接收到的通知
     */
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    /**
     * PubSub通知数据类
     * 用于表示从XMPP PubSub系统接收到的通知
     */
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * PubSub通知数据类
     * 用于表示从XMPP PubSub系统接收到的通知
     */
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String toString() {
        return null;
    }
    
    public PubSubNotification(@org.jetbrains.annotations.NotNull
    java.lang.String nodeId, @org.jetbrains.annotations.NotNull
    java.lang.String itemId, @org.jetbrains.annotations.NotNull
    java.lang.String payload) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNodeId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getItemId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getPayload() {
        return null;
    }
}