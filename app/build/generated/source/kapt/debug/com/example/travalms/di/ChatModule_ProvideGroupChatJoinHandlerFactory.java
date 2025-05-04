package com.example.travalms.di;

import com.example.travalms.data.api.GroupChatApiClient;
import com.example.travalms.data.remote.GroupChatJoinHandler;
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
public final class ChatModule_ProvideGroupChatJoinHandlerFactory implements Factory<GroupChatJoinHandler> {
  private final Provider<GroupChatRepository> groupChatRepositoryProvider;

  private final Provider<XMPPManager> xmppManagerProvider;

  private final Provider<GroupChatApiClient> groupChatApiClientProvider;

  public ChatModule_ProvideGroupChatJoinHandlerFactory(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatApiClient> groupChatApiClientProvider) {
    this.groupChatRepositoryProvider = groupChatRepositoryProvider;
    this.xmppManagerProvider = xmppManagerProvider;
    this.groupChatApiClientProvider = groupChatApiClientProvider;
  }

  @Override
  public GroupChatJoinHandler get() {
    return provideGroupChatJoinHandler(groupChatRepositoryProvider.get(), xmppManagerProvider.get(), groupChatApiClientProvider.get());
  }

  public static ChatModule_ProvideGroupChatJoinHandlerFactory create(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatApiClient> groupChatApiClientProvider) {
    return new ChatModule_ProvideGroupChatJoinHandlerFactory(groupChatRepositoryProvider, xmppManagerProvider, groupChatApiClientProvider);
  }

  public static GroupChatJoinHandler provideGroupChatJoinHandler(
      GroupChatRepository groupChatRepository, XMPPManager xmppManager,
      GroupChatApiClient groupChatApiClient) {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideGroupChatJoinHandler(groupChatRepository, xmppManager, groupChatApiClient));
  }
}
