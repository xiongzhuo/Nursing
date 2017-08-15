package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/4/10
 */
public class HandDepartReportVo implements Serializable{

    /**
     * result_msg : 成功
     * result_id : 0
     * data : [{"dataList":[{"timers":279,"check_type":10,"yc_rate":"91.76%","yc_cnt":256,"valid_rate":"70.70%","valid_cnt":181,"checkTypeName":"抽查"},{"timers":101,"check_type":20,"yc_rate":"95.05%","yc_cnt":96,"valid_rate":"67.71%","valid_cnt":65,"checkTypeName":"自查"},{"timers":30,"check_type":30,"yc_rate":"90.00%","yc_cnt":27,"valid_rate":"77.78%","valid_cnt":21,"checkTypeName":"暗访"}],"time":"2017"},{"dataList":[{"timers":14,"check_type":10,"yc_rate":"100%","yc_cnt":14,"valid_rate":"78.57%","valid_cnt":11,"checkTypeName":"抽查"},{"timers":4,"check_type":20,"yc_rate":"100%","yc_cnt":4,"valid_rate":"75.00%","valid_cnt":3,"checkTypeName":"自查"},{"timers":10,"check_type":30,"yc_rate":"80.00%","yc_cnt":8,"valid_rate":"62.50%","valid_cnt":5,"checkTypeName":"暗访"}],"time":"2017-03"},{"dataList":[{"check_type":"10","yc_rate":0,"timers":0,"valid_cnt":0,"checkTypeName":"抽查","valid_rate":0,"departmentId":"0","yc_cnt":0},{"check_type":"20","yc_rate":0,"timers":0,"valid_cnt":0,"checkTypeName":"自查","valid_rate":0,"departmentId":"0","yc_cnt":0},{"check_type":"30","yc_rate":0,"timers":0,"valid_cnt":0,"checkTypeName":"暗访","valid_rate":0,"departmentId":"0","yc_cnt":0}],"time":"2017-02"},{"dataList":[{"timers":105,"check_type":10,"yc_rate":"97.14%","yc_cnt":102,"valid_rate":"69.61%","valid_cnt":71,"checkTypeName":"抽查"},{"timers":17,"check_type":20,"yc_rate":"94.12%","yc_cnt":16,"valid_rate":"43.75%","valid_cnt":7,"checkTypeName":"自查"},{"timers":16,"check_type":30,"yc_rate":"100%","yc_cnt":16,"valid_rate":"87.50%","valid_cnt":14,"checkTypeName":"暗访"}],"time":"2017-01"}]
     */

    private String result_msg;
    private String result_id;
    /**
     * dataList : [{"timers":279,"check_type":10,"yc_rate":"91.76%","yc_cnt":256,"valid_rate":"70.70%","valid_cnt":181,"checkTypeName":"抽查"},{"timers":101,"check_type":20,"yc_rate":"95.05%","yc_cnt":96,"valid_rate":"67.71%","valid_cnt":65,"checkTypeName":"自查"},{"timers":30,"check_type":30,"yc_rate":"90.00%","yc_cnt":27,"valid_rate":"77.78%","valid_cnt":21,"checkTypeName":"暗访"}]
     * time : 2017
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String time;
        private String   showTime;

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        /**
         * timers : 279
         * check_type : 10
         * yc_rate : 91.76%
         * yc_cnt : 256
         * valid_rate : 70.70%
         * valid_cnt : 181
         * checkTypeName : 抽查
         */

        private List<HandReportVo.DepartmentReportBean.DataListBean> dataList;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<HandReportVo.DepartmentReportBean.DataListBean> getDataList() {
            return dataList;
        }

        public void setDataList(List<HandReportVo.DepartmentReportBean.DataListBean> dataList) {
            this.dataList = dataList;
        }


    }
}
