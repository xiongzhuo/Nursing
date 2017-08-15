package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/10/24
 */
public class CaseListVo implements Serializable {

    /**
     * result_msg : 获取病例列表成功
     * result_id : 0
     * pageTotal : 1
     * case_list : [{"task_id":0,"case_id":5,"department_id":7718,"hospital_id":3347,"bed_no":"4554","patient_name":"","patient_id":"","check_ids":"2,","drug_ids":"2,","user_id":3,"sex":null,"apache":null,"temp1":null,"temp2":null,"temp3":null,"temp_type_id":1,"operation_name":null,"time":"2016-10-24","mission_time":"2016-10-24 11:35","status":0,"userName":"fcc","hospitalName":"德雅曼达测试医院","departmentName":"12病房"},{"task_id":0,"case_id":4,"department_id":2378,"hospital_id":3347,"bed_no":"2555","patient_name":"","patient_id":"","check_ids":"4,","drug_ids":"","user_id":3,"sex":null,"apache":null,"temp1":null,"temp2":null,"temp3":null,"temp_type_id":1,"operation_name":null,"time":"2016-10-24","mission_time":"2016-10-24 11:34","status":0,"userName":"fcc","hospitalName":"德雅曼达测试医院","departmentName":"免疫内科"},{"task_id":0,"case_id":3,"department_id":7720,"hospital_id":3347,"bed_no":"2255","patient_name":"","patient_id":"","check_ids":"4,","drug_ids":"3,","user_id":3,"sex":null,"apache":null,"temp1":null,"temp2":null,"temp3":null,"temp_type_id":1,"operation_name":null,"time":"2016-10-24","mission_time":"2016-10-24 11:34","status":0,"userName":"fcc","hospitalName":"德雅曼达测试医院","departmentName":"55病房"},{"task_id":0,"case_id":2,"department_id":7721,"hospital_id":3347,"bed_no":"152","patient_name":"","patient_id":"","check_ids":"2,","drug_ids":"5,","user_id":3,"sex":null,"apache":null,"temp1":null,"temp2":null,"temp3":null,"temp_type_id":1,"operation_name":null,"time":"2016-10-24","mission_time":"2016-10-24 11:33","status":0,"userName":"fcc","hospitalName":"德雅曼达测试医院","departmentName":"54病房"}]
     */

    private String result_msg;
    private String result_id;
    private int pageTotal;
    /**
     * task_id : 0
     * case_id : 5
     * department_id : 7718
     * hospital_id : 3347
     * bed_no : 4554
     * patient_name :
     * patient_id :
     * check_ids : 2,
     * drug_ids : 2,
     * user_id : 3
     * sex : null
     * apache : null
     * temp1 : null
     * temp2 : null
     * temp3 : null
     * temp_type_id : 1
     * operation_name : null
     * time : 2016-10-24
     * mission_time : 2016-10-24 11:35
     * status : 0
     * userName : fcc
     * hospitalName : 德雅曼达测试医院
     * departmentName : 12病房
     */

    private List<CaseListBean> case_list;

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getResult_id() {
        return result_id;
    }

