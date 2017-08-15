package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/7/7
 */
public class OranizeEntity {
    private String result_msg;
    private String result_id;
    private InfoBean info;
    private List<ArticlelistBean> articlelist;

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

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ArticlelistBean> getArticlelist() {
        return articlelist;
    }

    public void setArticlelist(List<ArticlelistBean> articlelist) {
        this.articlelist = articlelist;
    }

    public static class InfoBean {
        private int id;
        private String name;
        private String company;
        private int sort;
        private String introduce;
        private String avatar;
        private String create_time;
        private String update_time;
        private int article_count;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getArticle_count() {
            return article_count;
        }

        public void setArticle_count(int article_count) {
            this.article_count = article_count;
        }
    }

    public static class ArticlelistBean {
        private int id;
        private String title;
        private int is_recommend;
        private String user_name;
        private int is_publish;
        private int channel_id;
        private int expert_id;
        private int list_type;// 列表类型  0：纯文本 1：单图  3：三张图
        private int like_count;
        private int comment_count;
        private int is_pdf;
        private String pdf_attach;
        private String channel_name;
        private String expert_name;
        private String expert_post;
        private String create_time;

        private List<AttachmentBean> attachment;

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

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(int is_publish) {
            this.is_publish = is_publish;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public int getExpert_id() {
            return expert_id;
        }

        public void setExpert_id(int expert_id) {
            this.expert_id = expert_id;
        }

        public int getList_type() {
            return list_type;
        }

        public void setList_type(int list_type) {
            this.list_type = list_type;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getIs_pdf() {
            return is_pdf;
        }

        public void setIs_pdf(int is_pdf) {
            this.is_pdf = is_pdf;
        }

        public String getPdf_attach() {
            return pdf_attach;
        }

        public void setPdf_attach(String pdf_attach) {
            this.pdf_attach = pdf_attach;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public String getExpert_name() {
            return expert_name;
        }

        public void setExpert_name(String expert_name) {
            this.expert_name = expert_name;
        }

        public String getExpert_post() {
            return expert_post;
        }

        public void setExpert_post(String expert_post) {
            this.expert_post = expert_post;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public List<AttachmentBean> getAttachment() {
            return attachment;
        }

        public void setAttachment(List<AttachmentBean> attachment) {
            this.attachment = attachment;
        }

        public static class AttachmentBean {
            private String file_name;
            private int file_type;

            public String getFile_name() {
                return file_name;
            }

            public void setFile_name(String file_name) {
                this.file_name = file_name;
            }

            public int getFile_type() {
                return file_type;
            }

            public void setFile_type(int file_type) {
                this.file_type = file_type;
            }
        }
    }
}
