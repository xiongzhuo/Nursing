package com.deya.hospital.vo.dbdata;

import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ConsumptionFormVo;
import com.deya.hospital.vo.RisistantVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "hospital_tasklist_db11")
public class TaskVo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 00000000110L;
	@Id(column = "dbid")
	private int dbid;
	private int task_id;
	private String hospital = "";
	private String department = "";
	private String departmentName = "";
	private String uid = "";
	private String mission_time = "";
	private String task_type = "";
	private String yc_rate = "";
	private String valid_rate = "";
	private String mobile = "";
	private int status;
	private String fiveTasks = "";
	private List<planListDb> fiveTasksInfo = new ArrayList<planListDb>();
	private String progress = "";
	private String totalNum = "";
	private String remark = "";
	private String type = "";
	private String tmp_id = "";
	private String items = "";
	private String score = "";
	private String uname = "";
	private String saveAttachmentsList = "";
	private String defaltWorkType = "";
	private String defaltWorkTypeName = "";
	private String defaltJobId = "";
	private String defaltJobName = "";
	String is_training = "";
	String training_recycle = "";
	String equip_examine = "";
	String feedback_obj = "";
	String types_info = "";
	int flag;// 判断为详细版还是精简版 1/0；
	boolean isTranning = false;
	boolean isUpdatedTask = false;
	int formType;
	double totalscore;
	String formId;
	private	String name="";
	int seconds = 0;// 不限时机剩余时间秒
	int minute = 0;// 不限时间剩余分
	int hours = 30;
	String ppost="";
	String work_type="";
	String uteam="";
	String address="";
	String main_remark="";
	String  main_remark_obj="";
	String main_remark_id="";
	int is_feedback_depa;//是否反馈到科室
	private int state;
	String sub_id;
	int  finished;
	int case_dbid;
	int is_temp_task;
	int remindState;//提醒类型
	int isAfterTask;
	String	memberName="";
	int recycleState;//重复类型
	SupervisorQuestion supervisor_question;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public int getRecycleState() {
		return recycleState;
	}

	public void setRecycleState(int recycleState) {
		this.recycleState = recycleState;
	}

	public int getIsAfterTask() {
		return isAfterTask;
	}

	public void setIsAfterTask(int isAfterTask) {
		this.isAfterTask = isAfterTask;
	}

	public int getRemindState() {
		return remindState;
	}

	public void setRemindState(int remindState) {
		this.remindState = remindState;
	}

	private int failNum;//任务上传失败次数

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public int getCaseDbId() {
		return case_dbid;
	}

	public void setCaseDbId(int case_dbid) {
		this.case_dbid = case_dbid;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getIs_temp_task() {
		return is_temp_task;
	}

	public void setIs_temp_task(int is_temp_task) {
		this.is_temp_task = is_temp_task;
	}

	int origin;
	private String isShow = "0";//是否推送通知
	String main_remark_name = "";
	String remind_date = "";
	int is_feedback_department=1;//是否反馈到科室
	int is_again_supervisor;
	private String improve_suggest = "";
	//问题列表新字段
	int id = -1;
	String title;
	String check_content = "";
	String exist_problem="";
	String deal_suggest="";
	String is_main_remark="";
	private String user_name="";
	String user_regis_job="";
	String department_confirm_user="";  //科室确认人id
	String department_confirm_user_name="";  //科室确认人名称
	String department_confirm_time="";  //科室确认时间
	String department_confirm_user_regis_job="";  //科室确认人岗位
	String improve_measures="";  //改进措施
	String improve_result_assess="";  //改进效果评价
	String again_supervisor_time="";  //再次督导时间
	String again_supervisor_user="";  //再次督导人id
	String again_supervisor_user_name="";  //再次督导人名称
	String again_supervisor_user_regis_job="";  //再次督导人岗位
	String remind_time="";  //提醒时间
	private String create_time="";//任务创建时间

	String businessStr="";
	CaseListVo.CaseListBean business;
	String  taskListBean="";
	private String note="";

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTaskListBean() {
		return taskListBean;
	}

	public void setTaskListBean(String taskListBean) {
		this.taskListBean = taskListBean;
	}

	public CaseListVo.CaseListBean getBusiness() {
		return business;
	}

	public void setBusiness(CaseListVo.CaseListBean business) {
		this.business = business;
	}

	public String getBusinessStr() {
		return businessStr;
	}

	public void setBusinessStr(String businessStr) {
		this.businessStr = businessStr;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIs_main_remark() {
		return is_main_remark;
	}

	public void setIs_main_remark(String is_main_remark) {
		this.is_main_remark = is_main_remark;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_regis_job() {
		return user_regis_job;
	}

	public void setUser_regis_job(String user_regis_job) {
		this.user_regis_job = user_regis_job;
	}

	public String getDepartment_confirm_user() {
		return department_confirm_user;
	}

	public void setDepartment_confirm_user(String department_confirm_user) {
		this.department_confirm_user = department_confirm_user;
	}

	public String getDepartment_confirm_user_name() {
		return department_confirm_user_name;
	}

	public void setDepartment_confirm_user_name(String department_confirm_user_name) {
		this.department_confirm_user_name = department_confirm_user_name;
	}

	public String getDepartment_confirm_time() {
		return department_confirm_time;
	}

	public void setDepartment_confirm_time(String department_confirm_time) {
		this.department_confirm_time = department_confirm_time;
	}

	public String getDepartment_confirm_user_regis_job() {
		return department_confirm_user_regis_job;
	}

	public void setDepartment_confirm_user_regis_job(String department_confirm_user_regis_job) {
		this.department_confirm_user_regis_job = department_confirm_user_regis_job;
	}

	public String getImprove_measures() {
		return improve_measures;
	}

	public void setImprove_measures(String improve_measures) {
		this.improve_measures = improve_measures;
	}

	public String getImprove_result_assess() {
		return improve_result_assess;
	}

	public void setImprove_result_assess(String improve_result_assess) {
		this.improve_result_assess = improve_result_assess;
	}

	public String getAgain_supervisor_time() {
		return again_supervisor_time;
	}

	public void setAgain_supervisor_time(String again_supervisor_time) {
		this.again_supervisor_time = again_supervisor_time;
	}

	public String getAgain_supervisor_user() {
		return again_supervisor_user;
	}

	public void setAgain_supervisor_user(String again_supervisor_user) {
		this.again_supervisor_user = again_supervisor_user;
	}

	public String getAgain_supervisor_user_name() {
		return again_supervisor_user_name;
	}

	public void setAgain_supervisor_user_name(String again_supervisor_user_name) {
		this.again_supervisor_user_name = again_supervisor_user_name;
	}

	public String getAgain_supervisor_user_regis_job() {
		return again_supervisor_user_regis_job;
	}

	public void setAgain_supervisor_user_regis_job(String again_supervisor_user_regis_job) {
		this.again_supervisor_user_regis_job = again_supervisor_user_regis_job;
	}

	public String getRemind_time() {
		return remind_time;
	}

	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}

	public int getIs_feedback_department() {
		return is_feedback_department;
	}

	public void setIs_feedback_department(int is_feedback_department) {
		this.is_feedback_department = is_feedback_department;
	}

	public int getIs_again_supervisor() {
		return is_again_supervisor;
	}

	public void setIs_again_supervisor(int is_again_supervisor) {
		this.is_again_supervisor = is_again_supervisor;
	}

	public int getIs_feedback_depa() {
		return is_feedback_depa;
	}

	public void setIs_feedback_depa(int is_feedback_depa) {
		this.is_feedback_depa = is_feedback_depa;
	}

	public String getRemind_date() {
		return remind_date;
	}

	public void setRemind_date(String remind_date) {
		this.remind_date = remind_date;
	}

	public String getMain_remark_id() {
		return main_remark_id;
	}

	public void setMain_remark_id(String main_remark_id) {
		this.main_remark_id = main_remark_id;
	}

	public String getMain_remark_obj() {
		return main_remark_obj;
	}

	public void setMain_remark_obj(String main_remark_obj) {
		this.main_remark_obj = main_remark_obj;
	}

	public String getMain_remark() {
		return main_remark;
	}

	public void setMain_remark(String main_remark) {
		this.main_remark = main_remark;
	}



	public String getMain_remark_name() {
		return main_remark_name;
	}

	public void setMain_remark_name(String main_remark_name) {
		this.main_remark_name = main_remark_name;
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

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public double getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(double totalscore) {
		this.totalscore = totalscore;
	}

	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	boolean isAdd = false;// 修改手卫生时，判断是否新增过

	public boolean isTranning() {
		return isTranning;
	}

	public void setTranning(boolean isTranning) {
		this.isTranning = isTranning;
	}

	public boolean isUpdatedTask() {
		return isUpdatedTask;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setUpdatedTask(boolean isUpdatedTask) {
		this.isUpdatedTask = isUpdatedTask;
	}

	List<ConsumptionFormVo> consume_info;

	public List<planListDb> getFiveTasksInfo() {
		return fiveTasksInfo;
	}

	public void setFiveTasksInfo(List<planListDb> fiveTasksInfo) {
		this.fiveTasksInfo = fiveTasksInfo;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

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
		return consume_info==null?new ArrayList<ConsumptionFormVo>():consume_info;
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


	List<Attachments> attachments=new ArrayList<Attachments>();
	private String fileList = "";

	public String getFileList() {
		return fileList==null?"":fileList;
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

	public List<planListDb> subTasks(){
		Gson gson = new Gson();
		return gson.fromJson(this
						.getFiveTasks().toString(),
				new TypeToken<List<planListDb>>() {
				}.getType());
	}

	public static class SupervisorQuestion implements Serializable{

		int origin;
		private String isShow = "0";//是否推送通知
		String main_remark_name = "";
		String remind_date = "";
		int is_feedback_department=1;//是否反馈到科室
		int is_again_supervisor;
		private String improve_suggest = "";
		//问题列表新字段
		int id = -1;
		private String hospital = "";
		private String department = "";
		private String departmentName = "";
		String title;
		String check_content = "";
		String exist_problem="";
		String deal_suggest="";
		String is_main_remark="";
		String user_name="";
		String user_regis_job="";
		String department_confirm_user="";  //科室确认人id
		String department_confirm_user_name="";  //科室确认人名称
		String department_confirm_time="";  //科室确认时间
		String department_confirm_user_regis_job="";  //科室确认人岗位
		String improve_measures="";  //改进措施
		String improve_result_assess="";  //改进效果评价
		String again_supervisor_time="";  //再次督导时间
		String again_supervisor_user="";  //再次督导人id
		String again_supervisor_user_name="";  //再次督导人名称
		String again_supervisor_user_regis_job="";  //再次督导人岗位
		String remind_time="";  //提醒时间
		private String create_time="";//任务创建时间
		List<Attachments> attachments=new ArrayList<>();
		public String getHospital() {
			return hospital;
		}

		public List<Attachments> getAttachments() {
			return attachments;
		}

		public void setAttachments(List<Attachments> attachments) {
			this.attachments = attachments;
		}

		public void setHospital(String hospital) {
			this.hospital = hospital;
		}

		public String getCreate_time() {
			return create_time;
		}

		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}

		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		public String getDepartmentName() {
			return departmentName;
		}

		public void setDepartmentName(String departmentName) {
			this.departmentName = departmentName;
		}

		public int getOrigin() {
			return origin;
		}

		public void setOrigin(int origin) {
			this.origin = origin;
		}

		public String getIsShow() {
			return isShow;
		}

		public void setIsShow(String isShow) {
			this.isShow = isShow;
		}

		public String getMain_remark_name() {
			return main_remark_name;
		}

		public void setMain_remark_name(String main_remark_name) {
			this.main_remark_name = main_remark_name;
		}

		public String getRemind_date() {
			return remind_date;
		}

		public void setRemind_date(String remind_date) {
			this.remind_date = remind_date;
		}

		public int getIs_feedback_department() {
			return is_feedback_department;
		}

		public void setIs_feedback_department(int is_feedback_department) {
			this.is_feedback_department = is_feedback_department;
		}

		public int getIs_again_supervisor() {
			return is_again_supervisor;
		}

		public void setIs_again_supervisor(int is_again_supervisor) {
			this.is_again_supervisor = is_again_supervisor;
		}

		public String getImprove_suggest() {
			return improve_suggest;
		}

		public void setImprove_suggest(String improve_suggest) {
			this.improve_suggest = improve_suggest;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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

		public String getIs_main_remark() {
			return is_main_remark;
		}

		public void setIs_main_remark(String is_main_remark) {
			this.is_main_remark = is_main_remark;
		}

		public String getUser_name() {
			return user_name;
		}

		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}

		public String getUser_regis_job() {
			return user_regis_job;
		}

		public void setUser_regis_job(String user_regis_job) {
			this.user_regis_job = user_regis_job;
		}

		public String getDepartment_confirm_user() {
			return department_confirm_user;
		}

		public void setDepartment_confirm_user(String department_confirm_user) {
			this.department_confirm_user = department_confirm_user;
		}

		public String getDepartment_confirm_user_name() {
			return department_confirm_user_name;
		}

		public void setDepartment_confirm_user_name(String department_confirm_user_name) {
			this.department_confirm_user_name = department_confirm_user_name;
		}

		public String getDepartment_confirm_time() {
			return department_confirm_time;
		}

		public void setDepartment_confirm_time(String department_confirm_time) {
			this.department_confirm_time = department_confirm_time;
		}

		public String getDepartment_confirm_user_regis_job() {
			return department_confirm_user_regis_job;
		}

		public void setDepartment_confirm_user_regis_job(String department_confirm_user_regis_job) {
			this.department_confirm_user_regis_job = department_confirm_user_regis_job;
		}

		public String getImprove_measures() {
			return improve_measures;
		}

		public void setImprove_measures(String improve_measures) {
			this.improve_measures = improve_measures;
		}

		public String getImprove_result_assess() {
			return improve_result_assess;
		}

		public void setImprove_result_assess(String improve_result_assess) {
			this.improve_result_assess = improve_result_assess;
		}

		public String getAgain_supervisor_time() {
			return again_supervisor_time;
		}

		public void setAgain_supervisor_time(String again_supervisor_time) {
			this.again_supervisor_time = again_supervisor_time;
		}

		public String getAgain_supervisor_user() {
			return again_supervisor_user;
		}

		public void setAgain_supervisor_user(String again_supervisor_user) {
			this.again_supervisor_user = again_supervisor_user;
		}

		public String getAgain_supervisor_user_name() {
			return again_supervisor_user_name;
		}

		public void setAgain_supervisor_user_name(String again_supervisor_user_name) {
			this.again_supervisor_user_name = again_supervisor_user_name;
		}

		public String getAgain_supervisor_user_regis_job() {
			return again_supervisor_user_regis_job;
		}

		public void setAgain_supervisor_user_regis_job(String again_supervisor_user_regis_job) {
			this.again_supervisor_user_regis_job = again_supervisor_user_regis_job;
		}

		public String getRemind_time() {
			return remind_time;
		}

		public void setRemind_time(String remind_time) {
			this.remind_time = remind_time;
		}
	}


	private RisistantVo.TempListBean temp_list;

	public RisistantVo.TempListBean getTemp_list() {
		return temp_list==null?new RisistantVo.TempListBean():temp_list;
	}

	public void setTemp_list(RisistantVo.TempListBean temp_list) {
		this.temp_list = temp_list;
	}
}
