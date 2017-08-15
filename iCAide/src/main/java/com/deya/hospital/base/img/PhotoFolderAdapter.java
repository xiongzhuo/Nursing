package com.deya.hospital.base.img;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.AlbumInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * .ClassName: PhotoFolderAdapter(相册适配器) <br/>
 * date: 2015年4月23日 下午5:39:21 <br/>
 * 
 * @author BMM
 */
public class PhotoFolderAdapter extends BaseAdapter {
  private LayoutInflater myInflater;
  private List<AlbumInfo> list;
  private ViewHolder viewHolder;
  DisplayImageOptions optionsSquare;

  /**
   * Creates a new instance of PhotoFolderAdapter.
   *
   * @param context 上下文
   * @param list 相册集合
   */
  public PhotoFolderAdapter(Context context, List<AlbumInfo> list) {
    myInflater = LayoutInflater.from(context);
    this.list = list;
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
  public Object getItem(int arg0) {
    return list.get(arg0);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = myInflater.inflate(R.layout.common_item_photofolder, null);
      viewHolder.image = (ImageView) convertView.findViewById(R.id.imageView);
      viewHolder.text = (TextView) convertView.findViewById(R.id.info);
      viewHolder.num = (TextView) convertView.findViewById(R.id.num);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    final AlbumInfo albumInfo = list.get(position);
    if (albumInfo != null) {
      ImageLoader.getInstance().displayImage(albumInfo.getPath_file(), viewHolder.image,
          optionsSquare);
      // UniversalImageLoadTool.disPlay(
      // ThumbnailsUtil.MapgetHashValue(albumInfo.getImage_id(), albumInfo.getPath_file()),
      // new RotateImageViewAware(viewHolder.image, albumInfo.getPath_absolute()),
      // R.drawable.square_default_diagram);
    }
    viewHolder.text.setText(albumInfo.getName_album());
//    viewHolder.num.setText("(" + list.get(position).getList().size()
//        + CommonUtils.getString(R.string.photo_folder_num) + ")");
    return convertView;
  }

  public class ViewHolder {
    public ImageView image;
    public TextView text;
    public TextView num;
  }
}
