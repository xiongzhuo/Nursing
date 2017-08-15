package com.deya.hospital.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.ShowNetWorkImageActivity;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class FileLisAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Attachments> list;
	private DisplayImageOptions optionsSquare;
	JSONArray jarr;
	Context mContext;
	HandWashTasksActivity activity;

	public FileLisAdapter(Context context, List<Attachments> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		mContext = context;
		activity = (HandWashTasksActivity) context;
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defult_img)
				.showImageForEmptyUri(R.drawable.defult_img)
				.showImageOnFail(R.drawable.defult_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();


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
			mviewHolder.size = (TextView) convertView.findViewById(R.id.size);
			mviewHolder.recorderlayout = (LinearLayout) convertView
					.findViewById(R.id.recorderlayout);
			mviewHolder.recordimg = (ImageView) convertView
					.findViewById(R.id.recordimg);
			mviewHolder.delet = (ImageView) convertView
					.findViewById(R.id.delet);
			//mviewHolder.line=convertView.findViewById(R.id.line);

			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}

		mviewHolder.delet.setVisibility(View.GONE);
		final Attachments att=list.get(position);
		//mviewHolder.line.setVisibility(View.GONE);
		if (!AbStrUtil.isEmpty(list.get(position).getFile_type())) {
			int type = Integer.parseInt(list.get(position).getFile_type());
			File file = new File(list.get(position).getFile_name());
			if (file.exists()) {
				String size = FormetFileSize(file.length());
				mviewHolder.size.setText(size);
			}

			switch (type) {
				case 1:
					String  imgurl="";
					if(att.getState().equals("2")){
						imgurl= WebUrl.FILE_LOAD_URL + att.getFile_name();
					}else{
						imgurl="file://" + list.get(position).getFile_name();
					}
					final String[] strings = {imgurl };
					ImageLoader.getInstance().displayImage(imgurl
							,
							mviewHolder.img, optionsSquare);

					mviewHolder.recorderlayout.setVisibility(View.GONE);
					mviewHolder.img.setVisibility(View.VISIBLE);
					convertView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent it = new Intent(mContext,
									ShowNetWorkImageActivity.class);

							it.putExtra("urls", strings);
							it.putExtra("nowImage", 0);
							mContext.startActivity(it);

						}
					});
					break;
				case 2:
					mviewHolder.recorderlayout.setVisibility(View.VISIBLE);
					mviewHolder.img.setVisibility(View.GONE);
					if(att.getState().equals("1")){

						String timeStr = list.get(position).getTime() + "";
						if (timeStr.length() < 3) {
							timeStr = timeStr + "000";
						}
						String time3 = timeStr.substring(0, 3);
						mviewHolder.title.setText(time3 + "''");

					}
					mviewHolder.recorderlayout
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									mviewHolder.recordimg
											.setBackgroundResource(R.drawable.play);
									AnimationDrawable drawable = (AnimationDrawable) mviewHolder.recordimg
											.getBackground();
									drawable.start();

									// 播放音频

									if(att.getState().equals("1")){
										playMusic(list.get(position).getFile_name(),
												mviewHolder.recordimg);
									}else{
										//activity.playRecord(list.get(position).getFile_name(),mviewHolder.recordimg);
									}
									// MediaManager.playSound(list.get(position).getFile_name(),
									// new MediaPlayer.OnCompletionListener() {
									//
									// @Override
									// public void onCompletion(MediaPlayer mp) {
									// mviewHolder.recordimg.setBackgroundResource(R.drawable.adj);
									//
									// }
									// });
								}
							});



					convertView.setOnClickListener(null);

					break;
				default:
					break;
			}
		} else {
			mviewHolder.title.setText("");
		}
		String date = list.get(position).getDate();
		if (!AbStrUtil.isEmpty(date)) {
			mviewHolder.title.setText(date);
		}

		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				activity.showDeletFile(list.get(position).getFile_name(),
						position);
				return false;
			}
		});
		return convertView;
	}

	class ViewHolder2 {
		public TextView title;
		public ImageView img, recordimg,delet;

		View line;
		public TextView size;
		public LinearLayout recorderlayout;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}

	private static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	// 播放录音
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	public  void playMusic(String name, final ImageView view) {
		try {
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
