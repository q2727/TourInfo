package com.example.travalms.data.remote;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class XMPPService_MembersInjector implements MembersInjector<XMPPService> {
  private final Provider<GroupChatManager> groupChatManagerProvider;

  private final Provider<GroupChatJoinHandler> groupChatJoinHandlerProvider;

  public XMPPService_MembersInjector(Provider<GroupChatManager> groupChatManagerProvider,
      Provider<GroupChatJoinHandler> groupChatJoinHandlerProvider) {
    this.groupChatManagerProvider = groupChatManagerProvider;
    this.groupChatJoinHandlerProvider = groupChatJoinHandlerProvider;
  }

  public static MembersInjector<XMPPService> create(
      Provider<GroupChatManager> groupChatManagerProvider,
      Provider<GroupChatJoinHandler> groupChatJoinHandlerProvider) {
    return new XMPPService_MembersInjector(groupChatManagerProvider, groupChatJoinHandlerProvider);
  }

  @Override
  public void injectMembers(XMPPService instance) {
    injectGroupChatManager(instance, groupChatManagerProvider.get());
    injectGroupChatJoinHandler(instance, groupChatJoinHandlerProvider.get());
  }

  @InjectedFieldSignature("com.example.travalms.data.remote.XMPPService.groupChatManager")
  public static void injectGroupChatManager(XMPPService instance,
      GroupChatManager groupChatManager) {
    instance.groupChatManager = groupChatManager;
  }

  @InjectedFieldSignature("com.example.travalms.data.remote.XMPPService.groupChatJoinHandler")
  public static void injectGroupChatJoinHandler(XMPPService instance,
      GroupChatJoinHandler groupChatJoinHandler) {
    instance.groupChatJoinHandler = groupChatJoinHandler;
  }
}
