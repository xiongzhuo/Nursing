package com.deya.hospital.downloader;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;

public class DownProgressBarDialog extends BaseDialog {
	public DownProgressBarDialog(Context context) {
		super(context);
	}

	Context mContext;
	String path; 
	File filePath;
	DownInterface downInter;
	public DownProgressBarDialog(Context context,String path,File filePath,DownInterface downInter) {
		super(context);
		mContext=context;
		this.path=path;
		this.filePath=filePath;
		this.downInter=downInter;
	}

	private static final int PROCESSING = 1;
	private static final int FAILURE = -1;

	private TextView resultView;
	private Button downloadButton;
	private Button stopButton;
	private ProgressBar progressBar;
	private TextView calcleApp;

	private Handler handler = new UIHandler();

	private final class UIHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PROCESSING: // ���½��
				progressBar.setProgress(msg.getData().getInt("size"));
				float num = (float) progressBar.getProgress()
						/ (float) progressBar.getMax();
				int result = (int) (num * 100); // ������
				resultView.setText(result + "%");
				if (progressBar.getProgress() == progressBar.getMax()) {
					downInter.onDownLoadComplet();
				}
				Log.i("11111111111111", progressBar.getProgress()+"--------"+progressBar.getMax());
				break;
			case FAILURE: // ����ʧ��
				Toast.makeText(mContext,"下载错误",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_downloder_progress);
		resultView = (TextView) findViewById(R.id.resultView);
		downloadButton = (Button) findViewById(R.id.downloadbutton);
		stopButton = (Button) findViewById(R.id.stopbutton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		calcleApp=(TextView) this.findViewById(R.id.calcleApp);
		
		ButtonClickListener listener = new ButtonClickListener();
		downloadButton.setOnClickListener(listener);
		stopButton.setOnClickListener(listener);
		calcleApp.setOnClickListener(listener);
		listener.startDownload();
	}

	private final class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.downloadbutton: // ��ʼ����
				startDownload();
				break;
			case R.id.stopbutton: // ��ͣ����
				exit();
				Toast.makeText(mContext,
						"Now thread is Stopping!!", Toast.LENGTH_LONG).show();
				downloadButton.setEnabled(true);
				stopButton.setEnabled(false);
				break;
			case R.id.calcleApp:
				downInter.onDownLoadCancle();
				break;
			}
		}

	
		
		
		
		private DownloadTask task;

		private void exit() {
			if (task != null)
				task.exit();
		}
		public void startDownload(){
			String filename = path.substring(path.lastIndexOf('/') + 1);

			try {
				filename = URLEncoder.encode(filename, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			path = path.substring(0, path.lastIndexOf("/") + 1) + filename;
				download(path, filePath);
			downloadButton.setEnabled(false);
			stopButton.setEnabled(true);
		}
		private void download(String path, File savDir) {
			task = new DownloadTask(path, savDir);
			new Thread(task).start();
		}

		/**
		 * 
		 * UI�ؼ�������ػ�(����)�������̸߳�����ģ���������߳��и���UI�ؼ���ֵ�����º��ֵ�����ػ浽��Ļ��
		 * һ��Ҫ�����߳������UI�ؼ���ֵ�������������Ļ����ʾ���������������߳��и���UI�ؼ���ֵ
		 * 
		 */
		private final class DownloadTask implements Runnable {
			private String path;
			private File saveDir;
			private FileDownloader loader;

			public DownloadTask(String path, File saveDir) {
				this.path = path;
				this.saveDir = saveDir;
			}

			/**
			 * �˳�����
			 */
			public void exit() {
				if (loader != null)
					loader.exit();
			}

			DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
				@Override
				public void onDownloadSize(int size) {
					Message msg = new Message();
					msg.what = PROCESSING;
					msg.getData().putInt("size", size);
					handler.sendMessage(msg);
				}
			};

			public void run() {
				try {
					loader = new FileDownloader(mContext, path,
							saveDir, 3);
					progressBar.setMax(loader.getFileSize());
					if(loader.getExit()){
						Log.i("11111111111存在完整文件", "存在完整文件");
						downInter.onDownLoadComplet();
						
					}else{
					loader.download(downloadProgressListener);
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendMessage(handler.obtainMessage(FAILURE)); // ����һ������Ϣ����
				}
			}
		}
	}
public interface DownInterface{
	public void   onDownLoadComplet();
	public void   onDownLoadCancle();
	
}
}
