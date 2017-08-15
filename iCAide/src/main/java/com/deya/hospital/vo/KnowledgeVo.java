package com.deya.hospital.vo;

import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/27
 */
public class KnowledgeVo implements Serializable {

    /**
     * status : 1
     * list : [{"id":27,"title":"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置","note":"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置","img":"44bbc152-2445-4f3c-b524-fd7426f35a28.jpg","sub_type":1,"s_score":"10","listorder":0,"type":"0","items":[{"id":2286,"title":"T","is_yes":"2","note":"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置医疗卫生","img":"c761b36a-3da9-408c-8da6-32fa0f22a251.jpg"},{"id":2287,"title":"F","is_yes":"1","note":"错误答案22","img":"5d7eb308-450c-4808-bea6-e8027a38b6a8.jpg"}]},{"id":46,"title":"哪些情况应该洗手","note":"","img":null,"sub_type":2,"s_score":"10","listorder":0,"type":"0","items":[{"id":159,"title":"A","is_yes":"1","note":"接触患者黏膜、破损皮肤或伤口后 ","img":""},{"id":160,"title":"B","is_yes":"1","note":"接触患者的血液、体液、分泌物、排泄物、伤口敷料等之后 ","img":""},{"id":161,"title":"C","is_yes":"1","note":"直接接触每个患者前后","img":""},{"id":162,"title":"D","is_yes":"1","note":"直接为传染病患者进行检查、治疗、护理后","img":""}]},{"id":45,"title":"手消毒指征","note":"","img":null,"sub_type":2,"s_score":"10","listorder":0,"type":"0","items":[{"id":155,"title":"A","is_yes":"1","note":"进入和离开隔离病房、穿脱隔离衣前后","img":""},{"id":156,"title":"B","is_yes":"1","note":"接触特殊感染病原体后","img":""},{"id":157,"title":"C","is_yes":"1","note":"接触血液、体液和被污染的物品后","img":""},{"id":158,"title":"D","is_yes":"1","note":"接触未消毒仪器和设备后","img":""}]},{"id":44,"title":"手卫生设施是指用于洗手与手消毒的设施，包括洗手池、水龙头、流动水、（    ）等","note":"","img":null,"sub_type":2,"s_score":"10","listorder":0,"type":"0","items":[{"id":151,"title":"A","is_yes":"1","note":"肥皂（液）","img":""},{"id":152,"title":"B","is_yes":"1","note":"干手用品","img":""},{"id":153,"title":"C","is_yes":"1","note":"手消毒剂","img":""},{"id":154,"title":"D","is_yes":"2","note":"手套","img":""}]},{"id":43,"title":"速干手消毒剂是指含有醇类和护肤成分的手消毒剂，包括","note":"","img":null,"sub_type":2,"s_score":"10","listorder":0,"type":"0","items":[{"id":147,"title":"A","is_yes":"1","note":"水剂  ","img":""},{"id":148,"title":"B","is_yes":"1","note":"凝胶    ","img":""},{"id":149,"title":"C","is_yes":"1","note":"泡沫型   ","img":""},{"id":150,"title":"D","is_yes":"2","note":"油剂","img":""}]},{"id":42,"title":"医务人员工作期间手部要求哪句话描述正确","note":"","img":null,"sub_type":1,"s_score":"10","listorder":0,"type":"0","items":[{"id":143,"title":"A","is_yes":"2","note":"只要手套没有破就不用担心有害微生物会污染到手，摘手套后可以不必洗手","img":""},{"id":144,"title":"B","is_yes":"2","note":"易挥发性的醇类手消毒剂开瓶后的使用期限应大于30d","img":""},{"id":145,"title":"C","is_yes":"1","note":"医护人员工作期间指甲不应超过指尖，不应戴假指甲、戒指、手表、手镯或其它指甲产品","img":""},{"id":146,"title":"D","is_yes":"2","note":"洗手的目的是保护医务人员自身不受病原微生物的污染","img":""}]},{"id":41,"title":"《医务人员手卫生规范》中卫生手消毒是指","note":"","img":null,"sub_type":1,"s_score":"10","listorder":0,"type":"0","items":[{"id":139,"title":"A","is_yes":"2","note":"用速干手消毒剂揉搓双手，以杀灭手部暂居菌的过程","img":""},{"id":140,"title":"B","is_yes":"2","note":"用速干手消毒剂揉搓双手，以减少手部常居菌的过程","img":""},{"id":141,"title":"C","is_yes":"1","note":"用速干手消毒剂揉搓双手，以减少手部暂居菌的过程","img":""},{"id":142,"title":"D","is_yes":"2","note":"用速干手消毒剂揉搓双手，以杀灭手部常居菌的过程","img":""}]},{"id":40,"title":"关于手卫生的作用，包括","note":"","img":null,"sub_type":2,"s_score":"10","listorder":0,"type":"0","items":[{"id":135,"title":"A","is_yes":"1","note":"预防患者发生医院感染","img":""},{"id":136,"title":"B","is_yes":"1","note":"阻断微生物在不同患者之间的交叉传染","img":""},{"id":137,"title":"C","is_yes":"1","note":"预防潜在病原体对医院环境的污染 ","img":""},{"id":138,"title":"D","is_yes":"1","note":"帮助医务人员预防职业疾病","img":""}]},{"id":39,"title":"手消毒效果应达到的要求：外科手消毒监测的细菌数应","note":"","img":null,"sub_type":1,"s_score":"10","listorder":0,"type":"0","items":[{"id":131,"title":"A","is_yes":"2","note":"≤10cfu/cm2 ","img":""},{"id":132,"title":"B","is_yes":"1","note":" ≤5cfu/cm2 ","img":""},{"id":133,"title":"C","is_yes":"2","note":"≤15cfu/cm2 ","img":""},{"id":134,"title":"D","is_yes":"2","note":"≤8cfu/cm2","img":""}]},{"id":47,"title":"关于手卫生，下列说法正确的是","note":"","img":null,"sub_type":1,"s_score":"10","listorder":0,"type":"0","items":[{"id":163,"title":"A","is_yes":"2","note":"手部有肉眼可见污垢时，可以不洗手，进行卫生手消毒。","img":""},{"id":164,"title":"B","is_yes":"1","note":"若手部没有肉眼可见污染时宜使用速干手消毒剂消毒双手代替洗手。","img":""},{"id":165,"title":"C","is_yes":"1","note":"接触患者的血液、体液和分泌物后， 应先洗手，然后卫生手消毒。","img":""},{"id":166,"title":"D","is_yes":"1","note":"直接为传染病患者进行检查、治疗、护理或处理传染患者污物之后，应先洗手，然后卫生手消毒。","img":""}]}]
     */

