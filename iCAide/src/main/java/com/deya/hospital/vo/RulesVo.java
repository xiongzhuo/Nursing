package com.deya.hospital.vo;

import java.io.Serializable;

public class RulesVo implements Serializable {
	private static final long serialVersionUID = 0x32190831231L;
	private String id;
	private String pdata_id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPdata_id() {
		return pdata_id;
	}

	public void setPdata_id(String pdata_id) {
		this.pdata_id = pdata_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public boolean isChoosed;

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

}
