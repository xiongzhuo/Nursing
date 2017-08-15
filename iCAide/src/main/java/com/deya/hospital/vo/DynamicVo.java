package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class DynamicVo {

    /**
     * result_msg : 获取数据成功
     * result_id : 0
     * is_access_data : 1
     * list : [{"num":0,"count":"0","name":"住院人数","type":0},{"num":0,"count":"0","name":"疑似感染人数","type":1},{"num":0,"count":"0","name":"检出多耐人数","type":2},{"num":3,"count":"3","name":"三管监测","type":3},{"num":37,"count":"37","name":"任务数","type":4},{"num":0,"count":"0","name":"工作提醒","type":5}]
     */

    private String result_msg;
    private String result_id;
    private int is_access_data;
    /**
     * num : 0
     * count : 0
     * name : 住院人数
     * type : 0
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

    public int getIs_access_data() {
        return is_access_data;
    }

    public void setIs_access_data(int is_access_data) {
        this.is_access_data = is_access_data;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private int num;
        private String count;
        private String name;
        private int type;
        private String date;
        private String code="";
        private int  state;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

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

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
