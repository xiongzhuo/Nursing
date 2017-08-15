package com.deya.hospital.vo;

import java.io.Serializable;

/**
 * .ClassName: PhotoInfo(本地相册图片的bean) <br/>
 * date: 2015年4月23日 下午5:45:58 <br/>
 * @author BMM
 */
public class PhotoInfo implements Serializable {

  private static final long serialVersionUID = 1L;
  private int imageId;
  private String pathFile;
  private String pathAbsolute;
  private boolean choose = false;

  public int getImage_id() {
    return imageId;
  }

  public void setImage_id(int imageId) {
    this.imageId = imageId;
  }

  public String getPath_file() {
    return pathFile;
  }

  public void setPath_file(String pathFile) {
    this.pathFile = pathFile;
  }

  public String getPath_absolute() {
    return pathAbsolute;
  }

  public void setPath_absolute(String pathAbsolute) {
    this.pathAbsolute = pathAbsolute;
  }

  public boolean isChoose() {
    return choose;
  }

  public void setChoose(boolean choose) {
    this.choose = choose;
  }
}
