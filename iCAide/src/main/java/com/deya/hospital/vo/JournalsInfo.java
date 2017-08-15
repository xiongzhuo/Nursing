package com.deya.hospital.vo;

import java.io.Serializable;

public class JournalsInfo implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 11111111L;
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
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getMagazine_id() {
		return magazine_id;
	}
	public void setMagazine_id(String magazine_id) {
		this.magazine_id = magazine_id;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	private String  id;
	  private String  title;
	  private String  update_time;
	  private String  magazine_id;
	  private String  create_time;
	  private String  state;
	  private String  image;
	  private String   publisher;

}
