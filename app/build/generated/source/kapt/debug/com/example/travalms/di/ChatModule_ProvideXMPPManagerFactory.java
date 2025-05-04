package com.example.travalms.di;

import com.example.travalms.data.remote.XMPPManager;
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
public final class ChatModule_ProvideXMPPManagerFactory implements Factory<XMPPManager> {
  @Override
  public XMPPManager get() {
    return provideXMPPManager();
  }

  public static ChatModule_ProvideXMPPManagerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static XMPPManager provideXMPPManager() {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideXMPPManager());
  }

  private static final class InstanceHolder {
    private static final ChatModule_ProvideXMPPManagerFactory INSTANCE = new ChatModule_ProvideXMPPManagerFactory();
  }
}
