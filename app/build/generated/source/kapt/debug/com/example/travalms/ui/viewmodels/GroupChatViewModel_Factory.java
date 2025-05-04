package com.example.travalms.ui.viewmodels;

import com.example.travalms.data.remote.XMPPManager;
import com.example.travalms.data.repository.GroupChatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class GroupChatViewModel_Factory implements Factory<GroupChatViewModel> {
  private final Provider<XMPPManager> xmppManagerProvider;

  private final Provider<GroupChatRepository> groupChatRepositoryProvider;

  public GroupChatViewModel_Factory(Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatRepository> groupChatRepositoryProvider) {
    this.xmppManagerProvider = xmppManagerProvider;
    this.groupChatRepositoryProvider = groupChatRepositoryProvider;
  }

  @Override
  public GroupChatViewModel get() {
    return newInstance(xmppManagerProvider.get(), groupChatRepositoryProvider.get());
  }

  public static GroupChatViewModel_Factory create(Provider<XMPPManager> xmppManagerProvider,
      Provider<GroupChatRepository> groupChatRepositoryProvider) {
    return new GroupChatViewModel_Factory(xmppManagerProvider, groupChatRepositoryProvider);
  }

  public static GroupChatViewModel newInstance(XMPPManager xmppManager,
      GroupChatRepository groupChatRepository) {
    return new GroupChatViewModel(xmppManager, groupChatRepository);
  }
}
