package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class WrongRuleVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x3871263781L;
	List<RulesVo> items;
	String name;
	public List<RulesVo> getItems() {
		return items;
	}
	public void setItems(List<RulesVo> items) {
		this.items = items;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
