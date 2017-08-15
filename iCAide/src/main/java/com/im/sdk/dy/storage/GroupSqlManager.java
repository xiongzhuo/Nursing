package com.im.sdk.dy.storage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;

import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.ui.group.DemoGroup;
import com.yuntongxun.ecsdk.im.ECGroup;


/**
 * 群组数据处理
 * @author yung•
 * @date 2015-12-18
 * @version 4.0
 */
public class GroupSqlManager extends AbstractSQLManager {

    Object mLock = new Object();
    private static GroupSqlManager sInstance;
    private static GroupSqlManager getInstance() {
        if(sInstance == null) {
            sInstance = new GroupSqlManager();
        }
        return sInstance;
    }

    private GroupSqlManager() {

    }
    
    /**
     * 查询群组详情
     * @param groupId
     * @return
     */
    public static ECGroup getECGroup(String groupId) {
        try {
            String sql = "select name, type, count ,permission ,joined ,declared ,owner , isnotice from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupid = '" + groupId + "'";
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ECGroup group = new ECGroup();
                group.setGroupId(groupId);
                group.setName(cursor.getString(0));
                group.setGroupType(cursor.getInt(1));
                group.setCount(cursor.getInt(2));
                group.setPermission(ECGroup.Permission.values()[cursor.getInt(3)]);
                group.setDeclare(cursor.getString(5));
                group.setOwner(cursor.getString(6));
                group.setIsNotice(!(cursor.getInt(7) == 2));
                return group;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询群组列表
     * @return
     */
    public static Cursor getGroupCursor(boolean isDiscussion ) {
    	String sql = null;
        try {
        	if(!isDiscussion){
        		
        		sql = "select groupid, name, type, count ,permission ,joined from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where isdiscussion=1 and joined =1 order by joined desc , create_date desc";
        	}else {
        		sql = "select groupid, name, type, count ,permission ,joined from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where isdiscussion=0 and joined =1 order by joined desc , create_date desc";
        	}
        	return getInstance().sqliteDB().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }
    
    public static ArrayList<DemoGroup> getGroups( ) {
    	  ArrayList<DemoGroup> mArrayList = new ArrayList<DemoGroup>();
    	String sql = null;
        try {
        		sql = "select groupid, name, type, count from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where isdiscussion=0 and joined =1 order by joined desc , create_date desc";
        	Cursor cursor= getInstance().sqliteDB().rawQuery(sql, null);
        	if(cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    DemoGroup group = new DemoGroup();
                    group.setGroupId(cursor.getString(0));
                    group.setName(cursor.getString(1));
                    group.setGroupType(cursor.getInt(2));
                    group.setCount(cursor.getInt(3)); 
                    mArrayList.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;


    }

    /**
     * 查询加入的群组
     * @return
     */
    public static List<ECGroup> getJoinGroups() {
        ArrayList<ECGroup> mArrayList = new ArrayList<ECGroup>();
        try {
            String sql = "select groupid, name from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where isdiscussion=0 and joined=1 order by create_date desc";;
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ECGroup group = new ECGroup();
                    group.setGroupId(cursor.getString(0));
                    group.setName(cursor.getString(1));
                    mArrayList.add(group);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }
    
    /**
     * 查询加入的群组
     * @return
     */
    public static List<ECGroup> getLikeGroups(String name) {
        ArrayList<ECGroup> mArrayList = new ArrayList<ECGroup>();
        try {
            String sql = "select groupid, name from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where name like '"+name+"'";;
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ECGroup group = new ECGroup();
                    group.setGroupId(cursor.getString(0));
                    group.setName(cursor.getString(1));
                    mArrayList.add(group);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mArrayList;
    }

    /**
     * 判断是否加入
     * @param groupId
     * @return
     */
    public static boolean getJoinState(String groupId) {
        String sql = "select groupid, joined from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupId = '" + groupId + "'";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        if(cursor != null && cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                int joind = cursor.getInt(1);
                cursor.close();
                cursor = null;
                return joind == 1;
            }
        }
        return false;
    }



    /**
     * 批量更新群组
     * @param ECGroups
     * @param joined
     * @throws android.database.SQLException
     */
    public static ArrayList<Long> insertGroupInfos(List<ECGroup> ECGroups, int joined) {

        ArrayList<Long> rows = new ArrayList<Long>();
        if (ECGroups == null) {
            return rows;
        }
        try {
            synchronized (getInstance().mLock) {
                // Set the start transaction
                getInstance().sqliteDB().beginTransaction();

                // Batch processing operation
                for (ECGroup ECGroup : ECGroups) {
                    try {
                        long row = insertGroup(ECGroup, joined == 1 , joined == -1,false,TextUtils.isEmpty(ECGroup.getGroupDomain()));
                        if(row != -1) {
                            rows.add(row);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Set transaction successful, do not set automatically
                // rolls back not submitted.
                getInstance().sqliteDB().setTransactionSuccessful();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getInstance().sqliteDB().endTransaction();
        }
        return rows;
    }
    
    
    
    public static ArrayList<Long> insertDisGroupInfos(List<ECGroup> ECGroups, int joined) {

        ArrayList<Long> rows = new ArrayList<Long>();
        if (ECGroups == null) {
            return rows;
        }
        try {
            synchronized (getInstance().mLock) {
                // Set the start transaction
                getInstance().sqliteDB().beginTransaction();

                // Batch processing operation
                for (ECGroup ECGroup : ECGroups) {
                    try {
                        long row = insertGroup(ECGroup, joined == 1 , joined == -1,true,TextUtils.isEmpty(ECGroup.getGroupDomain()));
                        if(row != -1) {
                            rows.add(row);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Set transaction successful, do not set automatically
                // rolls back not submitted.
                getInstance().sqliteDB().setTransactionSuccessful();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getInstance().sqliteDB().endTransaction();
        }
        return rows;
    }
    

    /**
     * 更新群组到数据库
     * @param group
     * @param join
     */
    public static long insertGroup(ECGroup group , boolean join , boolean ignoreJoin,boolean isDiscussion,boolean isDefalut) {
        if(group == null || TextUtils.isEmpty(group.getGroupId())) {
            return -1L;
        }
        ContentValues values = null;
        try {

            values = new ContentValues();
            values.put(GroupColumn.GROUP_ID, group.getGroupId());
            values.put(GroupColumn.GROUP_NAME, group.getName());
            values.put(GroupColumn.GROUP_PERMISSION, group.getPermission().ordinal());
            values.put(GroupColumn.GROUP_TYPE, group.getGroupType());
            values.put(GroupColumn.GROUP_isdefault, isDefalut?1:0);
            values.put(GroupColumn.GROUP_ISNOTICE, group.isNotice());
            if(isDiscussion){
            	values.put(GroupColumn.GROUP_Discussion, 1);
            	
            }
            
            if(!TextUtils.isEmpty(group.getOwner())) {
                values.put(GroupColumn.GROUP_OWNER, group.getOwner());
                values.put(GroupColumn.GROUP_DECLARED, group.getDeclare());
            }
            values.put(GroupColumn.GROUP_DATE_CREATED, group.getDateCreated());
            
            values.put(GroupColumn.GROUP_MEMBER_COUNTS, isDefalut?group.getCount()-1:group.getCount());
            if(!ignoreJoin)
                values.put(GroupColumn.GROUP_JOINED, join);

            if(isExitGroup(group.getGroupId())) {
                return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, "groupid = ?", new String[]{group.getGroupId()});
            }
            long rowId = getInstance().sqliteDB().insert(DatabaseHelper.TABLES_NAME_GROUPS_2, null, values);
            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }

        }
        return -1L;
    }
    
    public static long insertGroup(ECGroup group) {
        if(group == null || TextUtils.isEmpty(group.getGroupId())) {
            return -1L;
        }
        ContentValues values = null;
        try {

            values = new ContentValues();
            values.put(GroupColumn.GROUP_ID, group.getGroupId());
            values.put(GroupColumn.GROUP_NAME, group.getName());
            values.put(GroupColumn.GROUP_PERMISSION, group.getPermission().ordinal());
            values.put(GroupColumn.GROUP_ISNOTICE, group.isNotice());
            values.put(GroupColumn.GROUP_TYPE, group.getGroupType());
            
            	values.put(GroupColumn.GROUP_Discussion, 0);
            
            if(!TextUtils.isEmpty(group.getOwner())) {
                values.put(GroupColumn.GROUP_OWNER, group.getOwner());
                values.put(GroupColumn.GROUP_DECLARED, group.getDeclare());
            }
            values.put(GroupColumn.GROUP_DATE_CREATED, group.getDateCreated());
            values.put(GroupColumn.GROUP_MEMBER_COUNTS, group.getCount());
                values.put(GroupColumn.GROUP_JOINED, 1);
                
                
                String dmomain=group.getGroupDomain();
                if(!TextUtils.isEmpty(dmomain)){
                	JSONObject j=new JSONObject();
                	j.get("");
                }

            if(isExitGroup(group.getGroupId())) {
                return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, "groupid = ?", new String[]{group.getGroupId()});
            }
            long rowId = getInstance().sqliteDB().insert(DatabaseHelper.TABLES_NAME_GROUPS_2, null, values);
            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }

        }
        return -1L;
    }


    /**
     * 群组是否存在
     * @param groupId
     * @return
     */
    public static boolean isExitGroup(String groupId) {
        String sql = "select groupid from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupid ='" + groupId + "'";
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean isDiscussionGroup(String groupId) {
        String sql = "select groupid from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where isdiscussion=1 and groupid ='" + groupId + "'";
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public static void checkGroup(String contactid) {

        if(!isExitGroup(contactid)) {
            ECGroup group = new ECGroup();
            group.setGroupId(contactid);
            group.setName(contactid);
            insertGroup(group , true , false,false,false);
        }
    }

    /**
     * 更新群组信息
     * @param group
     * @return
     */
    public static long updateGroup(ECGroup group) {
        if(group == null || TextUtils.isEmpty(group.getGroupId())) {
            return -1L;
        }
        ContentValues values = null;
        try {

            values = new ContentValues();
            values.put(GroupColumn.GROUP_ID, group.getGroupId());
            values.put(GroupColumn.GROUP_NAME, group.getName());
            // values.put(GroupColumn.GROUP_PERMISSION, group.getPermission());
            values.put(GroupColumn.GROUP_TYPE, group.getGroupType());
            values.put(GroupColumn.GROUP_OWNER, group.getOwner());
            values.put(GroupColumn.GROUP_DATE_CREATED, group.getDateCreated());
            values.put(GroupColumn.GROUP_DECLARED, group.getDeclare());
            values.put(GroupColumn.GROUP_MEMBER_COUNTS, group.getCount());
            
            	values.put(GroupColumn.GROUP_Discussion,group.isDiscuss());
            
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, "groupid = ?", new String[]{group.getGroupId()});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }

        }
        return -1L;
    }

    /**
     * 更新加入状态
     * @param groupId
     * @return
     */
    public static int updateUNJoin(String groupId) {
        return updateJoinStatus(groupId, false);
    }

    /**
     * 更新群组加入状态
     * @param groupId
     * @param join
     * @return
     */
    public static int updateJoinStatus(String groupId , boolean join) {
        try {
            ContentValues values = new ContentValues();
            values.put(GroupColumn.GROUP_JOINED, join ? 1:0);
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, "groupid = ?", new String[]{groupId});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 删除群组
     * @param groupId
     * @return
     */
    public static int delGroup(String groupId) {
        try {
            Intent intent = new Intent(IMessageSqlManager.ACTION_GROUP_DEL);
            intent.putExtra("group_id" , groupId);
            CCPAppManager.getContext().sendBroadcast(intent);
            return getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUPS_2, "groupid = ?", new String[]{groupId});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 清空所有群组
     * @return
     */
    public static int delALLGroup() {
        try {
            return getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUPS_2, "isdiscussion=?", new String[]{"0"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static int delALLDisGroup() {
    	try {
    		return getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_GROUPS_2, "isdiscussion=?", new String[]{"1"});
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return -1;
    }

    /**
     * 查询所有的groupId;
     * @return
     */
    public static List<String> getAllGroupId() {
        ArrayList<String> groupsId = new ArrayList<String>();
        Cursor cursor = null;
        try {
            String sql = "select groupid from " + DatabaseHelper.TABLES_NAME_GROUPS_2;
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String groupid = cursor.getString(0);
                    if(TextUtils.isEmpty(groupid)) {
                        continue;
                    }
                    groupsId.add(groupid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return groupsId;
    }

    /**
     * 根据加入的状态查询群组
     * @param joined
     * @return
     */
    public static List<String> getAllGroupIdBy(boolean joined) {
        ArrayList<String> groupsId = new ArrayList<String>();
        Cursor cursor = null;
        try {
            String sql = "select groupid from "+ DatabaseHelper.TABLES_NAME_GROUPS_2 +" where isdiscussion =0 and joined = " + (joined?1:0);
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String groupid = cursor.getString(0);
                    if(TextUtils.isEmpty(groupid)) {
                        continue;
                    }
                    groupsId.add(groupid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return groupsId;
    }
    
    
    public static List<String> getAllDisGroupIdBy(boolean joined) {
        ArrayList<String> groupsId = new ArrayList<String>();
        Cursor cursor = null;
        try {
            String sql = "select groupid from "+ DatabaseHelper.TABLES_NAME_GROUPS_2 +" where isdiscussion =1 and joined = " + (joined?1:0);
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String groupid = cursor.getString(0);
                    if(TextUtils.isEmpty(groupid)) {
                        continue;
                    }
                    groupsId.add(groupid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return groupsId;
    }
    
    
    

    /**
     * 查询群组详情
     * @param groupId
     * @return
     */
    public static ECGroup getECGroup1(String groupId) {
        try {
            String sql = "select name, type, count ,permission ,joined ,declared ,owner , isnotice,isdefalut from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupid = '" + groupId + "'";
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ECGroup group = new ECGroup();
                group.setGroupId(groupId);
                group.setName(cursor.getString(0));
                group.setGroupType(cursor.getInt(1));
                group.setCount(cursor.getInt(2));
                group.setPermission(ECGroup.Permission.values()[cursor.getInt(3)]);
                group.setDeclare(cursor.getString(5));
                group.setOwner(cursor.getString(6));
                group.setIsNotice(!(cursor.getInt(7) == 2));
                return group;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static DemoGroup getDemoGroup(String groupId) {
        try {
            String sql = "select name, type, count ,permission ,joined ,declared ,owner , isnotice,isdefalut from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupid = '" + groupId + "'";
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                DemoGroup group = new DemoGroup();
                group.setGroupId(groupId);
                group.setName(cursor.getString(0));
                group.setGroupType(cursor.getInt(1));
                group.setCount(cursor.getInt(2));
                group.setIsNotice(!(cursor.getInt(7) == 2));
                group.setPermission(ECGroup.Permission.values()[cursor.getInt(3)]);
                group.setDeclare(cursor.getString(5));
                group.setOwner(cursor.getString(6));
                group.setDefalut((cursor.getInt(8) == 1));
                return group;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

    public static boolean isNeedApply(String groupId) {
        boolean joind = false;
        try {
            String sql = "select joined from "+ DatabaseHelper.TABLES_NAME_GROUPS_2+" where groupid = '" + groupId + "'";
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                joind = cursor.getInt(0) == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !joind;
    }
    
    

    public static void registerGroupObserver(OnMessageChange observer) {
        getInstance().registerObserver(observer);
    }

    public static void unregisterGroupObserver(OnMessageChange observer) {
        getInstance().unregisterObserver(observer);
    }

    public static void notifyGroupChanged(String session) {
        getInstance().notifyChanged(session);
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
    	
        super.release();
        sInstance = null;
    }

    public static long updateGroupNofity(int ordinal , String groupid) {
        // 群组免打扰
        ContentValues values = new ContentValues();
        values.put(GroupColumn.GROUP_ISNOTICE , ordinal);
        return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2 , values, " groupid='" + groupid + "'" , null);
    }

    /**
     * 群组是否免打扰
     * @param groupId
     * @return
     */
    public static boolean isGroupNotify(String groupId) {
        String sql = "select isnotice from " + DatabaseHelper.TABLES_NAME_GROUPS_2 + " where groupid='" + groupId + "'";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        boolean isNotify = true;
        if(cursor != null && cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                isNotify = !(cursor.getInt(0) == 2);
            }
            cursor.close();
        }
        return isNotify;
    }
    
    
    public static boolean isExistDiscussionGroup(){
    	 String sql = "select groupid from " + DatabaseHelper.TABLES_NAME_GROUPS_2 + " where isdiscussion='" + 1 + "'";
         Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
         if(cursor != null && cursor.getCount() > 0) {
            
        	 return true;
         }
         return false;
    	
    	
    }
    
    
    
}