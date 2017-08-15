package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "form_table5")
public class FormVo implements Serializable{
	private static final long serialVersionUID = 0x000040005L;
	@Id(column = "dbid")
	private int dbid;
	private  String title;
	String creater;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	private int is_open;
	private int is_save;
	private  String  uid;
	private  String create_time;
	private  String id;
	private  String use_cnt;
	private  String uname;
	private  String hospital;
	private  String  name;
	private double score;
	String ppost="";
	String work_type="";
	String uteam="";
	String address="";
	String cacheItems="";
	RisistantVo.TempListBean tempListBean;

	public RisistantVo.TempListBean getBean() {
		return tempListBean;
	}

	public void setBean(RisistantVo.TempListBean bean) {
		this.tempListBean = bean;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	int types;
	List<FormDetailListVo> item;
	int type;

	public int getIs_save() {
		return is_save;
	}

	public void setIs_save(int is_save) {
		this.is_save = is_save;
	}

	public int getTypes() {
		return types;
	}
	public void setTypes(int types) {
		this.types = types;
	}
	public String getCacheItems() {
		return cacheItems;
	}
	public void setCacheItems(String cacheItems) {
		this.cacheItems = cacheItems;
	}

	public int getIs_open() {
		return is_open;
	}

	public void setIs_open(int is_open) {
		this.is_open = is_open;
	}

	public List<FormDetailListVo> getItem() {
		return item;
	}
	public void setItem(List<FormDetailListVo> item) {
		this.item = item;
	}
	public String getPpost() {
		return ppost;
	}
	public void setPpost(String ppost) {
		this.ppost = ppost;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	public String getUteam() {
		return uteam;
	}
	public void setUteam(String uteam) {
		this.uteam = uteam;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}


	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUse_cnt() {
		return use_cnt;
	}
	public void setUse_cnt(String use_cnt) {
		this.use_cnt = use_cnt;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
