package com.deya.hospital.vo;

import java.io.Serializable;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/20
 */
public class ClassifyVo implements Serializable {
    int wrongNum;
    int rightNum;
    int toalScore;
    int undonum;
    String note;
    String formName;
    int sumbScore;
    double getScore;

    public int getSumbScore() {
        return sumbScore;
    }

    public void setSumbScore(int sumbScore) {
        this.sumbScore = sumbScore;
    }

    public double getGetScore() {
        return getScore;
    }

    public void setGetScore(double getScore) {
        this.getScore = getScore;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(int wrongNum) {
        this.wrongNum = wrongNum;
    }

    public int getRightNum() {
        return rightNum;
    }

    public void setRightNum(int rightNum) {
        this.rightNum = rightNum;
    }

    public int getToalScore() {
        return toalScore;
    }

    public void setToalScore(int toalScore) {
        this.toalScore = toalScore;
    }

    public int getUndonum() {
        return undonum;
    }

    public void setUndonum(int undonum) {
        this.undonum = undonum;
    }
}
