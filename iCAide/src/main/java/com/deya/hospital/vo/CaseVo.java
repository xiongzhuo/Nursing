package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
@Table(name = "tab_case")
public class CaseVo {

    /**
     * authent : xxxxxx
     * department_id :
     * bed_no :
     * patient_name :
     * patient_id :
     * time :
     * check_ids :
     * drug_ids :
     */
    @Id(column = "dbid")
    private int dbid;
    private String authent;
    private String department_id;
    private String bed_no;
    private String patient_name;
    private String patient_id;
    private String time;
    private String check_ids="";
    private String drug_ids="";

    public String getAuthent() {
        return authent;
    }

    public void setAuthent(String authent) {
        this.authent = authent;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getBed_no() {
        return bed_no;
    }

    public void setBed_no(String bed_no) {
        this.bed_no = bed_no;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCheck_ids() {
        return check_ids;
    }

    public void setCheck_ids(String check_ids) {
        this.check_ids = check_ids;
    }

    public String getDrug_ids() {
        return drug_ids;
    }

    public void setDrug_ids(String drug_ids) {
        this.drug_ids = drug_ids;
    }
}
