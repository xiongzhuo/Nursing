package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class FormDetailListVo implements Serializable {
	private static final long serialVersionUID = 478978971L;
	List<FormDetailVo> sub_items;
	public String department;
	public String titile;
	private String scorType;// 此参数判断是为用计分方式还是对错方式
	private double substrScore;// 减少的分数

	public double getSubstrScore() {
		return substrScore;
	}

	public void setSubstrScore(double substrScore) {
		this.substrScore = substrScore;
	}
	private double itemTotalScore=-1;// 减少的分数
	
	public double getItemTotalScore() {
		return itemTotalScore;
	}

	public void setItemTotalScore(double itemTotalScore) {
		this.itemTotalScore = itemTotalScore;
	}

	public List<FormDetailVo> getSub_items() {
		return sub_items;
	}

	public void setSub_items(List<FormDetailVo> sub_items) {
		this.sub_items.clear();
		this.sub_items.addAll(sub_items);
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public String getScorType() {
		return scorType;
	}

	public void setScorType(String scorType) {
		this.scorType = scorType;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public String getCheck_method() {
		return check_method;
	}

	public void setCheck_method(String check_method) {
		this.check_method = check_method;
	}

	double score;
	int id;
	int orders;
	String check_method = "";


	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String name="";
}
