package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
@Table(name = "ShareCodeVo")
public class ShareCodeVo {


    /**
     * type_id : SHARE_URL_LIST
     * data_id : 1
     * data_name : comm/tempTaskShare
     * pdata_id : 0
     */
    @Id(column = "id")
    private int id;
    private String type_id;
    private String data_id;
    private String data_name;
    private int pdata_id;

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

    public int  getPdata_id() {
        return pdata_id;
    }

    public void setPdata_id(int  pdata_id) {
        this.pdata_id = pdata_id;
    }
}

