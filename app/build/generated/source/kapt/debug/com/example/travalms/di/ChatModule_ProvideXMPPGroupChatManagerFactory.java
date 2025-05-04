package com.example.travalms.di;

import com.example.travalms.data.remote.XMPPGroupChatManager;
import com.example.travalms.data.remote.XMPPManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class ChatModule_ProvideXMPPGroupChatManagerFactory implements Factory<XMPPGroupChatManager> {
  private final Provider<XMPPManager> xmppManagerProvider;

  public ChatModule_ProvideXMPPGroupChatManagerFactory(Provider<XMPPManager> xmppManagerProvider) {
    this.xmppManagerProvider = xmppManagerProvider;
  }

  @Override
  public XMPPGroupChatManager get() {
    return provideXMPPGroupChatManager(xmppManagerProvider.get());
  }

  public static ChatModule_ProvideXMPPGroupChatManagerFactory create(
      Provider<XMPPManager> xmppManagerProvider) {
    return new ChatModule_ProvideXMPPGroupChatManagerFactory(xmppManagerProvider);
  }

  public static XMPPGroupChatManager provideXMPPGroupChatManager(XMPPManager xmppManager) {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideXMPPGroupChatManager(xmppManager));
  }
}
