package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class MagzineListVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 111111112L;
	private String id;
	private String name;
	private String image;
	private List<JournalsInfo> journals;
	public List<JournalsInfo> getJournals() {
		return journals;
	}
	public void setJournals(List<JournalsInfo> journals) {
		this.journals = journals;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	private String note;
	private String create_time;
	private String state;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
