package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/17
 */
public class QuaLityDepartReportVo {

    /**
     * result_msg : 成功
     * result_id : 0
     * pageTotal : 9
     * totalcnt : 41
     * list : [{"departmentId":209170,"departmentName":"痔瘘外科","tempList":[{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]},{"departmentId":209192,"departmentName":"临床功能检查","tempList":[{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]},{"departmentId":239836,"departmentName":"3333","tempList":[{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]},{"departmentId":209171,"departmentName":"心胸外科","tempList":[{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]},{"departmentId":209193,"departmentName":"营养科","tempList":[{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]}]
     * pageIndex : 1
     */

    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int totalcnt;
    private int pageIndex;
    /**
     * departmentId : 209170
     * departmentName : 痔瘘外科
     * tempList : [{"dataList":[{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}],"tempName":"临床质控模板2","temp_id":32}]
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int departmentId;
        private String departmentName;
        private  int temp_id;
        private String tempName="";

        public int getTemp_id() {
            return temp_id;
        }

        public void setTemp_id(int temp_id) {
            this.temp_id = temp_id;
        }

        public String getTempName() {
            return tempName;
        }

        public void setTempName(String tempName) {
            this.tempName = tempName;
        }

        /**
         * dataList : [{"check_type":"10","checkTypeName":"抽查","cnt":0,"average_score":0},{"check_type":"20","checkTypeName":"自查","cnt":0,"average_score":0}]
         * tempName : 临床质控模板2
         * temp_id : 32
         */

        private List<QualityReportVo.ListBean> tempList;

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

        public List<QualityReportVo.ListBean> getTempList() {
            return tempList;
        }

        public void setTempList(List<QualityReportVo.ListBean> tempList) {
            this.tempList = tempList;
        }


    }
}
