package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.Attachments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArticalVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0x31290831201L;
	String uid = "";
	String id;
	String title = "";
	int list_type;
	String channel_id = "";
	String channel_name = "";
	int comment_count;
	int like_count;
	String expert_name = "";
	String expert_post = "";
	String update_time = "";
	int is_pdf;
	String pdf_attach;
	public int getIs_pdf() {
		return is_pdf;
	}
	public void setIs_pdf(int is_pdf) {
		this.is_pdf = is_pdf;
	}
	public String getPdf_attach() {
		return pdf_attach;
	}
	public void setPdf_attach(String pdf_attach) {
		this.pdf_attach = pdf_attach;
	}


	List<Attachments> attachment = new ArrayList<Attachments>();
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
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
	public int getList_type() {
		return list_type;
	}
	public void setList_type(int list_type) {
		this.list_type = list_type;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getLike_count() {
		return like_count;
	}
	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}
	public String getExpert_name() {
		return expert_name;
	}
	public void setExpert_name(String expert_name) {
		this.expert_name = expert_name;
	}
	public String getExpert_post() {
		return expert_post;
	}
	public void setExpert_post(String expert_post) {
		this.expert_post = expert_post;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public List<Attachments> getAttachment() {
		return attachment;
	}
	public void setAttachment(List<Attachments> attachment) {
		this.attachment = attachment;
	}
	
}
