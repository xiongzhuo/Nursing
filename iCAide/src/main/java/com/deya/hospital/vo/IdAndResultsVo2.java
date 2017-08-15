package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class IdAndResultsVo2 implements Serializable{
    /**
     * result : result
     * select_id : 3
     * attr2 : attr2
     * attr1 : attr1
     * attr3 : attr3
     */

    private List<ItemBean> result;
    private String temp_id;

    public String getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(String temp_id) {
        this.temp_id = temp_id;
    }

    public List<ItemBean> getResult() {
        return result;
    }

    public void setResult(List<ItemBean> result) {
        this.result = result;
    }




   public static class ItemBean{
        public String select_id;
        public String result;
        private String attr2;
        private String attr1;
        private String attr3;

        public String getSelect_id() {
            return select_id;
        }

        public void setSelect_id(String select_id) {
            this.select_id = select_id;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getAttr2() {
            return attr2;
        }

        public void setAttr2(String attr2) {
            this.attr2 = attr2;
        }

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public String getAttr3() {
            return attr3;
        }

        public void setAttr3(String attr3) {
            this.attr3 = attr3;
        }
    }
}
