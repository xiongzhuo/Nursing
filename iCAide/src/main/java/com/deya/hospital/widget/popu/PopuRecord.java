package com.deya.hospital.widget.popu;

import android.annotation.SuppressLint;
import android.app.SearchManager.OnDismissListener;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnTimedTextListener;
import android.media.MediaRecorder;
import android.media.TimedText;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.img.PictureUtil;
import com.deya.hospital.util.RoundProgressBar;
import com.deya.hospital.util.SimpleSwitchButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PopuRecord implements OnClickListener,OnDismissListener {
	private MyPopu myPopu = null;

	private TextView btn_cancel, btn_enter,text_photo,text_choose,stateTv;
	private RelativeLayout playLayout;
	private LinearLayout lay1,takephotolay,chooseLocalLay,recordLay;
	private ImageView img_photo,img_choose;
	private Button button,playbtn;
	private RoundProgressBar progressBar;
	private OnPopuClick onPopuClick;
	private Context ctx;
	boolean showPhoto=true;
	private boolean isenter;
	LinearLayout photolay;
	SimpleSwitchButton setBtton;

	public PopuRecord(Context ctx,boolean showPhoto,boolean showSwitch,View view,OnPopuClick onPopuClick){
		myPopu = new MyPopu(ctx, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, R.layout.popwindow_record);
		this.ctx=ctx;
		this.onPopuClick=onPopuClick;
		btn_cancel=(TextView)myPopu.getTextView(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		
		btn_enter=(TextView)myPopu.getTextView(R.id.btn_enter);
		btn_enter.setOnClickListener(this);
		
		text_photo=(TextView)myPopu.getTextView(R.id.text_photo);
		text_photo.setOnClickListener(this);
		
		text_choose=(TextView)myPopu.getTextView(R.id.text_choose);
		text_choose.setOnClickListener(this);
		
		lay1=(LinearLayout)myPopu.getLinearLayout(R.id.lay1);
		lay1.setOnClickListener(this);
		
		takephotolay=(LinearLayout)myPopu.getLinearLayout(R.id.takephotolay);
		takephotolay.setOnClickListener(this);
		
		chooseLocalLay=(LinearLayout)myPopu.getLinearLayout(R.id.chooseLocalLay);
		chooseLocalLay.setOnClickListener(this);
		
		photolay=(LinearLayout)myPopu.getLinearLayout(R.id.photolay);
		if(!showPhoto){
			photolay.setVisibility(View.GONE);
		}
		
		recordLay=(LinearLayout)myPopu.getLinearLayout(R.id.recordLay);
		recordLay.setOnClickListener(this);
		
		img_photo=(ImageView)myPopu.getImageView(R.id.img_photo);
		img_photo.setOnClickListener(this);
		
		img_choose=(ImageView)myPopu.getImageView(R.id.img_choose);
		img_choose.setOnClickListener(this);
		
		button=(Button)myPopu.getButton(R.id.button);
		button.setOnClickListener(this);
		
		playLayout=(RelativeLayout)myPopu.getRelativeLayout(R.id.playLayout);
		playLayout.setOnClickListener(this);
		
		playbtn=(Button)myPopu.getButton(R.id.playbtn);
		playbtn.setOnClickListener(this);

		setBtton=(SimpleSwitchButton) myPopu.getSwitchBtn(R.id.setBtton);
		setBtton.setVisibility(showSwitch?View.VISIBLE:View.GONE);
		stateTv=(TextView)myPopu.getTextView(R.id.stateTv);

		progressBar=(RoundProgressBar)myPopu.getRoundProgressBar(R.id.playprogress);
		isenter=false;
		
		myPopu.setAlpha(200);
		myPopu.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
	}
	
	public void setSimpleSwichCheckListener(SimpleSwitchButton.SimpleSwitchInter listener){
		setBtton.setText("全部选中");
		setBtton.setText2("(督导选项-选择状态)");
		setBtton.setOncheckChangeListener(listener);
	}

	private void dismiss(){
		Log.i("record", "11111111111");
		if(null!=recorder&&recordStadio==RECORD_STADIO_ISSTART){
		
		//	recorder.stop();
			//recorder.release();
			handler.sendEmptyMessage(HD_RECORD_END);
		}
			if(null!=myPopu){
				myPopu.dismiss();
				myPopu=null;
			}
			
			this.onPopuClick.dismiss();
	}

	public void setDefultButtonState(boolean isCheck){
		setBtton.setCheck(isCheck);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
		case R.id.lay1:
			dismiss();
			this.onPopuClick.cancel();
			break;
		case R.id.takephotolay:
		case R.id.img_photo:
		case R.id.text_photo:
			//拍照
			onPhotographClick();
			dismiss();
			break;
		case R.id.chooseLocalLay:
		case R.id.img_choose:
		case R.id.text_choose:
			//相册
			this.onPopuClick.album();
			dismiss();
			break;
		case R.id.recordLay:
		case R.id.button:
			//录音
			record();
			
			
			this.onPopuClick.record();
			break;
		case R.id.playLayout:
		case R.id.playbtn:
			//播放
			play();
			
			
			this.onPopuClick.play();
			break;
		case R.id.btn_enter:
			if(!isenter){
				return;
			}
			
			if(recordStadio == RECORD_STADIO_PLAYING){
				endVoiceT = System.currentTimeMillis();
				totalTime = (float) ((endVoiceT - startVoiceT) / 1000);
				if (totalTime < 1) {
					return;
				}
			}
			
			if (totalTime >= 1) {
				String filename ="/sdcard/" + startVoiceT
						+ ".amr";
				startVoiceT = 0;
				
			//	addFile(filename, 2, totalTime + "");
				
				this.onPopuClick.enter(filename,totalTime);
			}
			recordStadio = RECORD_STADIO_INITIAL;
			
			dismiss();
			break;
		default:
			break;
		}

	}
	public int recordStadio = 0;
	private double startVoiceT, endVoiceT, totalTime;
	private void record(){
		if (recordStadio == RECORD_STADIO_INITIAL) {
			startVoiceT = System.currentTimeMillis();
			Log.i("11111111", "11111111111");
			initializeAudio(startVoiceT + "");
		
		} else {
			recordStop();
		}
	}
	
	private void recordStop(){
		
		handler.sendEmptyMessage(HD_RECORD_END);
	}
	
	private void play() {
		// TODO Auto-generated method stub
		if (recordStadio == RECORD_STADIO_STOPPLAY
				|| recordStadio == RECORD_STADIO_IS_STOPRECORD) {
			playMusic(startVoiceT + "");
			playbtn
					.setBackgroundResource(R.drawable.record_stop);
			startProgress();
			recordStadio = RECORD_STADIO_PLAYING;
			stateTv.setText("播放中...");
		} else {
			
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			playbtn.setBackgroundResource(R.drawable.play_btn);
			recordStadio = RECORD_STADIO_STOPPLAY;
			stateTv.setText("点击播放...");
		}
	}

	public interface OnPopuClick {
		public void enter(String filename,double totalTime);
		public void cancel();
		public void photograph(File file,List<String> resullist);
		public void album();
		public void record();
		public void play();
		public void dismiss();
		public void lonclick();
	}
	
	public static final int RECORD_STADIO_INITIAL = 0;// 未开始
	public static final int RECORD_STADIO_ISSTART = 1;// 已开始录制
	public static final int RECORD_STADIO_IS_STOPRECORD = 2;//
	public static final int RECORD_STADIO_PLAYING = 3;
	public static final int RECORD_STADIO_STOPPLAY = 4;
	public static final String MAX_UPLOAD_NUM = "maxUploadNum";
	public static int maxPhotoNums = 4;
	private static String mCurrentPhotoPath;
	private static final int REQUEST_TAKE_PHOTO = 0x0; // 拍照
	private static final int REQUEST_UPLOAD_PHOTO = 0x1; // 上传图片
	private static final int REQUEST_SELECT_LOCAL_PHOTO = 0x2; // 选择本地图片
	private static final int REQUEST_CUT_PHOTO = 0x3; // 剪切图片
	private static String imageId; // 图片id
	
	List<String> mimageIdList = new ArrayList<String>(); // 图片id
	List<String> msamllPathList = new ArrayList<String>(); // 图片路径

	private Uri mphotoUri; // 获取到的图片URI
	private int size = 0;
	
	List<String> resullist = new ArrayList<String>();

	
	public void onPhotographClick() {
		try {
			// 指定存放拍摄照片的位置
			File file = createImageFile();
			this.onPopuClick.photograph(file,resullist);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * createImageFile:【把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
	 * 照片的命名规则为：sheqing_20130125_173729.jpg】. <br/>
	 * .@return .@throws IOException.<br/>
	 */
	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "activity_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		resullist.add(mCurrentPhotoPath);
		return image;
	}
	
	
	// 录制
		MediaRecorder recorder;
		private Timer timerReciprocal;
		private void initializeAudio(String name) {
			Log.i("11111111", name);
			recorder = new MediaRecorder();// new出MediaRecorder对象
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置MediaRecorder的音频源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置MediaRecorder录制的音频格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 设置MediaRecorder录制音频的编码为amr.
			recorder.setOutputFile("/sdcard/" + name + ".amr");

			// 设置录制好的音频文件保存路径
			try {
				recorder.prepare();// 准备录制
				recorder.start();// 开始录制
				
				button.setBackgroundResource(R.drawable.record_stop);
				recordStadio = RECORD_STADIO_ISSTART;
				
				isenter=false;
				startProgressR();
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// 播放录音
		private MediaPlayer mMediaPlayer = new MediaPlayer();

		@SuppressLint("NewApi")
		private void playMusic(String name) {
			String filename = "file://" + "/sdcard/" + name + ".amr";
			Log.i("11111111", filename);
			try {
				Log.i("111111111", filename);
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.stop();
				}
				
				try {
					mMediaPlayer.reset();
					mMediaPlayer.setDataSource(filename);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (IllegalStateException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				mMediaPlayer.setOnTimedTextListener(new OnTimedTextListener() {

					@Override
					public void onTimedText(MediaPlayer mp, TimedText text) {

						Log.i("progress", text + "");
					}
				});
				
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						progressBar.setProgress(0);
						timerReciprocal.cancel();
						playbtn.setBackgroundResource(R.drawable.play_btn);
						recordStadio = RECORD_STADIO_STOPPLAY;

					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
		private void startProgress() {

			TimerTask task = new TimerTask() {

				private float second = 0;

				@Override
				public void run() {
					{
						second += 0.2;
						Message message = new Message();
						message.obj = second;
						message.arg2 = 1;
						message.what=HD_PLAY;
						handler.sendMessage(message);
					}
				}
			};
			timerReciprocal = new Timer(false);
			timerReciprocal.schedule(task, 0, 200);
		}
		private Timer timerReciprocalR;
		private void startProgressR() {

			TimerTask task = new TimerTask() {

				private float second = 0;

				@Override
				public void run() {
					{
						
						second += 1;
						Message message = new Message();
						message.obj = second;
						message.what=HD_RECORD;
						handler.sendMessage(message);
						
					}
				}
			};
			timerReciprocalR = new Timer(false);
			timerReciprocalR.schedule(task, 0, 1000);
		}
		
		private final int HD_PLAY=0x01;
		private final int HD_RECORD=0x02;
		private final int HD_RECORD_END=0x03;
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case HD_PLAY:
					float a = (Float) msg.obj;
					int progress = (int) ((a / totalTime) * 100);
					Log.i("progress", a + "=====" + totalTime);
					if (recordStadio == RECORD_STADIO_PLAYING
							|| recordStadio == RECORD_STADIO_STOPPLAY) {

						Log.i("progress", progress + "");
						progressBar.setProgress(progress);

					} else {
						progressBar.setProgress(0);
						timerReciprocal.cancel();
					}
					break;
				case HD_RECORD:
					float t = (Float) msg.obj;
					if(t<60){
						stateTv.setText("录音中..."+t+"秒");
					}else{
						
						recordStop();
					}
					break;
				case HD_RECORD_END:
					endVoiceT = System.currentTimeMillis();
					totalTime = (float) ((endVoiceT - startVoiceT) / 1000);
					if (totalTime < 1) {
						return;
					}
					isenter=true;
					//btn_enter.setEnabled(true);
					recorder.stop();
					recorder.release();
					recordLay.setVisibility(View.GONE);
					playLayout.setVisibility(View.VISIBLE);
					recordStadio = RECORD_STADIO_IS_STOPRECORD;
					stateTv.setText("点击播放");
					
					timerReciprocalR.cancel();
					break;

				default:
					break;
				}
				

			}

		};

		@Override
		public void onDismiss() {
			
			
		}
}
