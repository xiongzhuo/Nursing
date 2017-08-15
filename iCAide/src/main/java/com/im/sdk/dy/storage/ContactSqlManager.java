package com.im.sdk.dy.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.deya.hospital.util.AbStrUtil;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.core.ContactsCache;
import com.im.sdk.dy.ui.contact.ContactLogic;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.yuntongxun.ecsdk.im.ECGroupMember;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 联系人数据库管理
 */
public class ContactSqlManager extends AbstractSQLManager {

	private static ContactSqlManager sInstance;

	private static ContactSqlManager getInstance() {
		if (sInstance == null) {
			sInstance = new ContactSqlManager();
		}
		return sInstance;
	}

	public static boolean hasContact(String contactId) {
		String sql = "select contact_id from contacts where contact_id = '"
				+ contactId + "'";
		Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		return false;
	}

	public static ECContacts ckContact(String contactId) {
		String sql = "select username,hospital from contacts where contact_id = '"
				+ contactId + "'";
		ECContacts c = new ECContacts(contactId);
		Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToNext();
			c.setNickname(cursor.getString(0));
			c.setHospital(cursor.getString(1));
			cursor.close();
		}
		return c;
	}

	/**
	 * 插入联系人到数据库
	 * 
	 * @param contacts
	 * @return
	 */
	public static ArrayList<Long> insertContacts(List<ECContacts> contacts) {

		ArrayList<Long> rows = new ArrayList<Long>();
		try {

			getInstance().sqliteDB().beginTransaction();
			for (ECContacts c : contacts) {
				long rowId = insertContact(c);
				if (rowId != -1L) {
					rows.add(rowId);
				}
			}

			// 初始化系统联系人
			insertSystemNoticeContact();
			getInstance().sqliteDB().setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getInstance().sqliteDB().endTransaction();
		}
		return rows;
	}

	/**
	 * 初始化联系人数据库
	 * 
	 * @return
	 */
	private static long insertSystemNoticeContact() {
		ECContacts contacts = new ECContacts(GroupNoticeSqlManager.CONTACT_ID);
		contacts.setNickname("系统通知");
		contacts.setRemark("touxiang_notice.png");

		return insertContact(contacts);
	}

	public static long updateContactPhoto(ECContacts contact) {
		return insertContact(contact, 1, true);
	}

	/**
	 * 根据性别更新联系人信息（区分联系人头像）
	 * 
	 * @param contact
	 * @param sex
	 * @return
	 */
	public static long insertContact(ECContacts contact, int sex) {
		return insertContact(contact, sex, false);
	}

	public static long insertContact(ECContacts contact, int sex,
			boolean hasPhoto) {
		if (contact == null || AbStrUtil.isEmpty(contact.getContactid())) {
			return -1;
		}
//		if (CCPAppManager.getClientUser().getUserId()
//				.equals(contact.getContactid())) {
//			return -2;
//		}

		try {
			ContentValues values = contact.buildContentValues();
			// if(!hasPhoto ) {
			// int index = getIntRandom(3, 0);
			// if(sex == 2) {
			// index = 4;
			// }
			// String remark = ContactLogic.CONVER_PHONTO[index];
			// contact.setRemark(remark);
			// }

			// if(values.containsKey("status")){
			// values.remove("status");
			// }
			values.put(AbstractSQLManager.ContactsColumn.REMARK,
					contact.getRemark());
			if (!hasContact(contact.getContactid())) {
				return getInstance().sqliteDB().insert(
						DatabaseHelper.TABLES_NAME_CONTACT, null, values);
			}
			getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_CONTACT,
					values, "contact_id = '" + contact.getContactid() + "'",
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 插入联系人到数据库
	 * 
	 * @param contact
	 * @return
	 */
	public static long insertContact(ECContacts contact) {
		return insertContact(contact, 1);
	}

	/**
	 * 查询联系人名称
	 * 
	 * @param contactId
	 * @return
	 */
	public static ArrayList<String> getContactName(String[] contactId) {
		ArrayList<String> contacts = null;
		try {
			String sql = "select username ,contact_id,rname from contacts where contact_id in ";
			StringBuilder sb = new StringBuilder("(");
			for (int i = 0; i < contactId.length; i++) {
				sb.append("'").append(contactId[i]).append("'");
				if (i != contactId.length - 1) {
					sb.append(",");
				}
			}
			sb.append(")");
			Cursor cursor = getInstance().sqliteDB().rawQuery(
					sql + sb.toString(), null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<String>();
				// 过滤自己的联系人账号
				ClientUser clientUser = CCPAppManager.getClientUser();
				while (cursor.moveToNext()) {
					if (clientUser != null
							&& clientUser.getUserId().equals(
									cursor.getString(0))) {
						continue;
					}
					String displayName = cursor.getString(0);
					String contact_id = cursor.getString(1);
					String rname = cursor.getString(2);
					displayName = rname == null || rname.equals("") ? displayName
							: rname;
					if (TextUtils.isEmpty(displayName)
							|| TextUtils.isEmpty(contact_id)
							|| displayName.equals(contact_id)) {
						continue;
					}
					contacts.add(displayName);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	/**
	 * 查询联系人名称
	 * 
	 * @param contactId
	 * @return
	 */
	public static ArrayList<String> getContactRemark(String[] contactId) {
		ArrayList<String> contacts = null;
		try {
			String sql = "select remark from contacts where contact_id in ";
			StringBuilder sb = new StringBuilder("(");
			for (int i = 0; i < contactId.length; i++) {
				sb.append("'").append(contactId[i]).append("'");
				if (i != contactId.length - 1) {
					sb.append(",");
				}
			}
			sb.append(")");
			Cursor cursor = getInstance().sqliteDB().rawQuery(
					sql + sb.toString(), null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<String>();
				while (cursor.moveToNext()) {
					contacts.add(cursor.getString(0));
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contacts;
	}

	/**
	 * 更新联系人名字
	 * 
	 * @param member
	 */
	public static void updateContactName(ECGroupMember member) {
		ContentValues values = new ContentValues();
		values.put(AbstractSQLManager.ContactsColumn.USERNAME,
				member.getDisplayName());
		getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_CONTACT,
				values, "contact_id = '" + member.getVoipAccount() + "'", null);
	}

	/**
	 * 修改联系人备注信息
	 * 
	 * @param id
	 * @param rname
	 * @param remark
	 */
	public static void updateContactRemark(String id, String rname,
			String remark) {
		ContentValues values = new ContentValues();
		values.put(AbstractSQLManager.ContactsColumn.RNAME, rname);
		values.put(AbstractSQLManager.ContactsColumn.REMARK, remark);
		getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_CONTACT,
				values, "contact_id = '" + id + "'", null);
	}

	/**
	 * 查询联系人
	 * 
	 * @return
	 */
	public static ArrayList<ECContacts> getContacts() {
		ArrayList<ECContacts> contacts = null;
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID,
							ContactsColumn.CONTACT_ID, ContactsColumn.RNAME,
							ContactsColumn.AVATAR, ContactsColumn.SEX,
							ContactsColumn.USERNAME, ContactsColumn.MOBILE,
							ContactsColumn.HOSPITAL, ContactsColumn.type,ContactsColumn.OTHER_STR },
					"status=? and (mobile is not null or mobile <>'')",
					new String[] { String.valueOf(0) }, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<ECContacts>();
				// 过滤自己的联系人账号信息
				
				ContactLogic.contactPhones.clear();
				while (cursor.moveToNext()) {
					if (GroupNoticeSqlManager.CONTACT_ID.equals(cursor
							.getString(1))) {
						continue;
					}
					ECContacts c = new ECContacts(cursor.getString(1));
					c.setRname(cursor.getString(2));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(3));
					c.setSex(cursor.getInt(4));
					c.setNickname(cursor.getString(5));
					c.setMobile(cursor.getString(6));
					c.setHospital(cursor.getString(7));
					c.setType(cursor.getInt(8));
					ContactLogic.pyFormat(c);
					ClientUser clientUser = CCPAppManager.getClientUser();
					if (clientUser != null
							&& clientUser.getUserId().equals(c.getContactid())) {
						Log.i("imcontact", "是我自己");
					}
					ContactLogic.contactPhones.put(cursor.getString(6),
							cursor.getString(6));
					contacts.add(c);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ContactLogic.ArrayContacts(contacts);
		return contacts;

	}

	public static ArrayList<ECContacts> getContactsExpert() {
		ArrayList<ECContacts> contacts = null;
		try {
			Cursor cursor = getInstance()
					.sqliteDB()
					.query(DatabaseHelper.TABLES_NAME_CONTACT,
							new String[] { ContactsColumn.ID,
									ContactsColumn.CONTACT_ID,
									ContactsColumn.RNAME,
									ContactsColumn.AVATAR, ContactsColumn.SEX,
									ContactsColumn.USERNAME,
									ContactsColumn.MOBILE,
									ContactsColumn.HOSPITAL,
									ContactsColumn.type },
							"status=? and (mobile is not null or mobile <>'') and type=2",
							new String[] { String.valueOf(0) }, null, null,
							null, null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<ECContacts>();
				// 过滤自己的联系人账号信息
				ClientUser clientUser = CCPAppManager.getClientUser();
				ContactLogic.contactPhones.clear();
				while (cursor.moveToNext()) {
					if (GroupNoticeSqlManager.CONTACT_ID.equals(cursor
							.getString(1))) {
						continue;
					}
					ECContacts c = new ECContacts(cursor.getString(1));
					c.setRname(cursor.getString(2));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(3));
					c.setSex(cursor.getInt(4));
					c.setNickname(cursor.getString(5));
					c.setMobile(cursor.getString(6));
					c.setHospital(cursor.getString(7));
					c.setType(cursor.getInt(8));
				//	c.setRegis_job(cursor.getString(13));
					ContactLogic.pyFormat(c);

//					if (clientUser != null
//							&& clientUser.getUserId().equals(c.getContactid())) {
//						continue;
//					}
					ContactLogic.contactPhones.put(cursor.getString(6),
							cursor.getString(6));
					contacts.add(c);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ContactLogic.ArrayContacts(contacts);
		return contacts;

	}
	public static ArrayList<ECContacts> getContactsHelper() {
		ArrayList<ECContacts> contacts = null;
		try {
			Cursor cursor = getInstance()
					.sqliteDB()
					.query(DatabaseHelper.TABLES_NAME_CONTACT,
							new String[] { ContactsColumn.ID,
									ContactsColumn.CONTACT_ID,
									ContactsColumn.RNAME,
									ContactsColumn.AVATAR, ContactsColumn.SEX,
									ContactsColumn.USERNAME,
									ContactsColumn.MOBILE,
									ContactsColumn.HOSPITAL,
									ContactsColumn.type },
							"status=? and (mobile is not null or mobile <>'') and type=3",
							new String[] { String.valueOf(0) }, null, null,
							null, null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<ECContacts>();
				// 过滤自己的联系人账号信息
				ClientUser clientUser = CCPAppManager.getClientUser();
				ContactLogic.contactPhones.clear();
				while (cursor.moveToNext()) {
					if (GroupNoticeSqlManager.CONTACT_ID.equals(cursor
							.getString(1))) {
						continue;
					}
					ECContacts c = new ECContacts(cursor.getString(1));
					c.setRname(cursor.getString(2));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(3));
					c.setSex(cursor.getInt(4));
					c.setNickname(cursor.getString(5));
					c.setMobile(cursor.getString(6));
					c.setHospital(cursor.getString(7));
					c.setType(cursor.getInt(8));
				//	c.setRegis_job(cursor.getString(13));
					ContactLogic.pyFormat(c);
					if (clientUser != null
							&& clientUser.getUserId().equals(c.getContactid())) {
						continue;
					}
					ContactLogic.contactPhones.put(cursor.getString(6),
							cursor.getString(6));
					contacts.add(c);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ContactLogic.ArrayContacts(contacts);
		return contacts;

	}
	/**
	 * 根据联系人ID查询联系人
	 * 
	 * @param rawId
	 * @return
	 */
	public static ECContacts getContact(long rawId) {
		if (rawId == -1) {
			return null;
		}
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK,
							ContactsColumn.AVATAR, ContactsColumn.CITY,
							ContactsColumn.DEPARTMENT, ContactsColumn.HOSPITAL,
							ContactsColumn.MOBILE, ContactsColumn.PROVINCE,
							ContactsColumn.RNAME, ContactsColumn.SEX,
							ContactsColumn.type,ContactsColumn.OTHER_STR }, "id=?",
					new String[] { String.valueOf(rawId) }, null, null, null,
					null);
			ECContacts c = null;
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(4));
					c.setCity(cursor.getString(5));
					c.setDepartment(cursor.getString(6));
					c.setHospital(cursor.getString(7));
					c.setMobile(cursor.getString(8));
					c.setProvince(cursor.getString(9));
					c.setRname(cursor.getString(10));
					c.setSex(cursor.getInt(11));
					c.setType(cursor.getInt(12));
					c.setRegis_job(cursor.getString(13));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param phoneNumber
	 * @return
	 */
	public static ECContacts getCacheContact(String phoneNumber) {
		if (ContactsCache.getInstance().getContacts() != null) {
			return ContactsCache.getInstance().getContacts()
					.getValueByPhone(phoneNumber);
		}
		return null;
	}

	/**
	 * 根据联系人账号查询
	 * 
	 * @param contactId
	 * @return
	 */
	public static ECContacts getContact(String contactId) {
		if (TextUtils.isEmpty(contactId)) {
			return null;
		}
		ECContacts c = new ECContacts(contactId);
		// c.setNickname(contactId);
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK,
							ContactsColumn.AVATAR, ContactsColumn.RNAME,
							ContactsColumn.SEX, ContactsColumn.type },
					"contact_id=?", new String[] { contactId }, null, null,
					null, null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(4));
					c.setRname(cursor.getString(5));
					c.setSex(cursor.getInt(6));
					c.setType(cursor.getInt(7));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public static ECContacts getExpertContact(String contactId) {
		if (TextUtils.isEmpty(contactId)) {
			return null;
		}
		ECContacts c = new ECContacts(contactId);
		// c.setNickname(contactId);
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK,
							ContactsColumn.AVATAR, ContactsColumn.RNAME,
							ContactsColumn.SEX, ContactsColumn.type },
					"type=?", new String[] { contactId }, null, null, null,
					null);
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
					c.setAvatar(cursor.getString(4));
					c.setRname(cursor.getString(5));
					c.setSex(cursor.getInt(6));
					c.setType(cursor.getInt(7));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * 根据昵称查询信息
	 * 
	 * @param nikeName
	 * @return
	 */
	public static ECContacts getContactLikeUsername(String nikeName) {
		if (TextUtils.isEmpty(nikeName)) {
			return null;
		}
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK },
					"username LIKE '" + nikeName + "'", null, null, null, null,
					null);
			ECContacts c = null;
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ECContacts getContactLikeUserId(String nikeName) {
		if (TextUtils.isEmpty(nikeName)) {
			return null;
		}
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK },
					"username LIKE '" + nikeName + "'", null, null, null, null,
					null);
			ECContacts c = null;
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ECContacts getContactByPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return null;
		}
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID, ContactsColumn.USERNAME,
							ContactsColumn.CONTACT_ID, ContactsColumn.REMARK },
					"mobile= '" + phone + "'", null, null, null, null, null);
			ECContacts c = null;
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					c = new ECContacts(cursor.getString(2));
					c.setNickname(cursor.getString(1));
					c.setRemark(cursor.getString(3));
					c.setId(cursor.getInt(0));
				}
				cursor.close();
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据昵称或电话查找
	 * 
	 * @return
	 */
	public static ArrayList<ECContacts> getLikeContacts(String name) {
		ArrayList<ECContacts> contacts = null;
		try {
			Cursor cursor = getInstance().sqliteDB().query(
					DatabaseHelper.TABLES_NAME_CONTACT,
					new String[] { ContactsColumn.ID,
							ContactsColumn.CONTACT_ID, ContactsColumn.USERNAME,
							ContactsColumn.RNAME, ContactsColumn.MOBILE,
							ContactsColumn.AVATAR, ContactsColumn.SEX },
					"username LIKE '" + name + "' ||rname LIKE '" + name
							+ "'||mobile LIKE '" + name + "'", null, null,
					null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				contacts = new ArrayList<ECContacts>();
				// 过滤自己的联系人账号信息
				ClientUser clientUser = CCPAppManager.getClientUser();
				ContactLogic.contactPhones.clear();
				while (cursor.moveToNext()) {
					if (GroupNoticeSqlManager.CONTACT_ID.equals(cursor
							.getString(1))) {
						continue;
					}
					ECContacts c = new ECContacts(cursor.getString(1));
					c.setId(cursor.getInt(0));
					c.setNickname(cursor.getString(2));
					c.setRname(cursor.getString(3));
					c.setMobile(cursor.getString(4));
					c.setAvatar(cursor.getString(5));
					c.setSex(cursor.getInt(6));

					ContactLogic.pyFormat(c);
					if (clientUser != null
							&& clientUser.getUserId().equals(c.getContactid())) {
						continue;
					}
					ContactLogic.contactPhones.put(cursor.getString(6),
							cursor.getString(6));
					contacts.add(c);
				}
				cursor.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ContactLogic.ArrayContacts(contacts);
		return contacts;

	}

	public static void reset() {
		getInstance().release();
		sInstance = null;
	}

	public static int getIntRandom(int max, int min) {
		Assert.assertTrue(max > min);
		return (new Random(System.currentTimeMillis()).nextInt(max - min + 1) + min);
	}
}
