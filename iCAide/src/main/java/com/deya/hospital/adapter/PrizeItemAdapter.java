package com.deya.hospital.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.Prize;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

public class PrizeItemAdapter extends BaseAdapter implements OnClickListener {
	Context mcontext;
	private LayoutInflater inflater;
	LayoutParams para;
	List<Prize> list;
	
	DisplayImageOptions optionsSquare;
	float x1=0;
	float x2=0;
	int chooseIndex=0;
	String text="";
	int item_width=0;
	
	Resources res;
	int[] color_array;
	String[] string_array;
	
	
	/**
	 * Creates a new instance of MyImageListAdapter.
	 */
	public PrizeItemAdapter(Context context, List<Prize> list) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
		this.list = list;
		res=context.getResources();
		color_array=res.getIntArray(R.array.prize_status_colors);
		string_array=res.getStringArray(R.array.prize_status_string);
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
	
	public PrizeItemAdapter(Context context) {
		
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
			convertView = inflater.inflate(R.layout.adapter_item_prize, null);
			mviewHolder.lay_right=(RelativeLayout) convertView.findViewById(R.id.lay_right);
			mviewHolder.lay_text = (RelativeLayout) convertView
					.findViewById(R.id.lay_text);
			mviewHolder.img_left= (ImageView) convertView
					.findViewById(R.id.img_left);
			
			mviewHolder.text_name = (TextView) convertView
					.findViewById(R.id.text_name);
			
			mviewHolder.text_credit = (TextView) convertView.findViewById(R.id.text_credit);
			mviewHolder.text_time = (TextView) convertView.findViewById(R.id.text_time);
			mviewHolder.btn_fx = (Button) convertView.findViewById(R.id.btn_fx);
			mviewHolder.text_status = (TextView) convertView.findViewById(R.id.text_status);
			
			mviewHolder.btn_fx.setOnClickListener(this);
			
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		Prize prize=list.get(position);
		mviewHolder.btn_fx.setTag(prize);
		
		mviewHolder.text_name.setText(prize.getGoodsname());
		mviewHolder.text_credit.setText(prize.getIntegral()+"");		
		mviewHolder.text_time.setText(prize.getConvert_time());
		int status=prize.getConvert_status();
		status=status==3?2:status==2?1:0;
		
		mviewHolder.text_status.setText(string_array[status]);
		mviewHolder.text_status.setTextColor(color_array[status]);
		
//		if(status==3){
//			mviewHolder.text_status.setText(string_array[2]);
//			mviewHolder.text_status.setTextColor(color_array[2]);
//		}else if(status==2){
//			mviewHolder.text_status.setText(string_array[1]);
//			mviewHolder.text_status.setTextColor(color_array[1]);
//		} else{
//			mviewHolder.text_status.setText(string_array[0]);
//			mviewHolder.text_status.setTextColor(color_array[0]);
//		}
		
		mviewHolder.btn_fx.setText(String.format(
				res.getString(R.string.fx_add_credit), ""));
		
		
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
		TextView text_name,text_credit,text_time,text_status;
		RelativeLayout lay_text;
		Button btn_fx;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_fx:
			if(null!=this.itemListener){
				try {
					this.itemListener.OnButonClick((Prize)v.getTag());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			break;

		default:
			break;
		}
	}
	ItemListener itemListener;
	public void SetOnItemListener(ItemListener itemListener){
		this.itemListener=itemListener;
	}
	
	public interface ItemListener{
		public void OnButonClick(Prize prize);
	}
}
