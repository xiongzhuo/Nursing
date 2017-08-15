package com.deya.hospital.vo.dbdata;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "fivetask_vo")
public class planListDb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x1000000001L;
	@Id(column = "_id")
	private int _id;// 数据库的ID字段 自增长
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPtimer() {
		return ptimer;
	}
	public void setPtimer(String ptimer) {
		this.ptimer = ptimer;
	}
	public List<subTaskVo> subTasks;
	public List<subTaskVo> getSubTasks() {
		return subTasks==null?new ArrayList<subTaskVo>():subTasks;
	}
	public void setSubTasks(List<subTaskVo> subTasks) {
		this.subTasks = subTasks;
	}
	private String hospital;
	private String department;
	private String uid;
	private String pname;
	private String ptimer;
	private String task_type;
	private String task_id;
	private String mission_time;
	private String remark;
	private String work_type;
	private String workName;
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}
	
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMission_time() {
		return mission_time;
	}
	public void setMission_time(String mission_time) {
		this.mission_time = mission_time;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	
	public String getTask_type() {
		return task_type;
	}
	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}
	private String ppost;
	private String ppostName;
	public String getPpostName() {
		return ppostName;
	}
	public void setPpostName(String ppostName) {
		this.ppostName = ppostName;
	}
	public String getPpost() {
		return ppost;
	}
	
	public void setPpost(String ppost) {
		this.ppost = ppost;
	}
	
	//显示view类型，adpater用
	private int view_type;
	public int getView_type() {
		return view_type;
	}
	public void setView_type(int view_type) {
		this.view_type = view_type;
	}
	
	//选中状态
	private boolean select_status;
	public boolean isSelect_status() {
		return select_status;
	}
	public void setSelect_status(boolean select_status) {
		this.select_status = select_status;
	}
	
	//adapter 输入框临时存储
	private String temp_pname;
	public String getTemp_pname() {
		return temp_pname;
	}
	public void setTemp_pname(String temp_pname) {
		this.temp_pname = temp_pname;
	}
	
	
}
