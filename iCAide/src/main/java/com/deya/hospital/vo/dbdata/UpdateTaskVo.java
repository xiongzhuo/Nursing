package com.deya.hospital.vo.dbdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.deya.hospital.vo.ConsumptionFormVo;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "sup_list")
public class UpdateTaskVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 00000000110L;
	@Id(column = "id")
	private int task_id;
	private String hospital;
	private String department;
	private String departmentName;
	private String uid;
	private String mission_time;
	private String task_type;
	private String yc_rate;
	private String valid_rate;
	private String departmentId;
	private String mobile;
	private int status;
	private String fiveTasks="";
	private String progress;
	private String totalNum;
	private String remark;
	private String type;
	private String isShow;
	private String improve_suggest;
	private String tmp_id;
	private String items;
	private String score;
	private String uname="";
	private String saveAttachmentsList;
	private String defaltWorkType;
	private String defaltWorkTypeName;
	private String defaltJobId;
	private String defaltJobName;
	String is_training;
	String training_recycle;
	String equip_examine;
	String feedback_obj;
	String types_info="";
	List<ConsumptionFormVo> consume_info=new ArrayList<ConsumptionFormVo>();
	

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getTypes_info() {
		return types_info;
	}

	public void setTypes_info(String types_info) {
		this.types_info = types_info;
	}


	public List<ConsumptionFormVo> getConsume_info() {
		return consume_info;
	}

	public void setConsume_info(List<ConsumptionFormVo> consume_info) {
		this.consume_info = consume_info;
	}


	public boolean isWho;
	
	public boolean isWho() {
		return isWho;
	}

	public void setWho(boolean isWho) {
		this.isWho = isWho;
	}

	public String getFeedback_obj() {
		return feedback_obj;
	}

	public void setFeedback_obj(String feedback_obj) {
		this.feedback_obj = feedback_obj;
	}

	public String getIs_training() {
		return is_training;
	}

	public void setIs_training(String is_training) {
		this.is_training = is_training;
	}

	public String getTraining_recycle() {
		return training_recycle;
	}

	public void setTraining_recycle(String training_recycle) {
		this.training_recycle = training_recycle;
	}

	public String getEquip_examine() {
		return equip_examine;
	}

	public void setEquip_examine(String equip_examine) {
		this.equip_examine = equip_examine;
	}

	public String getDefaltJobId() {
		return defaltJobId;
	}

	public void setDefaltJobId(String defaltJobId) {
		this.defaltJobId = defaltJobId;
	}

	public String getDefaltJobName() {
		return defaltJobName;
	}

	public void setDefaltJobName(String defaltJobName) {
		this.defaltJobName = defaltJobName;
	}

	public String getDefaltWorkType() {
		return defaltWorkType;
	}

	public void setDefaltWorkType(String defaltWorkType) {
		this.defaltWorkType = defaltWorkType;
	}

	public String getDefaltWorkTypeName() {
		return defaltWorkTypeName;
	}

	public void setDefaltWorkTypeName(String defaltWorkTypeName) {
		this.defaltWorkTypeName = defaltWorkTypeName;
	}

	public String getSaveAttachmentsList() {
		return saveAttachmentsList;
	}

	public void setSaveAttachmentsList(String saveAttachmentsList) {
		this.saveAttachmentsList = saveAttachmentsList;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	private String grid_type;

	public String getGrid_type() {
		return grid_type;
	}

	public void setGrid_type(String grid_type) {
		this.grid_type = grid_type;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getTmp_id() {
		return tmp_id;
	}

	public void setTmp_id(String tmp_id) {
		this.tmp_id = tmp_id;
	}

	public String getImprove_suggest() {
		return improve_suggest;
	}

	public void setImprove_suggest(String improve_suggest) {
		this.improve_suggest = improve_suggest;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getCheck_content() {
		return check_content;
	}

	public void setCheck_content(String check_content) {
		this.check_content = check_content;
	}

	public String getExist_problem() {
		return exist_problem;
	}

	public void setExist_problem(String exist_problem) {
		this.exist_problem = exist_problem;
	}

	public String getDeal_suggest() {
		return deal_suggest;
	}

	public void setDeal_suggest(String deal_suggest) {
		this.deal_suggest = deal_suggest;
	}

	public List<Attachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachments> attachments) {
		this.attachments = attachments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	String check_content="";
	String exist_problem;
	String deal_suggest;
	List<Attachments> attachments;
	private String fileList;

	public String getFileList() {
		return fileList;
	}

	public void setFileList(String fileList) {
		this.fileList = fileList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getYc() {
		return yc;
	}

	public void setYc(String yc) {
		this.yc = yc;
	}

	private String yc;

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getFiveTasks() {
		return fiveTasks;
	}

	public void setFiveTasks(String fiveTasks) {
		this.fiveTasks = fiveTasks;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getMission_time() {
		return mission_time;
	}

	public void setMission_time(String mission_time) {
		this.mission_time = mission_time;
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

	public String getTask_type() {
		return task_type;
	}

	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}

	public String getYc_rate() {
		return yc_rate;
	}

	public void setYc_rate(String yc_rate) {
		this.yc_rate = yc_rate;
	}

	public String getValid_rate() {
		return valid_rate;
	}

	public void setValid_rate(String valid_rate) {
		this.valid_rate = valid_rate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
