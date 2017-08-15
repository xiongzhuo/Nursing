package com.deya.hospital.vo;

public class Prize {
	private int id;
	private int uid;
	private int goods_id;
	private String convert_time;
	private int convert_status;
	private String goodsname;
	private int integral;
	private String picture;
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public int getGoods_id() {
		return goods_id;
	}


	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}


	public String getConvert_time() {
		return convert_time;
	}


	public void setConvert_time(String convert_time) {
		this.convert_time = convert_time;
	}


	public int getConvert_status() {
		return convert_status;
	}


	public void setConvert_status(int convert_status) {
		this.convert_status = convert_status;
	}


	public String getGoodsname() {
		return goodsname;
	}


	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
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


	public Prize() {
		// TODO Auto-generated constructor stub
	}
}
