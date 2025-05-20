package com.example.travalms.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GroupChatDao_Impl implements GroupChatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GroupChatEntity> __insertionAdapterOfGroupChatEntity;

  private final DateConverters __dateConverters = new DateConverters();

  private final EntityDeletionOrUpdateAdapter<GroupChatEntity> __deletionAdapterOfGroupChatEntity;

  private final EntityDeletionOrUpdateAdapter<GroupChatEntity> __updateAdapterOfGroupChatEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteGroupChatByJid;

  private final SharedSQLiteStatement __preparedStmtOfUpdateGroupChatActivity;

  private final SharedSQLiteStatement __preparedStmtOfClearUnreadCount;

  public GroupChatDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroupChatEntity = new EntityInsertionAdapter<GroupChatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `group_chats` (`roomJid`,`name`,`description`,`memberCount`,`isPrivate`,`joinTime`,`lastActivityTime`,`unreadCount`,`lastMessage`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupChatEntity entity) {
        if (entity.getRoomJid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRoomJid());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getMemberCount());
        final int _tmp = entity.isPrivate() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final Long _tmp_1 = __dateConverters.dateToTimestamp(entity.getJoinTime());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp_1);
        }
        final Long _tmp_2 = __dateConverters.dateToTimestamp(entity.getLastActivityTime());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_2);
        }
        statement.bindLong(8, entity.getUnreadCount());
        if (entity.getLastMessage() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLastMessage());
        }
      }
    };
    this.__deletionAdapterOfGroupChatEntity = new EntityDeletionOrUpdateAdapter<GroupChatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `group_chats` WHERE `roomJid` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupChatEntity entity) {
        if (entity.getRoomJid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRoomJid());
        }
      }
    };
    this.__updateAdapterOfGroupChatEntity = new EntityDeletionOrUpdateAdapter<GroupChatEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `group_chats` SET `roomJid` = ?,`name` = ?,`description` = ?,`memberCount` = ?,`isPrivate` = ?,`joinTime` = ?,`lastActivityTime` = ?,`unreadCount` = ?,`lastMessage` = ? WHERE `roomJid` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupChatEntity entity) {
        if (entity.getRoomJid() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getRoomJid());
        }
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindLong(4, entity.getMemberCount());
        final int _tmp = entity.isPrivate() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final Long _tmp_1 = __dateConverters.dateToTimestamp(entity.getJoinTime());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp_1);
        }
        final Long _tmp_2 = __dateConverters.dateToTimestamp(entity.getLastActivityTime());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, _tmp_2);
        }
        statement.bindLong(8, entity.getUnreadCount());
        if (entity.getLastMessage() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLastMessage());
        }
        if (entity.getRoomJid() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getRoomJid());
        }
      }
    };
    this.__preparedStmtOfDeleteGroupChatByJid = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_chats WHERE roomJid = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateGroupChatActivity = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_chats SET lastActivityTime = ?, unreadCount = unreadCount + ?, lastMessage = ? WHERE roomJid = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearUnreadCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE group_chats SET unreadCount = 0 WHERE roomJid = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertGroupChat(final GroupChatEntity groupChat,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupChatEntity.insert(groupChat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertGroupChats(final List<GroupChatEntity> groupChats,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupChatEntity.insert(groupChats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteGroupChat(final GroupChatEntity groupChat,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGroupChatEntity.handle(groupChat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateGroupChat(final GroupChatEntity groupChat,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGroupChatEntity.handle(groupChat);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteGroupChatByJid(final String roomJid,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteGroupChatByJid.acquire();
        int _argIndex = 1;
        if (roomJid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, roomJid);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteGroupChatByJid.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateGroupChatActivity(final String roomJid, final LocalDateTime lastActivityTime,
      final int incrementUnread, final String lastMessage,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateGroupChatActivity.acquire();
        int _argIndex = 1;
        final Long _tmp = __dateConverters.dateToTimestamp(lastActivityTime);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, incrementUnread);
        _argIndex = 3;
        if (lastMessage == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, lastMessage);
        }
        _argIndex = 4;
        if (roomJid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, roomJid);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateGroupChatActivity.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object clearUnreadCount(final String roomJid,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearUnreadCount.acquire();
        int _argIndex = 1;
        if (roomJid == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, roomJid);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearUnreadCount.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<GroupChatEntity>> getAllGroupChats() {
    final String _sql = "SELECT `group_chats`.`roomJid` AS `roomJid`, `group_chats`.`name` AS `name`, `group_chats`.`description` AS `description`, `group_chats`.`memberCount` AS `memberCount`, `group_chats`.`isPrivate` AS `isPrivate`, `group_chats`.`joinTime` AS `joinTime`, `group_chats`.`lastActivityTime` AS `lastActivityTime`, `group_chats`.`unreadCount` AS `unreadCount`, `group_chats`.`lastMessage` AS `lastMessage` FROM group_chats ORDER BY lastActivityTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_chats"}, new Callable<List<GroupChatEntity>>() {
      @Override
      @NonNull
      public List<GroupChatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRoomJid = 0;
          final int _cursorIndexOfName = 1;
          final int _cursorIndexOfDescription = 2;
          final int _cursorIndexOfMemberCount = 3;
          final int _cursorIndexOfIsPrivate = 4;
          final int _cursorIndexOfJoinTime = 5;
          final int _cursorIndexOfLastActivityTime = 6;
          final int _cursorIndexOfUnreadCount = 7;
          final int _cursorIndexOfLastMessage = 8;
          final List<GroupChatEntity> _result = new ArrayList<GroupChatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupChatEntity _item;
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final LocalDateTime _tmpJoinTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfJoinTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfJoinTime);
            }
            _tmpJoinTime = __dateConverters.fromTimestamp(_tmp_1);
            final LocalDateTime _tmpLastActivityTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastActivityTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastActivityTime);
            }
            _tmpLastActivityTime = __dateConverters.fromTimestamp(_tmp_2);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            _item = new GroupChatEntity(_tmpRoomJid,_tmpName,_tmpDescription,_tmpMemberCount,_tmpIsPrivate,_tmpJoinTime,_tmpLastActivityTime,_tmpUnreadCount,_tmpLastMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getGroupChatByJid(final String roomJid,
      final Continuation<? super GroupChatEntity> continuation) {
    final String _sql = "SELECT * FROM group_chats WHERE roomJid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (roomJid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, roomJid);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GroupChatEntity>() {
      @Override
      @Nullable
      public GroupChatEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfJoinTime = CursorUtil.getColumnIndexOrThrow(_cursor, "joinTime");
          final int _cursorIndexOfLastActivityTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastActivityTime");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final GroupChatEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final LocalDateTime _tmpJoinTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfJoinTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfJoinTime);
            }
            _tmpJoinTime = __dateConverters.fromTimestamp(_tmp_1);
            final LocalDateTime _tmpLastActivityTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastActivityTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastActivityTime);
            }
            _tmpLastActivityTime = __dateConverters.fromTimestamp(_tmp_2);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            _result = new GroupChatEntity(_tmpRoomJid,_tmpName,_tmpDescription,_tmpMemberCount,_tmpIsPrivate,_tmpJoinTime,_tmpLastActivityTime,_tmpUnreadCount,_tmpLastMessage);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object isGroupChatJoined(final String roomJid,
      final Continuation<? super Integer> continuation) {
    final String _sql = "SELECT COUNT(*) FROM group_chats WHERE roomJid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (roomJid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, roomJid);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<Integer> getTotalUnreadCount() {
    final String _sql = "SELECT SUM(unreadCount) FROM group_chats";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_chats"}, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GroupChatEntity>> searchGroupChats(final String query) {
    final String _sql = "SELECT * FROM group_chats WHERE name LIKE '%' || ? || '%' ORDER BY lastActivityTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_chats"}, new Callable<List<GroupChatEntity>>() {
      @Override
      @NonNull
      public List<GroupChatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfJoinTime = CursorUtil.getColumnIndexOrThrow(_cursor, "joinTime");
          final int _cursorIndexOfLastActivityTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastActivityTime");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final List<GroupChatEntity> _result = new ArrayList<GroupChatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupChatEntity _item;
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final LocalDateTime _tmpJoinTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfJoinTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfJoinTime);
            }
            _tmpJoinTime = __dateConverters.fromTimestamp(_tmp_1);
            final LocalDateTime _tmpLastActivityTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastActivityTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastActivityTime);
            }
            _tmpLastActivityTime = __dateConverters.fromTimestamp(_tmp_2);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            _item = new GroupChatEntity(_tmpRoomJid,_tmpName,_tmpDescription,_tmpMemberCount,_tmpIsPrivate,_tmpJoinTime,_tmpLastActivityTime,_tmpUnreadCount,_tmpLastMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GroupChatEntity>> getRecentGroupChats(final int limit) {
    final String _sql = "SELECT * FROM group_chats ORDER BY lastActivityTime DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_chats"}, new Callable<List<GroupChatEntity>>() {
      @Override
      @NonNull
      public List<GroupChatEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMemberCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memberCount");
          final int _cursorIndexOfIsPrivate = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrivate");
          final int _cursorIndexOfJoinTime = CursorUtil.getColumnIndexOrThrow(_cursor, "joinTime");
          final int _cursorIndexOfLastActivityTime = CursorUtil.getColumnIndexOrThrow(_cursor, "lastActivityTime");
          final int _cursorIndexOfUnreadCount = CursorUtil.getColumnIndexOrThrow(_cursor, "unreadCount");
          final int _cursorIndexOfLastMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMessage");
          final List<GroupChatEntity> _result = new ArrayList<GroupChatEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupChatEntity _item;
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpMemberCount;
            _tmpMemberCount = _cursor.getInt(_cursorIndexOfMemberCount);
            final boolean _tmpIsPrivate;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPrivate);
            _tmpIsPrivate = _tmp != 0;
            final LocalDateTime _tmpJoinTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfJoinTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfJoinTime);
            }
            _tmpJoinTime = __dateConverters.fromTimestamp(_tmp_1);
            final LocalDateTime _tmpLastActivityTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfLastActivityTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfLastActivityTime);
            }
            _tmpLastActivityTime = __dateConverters.fromTimestamp(_tmp_2);
            final int _tmpUnreadCount;
            _tmpUnreadCount = _cursor.getInt(_cursorIndexOfUnreadCount);
            final String _tmpLastMessage;
            if (_cursor.isNull(_cursorIndexOfLastMessage)) {
              _tmpLastMessage = null;
            } else {
              _tmpLastMessage = _cursor.getString(_cursorIndexOfLastMessage);
            }
            _item = new GroupChatEntity(_tmpRoomJid,_tmpName,_tmpDescription,_tmpMemberCount,_tmpIsPrivate,_tmpJoinTime,_tmpLastActivityTime,_tmpUnreadCount,_tmpLastMessage);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
