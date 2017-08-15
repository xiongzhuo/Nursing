package com.deya.hospital.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.form.CreatReviewFormActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.dbdata.Attachments;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class ReviewRecordFileLisAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Attachments> list;
	private DisplayImageOptions optionsSquare;
	JSONArray jarr;
	Context mContext;
	CreatReviewFormActivity activity;
   
	public ReviewRecordFileLisAdapter(Context context, List<Attachments> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		mContext=context;
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defult_img)
				.showImageForEmptyUri(R.drawable.defult_img)
				.showImageOnFail(R.drawable.defult_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		activity=(CreatReviewFormActivity) context;
		for(Attachments att:list ){
			
		}
	}

	public void setData(List<Attachments> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder2 mviewHolder;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.filelist_item, null);
			mviewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_title);
			mviewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			mviewHolder.size=(TextView) convertView.findViewById(R.id.size);
			mviewHolder.recorderlayout=(LinearLayout) convertView.findViewById(R.id.recorderlayout);
			mviewHolder.recordimg=(ImageView) convertView.findViewById(R.id.recordimg);
			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		
		if (!AbStrUtil.isEmpty(list.get(position).getFile_type())) {
			int type = Integer.parseInt(list.get(position).getFile_type());
			File file=new File("file://" + list.get(position).getFile_name());
			Log.i("11111file.exists()", file.exists()+"");
			if(file.exists()){
				String size=	FormetFileSize(file.length());
				mviewHolder.size.setText(size);
			}
				mviewHolder.recorderlayout.setVisibility(View.VISIBLE);
				mviewHolder.img.setVisibility(View.GONE);
				Log.i("111111加载成功", list.get(position).getFile_name() + "");
			
				String timeStr=list.get(position).getTime();
				String time3=timeStr.substring(0,4);
				mviewHolder.title.setText(time3+"''");
				mviewHolder.recorderlayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mviewHolder.recordimg.setBackgroundResource(R.drawable.play);
						AnimationDrawable drawable = (AnimationDrawable) mviewHolder.recordimg
								.getBackground();
						drawable.start();

						// 播放音频
						
						playMusic(list.get(position).getFile_name(),mviewHolder.recordimg);
//						MediaManager.playSound(list.get(position).getFile_name(),
//								new MediaPlayer.OnCompletionListener() {
//
//									@Override
//									public void onCompletion(MediaPlayer mp) {
//										mviewHolder.recordimg.setBackgroundResource(R.drawable.adj);
//
//									}
//								});
					}
				});
				
				convertView.setOnClickListener(null);
		} else {
			mviewHolder.title.setText("");
		}
		
		
		
		return convertView;
	}

	class ViewHolder2 {
		public TextView title;
		public ImageView img,recordimg;
		
		public TextView size;
		public LinearLayout recorderlayout;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
	private static String FormetFileSize(long fileS)
	{
	DecimalFormat df = new DecimalFormat("#.00");
	String fileSizeString = "";
	String wrongSize="0B";
	if(fileS==0){
	return wrongSize;
	}
	if (fileS < 1024){
	fileSizeString = df.format((double) fileS) + "B";
	 }
	else if (fileS < 1048576){
	fileSizeString = df.format((double) fileS / 1024) + "KB";
	}
	else if (fileS < 1073741824){
	    fileSizeString = df.format((double) fileS / 1048576) + "MB";
	  }
	else{
	    fileSizeString = df.format((double) fileS / 1073741824) + "GB";
	  }
	return fileSizeString;
	}
	// 播放录音
		private MediaPlayer mMediaPlayer = new MediaPlayer();
	private void playMusic(String name,final ImageView view) {
		try {
			Log.i("111111111", name);
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					view.setBackgroundResource(R.drawable.adj);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private View viewanim;
	public void stoPplayMedia() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}
}
