package com.example.travalms.di;

import com.example.travalms.data.api.GroupChatApiClient;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ChatModule_ProvideGroupChatApiClientFactory implements Factory<GroupChatApiClient> {
  @Override
  public GroupChatApiClient get() {
    return provideGroupChatApiClient();
  }

  public static ChatModule_ProvideGroupChatApiClientFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GroupChatApiClient provideGroupChatApiClient() {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideGroupChatApiClient());
  }

  private static final class InstanceHolder {
    private static final ChatModule_ProvideGroupChatApiClientFactory INSTANCE = new ChatModule_ProvideGroupChatApiClientFactory();
  }
}
