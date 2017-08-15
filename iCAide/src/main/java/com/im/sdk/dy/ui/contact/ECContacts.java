package com.im.sdk.dy.ui.contact;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.im.sdk.dy.common.utils.LogUtil;
import com.im.sdk.dy.core.ClientUser;
import com.im.sdk.dy.core.Phone;
import com.im.sdk.dy.storage.AbstractSQLManager;
import com.im.sdk.dy.storage.ContactSqlManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 联系人信息
 * Created by yung on 2015/12/18.
 */
public class ECContacts  implements Parcelable {

    public static final Parcelable.Creator<ECContacts> CREATOR = new Creator<ECContacts>() {
        @Override
        public ECContacts[] newArray(int size) {
            return new ECContacts[size];
        }

        @Override
        public ECContacts createFromParcel(Parcel in) {
            return new ECContacts(in);
        }
    };

    private long id;
    /**联系人账号*/
    private String contactid;
    /**联系人昵称*/
    private String nickname;
    /**联系人类型*/
    private int type;
    /**联系人账号Token*/
    private String token;
    /**联系人子账号*/
    private String subAccount;
    /**联系人子账号Token*/
    private String subToken;
    /**备注*/
    private String remark;
    
    /**手机*/
    private String mobile;
    /**头像路劲*/
    private String avatar;
    /**性别*/
    private int sex;
    /**医院*/
    private String hospital;
    /**省*/
    private String province;
    /**市*/
    private String city;
    /**部门*/
    private String department;
    /**备注名称*/
    private String rname;
    
    /**简写名，后几位*/
    private String simpName;
    
    
    private int status;
    
    private int isAlreadyAdd=0;
    
    
    
	public int getIsAlreadyAdd() {
		return isAlreadyAdd;
	}
	public void setIsAlreadyAdd(int isAlreadyAdd) {
		this.isAlreadyAdd = isAlreadyAdd;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	private List<Phone> phoneList;
    // Other
    private String[] qpName;
    private String jpNumber; //简拼对应的拨号键盘的数字
    private String jpName;
    private String qpNameStr;
    private String[] qpNumber; //保存拼音对应的拨号键盘的数字
    private long photoId;
    private String regis_job;
    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return the contactid
     */
    public String getContactid() {
        return contactid;
    }
    /**
     * @param contactid the contactid to set
     */
    public void setContactid(String contactid) {
        this.contactid = contactid;
    }
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    
    public String getRegis_job() {
		return regis_job;
	}
	public void setRegis_job(String regis_job) {
		this.regis_job = regis_job;
	}
	private ECContacts (Parcel in) {
        this.id  = in.readLong();
        this.contactid = in.readString();
        this.type = in.readInt();
        this.nickname = in.readString();
        this.subAccount= in.readString();
        this.subToken= in.readString();
        this.token= in.readString();
        this.remark= in.readString();
        this.sex= in.readInt();
        this.mobile= in.readString();
        this.avatar= in.readString();
        this.hospital= in.readString();
        this.province= in.readString();
        this.city= in.readString();
        this.department= in.readString();
        this.rname= in.readString();
        this.status= in.readInt();
        this.regis_job=in.readString();
    }
    
    public String getSortKey() {
    	LogUtil.d("getSortKey", "jpName>>"+jpName);
        if(jpName == null || jpName.trim().length() <= 0) {
            return "#";
        }
        String c = jpName.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()) {
            return c.toUpperCase();
        } else {
            return "#";
        }
    }



    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        values.put(AbstractSQLManager.ContactsColumn.CONTACT_ID, this.contactid);
        values.put(AbstractSQLManager.ContactsColumn.type, this.type);
        values.put(AbstractSQLManager.ContactsColumn.USERNAME, this.nickname );
        values.put(AbstractSQLManager.ContactsColumn.SUBACCOUNT, this.subAccount );
        values.put(AbstractSQLManager.ContactsColumn.SUBTOKEN, this.subToken );
        values.put(AbstractSQLManager.ContactsColumn.TOKEN, this.token );
        values.put(AbstractSQLManager.ContactsColumn.REMARK, this.remark);
        values.put(AbstractSQLManager.ContactsColumn.MOBILE, this.mobile);
        values.put(AbstractSQLManager.ContactsColumn.AVATAR, this.avatar);
        values.put(AbstractSQLManager.ContactsColumn.SEX, this.sex);
        values.put(AbstractSQLManager.ContactsColumn.HOSPITAL, this.hospital);
        values.put(AbstractSQLManager.ContactsColumn.PROVINCE, this.province);
        values.put(AbstractSQLManager.ContactsColumn.CITY, this.city);
        values.put(AbstractSQLManager.ContactsColumn.DEPARTMENT, this.department);
        values.put(AbstractSQLManager.ContactsColumn.RNAME, this.rname);
        values.put(AbstractSQLManager.ContactsColumn.STATUS , this.status);
        values.put(AbstractSQLManager.ContactsColumn.OTHER_STR , this.regis_job);
        return values;
    }
   
    public ECContacts() {

    }

    public void setClientUser(ClientUser user) {
        setContactid(user.getUserId());
        setNickname(user.getUserName());
        setRemark(ContactLogic.CONVER_PHONTO[ContactSqlManager.getIntRandom(4, 0)]);
    }

    public ECContacts(String contactId) {
        this.contactid = contactId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(this.contactid);
        dest.writeInt(this.type);
        dest.writeString(this.nickname);
        dest.writeString(this.subAccount);
        dest.writeString(this.subToken);
        dest.writeString(this.token);
        dest.writeString(this.remark);
        dest.writeInt(this.sex);
        dest.writeString(this.mobile);
        dest.writeString(this.avatar);
        dest.writeString(this.hospital);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.department);
        dest.writeString(this.rname);
        dest.writeInt(this.status);
        dest.writeString(this.regis_job);
    }
  
    public void addPhoneList(List<Phone> phoneList) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<Phone>();
        }
        this.phoneList.addAll(phoneList);
        phoneList.clear();
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void addPhone(Phone phone) {
        if (this.phoneList == null) {
            this.phoneList = new ArrayList<Phone>();
            setContactid(phone.getPhoneNum());
        }
        this.phoneList.add(phone);
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String[] getQpName() {
        return qpName;
    }

    public void setQpName(String[] qpName) {
        this.qpName = qpName;
    }

    public void setQpNumber(String[] qpNumber) {
        this.qpNumber = qpNumber;
    }


    public void setJpNumber(String jpNumber) {
        this.jpNumber = jpNumber;
    }

    public void setJpName(String jpName) {
        this.jpName = jpName;
    }

    public String getQpNameStr() {
        return qpNameStr;
    }

    public void setQpNameStr(String qpNameStr) {
        this.qpNameStr = qpNameStr;
    }
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getJpNumber() {
		return jpNumber;
	}
	public String getJpName() {
		return jpName;
	}
	public String getRname() {
		return rname==null||rname.equals("")?nickname:rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getSimpName() {
		return simpName;
	}
	public void setSimpName(String simpName) {
		this.simpName = simpName;
	}
	
	
}
