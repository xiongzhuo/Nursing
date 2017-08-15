package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *  文章详情
 * @author : yugq
 * @date 2016/7/6
 */
public class AritcleEntity {
    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int totalcnt;
    private int pageIndex;
    private InfoBean info;

    private List<CommentlistBean> commentlist;

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

    public int getTotalcnt() {
        return totalcnt;
    }

    public void setTotalcnt(int totalcnt) {
        this.totalcnt = totalcnt;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<CommentlistBean> getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(List<CommentlistBean> commentlist) {
        this.commentlist = commentlist;
    }

    public static class InfoBean {
        private int id;
        private int is_publish;
        private String short_title;
        private String title;
        private int channel_id;
        private int is_recommend;
        private String labels;
        private String abstracts;
        private int expert_id;
        private int list_type;
        private String content;
        private String expert_name;
        private int like_count;
        private int comment_count;
        private int attachment_count;
        private String channel_name;
        private String create_time;
        private String update_time;
        private String revision_time;
        private String execute_time;
        private int is_pdf;
        private String pdf_attach;
        private String pdf_name;
        private String drafting_unit;
        private String draftman;
        private String issuing_unit;
        private String pdf_size;
        private int read_count;

        public String getPdf_size() {
            return pdf_size;
        }

        public void setPdf_size(String pdf_size) {
            this.pdf_size = pdf_size;
        }

        public int getRead_count() {
            return read_count;
        }

        public void setRead_count(int read_count) {
            this.read_count = read_count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIs_publish() {
            return is_publish;
        }

        public void setIs_publish(int is_publish) {
            this.is_publish = is_publish;
        }

        public String getShort_title() {
            return short_title;
        }

        public void setShort_title(String short_title) {
            this.short_title = short_title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(int channel_id) {
            this.channel_id = channel_id;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getLabels() {
            return labels;
        }

        public void setLabels(String labels) {
            this.labels = labels;
        }

        public String getAbstracts() {
            return abstracts;
        }

        public void setAbstracts(String abstracts) {
            this.abstracts = abstracts;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getExpert_name() {
            return expert_name;
        }

        public void setExpert_name(String expert_name) {
            this.expert_name = expert_name;
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

        public int getAttachment_count() {
            return attachment_count;
        }

        public void setAttachment_count(int attachment_count) {
            this.attachment_count = attachment_count;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
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

        public String getRevision_time() {
            return revision_time;
        }

        public void setRevision_time(String revision_time) {
            this.revision_time = revision_time;
        }

        public String getExecute_time() {
            return execute_time;
        }

        public void setExecute_time(String execute_time) {
            this.execute_time = execute_time;
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

        public String getPdf_name() {
            return pdf_name;
        }

        public void setPdf_name(String pdf_name) {
            this.pdf_name = pdf_name;
        }

        public String getDrafting_unit() {
            return drafting_unit;
        }

        public void setDrafting_unit(String drafting_unit) {
            this.drafting_unit = drafting_unit;
        }

        public String getDraftman() {
            return draftman;
        }

        public void setDraftman(String draftman) {
            this.draftman = draftman;
        }

        public String getIssuing_unit() {
            return issuing_unit;
        }

        public void setIssuing_unit(String issuing_unit) {
            this.issuing_unit = issuing_unit;
        }
    }

    public static class CommentlistBean {
        private int id;
        private String content;
        private String name;
        private String avatar;
        private String update_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
