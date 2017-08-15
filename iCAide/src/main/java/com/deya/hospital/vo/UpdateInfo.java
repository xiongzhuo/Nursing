/**
 * Project Name:Vicinity3.1.<br/>
 * File Name:UpdateInfo.java.<br/>
 * Package Name:com.trisun.vicinity.my.setting.vo.<br/>
 * Date:2015年4月21日下午5:28:16.<br/>
 * Copyright (c) 2015, 广东云上城网络科技有限公司.
 *
 */

package com.deya.hospital.vo;


/**
 * .ClassName: UpdateInfo(用一句话描述这个类的作用是做什么) <br/>
 * date: 2015年4月21日 下午5:28:16 <br/>
 * 
 * @author Administrator
 */
public class UpdateInfo {
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVer_number() {
		return ver_number;
	}
	public void setVer_number(String ver_number) {
		this.ver_number = ver_number;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	

	public int getIs_update() {
		return is_update;
	}
	public void setIs_update(int is_update) {
		this.is_update = is_update;
	}


	private String type;
	private String file_name;
	private String url;
	private String remark;
	private String ver_number;
	private String create_time;
	private int  is_update;

}
