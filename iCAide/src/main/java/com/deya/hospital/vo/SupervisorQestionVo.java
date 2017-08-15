package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.Attachments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class SupervisorQestionVo extends BaseTaskVo implements Serializable {
    private static final long serialVersionUID = 0023000110L;

    int id = -1;
    int origin;
    String title;
    String check_content = "";
    String exist_problem="";
    String deal_suggest="";
    private String isShow = "0";//是否推送通知
    String main_remark_name = "";
    String remind_date = "";
    int is_feedback_department=1;//是否反馈到科室
    int is_again_supervisor;
    private String improve_suggest = "";
    int is_main_remark;
    String user_name;
    String user_regis_job;
    String department_confirm_user;  //科室确认人id
    String department_confirm_user_name;  //科室确认人名称
    String department_confirm_time;  //科室确认时间
    String department_confirm_user_regis_job;  //科室确认人岗位
    String improve_measures;  //改进措施
    String improve_result_assess;  //改进效果评价
    String again_supervisor_time;  //再次督导时间
    String again_supervisor_user;  //再次督导人id
    String again_supervisor_user_name;  //再次督导人名称
    String again_supervisor_user_regis_job;  //再次督导人岗位
    String remind_time="";  //提醒时间

    List<Attachments> attachments = new ArrayList<Attachments>();
    private String fileList = "";

    public String getFileList() {
        return fileList;
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

    public void setFileList(String fileList) {
        this.fileList = fileList;
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

    public void setMain_remark_name(String main_remark_name) {
        this.main_remark_name = main_remark_name;
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

    public void setIs_again_supervisor(int is_again_supervisor) {
        this.is_again_supervisor = is_again_supervisor;
    }

    public void setAttachments(List<Attachments> attachments) {
        this.attachments = attachments;
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

    public String getRemind_date() {
        return remind_date;
    }


    public int getIs_again_supervisor() {
        return is_again_supervisor;
    }

    public List<Attachments> getAttachments() {
        return attachments;
    }

    public String getImprove_suggest() {
        return improve_suggest;
    }

    public void setImprove_suggest(String improve_suggest) {
        this.improve_suggest = improve_suggest;
    }

    public int getIs_main_remark() {
        return is_main_remark;
    }

    public void setIs_main_remark(int is_main_remark) {
        this.is_main_remark = is_main_remark;
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
