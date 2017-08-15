package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class ConsumptionFormVo implements Serializable{
	private static final long serialVersionUID = 203912709371290L;
	private int total_pull_num;
	private int total_stocks_num;
	private String unit;
	private String title;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTotal_pull_num() {
		return total_pull_num;
	}
	public void setTotal_pull_num(int total_pull_num) {
		this.total_pull_num = total_pull_num;
	}
	public int getTotal_stocks_num() {
		return total_stocks_num;
	}
	public void setTotal_stocks_num(int total_stocks_num) {
		this.total_stocks_num = total_stocks_num;
	}
	private List<ConsumptionFormItemVo> sub_info;
	
	public List<ConsumptionFormItemVo> getItems() {
		return sub_info;
	}
	public void setItems(List<ConsumptionFormItemVo> sub_info) {
		this.sub_info = sub_info;
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

}
