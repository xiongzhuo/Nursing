package com.deya.hospital.base.img;

import com.deya.hospital.vo.AlbumInfo;

import java.io.Serializable;
import java.util.List;


public class PhotoFolderSerializable implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<AlbumInfo> list;

  public List<AlbumInfo> getList() {
    return list;
  }

  public void setList(List<AlbumInfo> list) {
    this.list = list;
  }

}
