package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
@Table(name = "form_code1")
public class FormCodeVo implements Serializable {

    @Id(column = "dbid")
    private int dbid;

    private String type_id;
    private String data_id;
    private String data_name;
    private String pdata_id;

    public String getPdata_id() {
        return pdata_id;
    }

    public void setPdata_id(String pdata_id) {
        this.pdata_id = pdata_id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }
}
