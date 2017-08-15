package com.deya.hospital.vo;

import java.io.Serializable;

public class ArticalInfoVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 11111111L;
	private String id;
	private String title;
	private String type_id;
	private String summary;
	private String author;
	private String hospital_id;
	private String keywords;
	private String zt_type;
	private String pdf_name;
	private String art_flag;
	private String artice_number;
	private String read_count;
	private String state;
	private String create_time;
	private String is_collect;
	private boolean haspreviewdown;
	
	public boolean isHaspreviewdown() {
		return haspreviewdown;
	}
	public void setHaspreviewdown(boolean haspreviewdown) {
		this.haspreviewdown = haspreviewdown;
	}
	public String getDownload_count() {
		return download_count;
	}
	public void setDownload_count(String download_count) {
		this.download_count = download_count;
	}
	public String getJournal_title() {
		return journal_title;
	}
	public void setJournal_title(String journal_title) {
		this.journal_title = journal_title;
	}
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospital_name) {
		this.hospital_name = hospital_name;
	}
	private String download_count;
	private String journal_title;
	private String hospital_name;
	public String getIs_collect() {
		return is_collect;
	}
	public void setIs_collect(String is_collect) {
		this.is_collect = is_collect;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getHospital_id() {
		return hospital_id;
	}
	public void setHospital_id(String hospital_id) {
		this.hospital_id = hospital_id;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getZt_type() {
		return zt_type;
	}
	public void setZt_type(String zt_type) {
		this.zt_type = zt_type;
	}
	public String getPdf_name() {
		return pdf_name;
	}
	public void setPdf_name(String pdf_name) {
		this.pdf_name = pdf_name;
	}
	public String getArt_flag() {
		return art_flag;
	}
	public void setArt_flag(String art_flag) {
		this.art_flag = art_flag;
	}
	public String getArtice_number() {
		return artice_number;
	}
	public void setArtice_number(String artice_number) {
		this.artice_number = artice_number;
	}
	public String getRead_count() {
		return read_count;
	}
	public void setRead_count(String read_count) {
		this.read_count = read_count;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
