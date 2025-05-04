package com.example.travalms.di;

import com.example.travalms.data.remote.XMPPServiceLocator;
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
public final class ChatModule_ProvideXMPPServiceLocatorFactory implements Factory<XMPPServiceLocator> {
  @Override
  public XMPPServiceLocator get() {
    return provideXMPPServiceLocator();
  }

  public static ChatModule_ProvideXMPPServiceLocatorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static XMPPServiceLocator provideXMPPServiceLocator() {
    return Preconditions.checkNotNullFromProvides(ChatModule.INSTANCE.provideXMPPServiceLocator());
  }

  private static final class InstanceHolder {
    private static final ChatModule_ProvideXMPPServiceLocatorFactory INSTANCE = new ChatModule_ProvideXMPPServiceLocatorFactory();
  }
}
