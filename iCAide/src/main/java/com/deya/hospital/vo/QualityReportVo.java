package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/17
 */
public class QualityReportVo implements Serializable{

    /**
     * result_msg : 成功
     * result_id : 0
     * pageTotal : 2
     * totalcnt : 7
     * list : [{"item_repo_id":1,"item_title":"1 床头抬高30度","item_type":1,"create_user":368,"hospital_id":6140,"item_detail":null,"item_desc":null,"item_class":null,"state":1,"ext1":null,"ext2":null,"dataList":[{"item_repo_id":1,"check_type":10,"average_score":"75.00","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":1,"check_type":20,"average_score":"75.00","cnt":1,"checkTypeName":"自查"}]},{"item_repo_id":2,"item_title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","item_type":2,"create_user":368,"hospital_id":6140,"item_detail":"评分细则","item_desc":"考评方法","item_class":"指标分类","state":1,"ext1":null,"ext2":null,"dataList":[{"item_repo_id":2,"check_type":10,"average_score":"50.00","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":2,"check_type":20,"average_score":"50.00","cnt":1,"checkTypeName":"自查"}]},{"item_repo_id":3,"item_title":"评估是否合格","item_type":3,"create_user":368,"hospital_id":6140,"item_detail":"评分细则","item_desc":"考评方法","item_class":"指标分类","state":1,"ext1":null,"ext2":null,"dataList":[{"item_repo_id":3,"check_type":10,"average_score":"25.00","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":3,"check_type":20,"average_score":"15.00","cnt":1,"checkTypeName":"自查"}]},{"item_repo_id":4,"item_title":"进行插管及护理前严格执行无菌操作规程，认真执行手卫生制度","item_type":2,"create_user":368,"hospital_id":6140,"item_detail":"评分细则","item_desc":"考评方法","item_class":"指标分类","state":1,"ext1":null,"ext2":null,"dataList":[{"item_repo_id":4,"check_type":10,"average_score":"100","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":4,"check_type":20,"average_score":"0.00","cnt":1,"checkTypeName":"自查"}]},{"item_repo_id":5,"item_title":"单间隔离或同耐药菌安置同一病室并悬挂相应隔标识","item_type":3,"create_user":368,"hospital_id":6140,"item_detail":"评分细则","item_desc":"考评方法","item_class":"指标分类","state":1,"ext1":null,"ext2":null,"dataList":[{"item_repo_id":5,"check_type":10,"average_score":"75.00","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":5,"check_type":20,"average_score":"75.00","cnt":1,"checkTypeName":"自查"}]}]
     * pageIndex : 1
     */

    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int totalcnt;
    private int pageIndex;
    /**
     * item_repo_id : 1
     * item_title : 1 床头抬高30度
     * item_type : 1
     * create_user : 368
     * hospital_id : 6140
     * item_detail : null
     * item_desc : null
     * item_class : null
     * state : 1
     * ext1 : null
     * ext2 : null
     * dataList : [{"item_repo_id":1,"check_type":10,"average_score":"75.00","cnt":9,"checkTypeName":"抽查"},{"item_repo_id":1,"check_type":20,"average_score":"75.00","cnt":1,"checkTypeName":"自查"}]
     */

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private int item_repo_id;
        private String item_title;
        private int item_type;
        private int create_user;
        private int hospital_id;
        private Object item_detail;
        private Object item_desc;
        private Object item_class;
        private int state;
        private Object ext1;
        private Object ext2;
        private  int temp_id;
        private int departmentId;
        private String departmentName="";
        private String tempName="";
        private String score="";

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTempName() {
            return tempName;
        }

        public void setTempName(String tempName) {
            this.tempName = tempName;
        }

        public int getTemp_id() {
            return temp_id;
        }

        public void setTemp_id(int temp_id) {
            this.temp_id = temp_id;
        }

        public int getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(int departmentId) {
            this.departmentId = departmentId;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        /**
         * item_repo_id : 1
         * check_type : 10
         * average_score : 75.00
         * cnt : 9
         * checkTypeName : 抽查
         */

        private List<DataListBean> dataList;

        public int getItem_repo_id() {
            return item_repo_id;
        }

        public void setItem_repo_id(int item_repo_id) {
            this.item_repo_id = item_repo_id;
        }

        public String getItem_title() {
            return item_title;
        }

        public void setItem_title(String item_title) {
            this.item_title = item_title;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public int getCreate_user() {
            return create_user;
        }

        public void setCreate_user(int create_user) {
            this.create_user = create_user;
        }

        public int getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(int hospital_id) {
            this.hospital_id = hospital_id;
        }

        public Object getItem_detail() {
            return item_detail;
        }

        public void setItem_detail(Object item_detail) {
            this.item_detail = item_detail;
        }

        public Object getItem_desc() {
            return item_desc;
        }

        public void setItem_desc(Object item_desc) {
            this.item_desc = item_desc;
        }

        public Object getItem_class() {
            return item_class;
        }

        public void setItem_class(Object item_class) {
            this.item_class = item_class;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Object getExt1() {
            return ext1;
        }

        public void setExt1(Object ext1) {
            this.ext1 = ext1;
        }

        public Object getExt2() {
            return ext2;
        }

        public void setExt2(Object ext2) {
            this.ext2 = ext2;
        }

        public List<DataListBean> getDataList() {
            return dataList==null?new ArrayList<DataListBean>():dataList;
        }

        public void setDataList(List<DataListBean> dataList) {
            this.dataList = dataList;
        }

        public static class DataListBean implements Serializable {
            private int item_repo_id;
            private int check_type;
            private String average_score="";
            private int cnt;
            private String checkTypeName="";

            public int getItem_repo_id() {
                return item_repo_id;
            }

            public void setItem_repo_id(int item_repo_id) {
                this.item_repo_id = item_repo_id;
            }

            public int getCheck_type() {
                return check_type;
            }

            public void setCheck_type(int check_type) {
                this.check_type = check_type;
            }

            public String getAverage_score() {
                return average_score;
            }

            public void setAverage_score(String average_score) {
                this.average_score = average_score;
            }

            public int getCnt() {
                return cnt;
            }

            public void setCnt(int cnt) {
                this.cnt = cnt;
            }

            public String getCheckTypeName() {
                return checkTypeName;
            }

            public void setCheckTypeName(String checkTypeName) {
                this.checkTypeName = checkTypeName;
            }
        }
    }
}
