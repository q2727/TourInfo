package com.example.travalms.di;

import com.example.travalms.data.db.ChatDatabase;
import com.example.travalms.data.db.GroupChatDao;
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
public final class DatabaseModule_ProvideGroupChatDaoFactory implements Factory<GroupChatDao> {
  private final Provider<ChatDatabase> databaseProvider;

  public DatabaseModule_ProvideGroupChatDaoFactory(Provider<ChatDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public GroupChatDao get() {
    return provideGroupChatDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideGroupChatDaoFactory create(
      Provider<ChatDatabase> databaseProvider) {
    return new DatabaseModule_ProvideGroupChatDaoFactory(databaseProvider);
  }

  public static GroupChatDao provideGroupChatDao(ChatDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideGroupChatDao(database));
  }
}
