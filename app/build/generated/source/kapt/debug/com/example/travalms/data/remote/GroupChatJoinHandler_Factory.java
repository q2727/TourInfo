package com.example.travalms.data.remote;

import com.example.travalms.data.api.GroupChatApiClient;
import com.example.travalms.data.repository.GroupChatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class GroupChatJoinHandler_Factory implements Factory<GroupChatJoinHandler> {
  private final Provider<GroupChatRepository> groupChatRepositoryProvider;

  private final Provider<XMPPManager> xmppManagerProvider;

  private final Provider<GroupChatApiClient> groupChatApiClientProvider;

  public GroupChatJoinHandler_Factory(Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatApiClient> groupChatApiClientProvider) {
    this.groupChatRepositoryProvider = groupChatRepositoryProvider;
    this.xmppManagerProvider = xmppManagerProvider;
    this.groupChatApiClientProvider = groupChatApiClientProvider;
  }

  @Override
  public GroupChatJoinHandler get() {
    return newInstance(groupChatRepositoryProvider.get(), xmppManagerProvider.get(), groupChatApiClientProvider.get());
  }

  public static GroupChatJoinHandler_Factory create(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatApiClient> groupChatApiClientProvider) {
    return new GroupChatJoinHandler_Factory(groupChatRepositoryProvider, xmppManagerProvider, groupChatApiClientProvider);
  }

  public static GroupChatJoinHandler newInstance(GroupChatRepository groupChatRepository,
      XMPPManager xmppManager, GroupChatApiClient groupChatApiClient) {
    return new GroupChatJoinHandler(groupChatRepository, xmppManager, groupChatApiClient);
  }
}
