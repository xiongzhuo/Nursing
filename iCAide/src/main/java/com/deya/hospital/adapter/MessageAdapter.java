package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.picMessageVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
	// List<PicListVo> list;
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<picMessageVo> list;
	DisplayImageOptions optionsSquare;

	/**
	 * Creates a new instance of MyImageListAdapter.
	 */
	public MessageAdapter(Context context, List<picMessageVo> list) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
		this.list = list;
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defult_list_img)
				.showImageForEmptyUri(R.drawable.defult_list_img)
				.showImageOnFail(R.drawable.defult_list_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		

	}

	/**
	 * getSDPath:【获取sd卡】. <br/>
	 * .@return.<br/>
	 */
	public static File getSdPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/deyaCache/");
		}
		return sdDir;
	}

	@Override
	public int getCount() {
		return list == null ?0: list.size()<=1?0 : list.size()-1;

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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder2 mviewHolder = null;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.adapter_message_picmessage,
					null);
			mviewHolder.title = (TextView) convertView
					.findViewById(R.id.message_title);
			mviewHolder.img = (ImageView) convertView.findViewById(R.id.imge);
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		mviewHolder.title.setText(list.get(position+1).getTopic());
		if (!AbStrUtil.isEmpty(list.get(position).getTop_pic())) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL+list.get(position+1).getTop_pic(),
					mviewHolder.img, optionsSquare);
		}else{
			ImageLoader.getInstance().displayImage(
					"",
					mviewHolder.img, optionsSquare);
		}
		
		return convertView;
	}

	int selection = -1;

	public void serBgColor(int selection) {
		this.selection = selection;
		notifyDataSetChanged();
	}

	class ViewHolder2 {
		private ImageView img;
		private TextView title;

	}

}
