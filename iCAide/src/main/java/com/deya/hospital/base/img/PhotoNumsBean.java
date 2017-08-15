package com.deya.hospital.base.img;


/**
 * .ClassName: PhotoNumsBean(保存照片的最大数量) <br/>
 * date: 2015年4月23日 下午6:05:49 <br/>
 * @author BMM
 */
public class PhotoNumsBean {
  public static PhotoNumsBean bean;
  private int number = 0;

  /**
   * getInstant:【获取实例】. <br/>
   * .@return.<br/>
   */
  public static PhotoNumsBean getInstant() {
    if (null == bean) {
      bean = new PhotoNumsBean();
    }
    return bean;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }
}
