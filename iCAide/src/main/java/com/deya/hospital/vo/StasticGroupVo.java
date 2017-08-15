package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/9/20
 */
public class StasticGroupVo implements Serializable {
    private List<HotVo> list;
    private String title;
    private int imgId;

    public List<HotVo> getList() {
        if(null==list){
            list=new ArrayList<>();
        }
        return list;
    }

    public void setList(List<HotVo> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
