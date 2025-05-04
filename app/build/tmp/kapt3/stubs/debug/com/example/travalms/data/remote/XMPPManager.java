package com.example.travalms.data.remote;

import android.content.Context;
import android.util.Log;
import com.example.travalms.data.model.ChatInvitation;
import com.example.travalms.data.model.ChatMessage;
import com.example.travalms.data.model.ContactItem;
import com.example.travalms.data.model.Message;
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
 * XMPP连接和认证的管理类
 * 负责处理与XMPP服务器的所有交互
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u00f2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\"\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b5\b\u0007\u0018\u0000 \u00ba\u00012\u00020\u0001:\u0002\u00ba\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J6\u0010G\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140.0\u000e2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00140.H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bI\u0010JJ6\u0010K\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140.0\u000e2\f\u0010H\u001a\b\u0012\u0004\u0012\u00020\u00140.H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bL\u0010JJ\b\u0010M\u001a\u00020\u000fH\u0002J\u000e\u0010N\u001a\u00020\u000f2\u0006\u0010O\u001a\u00020PJ>\u0010Q\u001a\b\u0012\u0004\u0012\u00020R0\u000e2\u0006\u0010S\u001a\u00020\u00142\b\u0010T\u001a\u0004\u0018\u00010\u00142\b\u0010U\u001a\u0004\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bV\u0010WJ\u0006\u0010X\u001a\u00020\u000fJ\u0006\u0010Y\u001a\u00020\u000fJ(\u0010Z\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140.0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b[\u0010\\J:\u0010]\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110.0\u000e2\u0006\u0010^\u001a\u00020\u00142\b\b\u0002\u0010_\u001a\u00020`H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\ba\u0010bJ\b\u0010c\u001a\u0004\u0018\u00010\u001eJ\b\u0010d\u001a\u00020eH\u0002J*\u0010f\u001a\b\u0012\u0004\u0012\u00020\u00140\u000e2\u0006\u0010g\u001a\u00020hH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bi\u0010jJ6\u0010k\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140;0\u000e2\u0006\u0010l\u001a\u00020hH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bm\u0010jJ8\u0010n\u001a\u001e\u0012\u001a\u0012\u0018\u0012\u0014\u0012\u0012\u0012\u0006\u0012\u0004\u0018\u00010h\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u00130.0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bo\u0010\\J(\u0010p\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0q0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\br\u0010\\J.\u0010s\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020h\u0012\u0004\u0012\u00020\u00140;0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bt\u0010\\J\u0010\u0010u\u001a\u00020\u00142\u0006\u0010g\u001a\u00020hH\u0002J0\u0010v\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160.0\u000e2\u0006\u0010S\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bw\u0010xJ(\u0010y\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020h0.0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\bz\u0010\\J\u0019\u0010{\u001a\u00020\u00142\u0006\u0010g\u001a\u00020hH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010jJ6\u0010|\u001a\u001c\u0012\u0018\u0012\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020h\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u00130.0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b}\u0010\\J\b\u0010~\u001a\u0004\u0018\u00010\u0014J/\u0010\u007f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140;0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u0080\u0001\u0010\\J+\u0010\u0081\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0005\u0012\u00030\u0082\u00010.0\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u0083\u0001\u0010\\J\t\u0010\u0084\u0001\u001a\u00020\u000fH\u0002J\b\u0010\u0085\u0001\u001a\u00030\u0086\u0001J\n\u0010\u0087\u0001\u001a\u00030\u0086\u0001H\u0002J\u0013\u0010\u0088\u0001\u001a\u00030\u0086\u00012\u0007\u0010\u0089\u0001\u001a\u00020\u0014H\u0002JC\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0007\u0010\u008b\u0001\u001a\u00020\u00142\u0007\u0010\u008c\u0001\u001a\u00020\u00142\n\b\u0002\u0010\u008d\u0001\u001a\u00030\u0086\u0001H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0006\b\u008e\u0001\u0010\u008f\u0001J\u000f\u0010\u0090\u0001\u001a\u00020\u000f2\u0006\u0010O\u001a\u00020PJ\t\u0010\u0091\u0001\u001a\u00020\u000fH\u0002J@\u0010\u0092\u0001\u001a\b\u0012\u0004\u0012\u00020\u00140\u000e2\u0006\u0010S\u001a\u00020\u00142\u0007\u0010\u0093\u0001\u001a\u00020\u00142\t\b\u0002\u0010\u0094\u0001\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u0095\u0001\u0010WJ\u00a9\u0001\u0010\u0096\u0001\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0007\u0010\u008b\u0001\u001a\u00020\u00142\u0007\u0010\u008c\u0001\u001a\u00020\u00142\u000b\b\u0002\u0010\u0097\u0001\u001a\u0004\u0018\u00010\u00142\u000b\b\u0002\u0010\u0098\u0001\u001a\u0004\u0018\u00010\u00142\u0007\u0010\u0099\u0001\u001a\u00020\u00142\u0007\u0010\u009a\u0001\u001a\u00020\u00142\u0007\u0010\u009b\u0001\u001a\u00020\u00142\u0007\u0010\u009c\u0001\u001a\u00020\u00142\u000b\b\u0002\u0010\u009d\u0001\u001a\u0004\u0018\u00010\u00142\u000b\b\u0002\u0010\u009e\u0001\u001a\u0004\u0018\u00010\u00142\u000b\b\u0002\u0010\u009f\u0001\u001a\u0004\u0018\u00010\u00142\u000b\b\u0002\u0010\u00a0\u0001\u001a\u0004\u0018\u00010\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0006\b\u00a1\u0001\u0010\u00a2\u0001J\u0007\u0010\u00a3\u0001\u001a\u00020\u000fJ!\u0010\u00a4\u0001\u001a\u00020\u000f2\u0006\u0010O\u001a\u00020P2\u0007\u0010\u008b\u0001\u001a\u00020\u00142\u0007\u0010\u008c\u0001\u001a\u00020\u0014J-\u0010\u00a5\u0001\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0007\u0010\u00a6\u0001\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u00a7\u0001\u0010xJ\t\u0010\u00a8\u0001\u001a\u00020\u000fH\u0002J\u0007\u0010\u00a9\u0001\u001a\u00020\u000fJ7\u0010\u00aa\u0001\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e2\u0007\u0010\u00ab\u0001\u001a\u00020\u00142\u0007\u0010\u00ac\u0001\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0006\b\u00ad\u0001\u0010\u00ae\u0001J%\u0010\u00af\u0001\u001a\t\u0012\u0005\u0012\u00030\u0086\u00010\u000eH\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u00b0\u0001\u0010\\J\t\u0010\u00b1\u0001\u001a\u00020\u000fH\u0002J\t\u0010\u00b2\u0001\u001a\u00020\u000fH\u0002J\t\u0010\u00b3\u0001\u001a\u00020\u000fH\u0002J\t\u0010\u00b4\u0001\u001a\u00020\u000fH\u0002J\t\u0010\u00b5\u0001\u001a\u00020\u000fH\u0002J-\u0010\u00b6\u0001\u001a\t\u0012\u0005\u0012\u00030\u0086\u00010\u000e2\u0006\u0010S\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u00b7\u0001\u0010xJ-\u0010\u00b8\u0001\u001a\t\u0012\u0005\u0012\u00030\u0086\u00010\u000e2\u0006\u0010S\u001a\u00020\u0014H\u0086@\u00f8\u0001\u0001\u00f8\u0001\u0002\u00f8\u0001\u0000\u00f8\u0001\u0000\u00a2\u0006\u0005\b\u00b9\u0001\u0010xR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\rX\u0082\u0004\u00f8\u0001\u0000\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0012\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140\u00130\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\"\u0010\u001f\u001a\u0004\u0018\u00010\u001e2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b#\u0010\tR\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0)\u00f8\u0001\u0000\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R \u0010,\u001a\u0014\u0012\u0004\u0012\u00020\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160.0-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00110)\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010+R&\u00101\u001a\u001a\u0012\u0004\u0012\u00020\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020403020-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u00105\u001a\u0004\u0018\u000106X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u00107\u001a\u0004\u0018\u000108X\u0082\u000e\u00a2\u0006\u0002\n\u0000RN\u00109\u001aB\u0012\f\u0012\n :*\u0004\u0018\u00010\u00140\u0014\u0012\f\u0012\n :*\u0004\u0018\u00010\u00140\u0014 :* \u0012\f\u0012\n :*\u0004\u0018\u00010\u00140\u0014\u0012\f\u0012\n :*\u0004\u0018\u00010\u00140\u0014\u0018\u00010;0-X\u0082\u0004\u00a2\u0006\u0002\n\u0000R#\u0010<\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140\u00130)\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010+R\u0010\u0010>\u001a\u0004\u0018\u00010?X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010@\u001a\b\u0012\u0004\u0012\u00020\u00160)\u00a2\u0006\b\n\u0000\u001a\u0004\bA\u0010+R\u0010\u0010B\u001a\u0004\u0018\u00010CX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\'X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010E\u001a\b\u0012\u0004\u0012\u00020\u00140FX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u00bb\u0001"}, d2 = {"Lcom/example/travalms/data/remote/XMPPManager;", "", "()V", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/travalms/data/remote/ConnectionState;", "_groupChatManager", "Lcom/example/travalms/data/remote/XMPPGroupChatManager;", "get_groupChatManager", "()Lcom/example/travalms/data/remote/XMPPGroupChatManager;", "_groupChatManager$delegate", "Lkotlin/Lazy;", "_loginResultFlow", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lkotlin/Result;", "", "_messageFlow", "Lcom/example/travalms/data/model/ChatMessage;", "_presenceUpdateFlow", "Lkotlin/Pair;", "", "_pubsubItemsFlow", "Lcom/example/travalms/data/remote/PubSubNotification;", "connectionListener", "Lorg/jivesoftware/smack/ConnectionListener;", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "<set-?>", "Lorg/jivesoftware/smack/tcp/XMPPTCPConnection;", "currentConnection", "getCurrentConnection", "()Lorg/jivesoftware/smack/tcp/XMPPTCPConnection;", "groupChatManager", "getGroupChatManager", "incomingChatMessageListener", "Lorg/jivesoftware/smack/chat2/IncomingChatMessageListener;", "initScope", "Lkotlinx/coroutines/CoroutineScope;", "loginResultFlow", "Lkotlinx/coroutines/flow/SharedFlow;", "getLoginResultFlow", "()Lkotlinx/coroutines/flow/SharedFlow;", "messageCache", "", "", "messageFlow", "getMessageFlow", "nodeListeners", "Lorg/jivesoftware/smackx/pubsub/listener/ItemEventListener;", "Lorg/jivesoftware/smackx/pubsub/PayloadItem;", "Lorg/jivesoftware/smackx/pubsub/SimplePayload;", "pingFailedListener", "Lorg/jivesoftware/smackx/ping/PingFailedListener;", "presenceListener", "Lorg/jivesoftware/smack/StanzaListener;", "presenceMap", "kotlin.jvm.PlatformType", "", "presenceUpdateFlow", "getPresenceUpdateFlow", "pubSubManager", "Lorg/jivesoftware/smackx/pubsub/PubSubManager;", "pubsubItemsFlow", "getPubsubItemsFlow", "rosterListener", "Lorg/jivesoftware/smack/roster/RosterListener;", "scope", "subscribedNodes", "", "batchSubscribe", "nodeIds", "batchSubscribe-gIAlu-s", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "batchUnsubscribe", "batchUnsubscribe-gIAlu-s", "checkConnection", "clearCredentials", "context", "Landroid/content/Context;", "createNode", "Lorg/jivesoftware/smackx/pubsub/LeafNode;", "nodeId", "name", "description", "createNode-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "forceDisconnect", "getAllNodes", "getAllNodes-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getChatHistory", "otherJid", "limit", "", "getChatHistory-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getConnection", "getConnectionConfig", "Lorg/jivesoftware/smack/tcp/XMPPTCPConnectionConfiguration;", "getFriendPresence", "jid", "Lorg/jxmpp/jid/BareJid;", "getFriendPresence-gIAlu-s", "(Lorg/jxmpp/jid/BareJid;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFriendProfile", "friendJid", "getFriendProfile-gIAlu-s", "getFriends", "getFriends-IoAF18A", "getFriendsJids", "", "getFriendsJids-IoAF18A", "getFriendsPresence", "getFriendsPresence-IoAF18A", "getLocalpartSafely", "getNodeItems", "getNodeItems-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecentContacts", "getRecentContacts-IoAF18A", "getSenderName", "getServerUsers", "getServerUsers-IoAF18A", "getUserNickname", "getUserProfile", "getUserProfile-IoAF18A", "getUserSubscriptions", "Lorg/jivesoftware/smackx/pubsub/Subscription;", "getUserSubscriptions-IoAF18A", "initPubSubManager", "isAuthenticated", "", "isConnected", "isServiceComponent", "localpart", "login", "username", "password", "forceLogin", "login-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "onLoginSuccess", "publishToNode", "content", "contentType", "publishToNode-BWLJW6A", "register", "nickname", "email", "companyName", "mobileNumber", "province", "city", "businessLicensePath", "idCardFrontPath", "idCardBackPath", "avatarPath", "register-1iavgos", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requestAllContactsPresence", "saveCredentials", "sendFriendRequest", "friendJidString", "sendFriendRequest-gIAlu-s", "sendInitialPresence", "sendKeepAlivePresence", "sendMessage", "recipientJid", "messageContent", "sendMessage-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendPing", "sendPing-IoAF18A", "setupMessageListener", "setupMessageListeners", "setupPingMechanism", "setupPresenceListener", "setupRosterListener", "subscribeToNode", "subscribeToNode-gIAlu-s", "unsubscribeFromNode", "unsubscribeFromNode-gIAlu-s", "Companion", "app_debug"})
public final class XMPPManager {
    @org.jetbrains.annotations.NotNull
    public static final com.example.travalms.data.remote.XMPPManager.Companion Companion = null;
    private static final java.lang.String TAG = "XMPPManager";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SERVER_DOMAIN = "localhost";
    private static final java.lang.String SERVER_HOST = "120.46.26.49";
    private static final int SERVER_PORT = 5222;
    private static final java.lang.String RESOURCE = "AndroidClient";
    private static final java.lang.String PUBSUB_SERVICE = "localhost";
    private static final int PING_INTERVAL = 30;
    private static final int INACTIVITY_TIMEOUT = 600;
    private static final int CONNECTION_TIMEOUT = 60000;
    @kotlin.jvm.Volatile
    private static volatile com.example.travalms.data.remote.XMPPManager INSTANCE;
    @org.jetbrains.annotations.Nullable
    private org.jivesoftware.smack.tcp.XMPPTCPConnection currentConnection;
    private org.jivesoftware.smackx.pubsub.PubSubManager pubSubManager;
    private final kotlin.Lazy _groupChatManager$delegate = null;
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.example.travalms.data.remote.PubSubNotification> _pubsubItemsFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.data.remote.PubSubNotification> pubsubItemsFlow = null;
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.example.travalms.data.model.ChatMessage> _messageFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.data.model.ChatMessage> messageFlow = null;
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Result<kotlin.Unit>> _loginResultFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Result<kotlin.Unit>> loginResultFlow = null;
    private final kotlinx.coroutines.flow.MutableSharedFlow<kotlin.Pair<java.lang.String, java.lang.String>> _presenceUpdateFlow = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<kotlin.Pair<java.lang.String, java.lang.String>> presenceUpdateFlow = null;
    private final java.util.Map<java.lang.String, java.lang.String> presenceMap = null;
    private final kotlinx.coroutines.CoroutineScope scope = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<com.example.travalms.data.remote.ConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.example.travalms.data.remote.ConnectionState> connectionState = null;
    private final kotlinx.coroutines.CoroutineScope initScope = null;
    private final java.util.Set<java.lang.String> subscribedNodes = null;
    private final java.util.Map<java.lang.String, org.jivesoftware.smackx.pubsub.listener.ItemEventListener<org.jivesoftware.smackx.pubsub.PayloadItem<org.jivesoftware.smackx.pubsub.SimplePayload>>> nodeListeners = null;
    private final java.util.Map<java.lang.String, java.util.List<com.example.travalms.data.remote.PubSubNotification>> messageCache = null;
    private org.jivesoftware.smack.chat2.IncomingChatMessageListener incomingChatMessageListener;
    private org.jivesoftware.smack.StanzaListener presenceListener;
    private org.jivesoftware.smack.roster.RosterListener rosterListener;
    private org.jivesoftware.smackx.ping.PingFailedListener pingFailedListener;
    private final org.jivesoftware.smack.ConnectionListener connectionListener = null;
    
    private XMPPManager() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.jivesoftware.smack.tcp.XMPPTCPConnection getCurrentConnection() {
        return null;
    }
    
    private final com.example.travalms.data.remote.XMPPGroupChatManager get_groupChatManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.example.travalms.data.remote.XMPPGroupChatManager getGroupChatManager() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.data.remote.PubSubNotification> getPubsubItemsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.example.travalms.data.model.ChatMessage> getMessageFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<kotlin.Result<kotlin.Unit>> getLoginResultFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<kotlin.Pair<java.lang.String, java.lang.String>> getPresenceUpdateFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.example.travalms.data.remote.ConnectionState> getConnectionState() {
        return null;
    }
    
    private final org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration getConnectionConfig() {
        return null;
    }
    
    /**
     * 初始化PubSub管理器
     * 确保使用正确的服务JID并配置IQ过滤器
     */
    private final void initPubSubManager() {
    }
    
    /**
     * 断开与XMPP服务器的连接并清理资源
     */
    public final void disconnect() {
    }
    
    /**
     * 配置XMPP连接的保活机制
     */
    private final void setupPingMechanism() {
    }
    
    /**
     * 发送初始在线状态
     */
    private final void sendInitialPresence() {
    }
    
    /**
     * 请求所有联系人的在线状态
     */
    public final void requestAllContactsPresence() {
    }
    
    private final java.lang.String getLocalpartSafely(org.jxmpp.jid.BareJid jid) {
        return null;
    }
    
    private final boolean isServiceComponent(java.lang.String localpart) {
        return false;
    }
    
    private final void setupMessageListener() {
    }
    
    private final java.lang.Object getSenderName(org.jxmpp.jid.BareJid jid, kotlin.coroutines.Continuation<? super java.lang.String> continuation) {
        return null;
    }
    
    private final void onLoginSuccess() {
    }
    
    /**
     * 保存登录凭据到安全存储
     * 注意：这是一个简单的实现，生产环境中应使用更安全的存储方式
     */
    public final void saveCredentials(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String username, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    /**
     * 清除保存的登录凭据
     */
    public final void clearCredentials(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 检查当前连接状态，如果未认证或连接无效则抛出异常
     * 此方法在需要确保连接有效的情况下使用
     */
    private final void checkConnection() {
    }
    
    /**
     * 初始化Roster（联系人列表）并配置监听器
     */
    private final void setupRosterListener() {
    }
    
    /**
     * 设置直接的Presence监听器，绕过Roster直接监听所有Presence包
     */
    private final void setupPresenceListener() {
    }
    
    /**
     * 添加消息监听器，处理各类消息 (Using ChatManager)
     */
    private final void setupMessageListeners() {
    }
    
    /**
     * 检查连接是否有效
     * @return 如果连接有效且已认证则返回true
     */
    private final boolean isConnected() {
        return false;
    }
    
    /**
     * 登出并清理资源
     * @param context Android Context, 用于访问 SharedPreferences
     */
    public final void logout(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    /**
     * 检查用户是否已登录并认证
     * @return 是否已认证
     */
    public final boolean isAuthenticated() {
        return false;
    }
    
    /**
     * 获取当前XMPP连接
     * @return XMPP连接对象，如果未连接则返回null
     */
    @org.jetbrains.annotations.Nullable
    public final org.jivesoftware.smack.tcp.XMPPTCPConnection getConnection() {
        return null;
    }
    
    /**
     * 获取当前用户昵称
     * @return 用户昵称，如果未能获取则返回null
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getUserNickname() {
        return null;
    }
    
    /**
     * 发送保活presence包，保持连接活跃
     * 这种方法可以防止某些服务器因客户端不活跃而断开连接
     */
    public final void sendKeepAlivePresence() {
    }
    
    /**
     * 强制断开XMPP连接，确保彻底释放资源
     * 在重连前调用此方法可以避免资源冲突
     */
    public final void forceDisconnect() {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0010\u001a\u00020\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/example/travalms/data/remote/XMPPManager$Companion;", "", "()V", "CONNECTION_TIMEOUT", "", "INACTIVITY_TIMEOUT", "INSTANCE", "Lcom/example/travalms/data/remote/XMPPManager;", "PING_INTERVAL", "PUBSUB_SERVICE", "", "RESOURCE", "SERVER_DOMAIN", "SERVER_HOST", "SERVER_PORT", "TAG", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.example.travalms.data.remote.XMPPManager getInstance() {
            return null;
        }
    }
}