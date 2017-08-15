package com.deya.hospital.vo.dbdata;

import java.io.Serializable;

public class Attachments implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x11111121133L;
	private String file_type;
	private String file_name;
	private String state="";
	private String date;//文件时间
	private String imgId;
	private String time;
	private int personIndex;
	private int positon;
	
	public int getPositon() {
		return positon;
	}
	public void setPositon(int positon) {
		this.positon = positon;
	}
	public int getPersonIndex() {
		return personIndex;
	}
	public void setPersonIndex(int personIndex) {
		this.personIndex = personIndex;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

}
