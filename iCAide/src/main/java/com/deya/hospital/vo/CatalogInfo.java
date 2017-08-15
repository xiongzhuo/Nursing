package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class CatalogInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 11111111L;
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public List<ArticalInfoVo> getArticles() {
		return articles;
	}
	public void setArticles(List<ArticalInfoVo> articles) {
		this.articles = articles;
	}
	private String type_id;
	private String type_name;
	private List<ArticalInfoVo> articles;

}
