package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/13
 */
public class NeedKnowVo implements Serializable{

    /**
     * result_msg : 查询文章信息成功
     * result_id : 0
     * docList : [{"id":857,"topic":"ICU建设与管理学习","author":null,"degest":"ICU建设与管理学习","create_time":"2016-11-10 15:59","read_count":35,"top_pic":"ca246084cc05dc9fbda6e27d6efa0ef5","revision_time":null,"execution_time":null,"drafting_unit":"","drafting_person":"","publish_unit":"","kind":null,"pdf_attach":"5bce89bddb9b5430acbbb57026fa040c"},{"id":821,"topic":"JCI认证中的医院感染预防与控制","author":null,"degest":"JCI认证中的医院感染预防与控制","create_time":"2016-05-30 10:04","read_count":19,"top_pic":"","revision_time":null,"execution_time":null,"drafting_unit":"","drafting_person":"","publish_unit":"","kind":119,"pdf_attach":"4b6809fe779def2ce0524812f238c5ee"},{"id":820,"topic":"执行手卫生的原因、方法和时间手册","author":null,"degest":"执行手卫生的原因、方法和时间手册","create_time":"2016-05-30 09:53","read_count":25,"top_pic":"f06dd75358150603bfccb1584559b7c0","revision_time":null,"execution_time":null,"drafting_unit":"","drafting_person":"","publish_unit":"","kind":117,"pdf_attach":"651e958d2ca77b339d5d30fa3330dc4c"},{"id":819,"topic":"PCI-001中南大学湘雅国际医疗部医院感染管理制度","author":null,"degest":"本感染预防/控制计划的目标是建立一个协调的操作流程，通过以下途径来保护患者和医务人员，并确保医疗体系优化运行。","create_time":"2016-05-30 09:48","read_count":51,"top_pic":"33a24f1b41562c29d86474b35c708c19","revision_time":null,"execution_time":null,"drafting_unit":"","drafting_person":"","publish_unit":"","kind":115,"pdf_attach":"faba801d8e7f629e8c2f3b7f2d1a3229"},{"id":818,"topic":"IPSG.5-001国际医疗部手卫生管理制度","author":null,"degest":"本制度旨在预防和减少湘雅医院国际医疗部(XIMC) 医院感染（HAI）的发生。手卫生（洗手）是避免病原体传播和减少HAI最简单有效的方法。","create_time":"2016-05-30 09:44","read_count":20,"top_pic":"5f9a8853401b46fda0b44b016a192b00","revision_time":null,"execution_time":null,"drafting_unit":"","drafting_person":"","publish_unit":"","kind":115,"pdf_attach":"b30319100bcd36238a7b1b5c1ccbb88d"}]
     * totalCnts : 5
     * totalPages : 1
     */

    private String result_msg;
    private String result_id;
    private int totalCnts;
    private int totalPages;
    /**
     * id : 857
     * topic : ICU建设与管理学习
     * author : null
     * degest : ICU建设与管理学习
     * create_time : 2016-11-10 15:59
     * read_count : 35
     * top_pic : ca246084cc05dc9fbda6e27d6efa0ef5
     * revision_time : null
     * execution_time : null
     * drafting_unit :
     * drafting_person :
     * publish_unit :
     * kind : null
     * pdf_attach : 5bce89bddb9b5430acbbb57026fa040c
     */

    private List<DocListBean> docList;

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

    public int getTotalCnts() {
        return totalCnts;
    }

    public void setTotalCnts(int totalCnts) {
        this.totalCnts = totalCnts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<DocListBean> getDocList() {
        return docList;
    }

    public void setDocList(List<DocListBean> docList) {
        this.docList = docList;
    }

    public static class DocListBean {
        private int id;
        private String topic;
        private Object author;
        private String degest;
        private String create_time;
        private int read_count;
        private String top_pic;
        private Object revision_time;
        private Object execution_time;
        private String drafting_unit;
        private String drafting_person;
        private String publish_unit;
        private Object kind;
        private String pdf_attach;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Object getAuthor() {
            return author;
        }

        public void setAuthor(Object author) {
            this.author = author;
        }

        public String getDegest() {
            return degest;
        }

        public void setDegest(String degest) {
            this.degest = degest;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getRead_count() {
            return read_count;
        }

        public void setRead_count(int read_count) {
            this.read_count = read_count;
        }

        public String getTop_pic() {
            return top_pic;
        }

        public void setTop_pic(String top_pic) {
            this.top_pic = top_pic;
        }

        public Object getRevision_time() {
            return revision_time;
        }

        public void setRevision_time(Object revision_time) {
            this.revision_time = revision_time;
        }

        public Object getExecution_time() {
            return execution_time;
        }

        public void setExecution_time(Object execution_time) {
            this.execution_time = execution_time;
        }

        public String getDrafting_unit() {
            return drafting_unit;
        }

        public void setDrafting_unit(String drafting_unit) {
            this.drafting_unit = drafting_unit;
        }

        public String getDrafting_person() {
            return drafting_person;
        }

        public void setDrafting_person(String drafting_person) {
            this.drafting_person = drafting_person;
        }

        public String getPublish_unit() {
            return publish_unit;
        }

        public void setPublish_unit(String publish_unit) {
            this.publish_unit = publish_unit;
        }

        public Object getKind() {
            return kind;
        }

        public void setKind(Object kind) {
            this.kind = kind;
        }

        public String getPdf_attach() {
            return pdf_attach;
        }

        public void setPdf_attach(String pdf_attach) {
            this.pdf_attach = pdf_attach;
        }
    }
}