    private int status;
    /**
     * id : 27
     * title : 医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置
     * note : 医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置
     * img : 44bbc152-2445-4f3c-b524-fd7426f35a28.jpg
     * sub_type : 1
     * s_score : 10
     * listorder : 0
     * type : 0
     * items : [{"id":2286,"title":"T","is_yes":"2","note":"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置医疗卫生","img":"c761b36a-3da9-408c-8da6-32fa0f22a251.jpg"},{"id":2287,"title":"F","is_yes":"1","note":"错误答案22","img":"5d7eb308-450c-4808-bea6-e8027a38b6a8.jpg"}]
     */

    private List<ListBean> list;

    String test_id = "";
    String article_src = "";
    String title = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getArticle_src() {
        return article_src;
    }

    public void setArticle_src(String article_src) {
        this.article_src = article_src;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ListBean> getList() {
        if (null == list) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    @Table(name = "hospital_knowledge_table4")
    public  static class ListBean implements Serializable {
        @Id(column = "dbid")
        private int dbid;
        private int id;
        private String title;
        private String note = "";
        private String img;
        private int sub_type;
        private String s_score;
        private int listorder;
        private String type;
        boolean isRight = true;
        String rightAswer;
        private int seqNo;
        private int is_colected;
        private int isWrong;
        private String itemStr="";
        int parent_id;
        boolean isAnswered;

        public boolean isAnswered() {
            return isAnswered;
        }

        public void setAnswered(boolean answered) {
            isAnswered = answered;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getIs_colected() {
            return is_colected;
        }

        public void setIs_colected(int is_colected) {
            this.is_colected = is_colected;
        }

        public String getItemStr() {
            return itemStr;
        }

        public void setItemStr(String itemStr) {
            this.itemStr = itemStr;
        }

        public int getIsWrong() {
            return isWrong;
        }

        public void setIsWrong(int isWrong) {
            this.isWrong = isWrong;
        }

        public int getDbid() {
            return dbid;
        }

        public void setDbid(int dbid) {
            this.dbid = dbid;
        }

        public int getIsColected() {
            return is_colected;
        }

        public void setIsColected(int isColected) {
            this.is_colected = isColected;
        }

        public int getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(int seqNo) {
            this.seqNo = seqNo;
        }

        String chooseAswer = "";

        public String getRightAswer() {
            return rightAswer == null ? "" : rightAswer;
        }

        public void setRightAswer(String rightAswer) {
            this.rightAswer = rightAswer;
        }

        public String getChooseAswer() {
            return chooseAswer;
        }

        public void setChooseAswer(String chooseAswer) {
            this.chooseAswer = chooseAswer;
        }

        public boolean isRight() {
            return isRight;
        }

        public void setRight(boolean right) {
            isRight = right;
        }

        /**
         * id : 2286
         * title : T
         * is_yes : 2
         * note : 医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置医疗卫生
         * img : c761b36a-3da9-408c-8da6-32fa0f22a251.jpg
         */

        private List<ItemsBean> items;

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

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getSub_type() {
            return sub_type;
        }

        public void setSub_type(int sub_type) {
            this.sub_type = sub_type;
        }

        public String getS_score() {
            return s_score;
        }

        public void setS_score(String s_score) {
            this.s_score = s_score;
        }

        public int getListorder() {
            return listorder;
        }

        public void setListorder(int listorder) {
            this.listorder = listorder;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ItemsBean> getItems() {

            if(null==items&&null!=itemStr){
                items= TaskUtils.gson.fromJson(itemStr,
                        new TypeToken<List<ItemsBean>>() {
                        }.getType());
            }
            return items==null?new ArrayList<ItemsBean>():items;

        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean implements Serializable {
            private int id;
            private String title;
            private String is_yes;
            private String note;
            private String img;
            private String result = "";

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIs_yes() {
                return is_yes;
            }

            public void setIs_yes(String is_yes) {
                this.is_yes = is_yes;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
