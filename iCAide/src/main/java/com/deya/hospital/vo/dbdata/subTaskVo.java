package com.deya.hospital.vo.dbdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.deya.hospital.vo.RulesVo;

public class subTaskVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 0x11110001L;
	private String col_type;
	private String resultsPosition;
	private String pname;
	private int subtaskId;
	private String ppoName;
	private String work_type;
	private String workName;
	
	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

	public int getSubtaskId() {
		return subtaskId;
	}

	public void setSubtaskId(int subtaskId) {
		this.subtaskId = subtaskId;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getResultsPosition() {
		return resultsPosition;
	}

	public void setResultsPosition(String resultsPosition) {
		this.resultsPosition = resultsPosition;
	}

	public String getCol_type() {
		return col_type;
	}

	public void setCol_type(String col_type) {
		this.col_type = col_type;
	}

	private String results;
	private List<Attachments> attachments=new ArrayList<Attachments>();

	public List<Attachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachments> attachments) {
		this.attachments = attachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getPpoName() {
		return ppoName;
	}

	public void setPpoName(String ppoName) {
		this.ppoName = ppoName;
	}

	public List<RulesVo> unrules;

	public List<RulesVo> getUnrules() {
		return unrules;
	}

	public void setUnrules(List<RulesVo> unrules) {
		this.unrules = unrules;
	}

}
