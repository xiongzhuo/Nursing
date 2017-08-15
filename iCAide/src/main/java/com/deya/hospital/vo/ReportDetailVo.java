package com.deya.hospital.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/17
 */
public class ReportDetailVo {

    /**
     * colTypeReportData : []
     * result_msg : 成功
     * result_id : 0
     * handFunReportData : [{"results":"洗手","yc_cnt":0,"no_yc_cnt":0,"valid_cnt":0,"valid_rate":"0"},{"results":"卫生手消毒","yc_cnt":0,"no_yc_cnt":0,"valid_cnt":0,"valid_rate":"0"},{"results":"未采取措施","yc_cnt":"-","no_yc_cnt":0,"valid_cnt":"-","valid_rate":"-"}]
     * postReportData : []
     * pnameReportData : []
     */

    private String result_msg;
    private String result_id;
    private List<HandFunReportDataBean> colTypeReportData;
    /**
     * results : 洗手
     * yc_cnt : 0
     * no_yc_cnt : 0
     * valid_cnt : 0
     * valid_rate : 0
     */

    private List<HandFunReportDataBean> handFunReportData;
    private List<HandFunReportDataBean> postReportData;
    private List<HandFunReportDataBean> pnameReportData;
    private List<HandFunReportDataBean> timesReportData;

    public List<HandFunReportDataBean> getTimesReportData() {
        return timesReportData;
    }

    public void setTimesReportData(List<HandFunReportDataBean> timesReportData) {
        this.timesReportData = timesReportData;
    }

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

    public List<HandFunReportDataBean> getColTypeReportData() {
        return colTypeReportData == null ? new ArrayList<HandFunReportDataBean>() : colTypeReportData;
    }

    public void setColTypeReportData(List<HandFunReportDataBean> colTypeReportData) {
        this.colTypeReportData = colTypeReportData;
    }

    public List<HandFunReportDataBean> getHandFunReportData() {
        return handFunReportData == null ? new ArrayList<HandFunReportDataBean>() : handFunReportData;
    }

    public void setHandFunReportData(List<HandFunReportDataBean> handFunReportData) {
        this.handFunReportData = handFunReportData;
    }

    public List<HandFunReportDataBean> getPostReportData() {
        return postReportData == null ? new ArrayList<HandFunReportDataBean>() : postReportData;
    }

    public void setPostReportData(List<HandFunReportDataBean> postReportData) {
        this.postReportData = postReportData;
    }

    public List<HandFunReportDataBean> getPnameReportData() {
        return pnameReportData == null ? new ArrayList<HandFunReportDataBean>() : pnameReportData;
    }

    public void setPnameReportData(List<HandFunReportDataBean> pnameReportData) {
        this.pnameReportData = pnameReportData;
    }

    public static class HandFunReportDataBean {
        private String results;
        private String yc_cnt;
        private String no_yc_cnt;
        private String valid_cnt;
        private String valid_rate;
        private String timers;
        private String yc_rate;
        private String colName;
        private String pname;
        private String ppostName;
        private int check_type;
        private String no_valid_cnt;

        public String getNo_valid_cnt() {
            return no_valid_cnt;
        }

        public void setNo_valid_cnt(String no_valid_cnt) {
            this.no_valid_cnt = no_valid_cnt;
        }

        public int getCheck_type() {
            return check_type;
        }

        public void setCheck_type(int check_type) {
            this.check_type = check_type;
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }

        public String getPpostName() {
            return ppostName;
        }

        public void setPpostName(String ppostName) {
            this.ppostName = ppostName;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getTimers() {
            return timers;
        }

        public void setTimers(String timers) {
            this.timers = timers;
        }

        public String getYc_rate() {
            return yc_rate;
        }

        public void setYc_rate(String yc_rate) {
            this.yc_rate = yc_rate;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }

        public String getYc_cnt() {
            return yc_cnt;
        }

        public void setYc_cnt(String yc_cnt) {
            this.yc_cnt = yc_cnt;
        }

        public String getNo_yc_cnt() {
            return no_yc_cnt;
        }

        public void setNo_yc_cnt(String no_yc_cnt) {
            this.no_yc_cnt = no_yc_cnt;
        }

        public String getValid_cnt() {
            return valid_cnt;
        }

        public void setValid_cnt(String valid_cnt) {
            this.valid_cnt = valid_cnt;
        }

        public String getValid_rate() {
            return valid_rate;
        }

        public void setValid_rate(String valid_rate) {
            this.valid_rate = valid_rate;
        }
    }
}
