package com.deya.hospital.vo;

import com.deya.hospital.vo.dbdata.Attachments;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/17
 */
public class HandTimesVo implements Serializable{

    /**
     * result_msg : 成功
     * result_id : 0
     * pageTotal : 2
     * timesList : [{"forder":1,"id":587364,"pname":"A","unrules":"","mission_time":"2017-01-12 09:54","postname":"实习生","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"无菌操作前","col_type":"2","results":"洗手","is_result":"是","right_result":"是"},{"forder":2,"id":587365,"pname":"A","unrules":"","mission_time":"2017-01-12 09:54","postname":"实习生","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"卫生手消毒不规范","is_result":"是","right_result":"否"},{"forder":3,"id":587366,"pname":"B","unrules":"","mission_time":"2017-01-12 09:54","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"卫生手消毒不规范","is_result":"是","right_result":"否"},{"forder":4,"id":587367,"pname":"B","unrules":"","mission_time":"2017-01-12 09:54","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"卫生手消毒","is_result":"是","right_result":"是"},{"forder":5,"id":587368,"pname":"B","unrules":"","mission_time":"2017-01-12 09:54","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触患者后","col_type":"1","results":"未采取措施","is_result":"否","right_result":""},{"forder":6,"id":587358,"pname":"A","unrules":"","mission_time":"2017-01-12 09:36","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"卫生手消毒不规范","is_result":"是","right_result":"否"},{"forder":7,"id":587359,"pname":"A","unrules":"","mission_time":"2017-01-12 09:36","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"未采取措施","is_result":"否","right_result":""},{"forder":8,"id":587360,"pname":"A","unrules":"","mission_time":"2017-01-12 09:36","postname":"后勤","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"无菌操作前","col_type":"2","results":"洗手不规范","is_result":"是","right_result":"否"},{"forder":9,"id":587361,"pname":"A","unrules":"","mission_time":"2017-01-12 09:36","postname":"实习生","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"接触血液体液后","col_type":"3","results":"卫生手消毒不规范","is_result":"是","right_result":"否"},{"forder":10,"id":587362,"pname":"A","unrules":"","mission_time":"2017-01-12 09:36","postname":"实习生","departmentName":null,"username":"与闻","check_type":20,"col_type_name":"无菌操作前","col_type":"2","results":"卫生手消毒","is_result":"是","right_result":"是"}]
     * totalcnt : 11
     * attachmentList : []
     * pageIndex : 1
     */

    private String result_msg;
    private String result_id;
    private int pageTotal;
    private int totalcnt;
    private int pageIndex;
    /**
     * forder : 1
     * id : 587364
     * pname : A
     * unrules :
     * mission_time : 2017-01-12 09:54
     * postname : 实习生
     * departmentName : null
     * username : 与闻
     * check_type : 20
     * col_type_name : 无菌操作前
     * col_type : 2
     * results : 洗手
     * is_result : 是
     * right_result : 是
     */

    private List<TimesListBean> timesList;
    private List<Attachments> attachmentList;

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

    public List<TimesListBean> getTimesList() {
        return timesList;
    }

    public void setTimesList(List<TimesListBean> timesList) {
        this.timesList = timesList;
    }

    public List<Attachments> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachments> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public static class TimesListBean {
        private int forder;
        private int id;
        private String pname;
        private String unrules;
        private String mission_time;
        private String postname;
        private Object departmentName;
        private String username;
        private int check_type;
        private String col_type_name;
        private String col_type;
        private String results;
        private String is_result;
        private String right_result;
        private  int uid;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getForder() {
            return forder;
        }

        public void setForder(int forder) {
            this.forder = forder;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getUnrules() {
            return unrules;
        }

        public void setUnrules(String unrules) {
            this.unrules = unrules;
        }

        public String getMission_time() {
            return mission_time;
        }

        public void setMission_time(String mission_time) {
            this.mission_time = mission_time;
        }

        public String getPostname() {
            return postname;
        }

        public void setPostname(String postname) {
            this.postname = postname;
        }

        public Object getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(Object departmentName) {
            this.departmentName = departmentName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getCheck_type() {
            return check_type;
        }

        public void setCheck_type(int check_type) {
            this.check_type = check_type;
        }

        public String getCol_type_name() {
            return col_type_name;
        }

        public void setCol_type_name(String col_type_name) {
            this.col_type_name = col_type_name;
        }

        public String getCol_type() {
            return col_type;
        }

        public void setCol_type(String col_type) {
            this.col_type = col_type;
        }

        public String getResults() {
            return results;
        }

        public void setResults(String results) {
            this.results = results;
        }

        public String getIs_result() {
            return is_result;
        }

        public void setIs_result(String is_result) {
            this.is_result = is_result;
        }

        public String getRight_result() {
            return right_result;
        }

        public void setRight_result(String right_result) {
            this.right_result = right_result;
        }
    }
}
