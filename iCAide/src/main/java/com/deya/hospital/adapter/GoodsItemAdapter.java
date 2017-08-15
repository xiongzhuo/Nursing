package com.deya.hospital.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.Goods;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class GoodsItemAdapter extends BaseAdapter {
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<Goods> list;
	
	DisplayImageOptions optionsSquare;
	float x1=0;
	float x2=0;
	int chooseIndex=0;
	String text="";
	int item_width=0;
	
	Resources res;
	int[] color_array;
	
	
	/**
	 * Creates a new instance of MyImageListAdapter.
	 */
	public GoodsItemAdapter(Context context, List<Goods> list) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
		this.list = list;
		res=context.getResources();
		color_array=res.getIntArray(R.array.shop_item_colors);
		 optionsSquare =
			        new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.defult_img)
			            .showImageForEmptyUri(R.drawable.defult_img)
			            .showImageOnFail(R.drawable.defult_img).resetViewBeforeLoading(true)
			            .cacheOnDisk(true).considerExifParams(true).cacheInMemory(true)
			            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
			            .displayer(new FadeInBitmapDisplayer(300)).build();
		 
		
		
		
	    int[] wh = AbViewUtil.getDeviceWH(mcontext);
	    item_width=(wh[0] - dp2Px(mcontext, 110)) /3;
	    para = new LayoutParams(item_width ,dp2Px(context, 30));

	}
	
	
	public int item_x(){
		return item_width/2;
	}
	
	public GoodsItemAdapter(Context context) {
		
	}


	@Override
	public int getCount() {
         return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return 0;
	}
	
	private ViewHolder mviewHolder;
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		
		if (convertView == null) {
			mviewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_item_shop, null);
			mviewHolder.lay_right=(RelativeLayout) convertView.findViewById(R.id.lay_right);
			mviewHolder.lay_text = (RelativeLayout) convertView
					.findViewById(R.id.lay_text);
			mviewHolder.img_left= (ImageView) convertView
					.findViewById(R.id.img_left);
			
			mviewHolder.text_dec = (TextView) convertView
					.findViewById(R.id.text_dec);
			
			mviewHolder.text_credit = (TextView) convertView.findViewById(R.id.text_credit);
			mviewHolder.isNeedVipTv=(TextView) convertView.findViewById(R.id.need_vip);
			
			
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		Goods prize=list.get(position);
		mviewHolder.text_dec.setText(prize.getName());
		mviewHolder.text_credit.setText(prize.getIntegral()+"");		
		mviewHolder.lay_right.setBackgroundColor(color_array[position%6]);	
		if(!AbStrUtil.isEmpty(prize.getIs_sign()+"")&&prize.getIs_sign()==0){
			mviewHolder.isNeedVipTv.setVisibility(View.VISIBLE);
		}else{
			mviewHolder.isNeedVipTv.setVisibility(View.GONE);
		}
		
		
		
		if (!AbStrUtil.isEmpty(prize.getPicture())) {
			ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL+prize.getPicture(), mviewHolder.img_left, optionsSquare);
			
	    } else {
	    	ImageLoader.getInstance().displayImage("",
					mviewHolder.img_left, optionsSquare);
	    }
		
		return convertView;
	}

	class ViewHolder{
		ImageView img_left,img_ico;
		RelativeLayout lay_right;
		TextView text_dec,text_credit;
		RelativeLayout lay_text;
		TextView isNeedVipTv;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
