package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/2
 */
public class ComunptionToalInfo {


    /**
     * result_msg : 查询成功
     * result_id : 0
     * list : [{"product_type":"1","total_pull_num":500,"total_stocks_num":3000,"records":[{"product_type":"1","pull_num":"1","stocks_num":"6","standard":"500","product_id":1}]},{"product_type":"2","total_pull_num":400,"total_stocks_num":1800,"records":[{"product_type":"2","pull_num":"1","stocks_num":"6","standard":"200","product_id":2}]},{"product_type":"3","total_pull_num":100,"total_stocks_num":600,"records":[{"product_type":"3","pull_num":"1","stocks_num":"6","standard":"100","product_id":3}]},{"product_type":"4","total_pull_num":null,"total_stocks_num":null,"records":[]}]
     */

    private String result_msg;
    private String result_id;
    String task_id;
    int bed_no;

    public int getBed_no() {
        return bed_no;
    }

    public void setBed_no(int bed_no) {
        this.bed_no = bed_no;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    /**
     * product_type : 1
     * total_pull_num : 500
     * total_stocks_num : 3000
     * records : [{"product_type":"1","pull_num":"1","stocks_num":"6","standard":"500","product_id":1}]
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable{
        private String product_type;
        private int total_pull_num;
        private int total_stocks_num;
        private int month_consume_num;
        private String day_consume_num;

        public int getMonth_consume_num() {
            return month_consume_num;
        }

        public void setMonth_consume_num(int month_consume_num) {
            this.month_consume_num = month_consume_num;
        }

        public String getDay_consume_num() {
            return day_consume_num;
        }

        public void setDay_consume_num(String day_consume_num) {
            this.day_consume_num = day_consume_num;
        }

        /**
         * product_type : 1
         * pull_num : 1
         * stocks_num : 6
         * standard : 500
         * product_id : 1
         */

        private List<RecordsBean> records;

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public int getTotal_pull_num() {
            return total_pull_num;
        }

        public void setTotal_pull_num(int total_pull_num) {
            this.total_pull_num = total_pull_num;
        }

        public int getTotal_stocks_num() {
            return total_stocks_num;
        }

        public void setTotal_stocks_num(int total_stocks_num) {
            this.total_stocks_num = total_stocks_num;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public static class RecordsBean implements Serializable{
            private String product_type="";
            private String pull_num="";
            private String stocks_num="";
            private String standard="";
            private int product_id;
            private     String standardName;

            public String getStandardName() {
                return standardName;
            }

            public void setStandardName(String standardName) {
                this.standardName = standardName;
            }

            public String getProduct_type() {
                return product_type;
            }

            public void setProduct_type(String product_type) {
                this.product_type = product_type;
            }

            public String getPull_num() {
                return pull_num;
            }

            public void setPull_num(String pull_num) {
                this.pull_num = pull_num;
            }

            public String getStocks_num() {
                return stocks_num;
            }

            public void setStocks_num(String stocks_num) {
                this.stocks_num = stocks_num;
            }

            public String getStandard() {
                return standard;
            }

            public void setStandard(String standard) {
                this.standard = standard;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }
        }
    }
}
