package com.im.sdk.dy.ui.adapter;

import java.util.ArrayList;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CircleImageView;
import com.deya.hospital.util.WebUrl;
import com.im.sdk.dy.storage.ContactSqlManager;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;
import com.im.sdk.dy.ui.contact.ECContacts;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GKExpertAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ECContacts> data;
	DisplayImageOptions optionsSquare_men;
	DisplayImageOptions optionsSquare_women;
	int type;

	public GKExpertAdapter(Context context, ArrayList<ECContacts> data, int type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;

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

	private class ViewHold {
		CircleImageView avatar_iv;
		EmojiconTextView nickname_tv, last_msg_tv;
		TextView name2Tv;
		View bottomline;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold hold = null;
		if (convertView == null) {
			hold = new ViewHold();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.im_conversation_item, null);
			hold.avatar_iv = (CircleImageView) convertView
					.findViewById(R.id.avatar_iv);
			hold.nickname_tv = (EmojiconTextView) convertView
					.findViewById(R.id.nickname_tv);
			hold.last_msg_tv = (EmojiconTextView) convertView
					.findViewById(R.id.last_msg_tv);
			hold.name2Tv = (TextView) convertView.findViewById(R.id.name2Tv);
			hold.bottomline=convertView.findViewById(R.id.bottomline);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}
		if(position==data.size()-1){
			hold.bottomline.setVisibility(View.GONE);
		}else{
			hold.bottomline.setVisibility(View.VISIBLE);
		}
		hold.nickname_tv.setText(data.get(position).getNickname());
		ECContacts contacts = ContactSqlManager.getContact(data.get(position)
				.getId());

		if (type == 2) {
			if (null != contacts && null != contacts.getRegis_job()) {
				hold.name2Tv.setText(contacts.getRegis_job());
			} else {
				hold.name2Tv.setText("");
			}
		} else {
			hold.name2Tv.setText("");
			hold.last_msg_tv.setText(data.get(position).getHospital());
		}

		Log.e("type", data.get(position).getType() + "");
		int sex = data.get(position).getSex();
		if (!AbStrUtil.isEmpty(data.get(position).getAvatar())) {
			ImageLoader.getInstance().displayImage(
					WebUrl.FILE_LOAD_URL + data.get(position).getAvatar(),
					hold.avatar_iv,
					sex == 1 ? optionsSquare_women : optionsSquare_men);

		} else {
			ImageLoader.getInstance().displayImage("", hold.avatar_iv,
					sex == 1 ? optionsSquare_women : optionsSquare_men);
		}

		return convertView;
	}

}
