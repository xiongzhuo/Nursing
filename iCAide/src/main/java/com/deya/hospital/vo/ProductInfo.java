package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/2
 */
public class ProductInfo {

    /**
     * result_msg : 获取消耗品列表成功
     * result_id : 0
     * list : [{"product_type":"1","product":[{"product_id":15,"name":"快速手消毒剂","hospital_id":6140,"product_type":"1","standard":2222222,"sub_type":"0","brand":"2222222222","status":1},{"product_id":13,"name":"快速手消毒剂","hospital_id":6140,"product_type":"1","standard":222,"sub_type":"0","brand":"2222222222","status":1}]},{"product_type":"2","product":[{"product_id":2,"name":"洗手液","hospital_id":6140,"product_type":"2","standard":200,"sub_type":"1","brand":"dashk","status":1},{"product_id":16,"name":"洗手液","hospital_id":6140,"product_type":"2","standard":11111111,"sub_type":"2","brand":"111111111","status":1},{"product_id":5,"name":"洗手液","hospital_id":6140,"product_type":"2","standard":500,"sub_type":"2","brand":"555","status":1}]},{"product_type":"3","product":[{"product_id":10,"name":"干手纸","hospital_id":6140,"product_type":"3","standard":1111,"sub_type":"0","brand":"3333","status":1},{"product_id":3,"name":"干手纸","hospital_id":6140,"product_type":"3","standard":100,"sub_type":null,"brand":"afdask","status":1}]},{"product_type":"4","product":[{"product_id":11,"name":"手套","hospital_id":6140,"product_type":"4","standard":5555,"sub_type":"5","brand":"55555","status":1},{"product_id":12,"name":"手套","hospital_id":6140,"product_type":"4","standard":5555,"sub_type":"5","brand":"55555555111111","status":1}]}]
     */

    private String result_msg;
    private String result_id;
    /**
     * product_type : 1
     * product : [{"product_id":15,"name":"快速手消毒剂","hospital_id":6140,"product_type":"1","standard":2222222,"sub_type":"0","brand":"2222222222","status":1},{"product_id":13,"name":"快速手消毒剂","hospital_id":6140,"product_type":"1","standard":222,"sub_type":"0","brand":"2222222222","status":1}]
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

    public static class ListBean {
        private String product_type="";
        /**
         * product_id : 15
         * name : 快速手消毒剂
         * hospital_id : 6140
         * product_type : 1
         * standard : 2222222
         * sub_type : 0
         * brand : 2222222222
         * status : 1
         */

        private List<ProductBean> product;

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public List<ProductBean> getProduct() {
            return product;
        }

        public void setProduct(List<ProductBean> product) {
            this.product = product;
        }
        @Table(name = "product_table")
        public static class ProductBean {
            @Id(column = "dbid")
            int dbid;
            private int product_id;
            private String name="";
            private int hospital_id;
            private String product_type;
            private int standard;
            private String sub_type;
            private String brand;
            private int status;
            private String sub_type_name="";

            public String getSub_type_name() {
                return sub_type_name;
            }

            public void setSub_type_name(String sub_type_name) {
                this.sub_type_name = sub_type_name;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getHospital_id() {
                return hospital_id;
            }

            public void setHospital_id(int hospital_id) {
                this.hospital_id = hospital_id;
            }

            public String getProduct_type() {
                return product_type;
            }

            public void setProduct_type(String product_type) {
                this.product_type = product_type;
            }

            public int getStandard() {
                return standard;
            }

            public void setStandard(int standard) {
                this.standard = standard;
            }

            public String getSub_type() {
                return sub_type;
            }

            public void setSub_type(String sub_type) {
                this.sub_type = sub_type;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
