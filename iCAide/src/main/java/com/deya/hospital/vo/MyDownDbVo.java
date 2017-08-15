package com.deya.hospital.vo;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "downata")
public class MyDownDbVo {
  @Id(column = "id")
  private int id;
  private String data;

  public int getId() {
    return id;
  }

  public void set_id(int id) {
    this.id = id;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

}
