package com.im.sdk.dy.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import com.im.sdk.dy.common.utils.LogUtil;
import com.yuntongxun.ecsdk.ECMessage;

/**
 * 会话消息数据库管理
 *
 * @author yung•
 * @version 4.0
 * @date 2015-12-11
 */
public class ConversationSqlManager extends AbstractSQLManager {

    private static ConversationSqlManager instance;

    private ConversationSqlManager() {
        super();
    }

    public static ConversationSqlManager getInstance() {
        if (instance == null) {
            instance = new ConversationSqlManager();
        }
        return instance;
    }

    /**
     * @return
     */
    public static Cursor getConversationCursor() {
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid order by dateTime desc";
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    /**
     * 过滤29和31,以及系统通知
     * @return
     */
    public static Cursor getConversationCursor3() {
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid where im_thread.sessionId not in (29,10089) order by dateTime desc";
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    /**
     * 以及系统通知
     * @return
     */
    public static Cursor getConversationCursor5() {
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid where im_thread.sessionId not in (10089) order by dateTime desc";
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    public static Cursor getConversationCursorFilter(String [] idStr) {
        String ids="'29','10089'";
        for(String str:idStr){
            ids+=",'"+str+"'";
        }
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid where im_thread.sessionId not in ("+ids+") order by dateTime desc";
            Log.i("IMtag", sql);
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public static Cursor getConversationCursorById(String sessionId) {
        try {
            //String sql = "select unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username from im_thread,contacts where im_thread.sessionId = contacts.contact_id order by dateTime desc";
            String sql = "SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid where im_thread.sessionId ='"+sessionId+"'";
            Log.e("IMconver", sql);
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    public static Cursor getConversationCursor2() {

        try {
            String sql= " SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid"
                    + "union all "+
                    " SELECT unreadCount, im_thread.type, sendStatus, dateTime, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname,contacts.type,contacts.sex,contacts.avatar\n" +
                    "      FROM im_thread \n" +" where im_thread.sessionId in (31,29) order by dateTime desc";
            return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static Cursor getLikeConversations(String text) {
        try {
            String sql = "SELECT im_thread.type, sessionId, text, username,name ,im_thread.contactid ,isnotice ,rname\n" +
                    "      FROM im_thread \n" +
                    "      LEFT JOIN contacts ON im_thread.sessionId = contacts.contact_id \n" +
                    "      LEFT JOIN groups2 ON im_thread.sessionId = groups2.groupid"
                    + " where  im_thread.type='0' and text like '"+text+"'"
                    + " order by dateTime desc;";
            Cursor cursor= getInstance().sqliteDB().rawQuery(sql, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * 通过会话ID查找消息数据库主键
     *
     * @param sessionId 会话ID
     * @return
     */
    public static long querySessionIdForBySessionId(String sessionId) {
        Cursor cursor = null;
        long threadId = 0;
        if (sessionId != null) {
            String where = IThreadColumn.THREAD_ID + " = '" + sessionId + "' ";
            try {
                cursor = getInstance().sqliteDB().query(
                        DatabaseHelper.TABLES_NAME_IM_SESSION, null, where,
                        null, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        threadId = cursor.getLong(cursor
                                .getColumnIndexOrThrow(IThreadColumn.ID));
                    }
                }
            } catch (SQLException e) {
                LogUtil.e(TAG + " " + e.toString());
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return threadId;
    }

    /**
     * 生成一个新的会话消息
     *
     * @param msg
     * @return
     */
    public static long insertSessionRecord(ECMessage msg) {
        if (msg == null || TextUtils.isEmpty(msg.getSessionId())) {
            throw new IllegalArgumentException("insert thread table "
                    + DatabaseHelper.TABLES_NAME_IM_SESSION
                    + "error , that Argument ECMessage:" + msg);
        }
        long row = -1;
        ContentValues values = new ContentValues();
        try {
            values.put(IThreadColumn.THREAD_ID, msg.getSessionId());
            values.put(IThreadColumn.DATE, System.currentTimeMillis());
            values.put(IThreadColumn.UNREAD_COUNT, 0);
            values.put(IThreadColumn.CONTACT_ID, msg.getForm());
            row = getInstance().sqliteDB().insertOrThrow(
                    DatabaseHelper.TABLES_NAME_IM_SESSION, null, values);
        } catch (SQLException ex) {
            ex.printStackTrace();
            LogUtil.e(TAG + " " + ex.toString());
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return row;
    }

    public int qureySessionUnreadCount() {
        int count = 0;
        String[] columnsList = {"count(" + IThreadColumn.UNREAD_COUNT + ")"};
        String where = IThreadColumn.UNREAD_COUNT + " > 0";
        Cursor cursor = null;
        try {
            cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_SESSION,
                    columnsList, where, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("count("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return count;
    }

    public int qureyAllSessionUnreadCount() {
        int count = 0;
        String[] columnsList = {"sum(" + IThreadColumn.UNREAD_COUNT + ")"};
        Cursor cursor = null;
        try {
            cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_SESSION,
                    columnsList, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("sum("
                            + IThreadColumn.UNREAD_COUNT + ")"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG + " " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public static void delSession(String contactId) {
        String where = IThreadColumn.THREAD_ID + " = '" + contactId + "' ";
        getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_SESSION , where, null);
    }

    /**
     * 更新会话已读状态
     * @param id
     * @return
     */
    public static long setChattingSessionRead(long id) {
        if(id <= 0) {
            return -1;
        }
        ContentValues values = new ContentValues();
        try {
            String where = IThreadColumn.ID + " = " + id + " and " + IThreadColumn.UNREAD_COUNT + "!=0";
            values.put(IThreadColumn.UNREAD_COUNT, 0);
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_SESSION, values, where, null);
        } catch (Exception e) {
            LogUtil.e(TAG ," " + e.toString());
            e.getStackTrace();
        } finally {
            if (values != null) {
                values.clear();
            }
        }
        return -1;
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        instance = null;
    }

}
