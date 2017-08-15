package com.deya.hospital.vo;

import java.io.Serializable;

public class ConsumptionFormItemVo implements Serializable{
	private static final long serialVersionUID = 203912709371290L;
	private String pull_num="";
	private String stocks_num="";
	private String standard="";
	private String unit="";
	private String title="";
	public String getPickupNum() {
		return pull_num;
	}
	public void setPickupNum(String pull_num) {
		this.pull_num = pull_num;
	}
	public String getStoreNum() {
		return stocks_num;
	}
	public void setStoreNum(String stocks_num) {
		this.stocks_num = stocks_num;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStandardNum() {
		return standard;
	}
	public void setStandardNum(String standard) {
		this.standard = standard;
	}

}
