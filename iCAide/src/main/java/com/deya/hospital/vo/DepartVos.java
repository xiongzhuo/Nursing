package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/3
 */
public class DepartVos {

    /**
     * departmentTypeList : [{"id":209158,"name":"内科","children":[{"id":209159,"name":"心血管内科","children":[],"parent":209158,"orders":101,"is_main":"0"}],"parent":0,"orders":255,"is_main":"0"},{"id":209186,"name":"医技","children":[{"id":209187,"name":"药剂科","children":[],"parent":209186,"orders":124,"is_main":"0"},{"id":209188,"name":"检验科","children":[],"parent":209186,"orders":125,"is_main":"0"}],"parent":0,"orders":255,"is_main":"0"},{"id":209194,"name":"其它","children":[],"parent":0,"orders":255,"is_main":"0"},{"id":218282,"name":"风暴","children":[],"parent":1,"orders":255,"is_main":null},{"id":218283,"name":"霹雳","children":[],"parent":1,"orders":255,"is_main":null},{"id":220454,"name":"消毒供应","children":[],"parent":1,"orders":255,"is_main":null}]
     * result_msg : 成功
     * result_id : 0
     * departmentList : [{"id":209160,"childrenCount":0,"name":"呼吸内科","parent":209158,"orders":102,"is_main":"0","hospital":6140},{"id":209161,"childrenCount":0,"name":"肾内科","parent":209158,"orders":103,"is_main":"0","hospital":6140},{"id":209162,"childrenCount":0,"name":"消化内科","parent":209158,"orders":104,"is_main":"0","hospital":6140},{"id":209163,"childrenCount":0,"name":"血液内科","parent":209158,"orders":105,"is_main":"0","hospital":6140},{"id":209164,"childrenCount":0,"name":"内分泌科","parent":209158,"orders":106,"is_main":"0","hospital":6140},{"id":209165,"childrenCount":0,"name":"神经内科","parent":209158,"orders":107,"is_main":"0","hospital":6140},{"id":209167,"childrenCount":0,"name":"普外科","parent":209166,"orders":108,"is_main":"0","hospital":6140},{"id":209168,"childrenCount":0,"name":"肝胆外科","parent":209166,"orders":109,"is_main":"0","hospital":6140},{"id":209169,"childrenCount":0,"name":"胃肠外科","parent":209166,"orders":110,"is_main":"0","hospital":6140},{"id":209170,"childrenCount":0,"name":"痔瘘外科","parent":209166,"orders":111,"is_main":"0","hospital":6140},{"id":209171,"childrenCount":0,"name":"心胸外科","parent":209166,"orders":112,"is_main":"0","hospital":6140},{"id":209172,"childrenCount":0,"name":"骨科","parent":209166,"orders":113,"is_main":"0","hospital":6140},{"id":209173,"childrenCount":0,"name":"神经外科","parent":209166,"orders":114,"is_main":"0","hospital":6140},{"id":209174,"childrenCount":0,"name":"泌尿外科","parent":209166,"orders":115,"is_main":"0","hospital":6140},{"id":209175,"childrenCount":0,"name":"整形外科","parent":209166,"orders":116,"is_main":"0","hospital":6140},{"id":209176,"childrenCount":0,"name":"烧伤科","parent":209166,"orders":117,"is_main":"0","hospital":6140},{"id":209178,"childrenCount":0,"name":"妇科","parent":209177,"orders":118,"is_main":"0","hospital":6140},{"id":209179,"childrenCount":0,"name":"产科","parent":209177,"orders":119,"is_main":"0","hospital":6140},{"id":209181,"childrenCount":0,"name":"儿科","parent":209180,"orders":120,"is_main":"0","hospital":6140},{"id":209183,"childrenCount":0,"name":"眼科","parent":209182,"orders":121,"is_main":"0","hospital":6140},{"id":209184,"childrenCount":0,"name":"口腔科","parent":209182,"orders":122,"is_main":"0","hospital":6140},{"id":209185,"childrenCount":0,"name":"耳鼻喉科","parent":209182,"orders":123,"is_main":"0","hospital":6140},{"id":209189,"childrenCount":0,"name":"放射科","parent":209186,"orders":126,"is_main":"0","hospital":6140},{"id":209190,"childrenCount":0,"name":"病理科","parent":209186,"orders":127,"is_main":"0","hospital":6140},{"id":209191,"childrenCount":0,"name":"核医学科","parent":209186,"orders":128,"is_main":"0","hospital":6140},{"id":209192,"childrenCount":0,"name":"临床功能检查","parent":209186,"orders":129,"is_main":"0","hospital":6140},{"id":209193,"childrenCount":0,"name":"营养科","parent":209186,"orders":130,"is_main":"0","hospital":6140},{"id":209195,"childrenCount":0,"name":"皮肤科","parent":209194,"orders":131,"is_main":"0","hospital":6140},{"id":209196,"childrenCount":0,"name":"麻醉科","parent":209194,"orders":132,"is_main":"0","hospital":6140},{"id":209197,"childrenCount":0,"name":"急诊科","parent":209194,"orders":133,"is_main":"0","hospital":6140},{"id":209198,"childrenCount":0,"name":"中医科","parent":209194,"orders":134,"is_main":"0","hospital":6140},{"id":209199,"childrenCount":0,"name":"传染科","parent":209194,"orders":135,"is_main":"0","hospital":6140},{"id":209200,"childrenCount":0,"name":"医疗美容科","parent":209194,"orders":136,"is_main":"0","hospital":6140},{"id":209201,"childrenCount":0,"name":"其它","parent":209194,"orders":137,"is_main":"0","hospital":6140},{"id":211610,"childrenCount":0,"name":"解决了333","parent":218282,"orders":255,"hospital":6140},{"id":220453,"childrenCount":0,"name":"消毒供应科","parent":239831,"orders":255,"hospital":6140},{"id":239830,"childrenCount":1,"name":"消毒科","parent":220454,"orders":22,"hospital":6140},{"id":239831,"childrenCount":1,"name":"消毒一科","parent":239832,"orders":233,"hospital":6140},{"id":239832,"childrenCount":1,"name":"消毒二科","parent":239830,"orders":22,"hospital":6140},{"id":239833,"childrenCount":0,"name":"新血管内科","parent":209159,"hospital":6140}]
     */

