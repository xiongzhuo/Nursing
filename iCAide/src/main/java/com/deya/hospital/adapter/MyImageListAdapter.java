package com.deya.hospital.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.vo.dbdata.Attachments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.List;

public class MyImageListAdapter extends BaseAdapter {
//  List<PicListVo> list;
  Context mcontext;
  private LayoutInflater inflater;
  LayoutParams para;
  List<Attachments> list;
  DisplayImageOptions optionsSquare;
  SupervisionActivity activity;
  int type=0;

  /**
   * Creates a new instance of MyImageListAdapter.
   */
  public MyImageListAdapter(Context context,List<Attachments> list) {
    this.list = list;
    inflater = LayoutInflater.from(context);
    mcontext = context;
    int[] wh = AbViewUtil.getDeviceWH(mcontext);
    para = new LayoutParams((wh[0] - dp2Px(mcontext, 40)) / 3, (wh[0] - dp2Px(mcontext, 40)) / 3);
    optionsSquare =
        new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_img)
            .showImageForEmptyUri(R.drawable.defult_img)
            .showImageOnFail(R.drawable.defult_img).resetViewBeforeLoading(true)
            .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new FadeInBitmapDisplayer(300)).build();
    activity=(SupervisionActivity) context;
  }
  public MyImageListAdapter(Context context, List<Attachments> list, int type) {
	    this.list = list;
	    inflater = LayoutInflater.from(context);
	    mcontext = context;
	    int[] wh = AbViewUtil.getDeviceWH(mcontext);
	    para = new LayoutParams((wh[0] - dp2Px(mcontext, 40)) / 3, (wh[0] - dp2Px(mcontext, 40)) / 3);
	    optionsSquare =
	        new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_img)
	            .showImageForEmptyUri(R.drawable.defult_img)
	            .showImageOnFail(R.drawable.defult_img).resetViewBeforeLoading(true)
	            .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
	            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
	            .displayer(new FadeInBitmapDisplayer(300)).build();
	    type=type;
	  }
  /**
   * getSDPath:【获取sd卡】. <br/>
   * .@return.<br/>
   */
  public static File getSdPath() {
    File sdDir = null;
    boolean sdCardExist = Environment.getExternalStorageState()
        .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
    if (sdCardExist) {
      sdDir =
          new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/deyaCache/");
    }
    return sdDir;
  }

  @Override
  public int getCount() {
    return list == null ? 0 : list.size();

  }

  @Override
  public Object getItem(int arg0) {
    // TODO 自动生成的方法存根
    return null;
  }

  @Override
  public long getItemId(int arg0) {
    // TODO 自动生成的方法存根
    return 0;
  }
  

  @Override
  public View getView(final int position, View convertView, ViewGroup arg2) {
    ViewHolder2 mviewHolder = null;
    if (convertView == null) {
      mviewHolder = new ViewHolder2();
      convertView = inflater.inflate(R.layout.sweetcircle_item_ttqimgview, null);
      mviewHolder.imgPic = (ImageView) convertView.findViewById(R.id.img_pic);
      mviewHolder.title=(TextView) convertView.findViewById(R.id.title);
      mviewHolder.delet=(ImageView) convertView.findViewById(R.id.delet);
      
      convertView.setTag(mviewHolder);
    } else {
      mviewHolder = (ViewHolder2) convertView.getTag();
    }
   // mviewHolder.title.setText(list.get(position).getState());
    mviewHolder.imgPic.setLayoutParams(para);
    if (!AbStrUtil.isEmpty(list.get(position).getFile_name())) {
		ImageLoader.getInstance().displayImage("file://"+list.get(position).getFile_name(),
				mviewHolder.imgPic, optionsSquare);
    } else {
      ImageLoader.getInstance().displayImage("", mviewHolder.imgPic, optionsSquare);
    }
    
    if(type==2){
    	mviewHolder.delet.setVisibility(View.GONE);
    }
    mviewHolder.delet.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			activity.deletPhoto(position);
		}
	});
    return convertView;
  }

  class ViewHolder2 {
    ImageView imgPic;
    ImageView delet;
    TextView title;
    TextView timeTv;
    LinearLayout ly;
    LinearLayout llPicparent;
  }

  public int dp2Px(Context context, int dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + (int) 0.5f);
  }
}
