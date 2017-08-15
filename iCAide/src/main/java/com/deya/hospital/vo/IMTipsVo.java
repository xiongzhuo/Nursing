package com.deya.hospital.vo;

import java.io.Serializable;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class IMTipsVo implements Serializable {
    private String  msgContent;
    private String id;
    private int  type;
    private String create_time;
    private UserInfoVo data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int  type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public UserInfoVo getData() {
        return data;
    }

    public void setData(UserInfoVo data) {
        this.data = data;
    }


}