    private String result_msg;
    private String result_id;
    /**
     * id : 209158
     * name : 内科
     * children : [{"id":209159,"name":"心血管内科","children":[],"parent":209158,"orders":101,"is_main":"0"}]
     * parent : 0
     * orders : 255
     * is_main : 0
     */

    private List<DepartmentTypeListBean> departmentTypeList;
    /**
     * id : 209160
     * childrenCount : 0
     * name : 呼吸内科
     * parent : 209158
     * orders : 102
     * is_main : 0
     * hospital : 6140
     */

    private List<DepartmentListBean> departmentList;

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

    public List<DepartmentTypeListBean> getDepartmentTypeList() {
        return departmentTypeList;
    }

    public void setDepartmentTypeList(List<DepartmentTypeListBean> departmentTypeList) {
        this.departmentTypeList = departmentTypeList;
    }

    public List<DepartmentListBean> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentListBean> departmentList) {
        this.departmentList = departmentList;
    }

    public static class DepartmentTypeListBean {
        private int id;
        private String name;
        private int parent;
        private int orders;
        private String is_main;
        /**
         * id : 209159
         * name : 心血管内科
         * children : []
         * parent : 209158
         * orders : 101
         * is_main : 0
         */

        private List<ChildrenBean> children;

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

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public String getIs_main() {
            return is_main;
        }

        public void setIs_main(String is_main) {
            this.is_main = is_main;
        }

        public List<ChildrenBean> getChildren() {
            return children==null?new ArrayList<ChildrenBean>():children;
        }

        public void setChildren(List<ChildrenBean> children) {
            this.children = children;
        }

        public static class ChildrenBean {
            private int id;
            private String name;
            private int parent;
            private int orders;
            private String is_main;
            private List<?> children;

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

            public int getParent() {
                return parent;
            }

            public void setParent(int parent) {
                this.parent = parent;
            }

            public int getOrders() {
                return orders;
            }

            public void setOrders(int orders) {
                this.orders = orders;
            }

            public String getIs_main() {
                return is_main;
            }

            public void setIs_main(String is_main) {
                this.is_main = is_main;
            }

            public List<?> getChildren() {
                return children;
            }

            public void setChildren(List<?> children) {
                this.children = children;
            }
        }
    }

    public static class DepartmentListBean implements Serializable{
        private int id;
        private int childrenCount;
        private String name;
        private int parent;
        private int orders;
        private String is_main;
        private int hospital;
        private  boolean isChoosed;

        public boolean getIsChoosed() {
            return isChoosed;
        }

        public void setIsChoosed(boolean isChoosed) {
            this.isChoosed = isChoosed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChildrenCount() {
            return childrenCount;
        }

        public void setChildrenCount(int childrenCount) {
            this.childrenCount = childrenCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParent() {
            return parent;
        }

        public void setParent(int parent) {
            this.parent = parent;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public String getIs_main() {
            return is_main;
        }

        public void setIs_main(String is_main) {
            this.is_main = is_main;
        }

        public int getHospital() {
            return hospital;
        }

        public void setHospital(int hospital) {
            this.hospital = hospital;
        }
    }
}
