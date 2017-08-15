package com.im.sdk.dy.ui.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.google.gson.JsonObject;
import com.im.sdk.dy.common.CCPAppManager;
import com.im.sdk.dy.common.utils.BitmapUtil;
import com.im.sdk.dy.common.utils.DemoUtils;
import com.im.sdk.dy.common.utils.DialNumberMap;
import com.im.sdk.dy.common.utils.ECPropertiesUtil;
import com.im.sdk.dy.common.utils.FileAccessor;
import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.common.utils.ResourceHelper;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.core.DyMessage;
import com.im.sdk.dy.core.ECArrayLists;
import com.im.sdk.dy.core.FriendCmdType;
import com.im.sdk.dy.core.MessageType;
import com.im.sdk.dy.core.comparator.PyComparator;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.storage.GroupMemberSqlManager;
import com.im.sdk.dy.ui.chatting.IMChattingHelper;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECMessage.Type;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 联系人逻辑处理 Created by yung on 2015/12/18.
 */
public class ContactLogic {

	/** 已添加联系人 */
	public static ArrayList<ECContacts> contactList = new ArrayList<ECContacts>();
	/** 已添加联系人,同步数据用 */
	public static HashMap<String, String> contactPhones = new HashMap<String, String>();
	
	
	/** 未添加联系人，手机集合 */
	//public static List<String> phoneList = new ArrayList<String>();
	/** 未添加联系人，手机字符 */
	public static String phoneString = "";
	/** 已添加联系人 */
	public static ArrayList<ECContacts> contacts = new ArrayList<ECContacts>();
	/** 手机联系人,同步数据用 */
	public static ECArrayLists<ECContacts> mobileContacts = new ECArrayLists<ECContacts>();
	

	public static final String ALPHA_ACCOUNT = "izhangjy@163.com";
	public static final String CUSTOM_SERVICE = "KF4008818600668603";
	private static HashMap<String, Bitmap> photoCache = new HashMap<String, Bitmap>(
			20);
	public static final String[] CONVER_NAME = { "张三", "李四", "王五", "赵六", "钱七" };
	public static final String[] CONVER_PHONTO = {
			"select_account_photo_one.png", "select_account_photo_two.png",
			"select_account_photo_three.png", "select_account_photo_four.png",
			"select_account_photo_five.png" };

	private static String[] projection_getSettingList = {
			ContactsContract.Settings.ACCOUNT_TYPE,
			ContactsContract.Settings.ACCOUNT_NAME };

	private static String[] projection_getContractList = {
			ContactsContract.Data.RAW_CONTACT_ID,
			ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1,
			ContactsContract.Data.DATA2, ContactsContract.Data.DATA3,
			ContactsContract.RawContacts.ACCOUNT_TYPE,
			ContactsContract.Data._ID, ContactsContract.Data.TIMES_CONTACTED,
			ContactsContract.Data.DATA5, ContactsContract.Data.DATA6, };

	public static Bitmap mDefaultBitmap = null;

	static {
		try {
			if (mDefaultBitmap == null) {
				mDefaultBitmap = DemoUtils
						.decodeStream(
								CCPAppManager
										.getContext()
										.getAssets()
										.open("avatar/personal_center_default_avatar.png"),
								ResourceHelper.getDensity(null));
			}
		} catch (IOException e) {
		}
	}

	private static ContactLogic sInstance;

	public static ContactLogic getInstance() {
		if (sInstance == null) {
			sInstance = new ContactLogic();
		}
		return sInstance;
	}
/**
 * 获取联系人列表数据
 * @return
 */
	public static ArrayList<ECContacts> getContacts() {
		contacts = ContactSqlManager.getContacts();
		return contacts;
	}

	public static ArrayList<ECContacts> getSearchContacts() {
		if (null == contacts || contacts.size() == 0) {
			contacts = ContactSqlManager.getContacts();
		}
		return contacts;
	}

	/**
	 * 查找头像
	 * 
	 * @param username
	 * @return
	 */
	public static Bitmap getPhoto(String username) {

		if (TextUtils.isEmpty(username)) {
			return mDefaultBitmap;
		}
		try {
			if (photoCache.containsKey(username)) {
				return photoCache.get(username);
			}
			Bitmap bitmap;
			if (username.startsWith("mobilePhoto://")) {
				bitmap = BitmapFactory.decodeFile(new File(FileAccessor
						.getAvatarPathName(), username
						.substring("mobilePhoto://".length()))
						.getAbsolutePath());
			} else {

				bitmap = DemoUtils.decodeStream(CCPAppManager.getContext()
						.getAssets().open("avatar/" + username),
						ResourceHelper.getDensity(null));
			}
			photoCache.put(username, bitmap);
			return bitmap;
		} catch (IOException e) {
		}
		return mDefaultBitmap;
	}

