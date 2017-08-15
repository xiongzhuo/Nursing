package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.TaskVo;

import java.io.Serializable;

public class SubItemlVo implements Serializable {
	private static final long serialVersionUID = 4380129839012L;
	private String id;
	private int result;
	private double score;
	private TaskVo remarkVo;
	private String remark="";
	String r_id;
	int is_remark;
	
	public int getIs_remark() {
		return is_remark;
	}

	public void setIs_remark(int is_remark) {
		this.is_remark = is_remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	public TaskVo getRemarkVo() {
		return remarkVo;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemarkVo(TaskVo remarkVo) {
		this.remarkVo = remarkVo;
	}

	public String getR_id() {
		return r_id;
	}

	public void setR_id(String r_id) {
		this.r_id = r_id;
	}

	
	
}
