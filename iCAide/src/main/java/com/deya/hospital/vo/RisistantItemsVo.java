package com.deya.hospital.vo;

import java.io.Serializable;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class RisistantItemsVo implements Serializable{


        private String title;
        private int chooseState;
        private int columnsState;//单列是多列

        public int getColumnsState() {
            return columnsState;
        }

        public void setColumnsState(int columnsState) {
            this.columnsState = columnsState;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getChooseState() {
            return chooseState;
        }

        public void setChooseState(int chooseState) {
            this.chooseState = chooseState;
        }
}
