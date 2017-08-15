package com.deya.hospital.vo.dbdata;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "taskDbVo1")
public class TaskDbVo {
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
