package com.example.travalms.data.remote;

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
public final class GroupChatManager_Factory implements Factory<GroupChatManager> {
  private final Provider<GroupChatRepository> groupChatRepositoryProvider;

  private final Provider<XMPPManager> xmppManagerProvider;

  public GroupChatManager_Factory(Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider) {
    this.groupChatRepositoryProvider = groupChatRepositoryProvider;
    this.xmppManagerProvider = xmppManagerProvider;
  }

  @Override
  public GroupChatManager get() {
    return newInstance(groupChatRepositoryProvider.get(), xmppManagerProvider.get());
  }

  public static GroupChatManager_Factory create(
      Provider<GroupChatRepository> groupChatRepositoryProvider,
      Provider<XMPPManager> xmppManagerProvider) {
    return new GroupChatManager_Factory(groupChatRepositoryProvider, xmppManagerProvider);
  }

  public static GroupChatManager newInstance(GroupChatRepository groupChatRepository,
      XMPPManager xmppManager) {
    return new GroupChatManager(groupChatRepository, xmppManager);
  }
}
