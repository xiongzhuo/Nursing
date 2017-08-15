package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class ClassifyExciseVo {

    /**
     * result_id : 0
     * result_msg : 获取分类列表成功!
     * list : [{"count":89,"cateName":"传染病防控","cateId":"35"},{"count":27,"cateName":"其它","cateId":"22"},{"count":70,"cateName":"医疗废物","cateId":"28"},{"count":34,"cateName":"医院感染暴发流行处置","cateId":"16"},{"count":2,"cateName":"医院感染流行病学","cateId":"15"},{"count":60,"cateName":"医院感染病例判断与病例检测","cateId":"12"},{"count":66,"cateName":"医院感染管理学与工具","cateId":"33"},{"count":7,"cateName":"医院感染统计学","cateId":"34"},{"count":284,"cateName":"医院消毒药械管理","cateId":"13"},{"count":47,"cateName":"医院环境清洁与消毒","cateId":"26"},{"count":51,"cateName":"安全注射","cateId":"30"},{"count":33,"cateName":"微生物检测","cateId":"24"},{"count":40,"cateName":"手卫生","cateId":"21"},{"count":52,"cateName":"抗菌药物临床应用管理","cateId":"23"},{"count":1,"cateName":"污水管理","cateId":"29"},{"count":9,"cateName":"生物安全","cateId":"20"},{"count":34,"cateName":"耐药菌感染防控","cateId":"32"},{"count":51,"cateName":"职业暴露与防护","cateId":"19"},{"count":43,"cateName":"重点部位医院感染管理","cateId":"36"},{"count":125,"cateName":"重点部门医院感染管理","cateId":"25"},{"count":50,"cateName":"隔离技术","cateId":"18"}]
     */

    private String result_id;
    private String result_msg;
    /**
     * count : 89
     * cateName : 传染病防控
     * cateId : 35
     */

    private List<ListBean> list;

    public String getResult_id() {
        return result_id;
    }

    public void setResult_id(String result_id) {
        this.result_id = result_id;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }
    @Table(name = "hospital_knowledge_classify1")
    public static class ListBean implements Serializable{
        @Id(column = "dbid")
        private int dbid;
        private int count;
        private String cateName;
        private String cateId;
        List<KnowledgeVo.ListBean> items;

        public List<KnowledgeVo.ListBean> getItems() {
            return items;
        }

        public void setItems(List<KnowledgeVo.ListBean> items) {
            this.items = items;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getCateId() {
            return cateId;
        }

        public void setCateId(String cateId) {
            this.cateId = cateId;
        }
    }
}
