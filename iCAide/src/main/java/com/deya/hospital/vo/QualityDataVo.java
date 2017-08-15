package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/22
 */
public class QualityDataVo {

    /**
     * result_msg : 查询成功
     * result_id : 0
     * task_info : {"task_id":402,"sub_id":"500","temp_type_id":10,"hospital_id":6140,"department_id":"209161","departmentName":"肾内科","hospitalName":"专业版测试医院","memberName":"潘孝","business_id":"1","check_type":10,"mission_time":"2017-03-15 13:55:44","tempId":31,"baseInfo":{"id":1,"department_id":209161,"hospital_id":6140,"room_no":null,"bed_no":null,"person_liable":null,"mission_time":null,"create_time":1489630391000,"create_date":"2017-03-16","temp_type":0,"temp_id":null,"status":0,"ext1":null,"ext2":null,"ext3":null},"temp_list":{"id":31,"hospital_id":"6140","department_id":null,"temp_type_id":12,"role":1,"title":"临床质控标准模板","note":null,"state":1,"create_time":1489557102000,"update_time":1489557105000,"uid":368,"listsort":50,"range":1,"descption":null,"sub_type":null,"item_list":[{"id":2,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"10","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":2,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":3,"item_repo_id":3,"title":"评估是否合格","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":3,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":4,"item_repo_id":4,"title":"进行插管及护理前严格执行无菌操作规程，认真执行手卫生制度","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"5","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":4,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":5,"item_repo_id":5,"title":"单间隔离或同耐药菌安置同一病室并悬挂相应隔标识","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"15","result_id":5,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":6,"item_repo_id":6,"title":"接触或处理血液体液、分泌物、排泄物等以及污染的组织敷料或床单衣物时戴手套","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"18","result_id":6,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":7,"item_repo_id":7,"title":"是否无双手回套针帽情况","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[]},{"id":11,"item_repo_id":8,"title":"单选","show_no":"1-1","type":5,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":"三翻四复","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[{"id":1,"type_id":3,"title":"111","score":"1","is_yes":"1","parent_id":0},{"id":2,"type_id":3,"title":"222","score":"2","is_yes":"0","parent_id":0}]},{"id":1,"item_repo_id":1,"title":"1 床头抬高30度","show_no":null,"type":1,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":null,"result":"15","item_score":"15","result_id":1,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]}]},"supervisor_question":{"id":500,"title":"重点部门感染管理（问题反馈）","hospital":6140,"department":209161,"uid":6761,"user_name":"孙燕姿","departmentName":"肾内科","user_regis_job":"专职感控人员","origin":3,"state":2,"check_content":"??????","exist_problem":"","deal_suggest":"","is_feedback_department":1,"department_confirm_user":0,"department_confirm_user_name":null,"department_confirm_time":null,"department_confirm_user_regis_job":"其他","improve_measures":null,"improve_result_assess":null,"is_again_supervisor":0,"again_supervisor_time":null,"again_supervisor_user":0,"again_supervisor_user_name":null,"again_supervisor_user_regis_job":"其他","attachments":[],"remind_time":null,"remind_time1":null,"create_time":"2016-12-23","attachments_pic":[],"attachments_audio":[]}}
     */