	/**
	 * 随即设置用户昵称
	 * 
	 * @param beas
	 * @return
	 */
	public static ArrayList<ECContacts> converContacts(
			ArrayList<ECContacts> beas) {

		if (beas == null || beas.isEmpty()) {
			return null;
		}
		Collections.sort(beas, new Comparator<ECContacts>() {

			@Override
			public int compare(ECContacts lhs, ECContacts rhs) {

				return lhs.getContactid().compareTo(rhs.getContactid());
			}

		});

		for (int i = 0; i < beas.size(); i++) {
			ECContacts accountBean = beas.get(i);
			if (i < 5) {
				// accountBean.setNickname(CONVER_NAME[i]);
				accountBean.setRemark(ContactLogic.CONVER_PHONTO[i]);
			} else {
				// accountBean.setNickname("" + i);
				accountBean.setRemark("personal_center_default_avatar.png");
			}
		}
		return beas;
	}

	public static ArrayList<ECContacts> initContacts() {
		ArrayList<ECContacts> list = new ArrayList<ECContacts>();
		ECContacts contacts = new ECContacts("KF4008818600668603");
		contacts.setNickname(CCPAppManager.getContext().getString(
				R.string.main_plus_mcmessage));
		contacts.setRemark(CONVER_PHONTO[0]);
		list.add(contacts);
		return list;
	}

	/**
	 * 是否在线客服
	 * 
	 * @param contact
	 * @return
	 */
	public static boolean isCustomService(String contact) {
		return CUSTOM_SERVICE.equals(contact);
	}

