package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.Attachments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnswerVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 238971298371921L;
	String id;
	String name;
	String parentId;
	int zanState;
	int adoptState;
	int zanNums;
	List<Attachments> a_attachment = new ArrayList<Attachments>();
	Attachments headImg;
	String sex;
	String avatar;
	String hospital;
	String regis_job;
	int is_adopt;
	int is_recommend;

	
	
	public int getIs_recommend() {
		return is_recommend;
	}

	public void setIs_recommend(int is_recommend) {
		this.is_recommend = is_recommend;
	}

	public int getIs_adopt() {
		return is_adopt;
	}

	public void setIs_adopt(int is_adopt) {
		this.is_adopt = is_adopt;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getRegis_job() {
		return regis_job;
	}

	public void setRegis_job(String regis_job) {
		this.regis_job = regis_job;
	}

	public Attachments getHeadImg() {
		return headImg;
	}

	public void setHeadImg(Attachments headImg) {
		this.headImg = headImg;
	}

	public List<Attachments> getA_attachment() {
		return a_attachment;
	}

	public void setA_attachment(List<Attachments> a_attachment) {
		this.a_attachment = a_attachment;
	}

	public int getZanState() {
		return zanState;
	}

	public void setZanState(int zanState) {
		this.zanState = zanState;
	}

	public int getAdoptState() {
		return adoptState;
	}

	public void setAdoptState(int adoptState) {
		this.adoptState = adoptState;
	}

	public int getZanNums() {
		return zanNums;
	}

	public void setZanNums(int zanNums) {
		this.zanNums = zanNums;
	}

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	String content;
	String user_name;
	String q_id;
	String a_id;
	String update_time;
	int like_count;
	int is_like;
	String attachment_count;
	String create_time;
	String is_niming;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getQ_id() {
		return q_id;
	}

	public void setQ_id(String q_id) {
		this.q_id = q_id;
	}

	public String getA_id() {
		return a_id;
	}

	public void setA_id(String a_id) {
		this.a_id = a_id;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public int getLike_count() {
		return like_count;
	}

	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public int getIs_like() {
		return is_like;
	}

	public void setIs_like(int is_like) {
		this.is_like = is_like;
	}

	public String getAttachment_count() {
		return attachment_count;
	}

	public void setAttachment_count(String attachment_count) {
		this.attachment_count = attachment_count;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getIs_niming() {
		return is_niming;
	}

	public void setIs_niming(String is_niming) {
		this.is_niming = is_niming;
	}

}
