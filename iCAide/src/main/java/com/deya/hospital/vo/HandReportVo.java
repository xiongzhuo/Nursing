package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/10
 */
public class HandReportVo implements Serializable{

    /**
     * hospitalReport : [{"timers":5,"check_type":1,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"66.67","valid_cnt":2},{"timers":4,"check_type":2,"yc_rate":"100","yc_cnt":4,"valid_rate":"75.00","valid_cnt":3},{"timers":5,"check_type":3,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"33.33","valid_cnt":1}]
     * result_msg : 成功
     * result_id : 0
     * pageTotal : 21
     * departmentReport : [{"departmentId":1579,"departmentName":"心胸外科","dataList":[{"timers":5,"departmentName":"心胸外科","check_type":1,"department":1579,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"66.67","valid_cnt":2},{"timers":4,"departmentName":"心胸外科","check_type":2,"department":1579,"yc_rate":"100","yc_cnt":4,"valid_rate":"75.00","valid_cnt":3},{"timers":5,"departmentName":"心胸外科","check_type":3,"department":1579,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"33.33","valid_cnt":1}]},{"departmentId":4825,"departmentName":"测试dd","dataList":[]},{"departmentId":1567,"departmentName":"消化内科","dataList":[]},{"departmentId":203401,"departmentName":"从东","dataList":[]},{"departmentId":1569,"departmentName":"免疫内科","dataList":[]}]
     * totalcnt : 103
     * pageIndex : 1
     */

    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int totalcnt;
    private int pageIndex;
    /**
     * timers : 5
     * check_type : 1
     * yc_rate : 60.00
     * yc_cnt : 3
     * valid_rate : 66.67
     * valid_cnt : 2
     */

    private List<HospitalReportBean> hospitalReport;
    /**
     * departmentId : 1579
     * departmentName : 心胸外科
     * dataList : [{"timers":5,"departmentName":"心胸外科","check_type":1,"department":1579,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"66.67","valid_cnt":2},{"timers":4,"departmentName":"心胸外科","check_type":2,"department":1579,"yc_rate":"100","yc_cnt":4,"valid_rate":"75.00","valid_cnt":3},{"timers":5,"departmentName":"心胸外科","check_type":3,"department":1579,"yc_rate":"60.00","yc_cnt":3,"valid_rate":"33.33","valid_cnt":1}]
     */

    private List<DepartmentReportBean> departmentReport;
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

    public int getTotalcnt() {
        return totalcnt;
    }

    public void setTotalcnt(int totalcnt) {
        this.totalcnt = totalcnt;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<HospitalReportBean> getHospitalReport() {
        return hospitalReport;
    }

    public void setHospitalReport(List<HospitalReportBean> hospitalReport) {
        this.hospitalReport = hospitalReport;
    }

    public List<DepartmentReportBean> getDepartmentReport() {
        return departmentReport;
    }

    public void setDepartmentReport(List<DepartmentReportBean> departmentReport) {
        this.departmentReport = departmentReport;
    }

    public static class HospitalReportBean {
        private int timers;
        private int check_type;
        private String yc_rate;
        private int yc_cnt;
        private String valid_rate;
        private int valid_cnt;

        public int getTimers() {
            return timers;
        }

        public void setTimers(int timers) {
            this.timers = timers;
        }

        public int getCheck_type() {
            return check_type;
        }

        public void setCheck_type(int check_type) {
            this.check_type = check_type;
        }

        public String getYc_rate() {
            return yc_rate;
        }

        public void setYc_rate(String yc_rate) {
            this.yc_rate = yc_rate;
        }

        public int getYc_cnt() {
            return yc_cnt;
        }

        public void setYc_cnt(int yc_cnt) {
            this.yc_cnt = yc_cnt;
        }

        public String getValid_rate() {
            return valid_rate;
        }

        public void setValid_rate(String valid_rate) {
            this.valid_rate = valid_rate;
        }

        public int getValid_cnt() {
            return valid_cnt;
        }

        public void setValid_cnt(int valid_cnt) {
            this.valid_cnt = valid_cnt;
        }
    }

    public static class DepartmentReportBean implements Serializable{
        private int departmentId;
        private String departmentName;
        /**
         * timers : 5
         * departmentName : 心胸外科
         * check_type : 1
         * department : 1579
         * yc_rate : 60.00
         * yc_cnt : 3
         * valid_rate : 66.67
         * valid_cnt : 2
         */

        private List<DataListBean> dataList;

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public List<DataListBean> getDataList() {
            return dataList==null?new ArrayList<DataListBean>():dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean {
            private int timers;
            private String departmentName;
            private int check_type;
            private int department;
            private String yc_rate;
            private int yc_cnt;
            private String valid_rate;
            private int valid_cnt;

            public int getTimers() {
                return timers;
            }

            public void setTimers(int timers) {
                this.timers = timers;
            }

            public String getDepartmentName() {
                return departmentName;
            }

            public void setDepartmentName(String departmentName) {
                this.departmentName = departmentName;
            }

            public int getCheck_type() {
                return check_type;
            }

            public void setCheck_type(int check_type) {
                this.check_type = check_type;
            }

            public int getDepartment() {
                return department;
            }

            public void setDepartment(int department) {
                this.department = department;
            }

            public String getYc_rate() {
                return yc_rate;
            }

            public void setYc_rate(String yc_rate) {
                this.yc_rate = yc_rate;
            }

            public int getYc_cnt() {
                return yc_cnt;
            }

            public void setYc_cnt(int yc_cnt) {
                this.yc_cnt = yc_cnt;
            }

            public String getValid_rate() {
                return valid_rate;
            }

            public void setValid_rate(String valid_rate) {
                this.valid_rate = valid_rate;
            }

            public int getValid_cnt() {
                return valid_cnt;
            }

            public void setValid_cnt(int valid_cnt) {
                this.valid_cnt = valid_cnt;
            }
        }
    }
}
