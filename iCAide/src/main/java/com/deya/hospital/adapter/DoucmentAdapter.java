package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DoucmentVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class DoucmentAdapter extends BaseAdapter {	
	private LayoutInflater inflater;
	List<DoucmentVo> list;
	Context context;
	private DisplayImageOptions optionsSquare;

	public DoucmentAdapter(Context context, List<DoucmentVo> list) {
		optionsSquare = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.defult_list_img)
		.showImageForEmptyUri(R.drawable.defult_list_img)
		.showImageOnFail(R.drawable.defult_list_img)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder=new ViewHolder();
			convertView = inflater.inflate(R.layout.doucment_list_item, null);
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.readNumTv = (TextView) convertView
					.findViewById(R.id.tv_readnum);
			viewHolder.publishTimeTv = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.img);
			viewHolder.author=(TextView) convertView.findViewById(R.id.author);
			viewHolder.company=(TextView) convertView.findViewById(R.id.company);
			viewHolder.creatCompany=(TextView) convertView.findViewById(R.id.creatcompany);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(!AbStrUtil.isEmpty(list.get(position).getTopic())){
			viewHolder.titleTv .setText(list.get(position).getTopic());
		}else{
			viewHolder.titleTv .setText("");
		}
		if(!AbStrUtil.isEmpty(list.get(position).getRead_count())){
			viewHolder.readNumTv .setText(list.get(position).getRead_count());
		}else{
			viewHolder.readNumTv .setText(0+"");
		}
		if(!AbStrUtil.isEmpty(list.get(position).getCreate_time())){
			viewHolder.publishTimeTv .setText(list.get(position).getCreate_time());
		}
//		if(!AbStrUtil.isEmpty(list.get(position).getTop_pic())){
//			ImageLoader.getInstance().displayImage(WebUrl.FILE_UPLOAD_URL+list.get(position).getTop_pic(), viewHolder.img, optionsSquare);;
//		}
		if(!AbStrUtil.isEmpty(list.get(position).getDrafting_person())){
			viewHolder.author .setText("起草人："+list.get(position).getDrafting_person());
			viewHolder.author .setVisibility(View.VISIBLE);
		}else{
			viewHolder.author .setVisibility(View.GONE);
		}
		if(!AbStrUtil.isEmpty(list.get(position).getPublish_unit())){
			viewHolder.company .setText("发布单位:"+list.get(position).getPublish_unit());
			viewHolder.company .setVisibility(View.VISIBLE);
		}else{
			viewHolder.company .setVisibility(View.GONE);
		}
		if(!AbStrUtil.isEmpty(list.get(position).getDrafting_unit())){
			viewHolder.creatCompany .setText("起草单位:"+list.get(position).getDrafting_unit());
			viewHolder.creatCompany .setVisibility(View.VISIBLE);
		}else{
			viewHolder.creatCompany .setVisibility(View.GONE);
		}
		
		String picType=list.get(position).getKind();
		ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL
				+ list.get(position).getTop_pic(), viewHolder.img, optionsSquare);
//		if(!AbStrUtil.isEmpty(picType)){
//			int kind=Integer.parseInt(picType);
//			switch (kind) {
//			case 1:
//				viewHolder.img.setImageResource(R.drawable.zhinan);
//				break;
//			case 2:
//				viewHolder.img.setImageResource(R.drawable.biaozhun);
//				break;
//			case 3:
//				viewHolder.img.setImageResource(R.drawable.fagui);
//				break;
//			case 4:
//				viewHolder.img.setImageResource(R.drawable.defult_list_img);
//				break;
//			case 5:
//				viewHolder.img.setImageResource(R.drawable.zhidu);
//				break;
//			case 6:
//				viewHolder.img.setImageResource(R.drawable.gongshi);
//				break;
//			case 7:
//				viewHolder.img.setImageResource(R.drawable.liucheng);
//				break;
//			case 8:
//				viewHolder.img.setImageResource(R.drawable.zhinan_foriner);
//				break;
//			case 9:
//				viewHolder.img.setImageResource(R.drawable.tongzhi);
//				break;
//			case 10:
//				viewHolder.img.setImageResource(R.drawable.gankong_plan);
//				break;
//			default:
//				viewHolder.img.setImageResource(R.drawable.other_kinds);
//				break;
//			}
//	
//		}else{
//			viewHolder.img.setImageResource(R.drawable.defult_list_img);
//		}
		
		return convertView;
	}

	public class ViewHolder {
		public ImageView img;
		public TextView titleTv;
		public TextView readNumTv;
		public TextView publishTimeTv;
		public TextView author;
		public TextView company;
		public TextView creatCompany;

	}
}
