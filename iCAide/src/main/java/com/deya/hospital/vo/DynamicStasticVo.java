package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class DynamicStasticVo {


    /**
     * result_msg : 获取数据成功
     * result_id : 0
     * list : [{"dep_no":"2374","dep_name":"心血管内科","patient_count":1,"patientList":[{"id":1,"patient_no":"213","patient_name":"xxx","hospital_no":"3347","hospital_name":"deya","dep_no":"2374","dep_name":"心血管内科","bed_no":"11","is_new":"1","is_inbed":"1","is_fever":"1","is_indwelling_catheter":"1","is_central_catheter":"1","is_respirator":"1","is_multi_resistant":"1","upload_date":"2016-08-10","is_suspected":"1"}]},{"dep_no":"2375","dep_name":"呼吸内科","patient_count":1,"patientList":[{"id":2,"patient_no":"11","patient_name":"nihao","hospital_no":"3347","hospital_name":"deya","dep_no":"2375","dep_name":"呼吸内科","bed_no":"1","is_new":"1","is_inbed":"1","is_fever":"1","is_indwelling_catheter":"1","is_central_catheter":"1","is_respirator":"1","is_multi_resistant":"1","upload_date":"2016-08-10","is_suspected":"1"}]},{"dep_no":"2379","dep_name":"内分泌科","patient_count":1,"patientList":[{"id":3,"patient_no":"2","patient_name":"啊大大","hospital_no":"3347","hospital_name":"deta","dep_no":"2379","dep_name":"内分泌科","bed_no":"1","is_new":"1","is_inbed":"1","is_fever":"1","is_indwelling_catheter":"1","is_central_catheter":"1","is_respirator":"1","is_multi_resistant":"1","upload_date":"2016-08-10","is_suspected":"1"}]}]
     */

    private String result_msg;
    private String result_id;
    /**
     * dep_no : 2374
     * dep_name : 心血管内科
     * patient_count : 1
     * patientList : [{"id":1,"patient_no":"213","patient_name":"xxx","hospital_no":"3347","hospital_name":"deya","dep_no":"2374","dep_name":"心血管内科","bed_no":"11","is_new":"1","is_inbed":"1","is_fever":"1","is_indwelling_catheter":"1","is_central_catheter":"1","is_respirator":"1","is_multi_resistant":"1","upload_date":"2016-08-10","is_suspected":"1"}]
     */

    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        private String dep_no;
        private String dep_name;
        private int patient_count;
        private String date;
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        /**
         * id : 1
         * patient_no : 213
         * patient_name : xxx
         * hospital_no : 3347
         * hospital_name : deya
         * dep_no : 2374
         * dep_name : 心血管内科
         * bed_no : 11
         * is_new : 1
         * is_inbed : 1
         * is_fever : 1
         * is_indwelling_catheter : 1
         * is_central_catheter : 1
         * is_respirator : 1
         * is_multi_resistant : 1
         * upload_date : 2016-08-10
         * is_suspected : 1
         */


        public String getDep_no() {
            return dep_no;
        }

        public void setDep_no(String dep_no) {
            this.dep_no = dep_no;
        }

        public String getDep_name() {
            return dep_name;
        }

        public void setDep_name(String dep_name) {
            this.dep_name = dep_name;
        }

        public int getPatient_count() {
            return patient_count;
        }

        public void setPatient_count(int patient_count) {
            this.patient_count = patient_count;
        }



    }
}