	// These are the Contacts rows that we will retrieve.
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.Contacts.PHOTO_ID, };

	
	public static ECArrayLists<ECContacts> getSearchPhoneContacts(Context ctx) {
		if (mobileContacts == null || mobileContacts.size() == 0) {
//			getPhoneContacts(ctx,100,0,mobileContacts);
		}
		return mobileContacts;
	}
	
	public static ECArrayLists<ECContacts> getPhoneContactsSearch(Context ctx) {
		if (mobileContacts == null || mobileContacts.size() == 0) {
			//send boradcast
		}
		return mobileContacts;
	}
	
	/**
	 * 获取手机联系人
	 * @param ctx
	 * @return
	 */
	public static ECArrayLists<ECContacts> getPhoneContactsSearch2(Context ctx) {
		return mobileContacts2;
	}
	
	
	/** 手机联系人,同步数据用 */
	public static ECArrayLists<ECContacts> mobileContacts2 = new ECArrayLists<ECContacts>();
	
	/**
	 * 获取手机系统联系人信息
	 * 
	 * @return
	 */
	public static int getPhoneContacts2(Context ctx,int pageSize,int page) {
		if(page==0){
			mobileContacts2.clear();
		}
		StringBuffer phoneBuf = new StringBuffer();
		ClientUser clientUser = CCPAppManager.getClientUser();

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + ContactsContract.Contacts.DISPLAY_NAME
				+ " NOTNULL) AND ("
				+ ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
		try {
			int offset=page*pageSize;
			Cursor cursor = ctx.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					CONTACTS_SUMMARY_PROJECTION,
					select,
					null,
					ContactsContract.Contacts.DISPLAY_NAME
							+ " COLLATE LOCALIZED ASC limit " + pageSize + " offset " + offset);
			
			int counts=0;
			
			while (cursor.moveToNext()) {
				int indexId = cursor
						.getColumnIndex(ContactsContract.Contacts._ID);
				String contactId = cursor.getString(indexId);
				int indexDisplayName = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String name = cursor.getString(indexDisplayName);

				Cursor phones = ctx.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);

				// data.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4,
				// 0)]);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Phone phone = new Phone(0,phoneNumber);

					if (isMobileNO(phoneNumber)
							&& !clientUser.getMoblie().equals(phoneNumber.trim())) {

						ECContacts data = new ECContacts();
						data.setId(indexId);
						data.setNickname(name);
						data.setRname(name);
						data.setMobile(phoneNumber);
						// data.addPhone(phone);
						pyFormat(data);
						data.setSimpName(simp_name(name));

							if (!contactPhones.containsKey(phoneNumber)) {
								data.setStatus(-2);
								phoneBuf.append(",");
								phoneBuf.append(phoneNumber);
							}
						

							mobileContacts2.add(data);
					}
					counts++;
				}
				phones.close();
			}
			LogUtil.d("getPhoneContacts2", "counts>>"+counts+"  pageSize>>"+pageSize);
			if(counts==pageSize){
				page+=1;
				LogUtil.d("getPhoneContacts2", "page>>"+page);
			}else{
				LogUtil.d("getPhoneContacts2", "page>>  stop");
			}
			cursor.close();

			if (mobileContacts2 != null) {
				ArrayContacts(mobileContacts2);
				// Collections.sort(contacts, new PyComparator());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}

	
	
	/**
	 * 获取手机系统联系人信息
	 * 
	 * 
	 * @return
	 */
	public static int getPhoneContacts(Context ctx,int pageSize,int page) {
		if(page==0){
			mobileContacts.clear();
		}
		StringBuffer phoneBuf = new StringBuffer();
		ClientUser clientUser = CCPAppManager.getClientUser();

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + ContactsContract.Contacts.DISPLAY_NAME
				+ " NOTNULL) AND ("
				+ ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
		try {
			int offset=page*pageSize;
			Cursor cursor = ctx.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					CONTACTS_SUMMARY_PROJECTION,
					select,
					null,
					ContactsContract.Contacts.DISPLAY_NAME
							+ " COLLATE LOCALIZED ASC limit " + pageSize + " offset " + offset);
			
			int counts=0;
			
			while (cursor.moveToNext()) {
				int indexId = cursor
						.getColumnIndex(ContactsContract.Contacts._ID);
				String contactId = cursor.getString(indexId);
				int indexDisplayName = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String name = cursor.getString(indexDisplayName);

				Cursor phones = ctx.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);

				// data.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4,
				// 0)]);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Phone phone = new Phone(0,phoneNumber);

					if (isMobileNO(phoneNumber)
							&& !clientUser.getMoblie().equals(phoneNumber)) {

						ECContacts data = new ECContacts();
						data.setId(indexId);
						data.setNickname(name);
						data.setRname(name);
						data.setMobile(phoneNumber);
						// data.addPhone(phone);
						pyFormat(data);
						data.setSimpName(simp_name(name));

							if (!contactPhones.containsKey(phoneNumber)) {
								data.setStatus(-2);
								phoneBuf.append(",");
								phoneBuf.append(phoneNumber);
							}
						

						mobileContacts.add(data);
					}
					counts++;
				}
				phones.close();
			}
			LogUtil.d("pagesizesss", "counts>>"+counts+"  pageSize>>"+pageSize);
			if(counts==pageSize){
				page+=1;
				LogUtil.d("pagesizesss", "page>>"+page);
			}else{
				LogUtil.d("pagesizesss", "page>>  stop");
			}
			cursor.close();

			if (mobileContacts != null) {
				ArrayContacts(mobileContacts);
				// Collections.sort(contacts, new PyComparator());
			}

			if (null != phoneBuf && phoneBuf.length() > 1) {
				phoneString = phoneBuf.substring(1, phoneBuf.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	
	/**
	 * 获取手机系统联系人信息
	 * 
	 * @return
	 */
	public static ECArrayLists<ECContacts> getPhoneContacts2(Context ctx) {
		mobileContacts.clear();
		StringBuffer phoneBuf = new StringBuffer();
		ClientUser clientUser = CCPAppManager.getClientUser();

		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = "((" + ContactsContract.Contacts.DISPLAY_NAME
				+ " NOTNULL) AND ("
				+ ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
		try {
			Cursor cursor = ctx.getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI,
					CONTACTS_SUMMARY_PROJECTION,
					select,
					null,
					ContactsContract.Contacts.DISPLAY_NAME
							+ " COLLATE LOCALIZED ASC");
			while (cursor.moveToNext()) {
				int indexId = cursor
						.getColumnIndex(ContactsContract.Contacts._ID);
				String contactId = cursor.getString(indexId);
				int indexDisplayName = cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				String name = cursor.getString(indexDisplayName);

				Cursor phones = ctx.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);

				// data.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4,
				// 0)]);
				while (phones.moveToNext()) {
					String phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// Phone phone = new Phone(0,phoneNumber);

					if (isMobileNO(phoneNumber)
							&& !clientUser.getMoblie().equals(phoneNumber)) {

						ECContacts data = new ECContacts();
						data.setId(indexId);
						data.setNickname(name);
						data.setRname(name);
						data.setMobile(phoneNumber);
						// data.addPhone(phone);
						pyFormat(data);
						data.setSimpName(simp_name(name));

						if (!contactPhones.containsKey(phoneNumber)) {
							data.setStatus(-2);
						//	phoneList.add(phoneNumber);
							phoneBuf.append(",");
							phoneBuf.append(phoneNumber);
						}

						mobileContacts.add(data);
					}
				}

				phones.close();
			}
			cursor.close();

			if (mobileContacts != null) {
				ArrayContacts(mobileContacts);
				// Collections.sort(contacts, new PyComparator());
			}

			if (null != phoneBuf && phoneBuf.length() > 1) {
				phoneString = phoneBuf.substring(1, phoneBuf.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mobileContacts;
	}

	public static String simp_name(String name) {
		int len = name.length();
		int h_w = 0;
		String tn = "";
		String n = "";

		if (len > 0) {
			tn = name.substring(len - 1);
			if (isChineseChar(tn)) {
				h_w += 2;
			} else {
				h_w++;
			}
		}
		if (len > 1) {
			n = name.substring(len - 2, len - 1);
			if (isChineseChar(n)) {
				h_w += 2;
			} else {
				h_w++;
			}
			tn = n + tn;
		}
		if (h_w < 4 && len > 2) {
			n = name.substring(len - 3, len - 2);
			if (isChineseChar(n)) {
				h_w += 2;
			} else {
				h_w++;
			}
			tn = n + tn;
		}
		if (h_w < 4 && len > 3) {
			n = name.substring(len - 4, len - 3);
			if (isChineseChar(n)) {
				if (h_w < 3) {
					tn = n + tn;
				}
			} else {
				tn = n + tn;
			}
		}
		return tn;
	}

	/**
	 * 判断是否中午
	 * @param str
	 * @return
	 */
	public static boolean isChineseChar(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 判断是否手机号
	 * @param mobiles
	 * @return
	 */
	
	public static String format(String s){ 
		  String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", ""); 
		  return str; 
		 }
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");

		Matcher m = p.matcher(format(mobiles.trim()));
Log.i("contacts", m.matches()+"-----------"+mobiles);
		return m.matches();

	}

	/**
	 * 获取联系人配置
	 *
	 * @return
	 */
	public List<String[]> getSettingList() {
		List<String[]> cl = null;
		Cursor cursor = null;
		try {
			cursor = CCPAppManager
					.getContext()
					.getContentResolver()
					.query(ContactsContract.Settings.CONTENT_URI,
							projection_getSettingList, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cl = new ArrayList<String[]>();
				while (cursor.moveToNext()) {
					String[] s = new String[cursor.getColumnCount()];
					for (int i = 0; i < s.length; i++) {
						s[i] = cursor.getString(i);
					}
					cl.add(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}

		return cl;
	}

	public static ECArrayLists<ECContacts> getContractList(boolean showSim)
			throws Exception {
		// TODO 可能需要添加，是否显示无号码联系人选项，显示哪些账户联系人选项
		getInstance().getSettingList();
		Cursor cursor = null;
		ECArrayLists<ECContacts> cl = null;
		String where = "(" + ContactsContract.Data.MIMETYPE + " = ? or "
				+ ContactsContract.Data.MIMETYPE + " = ? or "
				+ ContactsContract.Data.MIMETYPE + " = ? or "
				+ ContactsContract.Data.MIMETYPE + " = ? or "
				+ ContactsContract.Data.MIMETYPE + " = ? or "
				+ ContactsContract.Data.MIMETYPE + " = ?)";
		if (!showSim) {
			where += " AND (" + ContactsContract.RawContacts.ACCOUNT_TYPE
					+ " NOT LIKE " + "'%sim%'" + " or "
					+ ContactsContract.RawContacts.ACCOUNT_TYPE + " is null)";
		}
		String[] WhereParams = {
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
				ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
				ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
				ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
				ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
				ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE };
		Uri uri = ContactsContract.Data.CONTENT_URI;
		try {
			cursor = CCPAppManager
					.getContext()
					.getContentResolver()
					.query(uri,
							projection_getContractList,
							where,
							WhereParams,
							ContactsContract.Data.DISPLAY_NAME + ","
									+ ContactsContract.Data.RAW_CONTACT_ID);
			if (cursor != null) {
				cl = new ECArrayLists<ECContacts>();
				ECContacts cli = null;

				long tmpid = -1;
				while (cursor.moveToNext()) {
					long rawContactId = cursor.getLong(0);
					String mimetype = cursor.getString(1);
					String data1 = cursor.getString(2);
					int data2 = cursor.getInt(3);
					String data3 = cursor.getString(4);

					long Id = cursor.getLong(6);
					if (tmpid != rawContactId) {
						if (cursor.getPosition() != 0
								&& !TextUtils.isEmpty(cli.getContactid())) {
							if (TextUtils.isEmpty(cli.getNickname())) {
								cli.setNickname(cli.getContactid());
							}
							cl.add(cli);
						}
						cli = new ECContacts();
						cli.setId(rawContactId);
					}
					tmpid = rawContactId;
					if (mimetype
							.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
						com.im.sdk.dy.core.Phone phone = null;
						if (data2 == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
							phone = new com.im.sdk.dy.core.Phone(rawContactId,
									Id, data1, data3);
						} else {
							phone = new com.im.sdk.dy.core.Phone(rawContactId,
									Id, data2, data1);
						}
						cli.addPhone(phone);
					}
					if (mimetype
							.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
						cli.setNickname(data1);
					}
					if (mimetype
							.equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
						cli.setPhotoId(Id);
					}
					cli.setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager
							.getIntRandom(4, 0)]);
					pyFormat(cli);
					// 最后一条记录时添加最后一个
					if (cursor.getPosition() == (cursor.getCount() - 1)) {
						if (TextUtils.isEmpty(cli.getNickname())) {
							cli.setNickname(cli.getContactid());
						}
						cl.add(cli);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
			if (format != null) {
				format = null;
			}
		}
		if (cl != null) {
			Collections.sort(cl, new PyComparator());
		}
		// PinyinHelper.release();
		return cl;
	}

	private static HanyuPinyinOutputFormat format = null;

	public static void pyFormat(ECContacts contact) {
		try {
			String name = contact.getRname();
			if (name == null || name.trim().length() == 0) {
				name = contact.getNickname();
			}

			if (name == null || name.trim().length() == 0) {
				return;
			}
			name = name.trim();
			// 拼音转换设置
			if (format == null) {
				format = new HanyuPinyinOutputFormat();// 定义转换格式
				format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不要声调
				format.setVCharType(HanyuPinyinVCharType.WITH_V);// 设置 女 nv
			}

			String qpName = ""; // 用于分隔全拼数组
			StringBuilder qpBuilder = new StringBuilder();

			String qpNameStr = ""; // 完整的全拼Str
			StringBuilder qpStrBuilder = new StringBuilder();

			String qpNumber = ""; // 全拼对应的拨号盘数字
			StringBuilder qpNumberBuilder = new StringBuilder();

			String jpName = ""; // 简拼
			StringBuilder jpNameBuilder = new StringBuilder();

			String jpNumber = ""; // 简拼对应的拨号盘数字
			StringBuilder jpNumberBuilder = new StringBuilder();
			// LogUtil.v(name);
			int length = 0;
			char c = 0;
			// 处理英文名

			if (name.getBytes().length == name.length()) {
				qpName = name;
				String[] splitName = name.split(" ");
				for (String s : splitName) {
					length = s.length();
					for (int i = 0; i < length; i++) {
						qpNumberBuilder.append(DialNumberMap.numberMap.get(s
								.charAt(i)) == null ? String.valueOf(s
								.charAt(i)) : DialNumberMap.numberMap.get(s
								.charAt(i)));
					}
					c = s.charAt(0);
					qpNumberBuilder.append(" ");
					jpNumberBuilder
							.append(DialNumberMap.numberMap.get(c) == null ? String
									.valueOf(c) : DialNumberMap.numberMap
									.get(c));
					jpNameBuilder.append(String.valueOf(c));
					qpStrBuilder.append(String.valueOf(c).toUpperCase())
							.append(s.subSequence(1, s.length()));
				}
				length = splitName.length;
				for (int i = 0; i < length; i++) {
					splitName[i] = splitName[i].toLowerCase();
				}
				// jpName = jpNameBuilder.toString();
			} else { // 含有中文
				int namelength = name.length();
				for (int i = 0; i < namelength; i++) {
					try {
						String[] pyArray = PinyinHelper
								.toHanyuPinyinStringArray(name.charAt(i),
										format);
						if (pyArray == null) {
							// char c = name.charAt(i);
							c = name.charAt(i);
							if (' ' == c) {
								continue;
							}
							qpStrBuilder.append(c);
							Integer num = DialNumberMap.numberMap.get(c);
							qpNumberBuilder.append(
									num == null ? String.valueOf(c) : num)
									.append(" ");
							jpNumberBuilder.append(num == null ? String
									.valueOf(c) : num);
							qpBuilder.append(String.valueOf(c).toLowerCase())
									.append(" ");
							jpNameBuilder.append(String.valueOf(c)
									.toLowerCase());
							continue;
						} else {
							String py = pyArray[0];
							length = py.length();
							for (int j = 0; j < length; j++) {
								qpNumberBuilder.append(DialNumberMap.numberMap
										.get(py.charAt(j)));
							}
							c = py.charAt(0);
							qpNumberBuilder.append(" ");
							jpNameBuilder.append(c);
							jpNumberBuilder.append(DialNumberMap.numberMap
									.get(c));
							qpBuilder.append(py).append(" ");
							qpStrBuilder
									.append(String.valueOf(c).toUpperCase())
									.append(py.subSequence(1, py.length()));// 将拼音第一个字母转成大写后拼接在一起。
						}
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
				qpName = qpBuilder.toString();
			}
			jpName = jpNameBuilder.toString();
			jpNumber = jpNumberBuilder.toString();
			qpNumber = qpNumberBuilder.toString();
			qpNameStr = qpStrBuilder.toString();

			if (qpName.length() > 0) {
				contact.setQpName(qpName.trim().split(" "));
				contact.setQpNumber(qpNumber.trim().split(" "));
				contact.setJpNumber(jpNumber.trim());
				contact.setJpName(jpName);
				contact.setQpNameStr(qpNameStr.trim());
				contact.setQpNumber(qpNumber.trim().split(" "));
			}
			
			LogUtil.d("pyFormat","getJpName>>"+ contact.getJpName()+" getJpName>> "+contact.getJpNumber()+"  getJpNumber>>"+contact.getQpName()+" getQpName>>"+contact.getQpNameStr());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回讨论组的头像
	 * 
	 * @return
	 */
	public static Bitmap getChatroomPhoto(final String groupid) {
		try {
			if (photoCache.containsKey(groupid)) {
				return photoCache.get(groupid);
			}
			new Thread(new Runnable() {

				@Override
				public void run() {
					processChatroomPhoto(groupid);
				}
			});
			processChatroomPhoto(groupid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (groupid.toUpperCase().startsWith("G")) {
			return BitmapFactory.decodeResource(CCPAppManager.getContext()
					.getResources(), R.drawable.group_head);
		}
		return mDefaultBitmap;
	}

	/**
	 * @param groupid
	 */
	private static void processChatroomPhoto(String groupid) {
		ArrayList<String> groupMembers = GroupMemberSqlManager
				.getGroupMemberID(groupid);
		if (groupMembers != null) {
			ArrayList<String> contactName = ContactSqlManager
					.getContactRemark(groupMembers.toArray(new String[] {}));
			if (contactName != null) {
				Bitmap[] bitmaps = new Bitmap[contactName.size()];
				if (bitmaps.length > 9) {
					bitmaps = new Bitmap[9];
				}
				List<BitmapUtil.InnerBitmapEntity> bitmapEntitys = getBitmapEntitys(bitmaps.length);
				for (int i = 0; i < bitmaps.length; i++) {
					Bitmap photo = getPhoto(contactName.get(i));
					photo = ThumbnailUtils.extractThumbnail(photo,
							(int) bitmapEntitys.get(0).width,
							(int) bitmapEntitys.get(0).width);
					bitmaps[i] = photo;
				}
				Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(
						bitmapEntitys, bitmaps);
				if (combineBitmap != null) {
					photoCache.put(groupid, combineBitmap);
					BitmapUtil.saveBitmapToLocal(groupid, combineBitmap);
				}
			}
		}
	}

	private static List<BitmapUtil.InnerBitmapEntity> getBitmapEntitys(int count) {
		List<BitmapUtil.InnerBitmapEntity> mList = new LinkedList<BitmapUtil.InnerBitmapEntity>();
		String value = ECPropertiesUtil.readData(CCPAppManager.getContext(),
				String.valueOf(count), R.raw.nine_rect);
		LogUtil.d("value=>" + value);
		String[] arr1 = value.split(";");
		int length = arr1.length;
		for (int i = 0; i < length; i++) {
			String content = arr1[i];
			String[] arr2 = content.split(",");
			BitmapUtil.InnerBitmapEntity entity = null;
			for (int j = 0; j < arr2.length; j++) {
				entity = new BitmapUtil.InnerBitmapEntity();
				entity.x = Float.valueOf(arr2[0]);
				entity.y = Float.valueOf(arr2[1]);
				entity.width = Float.valueOf(arr2[2]);
				entity.height = Float.valueOf(arr2[3]);
			}
			mList.add(entity);
		}
		return mList;
	}

	public static void getMobileContactPhoto(List<ECContacts> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (ECContacts contact : list) {
			if (contact.getPhotoId() > 0) {
				getMobileContactPhoto(contact);
			}
		}
	}

	public static void getMobileContactPhoto(ECContacts contact) {
		try {
			Bitmap bitmap = getContactPhoto(contact);
			if (bitmap == null) {
				return;
			}
			contact.setRemark("mobilePhoto://" + contact.getContactid());
			DemoUtils.saveBitmapToLocal(
					new File(FileAccessor.getAvatarPathName(), contact
							.getContactid()), bitmap);
			ContactSqlManager.updateContactPhoto(contact);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getContactPhoto(ECContacts contact) {
		long photoId = contact.getPhotoId();
		if (photoId != 0) {
			Cursor cursor = null;
			ContentResolver contentResolver = MyAppliaction.getmInstance()
					.getApplicationContext().getContentResolver();
			try {
				cursor = contentResolver.query(
						ContactsContract.Data.CONTENT_URI, new String[] {
								ContactsContract.CommonDataKinds.Photo._ID,
								ContactsContract.CommonDataKinds.Photo.PHOTO },
						ContactsContract.CommonDataKinds.Photo._ID + " = "
								+ photoId, null, null);
				if (cursor != null && cursor.moveToNext()) {
					byte[] photo = cursor.getBlob(1);
					if (photo != null) {
						return BitmapFactory.decodeByteArray(photo, 0,
								photo.length);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
					cursor = null;
				}
			}
		}
		return null;
	}

	public static class ComparatorContacts implements Comparator {

		public int compare(Object arg0, Object arg1) {
			ECContacts c0 = (ECContacts) arg0;
			ECContacts c1 = (ECContacts) arg1;
			int flag = 0;
			if (null != c0.getJpName() && null != c1.getJpName()) {
				flag = c0.getJpName().toLowerCase().compareTo(c1.getJpName().toLowerCase());
			}

			if (flag == 0) {
				if (null != c0.getRname() && null != c1.getRname()) {
					flag = c0.getRname().compareTo(c1.getRname());
				}
			}
			return flag;
		}

	}

	public static void ArrayContacts(ArrayList<ECContacts> contacts) {
		if (null != contacts) {

			ComparatorContacts comparator = new ComparatorContacts();
			Collections.sort(contacts, comparator);
		}
	}

	public static void updateContacts(ECContacts contact) {
		// TODO Auto-generated method stub
		if (null != contacts) {
			for (int i = 0; i < contacts.size(); i++) {
				if (contact.getContactid().equals(
						contacts.get(i).getContactid())) {
					contacts.get(i).setRname(contact.getRname());
					contacts.get(i).setRemark(contact.getRemark());
					break;
				}
			}
		}
	}

	public static void sendSystemMessage(Activity activity, String name,
			String mobile) {
		insert(activity, name, mobile);
		sendSystemMessage(activity, mobile);
	}

	/**
	 * 发送短信
	 * 
	 * @param activity
	 * @param mobile
	 */
	public static void sendSystemMessage(Activity activity, String mobile) {
		if(null==activity||CCPAppManager
				.getClientUser()==null) return;
		Uri smsToUri = Uri.parse("smsto:" + mobile);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", String.format(activity
				.getString(R.string.contacts_message), CCPAppManager
				.getClientUser().getUserName()));
		activity.startActivity(intent);
	}

	/**
	 * 
	 * @param activity
	 * @param given_name
	 * @param mobile_number
	 * @return
	 */
	public static boolean insert(Activity activity, String given_name,
			String mobile_number) {
		try {
			ContentValues values = new ContentValues();

			// 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
			Uri rawContactUri = activity.getContentResolver().insert(
					RawContacts.CONTENT_URI, values);
			long rawContactId = ContentUris.parseId(rawContactUri);

			// 向data表插入姓名数据
			if (given_name != "") {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
				values.put(StructuredName.GIVEN_NAME, given_name);
				activity.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}

			// 向data表插入电话数据
			if (mobile_number != "") {
				values.clear();
				values.put(Data.RAW_CONTACT_ID, rawContactId);
				values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
				values.put(Phone.NUMBER, mobile_number);
				values.put(Phone.TYPE, Phone.TYPE_MOBILE);
				activity.getContentResolver().insert(
						ContactsContract.Data.CONTENT_URI, values);
			}

			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
			activity.getContentResolver().insert(
					ContactsContract.Data.CONTENT_URI, values);
		}

		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 处理好友操作第三方请求
	 * 
	 * @param cmdType
	 * @param mRecipients
	 */
	public static void sendFriendMessage(FriendCmdType cmdType,
			String mRecipients) {
		ClientUser user = CCPAppManager.getClientUser();
		ECMessage msg = ECMessage.createECMessage(Type.TXT);

		ECTextMessageBody msgBody = new ECTextMessageBody("");

		if (cmdType == FriendCmdType.REQUEST) {
			msgBody.setMessage("好友请求");
		}
		msg.setBody(msgBody);
		// 设置消息接收者
		msg.setTo(mRecipients);
		msg.setSessionId(mRecipients);
		JsonObject j = new JsonObject();
		j.addProperty("type", cmdType.ordinal());
		j.addProperty("avater", user.getAvatar());
		j.addProperty("sex", user.getSex());
		j.addProperty("name", user.getUserName());
		msg.setUserData(new DyMessage(MessageType.FRIEND, j.toString())
				.toMessgeUserData());
		LogUtil.d("sendFriendMessage",
				new DyMessage(MessageType.FRIEND, j.toString())
						.toMessgeUserData());
		IMChattingHelper.sendFriendECMessage(msg);

	}

	/**
	 * 处理联系人
	 * 
	 * @param msg
	 * @return
	 */
	public static long addContacts(ECMessage msg, int status) {

		ECContacts contacts = new ECContacts();
		contacts.setContactid(msg.getForm());
		DyMessage dyMessage = deUserData(msg);
		String data = dyMessage.getValue();
		LogUtil.d("sendFriendMessage", "re>>" + data);
		if (null != data && data.length() > 0) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(data);
				contacts.setAvatar(jsonObject.getString("avater"));
				contacts.setSex(jsonObject.getInt("sex"));
				contacts.setNickname(jsonObject.getString("name"));
				contacts.setRname(jsonObject.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		contacts.setStatus(status);
		return ContactSqlManager.insertContact(contacts);

	}

	public static long addAdminContacts(ECMessage msg,int type) {

		ECContacts contacts = new ECContacts();
		contacts.setContactid(msg.getForm());
		contacts.setAvatar("");
		contacts.setSex(0);
		contacts.setNickname(msg.getNickName());
		contacts.setRname(msg.getNickName());
		contacts.setType(type);
		return ContactSqlManager.insertContact(contacts);

	}

	/**
	 * 解析好友请求信息
	 * 
	 * @param msg
	 * @return
	 */
	public static DyMessage deUserData(ECMessage msg) {
		return new DyMessage(msg.getUserData());
		/*
		 * String userData = msg.getUserData(); if (null != userData &&
		 * userData.length() > 0 && userData.contains(DyMessage.IM_MYDATA)) {
		 * int start = userData.indexOf(DyMessage.IM_MYDATA); String value =
		 * userData.substring(start + DyMessage.IM_MYDATA.length()); if (null !=
		 * value && !value.equals("")) { return new
		 * DyMessage().toMessage(value); } }
		 */
		// return null;
	}

	/**
	 * 解析个人信息
	 * @param itemJson
	 * @return
	 */
	public static ECContacts GetContacts(JSONObject itemJson) {
		try {
			// 如果已经是好友，则添加到本地
			ECContacts contacts = new ECContacts();
			
			if(!itemJson.isNull("friend_id"))
			contacts.setContactid(itemJson.getInt("friend_id") + "");
			
			if(!itemJson.isNull("name"))
			contacts.setNickname(itemJson.getString("name"));
			
			if(!itemJson.isNull("mobile"))
			contacts.setMobile(itemJson.getString("mobile"));
			
			if(!itemJson.isNull("city"))
			contacts.setCity(itemJson.getString("city"));
			
			if(!itemJson.isNull("department"))
			contacts.setDepartment(itemJson.getString("department"));
			
			if(!itemJson.isNull("hospital"))
			contacts.setHospital(itemJson.getString("hospital"));
			
			if(!itemJson.isNull("remark")){
				contacts.setRemark(itemJson.getString("remark"));
			}
			
			if(!itemJson.isNull("sex"))
			contacts.setSex(itemJson.getInt("sex"));
			
			if(!itemJson.isNull("province"))
			contacts.setProvince(itemJson.getString("province"));

			
			if(!itemJson.isNull("avatar"))
			contacts.setAvatar(itemJson.getString("avatar"));
			
			if(!itemJson.isNull("friend_type"))
			contacts.setType(itemJson.getInt("friend_type"));
			
			String rname ="";

			if(!itemJson.isNull("rname"))
				rname=itemJson.getString("rname");

			rname = rname == null || rname.equals("") ? contacts.getNickname()
					: rname;

			contacts.setRname(rname);
			return contacts;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/** 
     * 添加联系人 
     * 数据一个表一个表的添加，每次都调用insert方法 
     * */  
    private static void testAddContacts(Context ctx,String name,String phone){  
        /* 往 raw_contacts 中添加数据，并获取添加的id号*/  
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");  
        ContentResolver resolver =ctx.getContentResolver();  
        ContentValues values = new ContentValues();  
        long contactId = ContentUris.parseId(resolver.insert(uri, values));  
          
        /* 往 data 中添加数据（要根据前面获取的id号） */  
        // 添加姓名  
        uri = Uri.parse("content://com.android.contacts/data");  
        values.put("raw_contact_id", contactId);  
        values.put("mimetype", "vnd.android.cursor.item/name");  
        values.put("data2", name);  
        resolver.insert(uri, values);  
          
        // 添加电话  
        values.clear();  
        values.put("raw_contact_id", contactId);  
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");  
        values.put("data2", "2");  
        values.put("data1", phone);  
        resolver.insert(uri, values);  
    }  
    
    public static void testAdd(Context ctx){
    	for (int i = 0; i < 500; i++) {
    		testAddContacts(ctx, i+"", tstPhone(i+""));
//    		try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    		LogUtil.d("testAdd","add>>"+i);
		}
    }
    
    private static String tstPhone(String _i){
    	
    	String p= "186"+_i;
    	if(p.length()<11){
    		int len=11-p.length();
    		String a="";
    		for (int i = 0; i < len; i++) {
				a=a+"0";
			}
    		p=p+a;
    	}
    	return p;
    }
    
    public static void testDelete(Context ctx) {  
	    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");  
	    ContentResolver resolver = ctx.getContentResolver();  
	        uri = Uri.parse("content://com.android.contacts/data");  
	        resolver.delete(uri, null, null);  
	}  
}
