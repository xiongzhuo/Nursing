package com.deya.hospital.vo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : yugq
 * @date 2016/8/23
 */
public class WorkCircleMainEntity {

    private String result_msg;
    private String result_id;
    private List<QuestionListBean> questionList;

    private List<TopImageListBean> topImageList;

    private List<CenterImageListBean> centerImageList;
    private List<ConvertListBean> convertList;

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

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

    public List<TopImageListBean> getTopImageList() {
        return topImageList;
    }

    public void setTopImageList(List<TopImageListBean> topImageList) {
        this.topImageList = topImageList;
    }

    public List<CenterImageListBean> getCenterImageList() {
        return centerImageList;
    }

    public void setCenterImageList(List<CenterImageListBean> centerImageList) {
        this.centerImageList = centerImageList;
    }

    public List<ConvertListBean> getConvertList() {
        return convertList;
    }

    public void setConvertList(List<ConvertListBean> convertList) {
        this.convertList = convertList;
    }

    public static class QuestionListBean {
        private int q_id;
        private String title;
        private int a_id;
        private int integral;

        public int getQ_id() {
            return q_id;
        }

        public void setQ_id(int q_id) {
            this.q_id = q_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getA_id() {
            return a_id;
        }

        public void setA_id(int a_id) {
            this.a_id = a_id;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }
    }

    public static class TopImageListBean {
        private int id;
        private String name;
        private String explains;
        private int business_id;
        private int type;
        private String href_url;
        /**
         * id : 0
         * name :
         */

        private DataBean data;

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

        public String getExplains() {
            return explains;
        }

        public void setExplains(String explains) {
            this.explains = explains;
        }

        public int getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(int business_id) {
            this.business_id = business_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getHref_url() {
            return href_url;
        }

        public void setHref_url(String href_url) {
            this.href_url = href_url;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class CenterImageListBean {
        private int id;
        private String name;
        private String explains;
        private int business_id;
        private int type;
        private String href_url;
        /**
         * id : 0
         * name :
         */

        private DataBean data;

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

        public String getExplains() {
            return explains;
        }

        public void setExplains(String explains) {
            this.explains = explains;
        }

        public int getBusiness_id() {
            return business_id;
        }

        public void setBusiness_id(int business_id) {
            this.business_id = business_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getHref_url() {
            return href_url;
        }

        public void setHref_url(String href_url) {
            this.href_url = href_url;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class ConvertListBean {
        private String str;

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }
}
