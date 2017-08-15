package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/3/16
 */
public class ZformVo implements Serializable{


    /**
     * result_msg : 查询成功
     * result_id : 0
     * list : [{"id":32,"hospital_id":"6140","department_id":null,"temp_type_id":12,"role":1,"title":"临床质控模板2","note":null,"state":1,"uid":368,"listsort":50,"update_time":null,"last_time":null,"create_time":"2017-03-16","uname":"潘孝","hospital":"专业版测试医院","use_cnt":0,"item_list":[{"id":8,"item_repo_id":1,"title":"项目一","type":1,"show_type":1,"score":"20","default_val":null,"item_class":null,"item_desc":null,"item_detail":null,"children":[]},{"id":9,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","type":2,"show_type":1,"score":"10","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":10,"item_repo_id":3,"title":"评估是否合格","type":3,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]}]},{"id":31,"hospital_id":"6140","department_id":null,"temp_type_id":12,"role":1,"title":"临床质控标准模板","note":null,"state":1,"uid":368,"listsort":50,"update_time":"2017-03-15 13:51:45","last_time":1489557105,"create_time":"2017-03-15","uname":"潘孝","hospital":"专业版测试医院","use_cnt":2,"item_list":[{"id":1,"item_repo_id":1,"title":"项目一","type":1,"show_type":1,"score":"20","default_val":null,"item_class":null,"item_desc":null,"item_detail":null,"children":[]},{"id":2,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","type":2,"show_type":1,"score":"10","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":3,"item_repo_id":3,"title":"评估是否合格","type":3,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":4,"item_repo_id":4,"title":"进行插管及护理前严格执行无菌操作规程，认真执行手卫生制度","type":2,"show_type":1,"score":"5","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":5,"item_repo_id":5,"title":"单间隔离或同耐药菌安置同一病室并悬挂相应隔标识","type":3,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":6,"item_repo_id":6,"title":"接触或处理血液体液、分泌物、排泄物等以及污染的组织敷料或床单衣物时戴手套","type":2,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":7,"item_repo_id":7,"title":"是否无双手回套针帽情况","type":2,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]}]}]
     */

    private String result_msg;
    private String result_id;
    /**
     * id : 32
     * hospital_id : 6140
     * department_id : null
     * temp_type_id : 12
     * role : 1
     * title : 临床质控模板2
     * note : null
     * state : 1
     * uid : 368
     * listsort : 50
     * update_time : null
     * last_time : null
     * create_time : 2017-03-16
     * uname : 潘孝
     * hospital : 专业版测试医院
     * use_cnt : 0
     * item_list : [{"id":8,"item_repo_id":1,"title":"项目一","type":1,"show_type":1,"score":"20","default_val":null,"item_class":null,"item_desc":null,"item_detail":null,"children":[]},{"id":9,"item_repo_id":2,"title":"一次性生活用品与一次性医疗用品都视为医疗垃圾","type":2,"show_type":1,"score":"10","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]},{"id":10,"item_repo_id":3,"title":"评估是否合格","type":3,"show_type":1,"score":"20","default_val":null,"item_class":"指标分类","item_desc":"考评方法","item_detail":"评分细则","children":[]}]
     */

    private List< RisistantVo.TaskInfoBean.TempListBean> list;

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

    public List<RisistantVo.TaskInfoBean.TempListBean> getList() {
        return list;
    }