    private String result_msg;
    private String result_id;
    /**
     * task_id : 402
     * sub_id : 500
     * temp_type_id : 10
     * hospital_id : 6140
     * department_id : 209161
     * departmentName : 肾内科
     * hospitalName : 专业版测试医院
     * memberName : 潘孝
     * business_id : 1
     * check_type : 10
     * mission_time : 2017-03-15 13:55:44
     * tempId : 31
     * baseInfo : {"id":1,"department_id":209161,"hospital_id":6140,"room_no":null,"bed_no":null,"person_liable":null,"mission_time":null,"create_time":1489630391000,"create_date":"2017-03-16","temp_type":0,"temp_id":null,"status":0,"ext1":null,"ext2":null,"ext3":null}
     * temp_list : {"id":31,"hospital_id":"6140","department_id":null,"temp_type_id":12,"role":1,"title":"临床质控标准模板","note":null,"state":1,"create_time":1489557102000,"update_time":1489557105000,"uid":368,"listsort":50,"range":1,"descption":null,"sub_type":null,"item_list":[{"id":2,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"10","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":2,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":3,"item_repo_id":3,"title":"评估是否合格","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":3,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":4,"item_repo_id":4,"title":"进行插管及护理前严格执行无菌操作规程，认真执行手卫生制度","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"5","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":4,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":5,"item_repo_id":5,"title":"单间隔离或同耐药菌安置同一病室并悬挂相应隔标识","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"15","result_id":5,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":6,"item_repo_id":6,"title":"接触或处理血液体液、分泌物、排泄物等以及污染的组织敷料或床单衣物时戴手套","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"18","result_id":6,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":7,"item_repo_id":7,"title":"是否无双手回套针帽情况","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[]},{"id":11,"item_repo_id":8,"title":"单选","show_no":"1-1","type":5,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":"三翻四复","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[{"id":1,"type_id":3,"title":"111","score":"1","is_yes":"1","parent_id":0},{"id":2,"type_id":3,"title":"222","score":"2","is_yes":"0","parent_id":0}]},{"id":1,"item_repo_id":1,"title":"1 床头抬高30度","show_no":null,"type":1,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":null,"result":"15","item_score":"15","result_id":1,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]}]}
     * supervisor_question : {"id":500,"title":"重点部门感染管理（问题反馈）","hospital":6140,"department":209161,"uid":6761,"user_name":"孙燕姿","departmentName":"肾内科","user_regis_job":"专职感控人员","origin":3,"state":2,"check_content":"??????","exist_problem":"","deal_suggest":"","is_feedback_department":1,"department_confirm_user":0,"department_confirm_user_name":null,"department_confirm_time":null,"department_confirm_user_regis_job":"其他","improve_measures":null,"improve_result_assess":null,"is_again_supervisor":0,"again_supervisor_time":null,"again_supervisor_user":0,"again_supervisor_user_name":null,"again_supervisor_user_regis_job":"其他","attachments":[],"remind_time":null,"remind_time1":null,"create_time":"2016-12-23","attachments_pic":[],"attachments_audio":[]}
     */

    private TaskInfoBean task_info;

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

    public TaskInfoBean getTask_info() {
        return task_info;
    }

    public void setTask_info(TaskInfoBean task_info) {
        this.task_info = task_info;
    }

    public static class TaskInfoBean {
        private int task_id;
        private String sub_id;
        private int temp_type_id;
        private int hospital_id;
        private String department_id;
        private String departmentName;
        private String hospitalName;
        private String memberName;
        private String business_id;
        private int check_type;
        private String mission_time;
        private int tempId;
        /**
         * id : 1
         * department_id : 209161
         * hospital_id : 6140
         * room_no : null
         * bed_no : null
         * person_liable : null
         * mission_time : null
         * create_time : 1489630391000
         * create_date : 2017-03-16
         * temp_type : 0
         * temp_id : null
         * status : 0
         * ext1 : null
         * ext2 : null
         * ext3 : null
         */

        private BaseInfoBean baseInfo;
        /**
         * id : 31
         * hospital_id : 6140
         * department_id : null
         * temp_type_id : 12
         * role : 1
         * title : 临床质控标准模板
         * note : null
         * state : 1
         * create_time : 1489557102000
         * update_time : 1489557105000
         * uid : 368
         * listsort : 50
         * range : 1
         * descption : null
         * sub_type : null
         * item_list : [{"id":2,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"10","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":2,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":3,"item_repo_id":3,"title":"评估是否合格","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":3,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":4,"item_repo_id":4,"title":"进行插管及护理前严格执行无菌操作规程，认真执行手卫生制度","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"5","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"5","result_id":4,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":5,"item_repo_id":5,"title":"单间隔离或同耐药菌安置同一病室并悬挂相应隔标识","show_no":null,"type":3,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"15","result_id":5,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":6,"item_repo_id":6,"title":"接触或处理血液体液、分泌物、排泄物等以及污染的组织敷料或床单衣物时戴手套","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":"15","item_score":"18","result_id":6,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]},{"id":7,"item_repo_id":7,"title":"是否无双手回套针帽情况","show_no":null,"type":2,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":"考评方法","item_detail":"评分细则","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[]},{"id":11,"item_repo_id":8,"title":"单选","show_no":"1-1","type":5,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":"三翻四复","result":null,"item_score":null,"result_id":null,"save_time":null,"name":null,"children":[{"id":1,"type_id":3,"title":"111","score":"1","is_yes":"1","parent_id":0},{"id":2,"type_id":3,"title":"222","score":"2","is_yes":"0","parent_id":0}]},{"id":1,"item_repo_id":1,"title":"1 床头抬高30度","show_no":null,"type":1,"show_type":1,"listsort":50,"score":"20","default_val":null,"item_class":"12","item_desc":null,"item_detail":null,"result":"15","item_score":"15","result_id":1,"save_time":"2017-03-15 13:57","name":"潘孝","children":[]}]
         */

