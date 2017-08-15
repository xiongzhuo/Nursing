package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class DynamicPatientVo {

    /**
     * result_msg : 获取数据成功
     * result_id : 0
     * list : [{"id":1,"patient_no":"4","patient_name":"2324","hospital_no":"3347","hospital_name":"sfd","dep_no":"2374","dep_name":"心血管内科","bed_no":"313","is_new":"1","is_inbed":"1","is_fever":"1","is_indwelling_catheter":"1","is_central_catheter":"1","is_respirator":"1","is_multi_resistant":"1","upload_date":"2016-08-11","is_suspected":"1"},{"id":3,"patient_no":"2","patient_name":"wqe","hospital_no":"3347","hospital_name":"ada","dep_no":"2374","dep_name":"心血管内科","bed_no":"wqeq","is_new":"1","is_inbed":"1","is_fever":"0","is_indwelling_catheter":"0","is_central_catheter":"0","is_respirator":"0","is_multi_resistant":"0","upload_date":"2016-08-11","is_suspected":"1"}]
     */

    private String result_msg;
    private String result_id;
    /**
     * id : 1
     * patient_no : 4
     * patient_name : 2324
     * hospital_no : 3347
     * hospital_name : sfd
     * dep_no : 2374
     * dep_name : 心血管内科
     * bed_no : 313
     * is_new : 1
     * is_inbed : 1
     * is_fever : 1
     * is_indwelling_catheter : 1
     * is_central_catheter : 1
     * is_respirator : 1
     * is_multi_resistant : 1
     * upload_date : 2016-08-11
     * is_suspected : 1
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

    public static class ListBean implements Serializable{
        private int id;
        private String patient_no;
        private String patient_name;
        private String hospital_no;
        private String hospital_name;
        private String dep_no;
        private String dep_name;
        private String bed_no;
        private String is_new;
        private String is_inbed;
        private String is_fever;
        private String is_indwelling_catheter;
        private String is_central_catheter;
        private String is_respirator;
        private String is_multi_resistant;
        private String upload_date;
        private String is_suspected;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPatient_no() {
            return patient_no;
        }

        public void setPatient_no(String patient_no) {
            this.patient_no = patient_no;
        }

        public String getPatient_name() {
            return patient_name;
        }

        public void setPatient_name(String patient_name) {
            this.patient_name = patient_name;
        }

        public String getHospital_no() {
            return hospital_no;
        }

        public void setHospital_no(String hospital_no) {
            this.hospital_no = hospital_no;
        }

        public String getHospital_name() {
            return hospital_name;
        }

        public void setHospital_name(String hospital_name) {
            this.hospital_name = hospital_name;
        }

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

        public String getBed_no() {
            return bed_no;
        }

        public void setBed_no(String bed_no) {
            this.bed_no = bed_no;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getIs_inbed() {
            return is_inbed;
        }

        public void setIs_inbed(String is_inbed) {
            this.is_inbed = is_inbed;
        }

        public String getIs_fever() {
            return is_fever;
        }

        public void setIs_fever(String is_fever) {
            this.is_fever = is_fever;
        }

        public String getIs_indwelling_catheter() {
            return is_indwelling_catheter;
        }

        public void setIs_indwelling_catheter(String is_indwelling_catheter) {
            this.is_indwelling_catheter = is_indwelling_catheter;
        }

        public String getIs_central_catheter() {
            return is_central_catheter;
        }

        public void setIs_central_catheter(String is_central_catheter) {
            this.is_central_catheter = is_central_catheter;
        }

        public String getIs_respirator() {
            return is_respirator;
        }

        public void setIs_respirator(String is_respirator) {
            this.is_respirator = is_respirator;
        }

        public String getIs_multi_resistant() {
            return is_multi_resistant;
        }

        public void setIs_multi_resistant(String is_multi_resistant) {
            this.is_multi_resistant = is_multi_resistant;
        }

        public String getUpload_date() {
            return upload_date;
        }

        public void setUpload_date(String upload_date) {
            this.upload_date = upload_date;
        }

        public String getIs_suspected() {
            return is_suspected;
        }

        public void setIs_suspected(String is_suspected) {
            this.is_suspected = is_suspected;
        }
    }
}
