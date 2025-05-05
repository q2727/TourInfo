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

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n\u00a8\u0006\u000b"}, d2 = {"Lcom/example/travalms/data/remote/ConnectionState;", "", "(Ljava/lang/String;I)V", "DISCONNECTED", "CONNECTING", "CONNECTED", "AUTHENTICATING", "AUTHENTICATED", "ERROR", "CONNECTION_CLOSED", "RECONNECTING", "app_debug"})
public enum ConnectionState {
    /*public static final*/ DISCONNECTED /* = new DISCONNECTED() */,
    /*public static final*/ CONNECTING /* = new CONNECTING() */,
    /*public static final*/ CONNECTED /* = new CONNECTED() */,
    /*public static final*/ AUTHENTICATING /* = new AUTHENTICATING() */,
    /*public static final*/ AUTHENTICATED /* = new AUTHENTICATED() */,
    /*public static final*/ ERROR /* = new ERROR() */,
    /*public static final*/ CONNECTION_CLOSED /* = new CONNECTION_CLOSED() */,
    /*public static final*/ RECONNECTING /* = new RECONNECTING() */;
    
    ConnectionState() {
    }
}