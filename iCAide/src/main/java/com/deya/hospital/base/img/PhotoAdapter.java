package com.deya.hospital.base.img;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.vo.PhotoInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * .ClassName: PhotoAdapter(相片适配器) <br/>
 * date: 2015年4月23日 下午5:37:13 <br/>
 * @author BMM
 */
public class PhotoAdapter extends BaseAdapter {

  private LayoutInflater myInflater;
  private List<PhotoInfo> list;
  private ViewHolder viewHolder;
  private GridView gridView;
  private int width = MyAppliaction.getDisplayMetrics().widthPixels / 3;
  DisplayImageOptions optionsSquare;

  /**
   * Creates a new instance of PhotoAdapter.
   *
   * @param context 上下文
   * @param list 图片集合
   * @param gridView 图片列表
   */
  public PhotoAdapter(Context context, List<PhotoInfo> list, GridView gridView) {
    myInflater = LayoutInflater.from(context);
    this.list = list;
    this.gridView = gridView;
    optionsSquare =
        new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_img)
            .showImageForEmptyUri(R.drawable.defult_img)
            .showImageOnFail(R.drawable.defult_img).resetViewBeforeLoading(true)
            .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(300)).build();
  }

  @Override
  public int getCount() {
    return list == null ? 0 : list.size();
  }

  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

 
  /**
   * refreshView:【刷新view】. <br/>
   * .@param index.<br/>
   */
  public void refreshView(int index) {
    int visiblePos = gridView.getFirstVisiblePosition();
    View view = gridView.getChildAt(index - visiblePos);
    ViewHolder holder = (ViewHolder) view.getTag();

    if (list.get(index).isChoose()) {
      holder.selectImage.setImageResource(R.drawable.gou_selected);
    } else {
      holder.selectImage.setImageResource(R.drawable.gou_normal);
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = myInflater.inflate(R.layout.common_item_selectphoto, null);
      viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
      viewHolder.selectImage = (ImageView) convertView.findViewById(R.id.selectImage);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    if (list.get(position).isChoose()) {
      viewHolder.selectImage.setImageResource(R.drawable.gou_selected);
    } else {
      viewHolder.selectImage.setImageResource(R.drawable.gou_normal);
    }
    LayoutParams layoutParams = viewHolder.image.getLayoutParams();
    layoutParams.width = width;
    layoutParams.height = width;
    viewHolder.image.setLayoutParams(layoutParams);
    PhotoInfo photoInfo = list.get(position);
    if (photoInfo != null) {
      ImageLoader.getInstance().displayImage(photoInfo.getPath_file(), viewHolder.image,
          optionsSquare);
      // PictureHelper.showPictureWithSquare(viewHolder.image, photoInfo.getPath_file());
      // UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),
      // photoInfo.getPath_file()),
      // new RotateImageViewAware(viewHolder.image,photoInfo.getPath_absolute()),
      // R.drawable.square_default_diagram);
    }
    return convertView;
  }

  public class ViewHolder {
    public ImageView image;
    public ImageView selectImage;
  }
}
