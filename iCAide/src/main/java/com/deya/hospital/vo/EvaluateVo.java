package com.deya.hospital.vo;

import java.io.Serializable;

public class EvaluateVo implements Serializable {
  
  /**
   * 评价ID
   */
  private String evalId;
  /**
   * .评价人
   */
  private String evalManName;
  /**
   * .评价人头像
   */
  private String evalManHead;
  /**
   * .评价内容
   */
  private String evalContent;
  /**
   * .评价星级
   */
  private String evalLevel;
  /**
   * .评价时间
   */
  private String evalTime;
  /**
   * .评价规格
   */
  private String evalSpecContent;

  public String getEvalId() {
    return evalId;
  }

  public void setEvalId(String evalId) {
    this.evalId = evalId;
  }

  public String getEvalManName() {
    return evalManName;
  }

  public void setEvalManName(String evalManName) {
    this.evalManName = evalManName;
  }

  public String getEvalManHead() {
    return evalManHead;
  }

  public void setEvalManHead(String evalManHead) {
    this.evalManHead = evalManHead;
  }

  public String getEvalContent() {
    return evalContent;
  }

  public void setEvalContent(String evalContent) {
    this.evalContent = evalContent;
  }

  public String getEvalLevel() {
    return evalLevel;
  }

  public void setEvalLevel(String evalLevel) {
    this.evalLevel = evalLevel;
  }

  public String getEvalTime() {
    return evalTime;
  }

  public void setEvalTime(String evalTime) {
    this.evalTime = evalTime;
  }

  public String getEvalSpecContent() {
    return evalSpecContent;
  }

  public void setEvalSpecContent(String evalSpecContent) {
    this.evalSpecContent = evalSpecContent;
  }

}
