package com.im.sdk.dy.ui.adapter;

import java.util.List;

import com.deya.acaide.R;
import com.deya.hospital.util.CircleImageView;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yuntongxun.ecsdk.im.ECGroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GKTeamAdapter extends BaseAdapter {
	private Context context;
	private List<ECGroup> data;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;
	
	
	
	public GKTeamAdapter(Context context, List<ECGroup> gorups) {
		super();
		this.context = context;
		this.data = gorups;
		
		optionsSquare_men = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_men_defalut)
		.showImageForEmptyUri(R.drawable.im_men_defalut)
		.showImageOnFail(R.drawable.im_men_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();

optionsSquare_women = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.im_women_defalut)
		.showImageForEmptyUri(R.drawable.im_women_defalut)
		.showImageOnFail(R.drawable.im_women_defalut)
		.resetViewBeforeLoading(true).cacheOnDisk(true)
		.considerExifParams(true).cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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

	
	private class ViewHold{
		CircleImageView avatar_iv;
		ImageView image_mute;
		EmojiconTextView nickname_tv,last_msg_tv;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold hold=null;
		if (convertView==null) {
			hold=new ViewHold();
			convertView=LayoutInflater.from(context).inflate(R.layout.im_conversation_item,null);
			hold.avatar_iv=(CircleImageView) convertView.findViewById(R.id.avatar_iv);
			hold.nickname_tv=(EmojiconTextView) convertView.findViewById(R.id.nickname_tv);
			hold.last_msg_tv=(EmojiconTextView) convertView.findViewById(R.id.last_msg_tv);
			hold.image_mute = (ImageView) convertView
					.findViewById(R.id.image_mute);
			
			convertView.setTag(hold);
		}else{
		hold=(ViewHold) convertView.getTag();
		}
		hold.nickname_tv.setText(data.get(position).getName());
		hold.image_mute
		.setVisibility(data.get(position).isNotice() ? View.GONE
				: View.VISIBLE);

		//hold.last_msg_tv.setText(data.get(position).getHospital());
		
//		Log.e("type", data.get(position).getType()+"");
//		int sex = data.get(position).getSex();
//		if (!AbStrUtil.isEmpty(data.get(position).getAvatar())) {
//			ImageLoader.getInstance().displayImage(
//					WebUrl.FILE_LOAD_URL + data.get(position).getAvatar(),
//					hold.avatar_iv,
//					sex == 1 ? optionsSquare_women : optionsSquare_men);
//
//		} else {
//			ImageLoader.getInstance().displayImage("",
//					hold.avatar_iv,
//					sex == 1 ? optionsSquare_women : optionsSquare_men);
//		}

		return convertView;
	}

}
