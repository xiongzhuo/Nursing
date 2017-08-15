package com.deya.hospital.vo;

import java.io.Serializable;

public class KindsVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getKind_name() {
		return kind_name;
	}

	public void setKind_name(String kind_name) {
		this.kind_name = kind_name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	String kind_name;
	String kind;
	String attachment;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
}