    public void setResult_id(String result_id) {
        this.result_id = result_id;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<CaseListBean> getCase_list() {
        return case_list;
    }

    public void setCase_list(List<CaseListBean> case_list) {
        this.case_list = case_list;
    }

    @Table(name = "case_list4")
    public static class CaseListBean implements Serializable {
        @Id(column = "dbid")
        private int dbid;
        private int task_id;
        private int case_id;
        private String department_id = "";
        private int hospital_id;
        private String bed_no = "";
        private String patient_name = "";
        private String patient_id = "";
        private String check_ids = "";
        private String drug_ids = "";
        private int user_id;
        private int sex;
        private String apache = "";
        private int temp1;
        private int temp2;
        private int temp3;
        private int temp_type_id;//1、多耐 2、三管 3、环境 4、手术部位 5、安全注射
        private String operation_name = "";
        private String time = "";
        private String mission_time = "";
        private int status;//0未提交 1、已完成 2、未完成(已提交)
        private String userName = "";
        private String hospitalName = "";
        private String departmentName = "";
        private String jsonStr;
        private String supStr;
        private RisistantVo.TaskInfoBean.SupervisorQuestionBean supervisor_question;
        private int uploadStatus; //0已上传 2未上传
        private RisistantVo risistantVo;
        private int sub_id;
        private String is_quarantine = "0";
        String cleaning_time = "0";
        String monitoring_method = "0";

        public String getIs_quarantine() {
            return is_quarantine;
        }

        public void setIs_quarantine(String is_quarantine) {
            this.is_quarantine = is_quarantine;
        }

        public String getCleaning_time() {
            return cleaning_time;
        }

        public void setCleaning_time(String cleaning_time) {
            this.cleaning_time = cleaning_time;
        }

        public String getMonitoring_method() {
            return monitoring_method;
        }

        public void setMonitoring_method(String monitoring_method) {
            this.monitoring_method = monitoring_method;
        }

        public List<IdAndResultsVo.ItemBean> resultlist = new ArrayList<>();//最终提交的模板结果集

        public List<IdAndResultsVo.ItemBean> getResultlist() {
            return resultlist;
        }

        public void setResultlist(List<IdAndResultsVo.ItemBean> resultlist) {
            this.resultlist = resultlist;
        }

        /**
         * 环境物表清洁字段
         */
        private String room_no;
        private String person_liable;

        /**
         * 安全注射字段
         */
        String work_type = "";
        String operation_type = "";
        int isInspector;

        public int getSub_id() {
            return sub_id;
        }

        public void setSub_id(int sub_id) {
            this.sub_id = sub_id;
        }

        public String getWork_type() {
            return work_type;
        }

        public void setWork_type(String work_type) {
            this.work_type = work_type;
        }

        public String getOperation_type() {
            return operation_type;
        }

        public void setOperation_type(String operation_type) {
            this.operation_type = operation_type;
        }

        public int getIsInspector() {
            return isInspector;
        }

        public void setIsInspector(int isInspector) {
            this.isInspector = isInspector;
        }

        public RisistantVo getRisistantVo() {
            return risistantVo;
        }

        public void setRisistantVo(RisistantVo risistantVo) {
            this.risistantVo = risistantVo;
        }

        public int getUploadStatus() {
            return uploadStatus;
        }

        public void setUploadStatus(int uploadStatus) {
            this.uploadStatus = uploadStatus;
        }

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public int getCase_id() {
            return case_id;
        }

        public void setCase_id(int case_id) {
            this.case_id = case_id;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public int getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(int hospital_id) {
            this.hospital_id = hospital_id;
        }

        public String getBed_no() {
            return bed_no;
        }

        public void setBed_no(String bed_no) {
            this.bed_no = bed_no;
        }

        public String getPatient_name() {
            return patient_name;
        }

        public void setPatient_name(String patient_name) {
            this.patient_name = patient_name;
        }

        public String getPatient_id() {
            return patient_id;
        }

        public void setPatient_id(String patient_id) {
            this.patient_id = patient_id;
        }

        public String getCheck_ids() {
            return check_ids;
        }

        public void setCheck_ids(String check_ids) {
            this.check_ids = check_ids;
        }

        public String getDrug_ids() {
            return drug_ids;
        }

        public void setDrug_ids(String drug_ids) {
            this.drug_ids = drug_ids;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getApache() {
            return apache;
        }

        public void setApache(String apache) {
            this.apache = apache;
        }

        public int getTemp1() {
            return temp1;
        }

        public void setTemp1(int temp1) {
            this.temp1 = temp1;
        }

        public int getTemp2() {
            return temp2;
        }

        public void setTemp2(int temp2) {
            this.temp2 = temp2;
        }

        public int getTemp3() {
            return temp3;
        }

        public void setTemp3(int temp3) {
            this.temp3 = temp3;
        }

        public int getTemp_type_id() {
            return temp_type_id;
        }

        public void setTemp_type_id(int temp_type_id) {
            this.temp_type_id = temp_type_id;
        }

        public String getOperation_name() {
            return operation_name;
        }

        public void setOperation_name(String operation_name) {
            this.operation_name = operation_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMission_time() {
            return mission_time;
        }

        public void setMission_time(String mission_time) {
            this.mission_time = mission_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public int getDbid() {
            return dbid;
        }

        public void setDbid(int dbid) {
            this.dbid = dbid;
        }


        public String getJsonStr() {
            return jsonStr;
        }

        public void setJsonStr(String jsonStr) {
            this.jsonStr = jsonStr;
        }

        public String getSupStr() {
            return supStr;
        }

        public void setSupStr(String supStr) {
            this.supStr = supStr;
        }

        public RisistantVo.TaskInfoBean.SupervisorQuestionBean getSupervisor_question() {
            return supervisor_question;
        }

        public void setSupervisor_question(RisistantVo.TaskInfoBean.SupervisorQuestionBean supervisor_question) {
            this.supervisor_question = supervisor_question;
        }

        public String getRoom_no() {
            return room_no;
        }

        public void setRoom_no(String room_no) {
            this.room_no = room_no;
        }

        public String getPerson_liable() {
            return person_liable;
        }

        public void setPerson_liable(String person_liable) {
            this.person_liable = person_liable;
        }
    }
}
