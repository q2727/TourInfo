package com.example.travalms.di;

import com.example.travalms.data.db.ChatDatabase;
import com.example.travalms.data.db.GroupChatMessageDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideGroupChatMessageDaoFactory implements Factory<GroupChatMessageDao> {
  private final Provider<ChatDatabase> databaseProvider;

  public DatabaseModule_ProvideGroupChatMessageDaoFactory(Provider<ChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GroupChatMessageDao get() {
    return provideGroupChatMessageDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideGroupChatMessageDaoFactory create(
      Provider<ChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideGroupChatMessageDaoFactory(databaseProvider);
  }

  public static GroupChatMessageDao provideGroupChatMessageDao(ChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupChatMessageDao(database));
  }
}
