package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class FormItemsVo implements Serializable{
	private static final long serialVersionUID = 478978971L;
	List<SubItemlVo> sub_items;
	public String department;
	public String titile;
	public  String check_method="";
	
	public String getCheck_method() {
		return check_method;
	}

	public void setCheck_method(String check_method) {
		this.check_method = check_method;
	}
	private double itemTotalScore=-1;// 减少的分数
	
	public double getItemTotalScore() {
		return itemTotalScore;
	}

	public void setItemTotalScore(double itemTotalScore) {
		this.itemTotalScore = itemTotalScore;
	}
	public List<SubItemlVo> getSub_items() {
		return sub_items;
	}
	public void setSub_items(List<SubItemlVo> sub_items) {
		this.sub_items = sub_items;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	double score;
    int id;

}
