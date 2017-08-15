package com.deya.hospital.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Goods implements Parcelable {
	private int id;
	private String name;
	private String description;
	private int integral;
	private String picture;
	private int is_sign;
	
	public int getIs_sign() {
		return is_sign;
	}

	public void setIs_sign(int is_sign) {
		this.is_sign = is_sign;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Creator<Goods> CREATOR = new Creator<Goods>() {
		
		@Override
		public Goods[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Goods[size];
		}
		
		@Override
		public Goods createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new Goods(in);
		}
	};
	  
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(name);
        out.writeString(description);
        out.writeInt(integral);
        out.writeString(picture);
        out.writeInt(is_sign);
	}
	
	public Goods(Parcel in)
    {
		id = in.readInt();
		name = in.readString();
		description = in.readString();
		integral = in.readInt();
		picture = in.readString();
		is_sign=in.readInt();
    }
	
	public Goods() {
		// TODO Auto-generated constructor stub
	}
}
