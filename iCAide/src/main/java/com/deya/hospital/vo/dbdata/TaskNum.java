package com.deya.hospital.vo.dbdata;

import java.io.Serializable;

public class TaskNum implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x23123123121L;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	private String date;
	private int num;
	private int unfinishNum;

	public int getUnfinishNum() {
		return unfinishNum;
	}

	public void setUnfinishNum(int unfinishNum) {
		this.unfinishNum = unfinishNum;
	}

}