    public void setList(List< RisistantVo.TaskInfoBean.TempListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private int id;
        private String hospital_id;
        private Object department_id;
        private int temp_type_id;
        private int role;
        private String title;
        private Object note;
        private int state;
        private int uid;
        private int listsort;
        private Object update_time;
        private Object last_time;
        private String create_time;
        private String uname;
        private String hospital;
        private int use_cnt;
        private int tempId;
        private int needChangeState;
        private int contentState;
        public int getContentState() {
            return contentState;
        }

        public void setContentState(int contentState) {
            this.contentState = contentState;
        }
        public int getTempId() {
            return tempId;
        }

        public void setTempId(int tempId) {
            this.tempId = tempId;
        }

        public int getNeedChangeState() {
            return needChangeState;
        }

        public void setNeedChangeState(int needChangeState) {
            this.needChangeState = needChangeState;
        }

        /**
         * id : 8
         * item_repo_id : 1
         * title : 项目一
         * type : 1
         * show_type : 1
         * score : 20
         * default_val : null
         * item_class : null
         * item_desc : null
         * item_detail : null
         * children : []
         */

        private List<ItemListBean> item_list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHospital_id() {
            return hospital_id;
        }

        public void setHospital_id(String hospital_id) {
            this.hospital_id = hospital_id;
        }

        public Object getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(Object department_id) {
            this.department_id = department_id;
        }

        public int getTemp_type_id() {
            return temp_type_id;
        }

        public void setTemp_type_id(int temp_type_id) {
            this.temp_type_id = temp_type_id;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getNote() {
            return note;
        }

        public void setNote(Object note) {
            this.note = note;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getListsort() {
            return listsort;
        }

        public void setListsort(int listsort) {
            this.listsort = listsort;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }

        public Object getLast_time() {
            return last_time;
        }

        public void setLast_time(Object last_time) {
            this.last_time = last_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public int getUse_cnt() {
            return use_cnt;
        }

        public void setUse_cnt(int use_cnt) {
            this.use_cnt = use_cnt;
        }

        public List<ItemListBean> getItem_list() {
            return item_list;
        }

        public void setItem_list(List<ItemListBean> item_list) {
            this.item_list = item_list;
        }

        public static class ItemListBean implements Serializable{
            private int id;
            private int item_repo_id;
            private String title;
            private int type;
            private int show_type;
            private String score;
            private String result;
            private Object default_val;
            private Object item_class;
            private Object item_desc;
            private Object item_detail;
            private List<ChildrenBean> children;


            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getItem_repo_id() {
                return item_repo_id;
            }

            public void setItem_repo_id(int item_repo_id) {
                this.item_repo_id = item_repo_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getShow_type() {
                return show_type;
            }

            public void setShow_type(int show_type) {
                this.show_type = show_type;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public Object getDefault_val() {
                return default_val;
            }

            public void setDefault_val(Object default_val) {
                this.default_val = default_val;
            }

            public Object getItem_class() {
                return item_class;
            }

            public void setItem_class(Object item_class) {
                this.item_class = item_class;
            }

            public Object getItem_desc() {
                return item_desc;
            }

            public void setItem_desc(Object item_desc) {
                this.item_desc = item_desc;
            }

            public Object getItem_detail() {
                return item_detail;
            }

            public void setItem_detail(Object item_detail) {
                this.item_detail = item_detail;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }


            public static class ChildrenBean implements Serializable {
                private int id;
                private int type_id;
                private String title;
                private String score = "";
                private String is_yes = "0";
                private int parent_id;
                private String result = "";
                private String item_id;
                private String item_score;
                private String name="";
                private String create_item;
                private  int state;
                private  String save_time;
                private String get_score="";

                public String getGet_score() {
                    return get_score;
                }

                public void setGet_score(String get_score) {
                    this.get_score = get_score;
                }

                public String getSave_time() {
                    return save_time;
                }

                public void setSave_time(String save_time) {
                    this.save_time = save_time;
                }

                public int getState() {
                    return state;
                }

                public void setState(int state) {
                    this.state = state;
                }

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public String getItem_score() {
                    return item_score;
                }

                public void setItem_score(String item_score) {
                    this.item_score = item_score;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCreate_item() {
                    return create_item;
                }

                public void setCreate_item(String create_item) {
                    this.create_item = create_item;
                }

                public String getResult() {
                    return result;
                }

                public void setResult(String result) {
                    this.result = result;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getType_id() {
                    return type_id;
                }

                public void setType_id(int type_id) {
                    this.type_id = type_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getIs_yes() {
                    return is_yes;
                }

                public void setIs_yes(String is_yes) {
                    this.is_yes = is_yes;
                }

                public int getParent_id() {
                    return parent_id;
                }

                public void setParent_id(int parent_id) {
                    this.parent_id = parent_id;
                }
            }
        }
    }
}
