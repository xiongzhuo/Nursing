/*
package com.deya.hospital.vo.dbdata;

import com.deya.hospital.vo.RisistantVo;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

*/
/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 *//*

public class ResistantVo implements Serializable {


    */
/**
     * task_list : [{"task_id":52,"temp_type_id":1,"hospital_id":1,"department_id":"1","case_id":null,"business_id":"37","sub_id":null,"mission_time":"2016-07-29 16:50:02","submit_time":null,"finished":0,"note":null,"uid":null,"attr":null,"userName":null,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","business":{"id":37,"department_id":1,"hospital_id":1,"bed_no":"bed_no","patient_name":"patient_name","patient_id":"patient_id","time":"2016-07-26 10:10:10","check_ids":"1,2,3","drug_ids":"1,2,3","create_time":"2016-07-29 16:50:02","user_id":1,"is_del":0,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","userName":null}},{"task_id":51,"temp_type_id":1,"hospital_id":1,"department_id":"1","case_id":null,"business_id":"35","sub_id":null,"mission_time":"2016-07-29 16:45:56","submit_time":null,"finished":0,"note":null,"uid":null,"attr":null,"userName":null,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","business":{"id":35,"department_id":1,"hospital_id":1,"bed_no":"bed_no","patient_name":"patient_name","patient_id":"patient_id","time":"2016-07-26 10:10:10","check_ids":"1,2,3","drug_ids":"1,2,3","create_time":"2016-07-29 16:45:56","user_id":1,"is_del":0,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","userName":null}},{"task_id":44,"temp_type_id":1,"hospital_id":1,"department_id":"1","case_id":null,"business_id":"30","sub_id":null,"mission_time":null,"submit_time":null,"finished":0,"note":null,"uid":null,"attr":null,"userName":null,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","business":{"id":30,"department_id":1,"hospital_id":1,"bed_no":"bed_no","patient_name":"patient_name","patient_id":"patient_id","time":"2016-07-26 10:10:10","check_ids":"1,2,3","drug_ids":"1,2,3","create_time":"2016-07-29 15:51:26","user_id":1,"is_del":0,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","userName":null}},{"task_id":49,"temp_type_id":1,"hospital_id":1,"department_id":"1","case_id":null,"business_id":"33","sub_id":null,"mission_time":null,"submit_time":null,"finished":0,"note":null,"uid":null,"attr":null,"userName":null,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","business":{"id":33,"department_id":1,"hospital_id":1,"bed_no":"bed_no","patient_name":"patient_name","patient_id":"patient_id","time":"2016-07-26 10:10:10","check_ids":"1,2,3","drug_ids":"1,2,3","create_time":"2016-07-29 16:26:21","user_id":1,"is_del":0,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","userName":null}}]
     * result_msg : 获取任务列表成功
     * result_id : 0
     *//*


    private String result_msg;
    private String result_id;
    */
/**
     * task_id : 52
     * temp_type_id : 1
     * hospital_id : 1
     * department_id : 1
     * case_id : null
     * business_id : 37
     * sub_id : null
     * mission_time : 2016-07-29 16:50:02
     * submit_time : null
     * finished : 0
     * note : null
     * uid : null
     * attr : null
     * userName : null
     * hospitalName : 中南大学湘雅医院
     * departmentName : 院感科
     * business : {"id":37,"department_id":1,"hospital_id":1,"bed_no":"bed_no","patient_name":"patient_name","patient_id":"patient_id","time":"2016-07-26 10:10:10","check_ids":"1,2,3","drug_ids":"1,2,3","create_time":"2016-07-29 16:50:02","user_id":1,"is_del":0,"hospitalName":"中南大学湘雅医院","departmentName":"院感科","userName":null}
     *//*


    private List<TaskListBean> case_list;

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

    public List<TaskListBean> getTask_list() {
        return case_list;
    }

    public void setTask_list(List<TaskListBean> task_list) {
        this.case_list = task_list;
    }

    @Table(name = "case_table8")
    public static class TaskListBean implements Serializable {
        @Id(column = "dbid")
        private int dbid;
        private int task_id;
        private int temp_type_id;
        private int hospital_id;
        private int uploadStatus; //0已上传 2未上传
        private String department_id;
        private int case_id;
        private String business_id = "";
        private Object sub_id;
        private String mission_time = "";
        private Object submit_time = "";
        private int finished;
        private Object note;
        private int uid;
        int id;
        private Object attr;
        private Object userName = "";
        private String hospitalName = "";
        private String departmentName = "";
        private String businessStr = "";
        private String jsonStr;
        private String supStr;
        private TaskVo supervisor_question;
        private RisistantVo risistantVo;
        private String taskDbId = "";

        public RisistantVo getRisistantVo() {
            return risistantVo;
        }

        public void setRisistantVo(RisistantVo risistantVo) {
            this.risistantVo = risistantVo;
        }

        public TaskVo getSupervisor_question() {
            return supervisor_question;
        }

        public void setSupervisor_question(TaskVo supervisor_question) {
            this.supervisor_question = supervisor_question;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDbid(int dbid) {
            this.dbid = dbid;
        }

        public int getUploadStatus() {
            return uploadStatus;
        }

        public void setUploadStatus(int uploadStatus) {
            this.uploadStatus = uploadStatus;
        }

        public String getSupStr() {
            return supStr;
        }

        public void setSupStr(String supStr) {
            this.supStr = supStr;
        }

        public String getJsonStr() {
            return jsonStr;
        }

        public void setJsonStr(String jsonStr) {
            this.jsonStr = jsonStr;
        }

        public int getDbid() {
            return dbid;
        }

        public String getBusinessStr() {
            return businessStr;
        }

        public void setBusinessStr(String businessStr) {
            this.businessStr = businessStr;
        }

        */
/**
         * id : 37
         * department_id : 1
         * hospital_id : 1
         * bed_no : bed_no
         * patient_name : patient_name
         * patient_id : patient_id
         * time : 2016-07-26 10:10:10
         * check_ids : 1,2,3
         * drug_ids : 1,2,3
         * create_time : 2016-07-29 16:50:02
         * user_id : 1
         * is_del : 0
         * hospitalName : 中南大学湘雅医院
         * departmentName : 院感科
         * userName : null
         *//*


        private BusinessBean business;

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public int getTemp_type_id() {
            return temp_type_id;
        }

        public void setTemp_type_id(int temp_type_id) {
            this.temp_type_id = temp_type_id;
        }

        public int getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(int hospital_id) {
            this.hospital_id = hospital_id;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public int getCase_id() {
            return case_id;
        }

        public void setCase_id(int case_id) {
            this.case_id = case_id;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public Object getSub_id() {
            return sub_id;
        }

        public void setSub_id(Object sub_id) {
            this.sub_id = sub_id;
        }

        public String getMission_time() {
            return mission_time;
        }

        public void setMission_time(String mission_time) {
            this.mission_time = mission_time;
        }

        public Object getSubmit_time() {
            return submit_time;
        }

        public void setSubmit_time(Object submit_time) {
            this.submit_time = submit_time;
        }

        public int getFinished() {
            return finished;
        }

        public void setFinished(int finished) {
            this.finished = finished;
        }

        public Object getNote() {
            return note;
        }

        public void setNote(Object note) {
            this.note = note;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public Object getAttr() {
            return attr;
        }

        public void setAttr(Object attr) {
            this.attr = attr;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
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

        public BusinessBean getBusiness() {
            return business;
        }

        public void setBusiness(BusinessBean business) {
            this.business = business;
        }

        public static class  BusinessBean implements Serializable {
            private int id;
            private String department_id = "";
            private int hospital_id;
            private String bed_no = "";
            private String time;
            private String check_ids = "";
            private String drug_ids = "";
            private String create_time = "";
            private String create_date = "";
            private int user_id;
            private int is_del;
            private String hospitalName = "";
            private String departmentName = "";
            private Object userName = "";
            private int sex;
            private String room_no;
            private String person_liable;
            private String apache = "";
            private int temp1;
            private int temp2;
            private int temp3;
            private int case_id;
            private int type_temp_id;
            String operation_name="";
            String patient_id="";
            String patient_name="";
            String work_type="";
            String operation_type="";
            int isInspector;

            public int getIsInspector() {
                return isInspector;
            }

            public void setIsInspector(int isInspector) {
                this.isInspector = isInspector;
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

            public String getOperation_name() {
                return operation_name;
            }

            public void setOperation_name(String operation_name) {
                this.operation_name = operation_name;
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

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public int getType_temp_id() {
                return type_temp_id;
            }

            public void setType_temp_id(int type_temp_id) {
                this.type_temp_id = type_temp_id;
            }

            public int getCase_id() {
                return case_id;
            }

            public void setCase_id(int case_id) {
                this.case_id = case_id;
            }

            public String getApache() {
                return apache;
            }

            public void setApache(String apache) {
                this.apache = apache;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getIs_del() {
                return is_del;
            }

            public void setIs_del(int is_del) {
                this.is_del = is_del;
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

            public Object getUserName() {
                return userName;
            }

            public void setUserName(Object userName) {
                this.userName = userName;
            }
        }
    }
}
*/
