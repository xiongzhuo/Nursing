package com.deya.hospital.vo;

import java.io.Serializable;
import java.util.List;

/**
 * .ClassName: AlbumInfo(相册Bean) <br/>
 * date: 2015年4月23日 下午5:42:11 <br/>
 * @author BMM
 */
public class AlbumInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int imageId;
  private String pathAbsolute;
  private String pathFile;
  private String nameAlbum;
  private List<PhotoInfo> list;

  public int getImage_id() {
    return imageId;
  }

  public void setImage_id(int imageId) {
    this.imageId = imageId;
  }

  public String getPath_absolute() {
    return pathAbsolute;
  }

  public void setPath_absolute(String pathAbsolute) {
    this.pathAbsolute = pathAbsolute;
  }

  public String getPath_file() {
    return pathFile;
  }

  public void setPath_file(String pathFile) {
    this.pathFile = pathFile;
  }

  public String getName_album() {
    return nameAlbum;
  }

  public void setName_album(String nameAlbum) {
    this.nameAlbum = nameAlbum;
  }

  public List<PhotoInfo> getList() {
    return list;
  }

  public void setList(List<PhotoInfo> list) {
    this.list = list;
  }
}
