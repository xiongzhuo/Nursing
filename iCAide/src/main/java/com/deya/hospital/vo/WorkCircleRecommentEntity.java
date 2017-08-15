package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/8/5
 */
public class
WorkCircleRecommentEntity {

    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int pageIndex;

    private List<ListBean> list;

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

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private int list_type;
        private String channel_name;
        private String title;
        private int is_pdf;
        private String pdf_attach;
        private String expert_name;
        private String expert_post;
        private int comment_count;
        private int like_count;
        private int read_count;
        private List<AttachmentBean> attachment;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getList_type() {
            return list_type;
        }

        public void setList_type(int list_type) {
            this.list_type = list_type;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getRead_count() {
            return read_count;
        }

        public void setRead_count(int read_count) {
            this.read_count = read_count;
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