package com.deya.hospital.vo;

import java.io.Serializable;

public class picMessageVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 000000000000012221L;
	private String top_pic;
	private String topic;
	private String id;
	private String recommend_time;
	private String types;
	private String contents;

	public String getTop_pic() {
		return top_pic;
	}

	public void setTop_pic(String top_pic) {
		this.top_pic = top_pic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecommend_time() {
		return recommend_time;
	}

	public void setRecommend_time(String recommend_time) {
		this.recommend_time = recommend_time;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

}