        private TempListBean temp_list;
        /**
         * id : 500
         * title : 重点部门感染管理（问题反馈）
         * hospital : 6140
         * department : 209161
         * uid : 6761
         * user_name : 孙燕姿
         * departmentName : 肾内科
         * user_regis_job : 专职感控人员
         * origin : 3
         * state : 2
         * check_content : ??????
         * exist_problem :
         * deal_suggest :
         * is_feedback_department : 1
         * department_confirm_user : 0
         * department_confirm_user_name : null
         * department_confirm_time : null
         * department_confirm_user_regis_job : 其他
         * improve_measures : null
         * improve_result_assess : null
         * is_again_supervisor : 0
         * again_supervisor_time : null
         * again_supervisor_user : 0
         * again_supervisor_user_name : null
         * again_supervisor_user_regis_job : 其他
         * attachments : []
         * remind_time : null
         * remind_time1 : null
         * create_time : 2016-12-23
         * attachments_pic : []
         * attachments_audio : []
         */

        private SupervisorQuestionBean supervisor_question;

        public int getTask_id() {
            return task_id;
        }

        public void setTask_id(int task_id) {
            this.task_id = task_id;
        }

        public String getSub_id() {
            return sub_id;
        }

        public void setSub_id(String sub_id) {
            this.sub_id = sub_id;
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

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(String business_id) {
            this.business_id = business_id;
        }

        public int getCheck_type() {
            return check_type;
        }

        public void setCheck_type(int check_type) {
            this.check_type = check_type;
        }

        public String getMission_time() {
            return mission_time;
        }

        public void setMission_time(String mission_time) {
            this.mission_time = mission_time;
        }

        public int getTempId() {
            return tempId;
        }

        public void setTempId(int tempId) {
            this.tempId = tempId;
        }

        public BaseInfoBean getBaseInfo() {
            return baseInfo;
        }

        public void setBaseInfo(BaseInfoBean baseInfo) {
            this.baseInfo = baseInfo;
        }

        public TempListBean getTemp_list() {
            return temp_list;
        }

        public void setTemp_list(TempListBean temp_list) {
            this.temp_list = temp_list;
        }

        public SupervisorQuestionBean getSupervisor_question() {
            return supervisor_question;
        }

        public void setSupervisor_question(SupervisorQuestionBean supervisor_question) {
            this.supervisor_question = supervisor_question;
        }

        public static class BaseInfoBean {
            private int id;
            private int department_id;
            private int hospital_id;
            private Object room_no;
            private Object bed_no;
            private Object person_liable;
            private Object mission_time;
            private long create_time;
            private String create_date;
            private int temp_type;
            private Object temp_id;
            private int status;
            private Object ext1;
            private Object ext2;
            private Object ext3;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getDepartment_id() {
                return department_id;
            }

            public void setDepartment_id(int department_id) {
                this.department_id = department_id;
            }

            public int getHospital_id() {
                return hospital_id;
            }

            public void setHospital_id(int hospital_id) {
                this.hospital_id = hospital_id;
            }

            public Object getRoom_no() {
                return room_no;
            }

            public void setRoom_no(Object room_no) {
                this.room_no = room_no;
            }

            public Object getBed_no() {
                return bed_no;
            }

            public void setBed_no(Object bed_no) {
                this.bed_no = bed_no;
            }

            public Object getPerson_liable() {
                return person_liable;
            }

            public void setPerson_liable(Object person_liable) {
                this.person_liable = person_liable;
            }

            public Object getMission_time() {
                return mission_time;
            }

            public void setMission_time(Object mission_time) {
                this.mission_time = mission_time;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public int getTemp_type() {
                return temp_type;
            }

            public void setTemp_type(int temp_type) {
                this.temp_type = temp_type;
            }

            public Object getTemp_id() {
                return temp_id;
            }

            public void setTemp_id(Object temp_id) {
                this.temp_id = temp_id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public Object getExt1() {
                return ext1;
            }

            public void setExt1(Object ext1) {
                this.ext1 = ext1;
            }

            public Object getExt2() {
                return ext2;
            }

            public void setExt2(Object ext2) {
                this.ext2 = ext2;
            }

            public Object getExt3() {
                return ext3;
            }

            public void setExt3(Object ext3) {
                this.ext3 = ext3;
            }
        }

        public static class TempListBean {
            private int id;
            private String hospital_id;
            private Object department_id;
            private int temp_type_id;
            private int role;
            private String title;
            private Object note;
            private int state;
            private long create_time;
            private long update_time;
            private int uid;
            private int listsort;
            private int range;
            private Object descption;
            private Object sub_type;
            /**
             * id : 2
             * item_repo_id : 2
             * title : 一次性生活用品与一次性医疗用品都视为医疗垃圾
             * show_no : null
             * type : 2
             * show_type : 1
             * listsort : 50
             * score : 10
             * default_val : null
             * item_class : 12
             * item_desc : 考评方法
             * item_detail : 评分细则
             * result : 15
             * item_score : 5
             * result_id : 2
             * save_time : 2017-03-15 13:57
             * name : 潘孝
             * children : []
             */

            private List<ItemListBean> item_list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getHospital_id() {
                return hospital_id;
            }

            public void setHospital_id(String hospital_id) {
                this.hospital_id = hospital_id;
            }

            public Object getDepartment_id() {
                return department_id;
            }

            public void setDepartment_id(Object department_id) {
                this.department_id = department_id;
            }

            public int getTemp_type_id() {
                return temp_type_id;
            }

            public void setTemp_type_id(int temp_type_id) {
                this.temp_type_id = temp_type_id;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getNote() {
                return note;
            }

            public void setNote(Object note) {
                this.note = note;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getListsort() {
                return listsort;
            }

            public void setListsort(int listsort) {
                this.listsort = listsort;
            }

            public int getRange() {
                return range;
            }

            public void setRange(int range) {
                this.range = range;
            }

            public Object getDescption() {
                return descption;
            }

            public void setDescption(Object descption) {
                this.descption = descption;
            }

            public Object getSub_type() {
                return sub_type;
            }

            public void setSub_type(Object sub_type) {
                this.sub_type = sub_type;
            }

            public List<ItemListBean> getItem_list() {
                return item_list;
            }

            public void setItem_list(List<ItemListBean> item_list) {
                this.item_list = item_list;
            }

            public static class ItemListBean {
                private int id;
                private int item_repo_id;
                private String title;
                private Object show_no;
                private int type;
                private int show_type;
                private int listsort;
                private String score;
                private Object default_val;
                private String item_class;
                private String item_desc;
                private String item_detail;
                private String result;
                private String item_score;
                private int result_id;
                private String save_time;
                private String name;
                private List<?> children;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getItem_repo_id() {
                    return item_repo_id;
                }

                public void setItem_repo_id(int item_repo_id) {
                    this.item_repo_id = item_repo_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public Object getShow_no() {
                    return show_no;
                }

                public void setShow_no(Object show_no) {
                    this.show_no = show_no;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getShow_type() {
                    return show_type;
                }

                public void setShow_type(int show_type) {
                    this.show_type = show_type;
                }

                public int getListsort() {
                    return listsort;
                }

                public void setListsort(int listsort) {
                    this.listsort = listsort;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public Object getDefault_val() {
                    return default_val;
                }

                public void setDefault_val(Object default_val) {
                    this.default_val = default_val;
                }

                public String getItem_class() {
                    return item_class;
                }

                public void setItem_class(String item_class) {
                    this.item_class = item_class;
                }

                public String getItem_desc() {
                    return item_desc;
                }

                public void setItem_desc(String item_desc) {
                    this.item_desc = item_desc;
                }

                public String getItem_detail() {
                    return item_detail;
                }

                public void setItem_detail(String item_detail) {
                    this.item_detail = item_detail;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public String getItem_score() {
                    return item_score;
                }

                public void setItem_score(String item_score) {
                    this.item_score = item_score;
                }

                public int getResult_id() {
                    return result_id;
                }

                public void setResult_id(int result_id) {
                    this.result_id = result_id;
                }

                public String getSave_time() {
                    return save_time;
                }

                public void setSave_time(String save_time) {
                    this.save_time = save_time;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<?> getChildren() {
                    return children;
                }

                public void setChildren(List<?> children) {
                    this.children = children;
                }
            }
        }

        public static class SupervisorQuestionBean {
            private int id;
            private String title;
            private int hospital;
            private int department;
            private int uid;
            private String user_name;
            private String departmentName;
            private String user_regis_job;
            private int origin;
            private int state;
            private String check_content;
            private String exist_problem;
            private String deal_suggest;
            private int is_feedback_department;
            private int department_confirm_user;
            private Object department_confirm_user_name;
            private Object department_confirm_time;
            private String department_confirm_user_regis_job;
            private Object improve_measures;
            private Object improve_result_assess;
            private int is_again_supervisor;
            private Object again_supervisor_time;
            private int again_supervisor_user;
            private Object again_supervisor_user_name;
            private String again_supervisor_user_regis_job;
            private Object remind_time;
            private Object remind_time1;
            private String create_time;
            private List<?> attachments;
            private List<?> attachments_pic;
            private List<?> attachments_audio;

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

            public int getHospital() {
                return hospital;
            }

            public void setHospital(int hospital) {
                this.hospital = hospital;
            }

            public int getDepartment() {
                return department;
            }

            public void setDepartment(int department) {
                this.department = department;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public String getUser_regis_job() {
                return user_regis_job;
            }

            public void setUser_regis_job(String user_regis_job) {
                this.user_regis_job = user_regis_job;
            }

            public int getOrigin() {
                return origin;
            }

            public void setOrigin(int origin) {
                this.origin = origin;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
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

            public int getIs_feedback_department() {
                return is_feedback_department;
            }

            public void setIs_feedback_department(int is_feedback_department) {
                this.is_feedback_department = is_feedback_department;
            }

            public int getDepartment_confirm_user() {
                return department_confirm_user;
            }

            public void setDepartment_confirm_user(int department_confirm_user) {
                this.department_confirm_user = department_confirm_user;
            }

            public Object getDepartment_confirm_user_name() {
                return department_confirm_user_name;
            }

            public void setDepartment_confirm_user_name(Object department_confirm_user_name) {
                this.department_confirm_user_name = department_confirm_user_name;
            }

            public Object getDepartment_confirm_time() {
                return department_confirm_time;
            }

            public void setDepartment_confirm_time(Object department_confirm_time) {
                this.department_confirm_time = department_confirm_time;
            }

            public String getDepartment_confirm_user_regis_job() {
                return department_confirm_user_regis_job;
            }

            public void setDepartment_confirm_user_regis_job(String department_confirm_user_regis_job) {
                this.department_confirm_user_regis_job = department_confirm_user_regis_job;
            }

            public Object getImprove_measures() {
                return improve_measures;
            }

            public void setImprove_measures(Object improve_measures) {
                this.improve_measures = improve_measures;
            }

            public Object getImprove_result_assess() {
                return improve_result_assess;
            }

            public void setImprove_result_assess(Object improve_result_assess) {
                this.improve_result_assess = improve_result_assess;
            }

            public int getIs_again_supervisor() {
                return is_again_supervisor;
            }

            public void setIs_again_supervisor(int is_again_supervisor) {
                this.is_again_supervisor = is_again_supervisor;
            }

            public Object getAgain_supervisor_time() {
                return again_supervisor_time;
            }

            public void setAgain_supervisor_time(Object again_supervisor_time) {
                this.again_supervisor_time = again_supervisor_time;
            }

            public int getAgain_supervisor_user() {
                return again_supervisor_user;
            }

            public void setAgain_supervisor_user(int again_supervisor_user) {
                this.again_supervisor_user = again_supervisor_user;
            }

            public Object getAgain_supervisor_user_name() {
                return again_supervisor_user_name;
            }

            public void setAgain_supervisor_user_name(Object again_supervisor_user_name) {
                this.again_supervisor_user_name = again_supervisor_user_name;
            }

            public String getAgain_supervisor_user_regis_job() {
                return again_supervisor_user_regis_job;
            }

            public void setAgain_supervisor_user_regis_job(String again_supervisor_user_regis_job) {
                this.again_supervisor_user_regis_job = again_supervisor_user_regis_job;
            }

            public Object getRemind_time() {
                return remind_time;
            }

            public void setRemind_time(Object remind_time) {
                this.remind_time = remind_time;
            }

            public Object getRemind_time1() {
                return remind_time1;
            }

            public void setRemind_time1(Object remind_time1) {
                this.remind_time1 = remind_time1;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public List<?> getAttachments() {
                return attachments;
            }

            public void setAttachments(List<?> attachments) {
                this.attachments = attachments;
            }

            public List<?> getAttachments_pic() {
                return attachments_pic;
            }

            public void setAttachments_pic(List<?> attachments_pic) {
                this.attachments_pic = attachments_pic;
            }

            public List<?> getAttachments_audio() {
                return attachments_audio;
            }

            public void setAttachments_audio(List<?> attachments_audio) {
                this.attachments_audio = attachments_audio;
            }
        }
    }
}
