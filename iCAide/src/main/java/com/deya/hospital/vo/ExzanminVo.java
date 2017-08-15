package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/25
 */
public class ExzanminVo implements Serializable{

    /**
     * result_msg : 获取试卷列表成功!
     * result_id : 0
     * list : [{"id":18,"title":"手卫生测试","business_type":"21","test_type":null,"max_score":100,"pass_score":60,"mins":20,"subject_count":1},{"id":20,"title":"耐药菌感染防控","business_type":"32","test_type":1,"max_score":100,"pass_score":60,"mins":20,"subject_count":6},{"id":21,"title":"手卫生","business_type":"","test_type":1,"max_score":100,"pass_score":60,"mins":10,"subject_count":3},{"id":56,"title":"公开课测试题：ICU三管患者的精准化感控","business_type":"25","test_type":2,"max_score":100,"pass_score":60,"mins":5,"subject_count":8}]
     */

    private String result_msg;
    private String result_id;
    /**
     * id : 18
     * title : 手卫生测试
     * business_type : 21
     * test_type : null
     * max_score : 100
     * pass_score : 60
     * mins : 20
     * subject_count : 1
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
        private String title;
        private String business_type;
        private Object test_type;
        private int max_score;
        private int pass_score;
        private int mins;
        private int subject_count;
        private int score;
        private int rightNum;
        private int wrongNum;

        public int getRightNum() {
            return rightNum;
        }

        public void setRightNum(int rightNum) {
            this.rightNum = rightNum;
        }

        public int getWrongNum() {
            return wrongNum;
        }

        public void setWrongNum(int wrongNum) {
            this.wrongNum = wrongNum;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

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

        public String getBusiness_type() {
            return business_type;
        }

        public void setBusiness_type(String business_type) {
            this.business_type = business_type;
        }

        public Object getTest_type() {
            return test_type;
        }

        public void setTest_type(Object test_type) {
            this.test_type = test_type;
        }

        public int getMax_score() {
            return max_score;
        }

        public void setMax_score(int max_score) {
            this.max_score = max_score;
        }

        public int getPass_score() {
            return pass_score;
        }

        public void setPass_score(int pass_score) {
            this.pass_score = pass_score;
        }

        public int getMins() {
            return mins;
        }

        public void setMins(int mins) {
            this.mins = mins;
        }

        public int getSubject_count() {
            return subject_count;
        }

        public void setSubject_count(int subject_count) {
            this.subject_count = subject_count;
        }
    }
}
