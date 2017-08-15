package com.deya.hospital.vo;

import java.io.Serializable;

public class CommentVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1398127391273L;
	public String content;
	public String id;
	public String update_time;
	public String avatar;
	public String name;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
