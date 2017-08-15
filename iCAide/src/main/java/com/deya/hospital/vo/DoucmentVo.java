package com.deya.hospital.vo;

import java.io.Serializable;

public class DoucmentVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x000040004L;
	public String getTop_pic() {
		return top_pic;
	}
	public void setTop_pic(String top_pic) {
		this.top_pic = top_pic;
	}
	public String getRead_count() {
		return read_count;
	}
	public void setRead_count(String read_count) {
		this.read_count = read_count;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDegest() {
		return degest;
	}
	public void setDegest(String degest) {
		this.degest = degest;
	}
	private String top_pic;
	private String read_count;
	private String topic;
	private String id;
	private String author;
	private String contents;
	private String create_time;
	private String document_id;
    
    
    public String getDocument_id() {
		return document_id;
	}
	public void setDocument_id(String document_id) {
		this.document_id = document_id;
	}
	public String getHas_pic() {
		return has_pic;
	}
	public void setHas_pic(String has_pic) {
		this.has_pic = has_pic;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getRevision_time() {
		return revision_time;
	}
	public void setRevision_time(String revision_time) {
		this.revision_time = revision_time;
	}
	public String getExecution_time() {
		return execution_time;
	}
	public void setExecution_time(String execution_time) {
		this.execution_time = execution_time;
	}
	public String getDrafting_unit() {
		return drafting_unit;
	}
	public void setDrafting_unit(String drafting_unit) {
		this.drafting_unit = drafting_unit;
	}
	public String getPublish_unit() {
		return publish_unit;
	}
	public void setPublish_unit(String publish_unit) {
		this.publish_unit = publish_unit;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getPdf_attach() {
		return pdf_attach;
	}
	public void setPdf_attach(String pdf_attach) {
		this.pdf_attach = pdf_attach;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private String  has_pic;//是否有图',
    private String  degest;//文章摘要',
    private String  types;//'资讯分类',
    private String  platform;//发布平台',
    private String  revision_time;//修订时间',
    private String  execution_time; //执行时间',
    private String  drafting_unit;//起草单位',
    public String getDrafting_person() {
		return drafting_person;
	}
	public void setDrafting_person(String drafting_person) {
		this.drafting_person = drafting_person;
	}
	private String  drafting_person;//起草人',
    private String  publish_unit;//发布单位
    private String  kind;
    private String  pdf_attach;
    String attachment;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
