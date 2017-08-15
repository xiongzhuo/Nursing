package com.deya.hospital.vo;

import java.io.Serializable;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class UserInfoVo implements Serializable{
    private int id;
    private String  department;
    private String name="";
    private String username="";
    private String mobile;
    private String phone="";
    private int state;
    private int hospital;
    private int post;
    private String sex;
    private String regis_job;
    private String avatar;
    private int integral;
    private int in_job;
    private int role_job;
    private String email;
    private String invitation_code;
    private String certify_photo;
    private String is_sign_old;
    private String departmentName;
    private String regis_job_name;
    private String hospital_name;
    private int is_sign;
    private String auth_name;
    private String auth_code;
    private String auth_valid_date;
    private int auth_status;
    private String user_id;
    private String a_note="";

    public String getA_note() {
        return a_note;
    }

    public void setA_note(String a_note) {
        this.a_note = a_note;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String  department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getHospital() {
        return hospital;
    }

    public void setHospital(int hospital) {
        this.hospital = hospital;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRegis_job() {
        return regis_job;
    }

    public void setRegis_job(String regis_job) {
        this.regis_job = regis_job;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getIn_job() {
        return in_job;
    }

    public void setIn_job(int in_job) {
        this.in_job = in_job;
    }

    public int getRole_job() {
        return role_job;
    }

    public void setRole_job(int role_job) {
        this.role_job = role_job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getCertify_photo() {
        return certify_photo;
    }

    public void setCertify_photo(String certify_photo) {
        this.certify_photo = certify_photo;
    }

    public String getIs_sign_old() {
        return is_sign_old;
    }

    public void setIs_sign_old(String is_sign_old) {
        this.is_sign_old = is_sign_old;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRegis_job_name() {
        return regis_job_name;
    }

    public void setRegis_job_name(String regis_job_name) {
        this.regis_job_name = regis_job_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public int getIs_sign() {
        return is_sign;
    }

    public void setIs_sign(int is_sign) {
        this.is_sign = is_sign;
    }

    public String getAuth_name() {
        return auth_name;
    }

    public void setAuth_name(String auth_name) {
        this.auth_name = auth_name;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getAuth_valid_date() {
        return auth_valid_date;
    }

    public void setAuth_valid_date(String auth_valid_date) {
        this.auth_valid_date = auth_valid_date;
    }

    public int getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(int auth_status) {
        this.auth_status = auth_status;
    }
}
