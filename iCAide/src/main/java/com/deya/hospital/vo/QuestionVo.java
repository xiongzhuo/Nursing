package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.Attachments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 9389012830671L;
    String uid;
    int a_id;
    AnswerVo answer;
    int answer_count;
    int is_niming;
    String content;
    String user_name;
    int q_id;
    String title;
    String update_time;
    int attachment_count;
    String create_time;
    String type_name;
    int q_type;
    int is_recommend;
    int type;//1：提问 2：回答
    String my_type;
    int integral;
    int state;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getA_id() {
        return a_id;
    }

    public void setA_id(int a_id) {
        this.a_id = a_id;
    }

    public String getMy_type() {
        return my_type;
    }

    public void setMy_type(String my_type) {
        this.my_type = my_type;
    }

    List<Attachments> q_attachments = new ArrayList<Attachments>();

    public List<Attachments> getQ_attachments() {
        return q_attachments;
    }

    public void setQ_attachments(List<Attachments> q_attachments) {
        this.q_attachments = q_attachments;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AnswerVo getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerVo answer) {
        this.answer = answer;
    }

    public int getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(int answer_count) {
        this.answer_count = answer_count;
    }

    public int getIs_niming() {
        return is_niming;
    }

    public void setIs_niming(int is_niming) {
        this.is_niming = is_niming;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getQ_id() {
        return q_id;
    }

    public void setQ_id(int q_id) {
        this.q_id = q_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getAttachment_count() {
        return attachment_count;
    }

    public void setAttachment_count(int attachment_count) {
        this.attachment_count = attachment_count;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getQ_type() {
        return q_type;
    }

    public void setQ_type(int q_type) {
        this.q_type = q_type;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

}
