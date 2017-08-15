package com.deya.hospital.vo;

import java.io.Serializable;

public class Subrules implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 0x123280378071L;
	double score;
	String r_id;
	String name;
	int openState;
	boolean isChoosed;
	String choose="";
	int is_choosed;
	boolean canOPen=true;
	
	
	public String getChoose() {
		return choose;
	}
	public void setChoose(String choose) {
		this.choose = choose;
	}
	boolean isRemarked;
	
	public boolean isCanOPen() {
		return canOPen;
	}
	public void setCanOPen(boolean canOPen) {
		this.canOPen = canOPen;
	}
	public int getIs_choosed() {
		return is_choosed;
	}
	public void setIs_choosed(int is_choosed) {
		this.is_choosed = is_choosed;
	}
	public boolean isRemarked() {
		return isRemarked;
	}
	public void setRemarked(boolean isRemarked) {
		this.isRemarked = isRemarked;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenState() {
		return openState;
	}
	public void setOpenState(int openSate) {
		this.openState = openSate;
	}
	public boolean isChoosed() {
		return isChoosed;
	}
	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}
	

}
