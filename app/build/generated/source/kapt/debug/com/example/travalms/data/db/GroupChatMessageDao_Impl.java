package com.example.travalms.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
public final class GroupChatMessageDao_Impl implements GroupChatMessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GroupChatMessageEntity> __insertionAdapterOfGroupChatMessageEntity;

  private final DateConverters __dateConverters = new DateConverters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteMessagesForRoom;

  public GroupChatMessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGroupChatMessageEntity = new EntityInsertionAdapter<GroupChatMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `group_chat_messages` (`id`,`roomJid`,`senderJid`,`senderNickname`,`content`,`timestamp`,`messageType`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GroupChatMessageEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getRoomJid() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRoomJid());
        }
        if (entity.getSenderJid() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSenderJid());
        }
        if (entity.getSenderNickname() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getSenderNickname());
        }
        if (entity.getContent() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getContent());
        }
        final Long _tmp = __dateConverters.dateToTimestamp(entity.getTimestamp());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
        if (entity.getMessageType() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMessageType());
        }
      }
    };
    this.__preparedStmtOfDeleteMessagesForRoom = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM group_chat_messages WHERE roomJid = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMessage(final GroupChatMessageEntity message,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupChatMessageEntity.insert(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insertMessages(final List<GroupChatMessageEntity> messages,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGroupChatMessageEntity.insert(messages);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteMessagesForRoom(final String roomJid, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMessagesForRoom.acquire();
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
          __preparedStmtOfDeleteMessagesForRoom.release(_stmt);
        }
      }
    }, arg1);
  }

  @Override
  public Flow<List<GroupChatMessageEntity>> getMessagesForRoomFlow(final String roomJid) {
    final String _sql = "SELECT * FROM group_chat_messages WHERE roomJid = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (roomJid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, roomJid);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"group_chat_messages"}, new Callable<List<GroupChatMessageEntity>>() {
      @Override
      @NonNull
      public List<GroupChatMessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfSenderJid = CursorUtil.getColumnIndexOrThrow(_cursor, "senderJid");
          final int _cursorIndexOfSenderNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "senderNickname");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final List<GroupChatMessageEntity> _result = new ArrayList<GroupChatMessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupChatMessageEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpSenderJid;
            if (_cursor.isNull(_cursorIndexOfSenderJid)) {
              _tmpSenderJid = null;
            } else {
              _tmpSenderJid = _cursor.getString(_cursorIndexOfSenderJid);
            }
            final String _tmpSenderNickname;
            if (_cursor.isNull(_cursorIndexOfSenderNickname)) {
              _tmpSenderNickname = null;
            } else {
              _tmpSenderNickname = _cursor.getString(_cursorIndexOfSenderNickname);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final LocalDateTime _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __dateConverters.fromTimestamp(_tmp);
            final String _tmpMessageType;
            if (_cursor.isNull(_cursorIndexOfMessageType)) {
              _tmpMessageType = null;
            } else {
              _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            }
            _item = new GroupChatMessageEntity(_tmpId,_tmpRoomJid,_tmpSenderJid,_tmpSenderNickname,_tmpContent,_tmpTimestamp,_tmpMessageType);
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
  public Object getMessagesForRoom(final String roomJid,
      final Continuation<? super List<GroupChatMessageEntity>> arg1) {
    final String _sql = "SELECT * FROM group_chat_messages WHERE roomJid = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (roomJid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, roomJid);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GroupChatMessageEntity>>() {
      @Override
      @NonNull
      public List<GroupChatMessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfSenderJid = CursorUtil.getColumnIndexOrThrow(_cursor, "senderJid");
          final int _cursorIndexOfSenderNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "senderNickname");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final List<GroupChatMessageEntity> _result = new ArrayList<GroupChatMessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GroupChatMessageEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpSenderJid;
            if (_cursor.isNull(_cursorIndexOfSenderJid)) {
              _tmpSenderJid = null;
            } else {
              _tmpSenderJid = _cursor.getString(_cursorIndexOfSenderJid);
            }
            final String _tmpSenderNickname;
            if (_cursor.isNull(_cursorIndexOfSenderNickname)) {
              _tmpSenderNickname = null;
            } else {
              _tmpSenderNickname = _cursor.getString(_cursorIndexOfSenderNickname);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final LocalDateTime _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __dateConverters.fromTimestamp(_tmp);
            final String _tmpMessageType;
            if (_cursor.isNull(_cursorIndexOfMessageType)) {
              _tmpMessageType = null;
            } else {
              _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            }
            _item = new GroupChatMessageEntity(_tmpId,_tmpRoomJid,_tmpSenderJid,_tmpSenderNickname,_tmpContent,_tmpTimestamp,_tmpMessageType);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object getLatestMessageForRoom(final String roomJid,
      final Continuation<? super GroupChatMessageEntity> arg1) {
    final String _sql = "SELECT * FROM group_chat_messages WHERE roomJid = ? ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (roomJid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, roomJid);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GroupChatMessageEntity>() {
      @Override
      @Nullable
      public GroupChatMessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRoomJid = CursorUtil.getColumnIndexOrThrow(_cursor, "roomJid");
          final int _cursorIndexOfSenderJid = CursorUtil.getColumnIndexOrThrow(_cursor, "senderJid");
          final int _cursorIndexOfSenderNickname = CursorUtil.getColumnIndexOrThrow(_cursor, "senderNickname");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final GroupChatMessageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpRoomJid;
            if (_cursor.isNull(_cursorIndexOfRoomJid)) {
              _tmpRoomJid = null;
            } else {
              _tmpRoomJid = _cursor.getString(_cursorIndexOfRoomJid);
            }
            final String _tmpSenderJid;
            if (_cursor.isNull(_cursorIndexOfSenderJid)) {
              _tmpSenderJid = null;
            } else {
              _tmpSenderJid = _cursor.getString(_cursorIndexOfSenderJid);
            }
            final String _tmpSenderNickname;
            if (_cursor.isNull(_cursorIndexOfSenderNickname)) {
              _tmpSenderNickname = null;
            } else {
              _tmpSenderNickname = _cursor.getString(_cursorIndexOfSenderNickname);
            }
            final String _tmpContent;
            if (_cursor.isNull(_cursorIndexOfContent)) {
              _tmpContent = null;
            } else {
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
            }
            final LocalDateTime _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __dateConverters.fromTimestamp(_tmp);
            final String _tmpMessageType;
            if (_cursor.isNull(_cursorIndexOfMessageType)) {
              _tmpMessageType = null;
            } else {
              _tmpMessageType = _cursor.getString(_cursorIndexOfMessageType);
            }
            _result = new GroupChatMessageEntity(_tmpId,_tmpRoomJid,_tmpSenderJid,_tmpSenderNickname,_tmpContent,_tmpTimestamp,_tmpMessageType);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
