package com.example.travalms.di;

import com.example.travalms.data.remote.GroupChatManager;
import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.repository.GroupChatRepository;
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
public final class ChatModule_ProvideGroupChatManagerFactory implements Factory<GroupChatManager> {
  private final Provider<GroupChatRepository> groupChatRepositoryProvider;

  private final Provider<XMPPManager> xmppManagerProvider;

  public ChatModule_ProvideGroupChatManagerFactory(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider) {
    this.groupChatRepositoryProvider = groupChatRepositoryProvider;
    this.xmppManagerProvider = xmppManagerProvider;
  }

  @Override
  public GroupChatManager get() {
    return provideGroupChatManager(groupChatRepositoryProvider.get(), xmppManagerProvider.get());
  }

  public static ChatModule_ProvideGroupChatManagerFactory create(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider) {
    return new ChatModule_ProvideGroupChatManagerFactory(groupChatRepositoryProvider, xmppManagerProvider);
  }

  public static GroupChatManager provideGroupChatManager(GroupChatRepository groupChatRepository,
      XMPPManager xmppManager) {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideGroupChatManager(groupChatRepository, xmppManager));
  }
}
