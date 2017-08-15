package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ResistantMutiTextVo implements Serializable{

    /**
     * update : true
     * result_msg : 获取字典列表成功
     * result_id : 0
     * resultList : [{"type_id":"CHECK_SPECIMEN","data_id":"1","data_name":"血液"},{"type_id":"CHECK_SPECIMEN","data_id":"2","data_name":"脑脊液"},{"type_id":"CHECK_SPECIMEN","data_id":"3","data_name":"痰液"},{"type_id":"CHECK_SPECIMEN","data_id":"4","data_name":"尿液"},{"type_id":"CHECK_SPECIMEN","data_id":"5","data_name":"其他"}]
     * maxTime : 1469526424000
     */

    private boolean update;
    private String result_msg;
    private int result_id;
    private String maxTime;
    /**
     * type_id : CHECK_SPECIMEN
     * data_id : 1
     * data_name : 血液
     */

    private List<ResultListBean> resultList;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public int getResult_id() {
        return result_id;
    }

    public void setResult_id(int result_id) {
        this.result_id = result_id;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ResultListBean implements Serializable{
        private String type_id;
        private int data_id;
        private String data_name;

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public int getData_id() {
            return data_id;
        }

        public void setData_id(int data_id) {
            this.data_id = data_id;
        }

        public String getData_name() {
            return data_name;
        }

        public void setData_name(String data_name) {
            this.data_name = data_name;
        }
    }
}
