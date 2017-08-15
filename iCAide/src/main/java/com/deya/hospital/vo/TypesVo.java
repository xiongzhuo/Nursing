package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

public class TypesVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x000400001L;
	private String name;
	private String id;
    public String getForder() {
		return forder;
	}
	public void setForder(String forder) {
		this.forder = forder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private  String forder;
    private String description;
    private String create_time;
    private  String type_name;
    private  String pid;
    private String attachment;
	private List<GuideTypeVo> subTypes;
	public List<GuideTypeVo> getSubTypes() {
		return subTypes;
	}
	public void setSubTypes(List<GuideTypeVo> subTypes) {
		this.subTypes = subTypes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
