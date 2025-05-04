package com.example.travalms.data.repository;

import com.example.travalms.data.db.GroupChatDao;
import com.example.travalms.data.db.GroupChatMessageDao;
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
public final class GroupChatRepository_Factory implements Factory<GroupChatRepository> {
  private final Provider<GroupChatDao> groupChatDaoProvider;

  private final Provider<GroupChatMessageDao> groupChatMessageDaoProvider;

  public GroupChatRepository_Factory(Provider<GroupChatDao> groupChatDaoProvider,
      Provider<GroupChatMessageDao> groupChatMessageDaoProvider) {
    this.groupChatDaoProvider = groupChatDaoProvider;
    this.groupChatMessageDaoProvider = groupChatMessageDaoProvider;
  }

  @Override
  public GroupChatRepository get() {
    return newInstance(groupChatDaoProvider.get(), groupChatMessageDaoProvider.get());
  }

  public static GroupChatRepository_Factory create(Provider<GroupChatDao> groupChatDaoProvider,
      Provider<GroupChatMessageDao> groupChatMessageDaoProvider) {
    return new GroupChatRepository_Factory(groupChatDaoProvider, groupChatMessageDaoProvider);
  }

  public static GroupChatRepository newInstance(GroupChatDao groupChatDao,
      GroupChatMessageDao groupChatMessageDao) {
    return new GroupChatRepository(groupChatDao, groupChatMessageDao);
  }
}
