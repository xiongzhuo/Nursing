package com.deya.hospital.supervisor;

import java.util.List;

import com.deya.hospital.vo.dbdata.TaskVo;

public class GroupClass {
	private List<TaskVo> sycrolist;
	public List<TaskVo> getSycrolist() {
		return sycrolist;
	}
	public void setSycrolist(List<TaskVo> sycrolist) {
		this.sycrolist = sycrolist;
	}
	private List<TaskVo> unfinishList;
	private List<TaskVo> finishList;
	public List<TaskVo> getUnfinishList() {
		return unfinishList;
	}
	public void setUnfinishList(List<TaskVo> unfinishList) {
		this.unfinishList = unfinishList;
	}
	public List<TaskVo> getFinishList() {
		return finishList;
	}
	public void setFinishList(List<TaskVo> finishList) {
		this.finishList = finishList;
	}


}
