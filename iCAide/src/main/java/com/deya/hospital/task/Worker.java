package com.deya.hospital.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.task.notify.WorkerFinishedNotify;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;

import java.util.List;

public class Worker extends AsyncTask<String, Integer, String> {
	Context mcontext = MyAppliaction.getContext();
	WorkerFinishedNotify nofity = null;

	public Worker(WorkerFinishedNotify notify) {
		super();
		this.nofity = notify;
	}

	// onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {
		int status = progresses[0];
		Log.i("upload", "onProgressUpdate(Progress... progresses) called");
		if (this.nofity != null) {
			if (status == 100) {
				this.nofity.Finshed(null);
			} else if (status == 80) {
				this.nofity.workFinish();
			} else {
				this.nofity.RefreshNetwork();

			}

		}
	}

	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result) {
		Log.i("upload", "onPostExecute(Result result) called");

	}

	private void run() {
		Log.i("upload", "run start");
		boolean uploaded = false;
		List<TaskVo> tasks = Tasker.getUnUploadedTask();
		boolean isform = false;
		for (TaskVo task : tasks) {
			int type = Integer.parseInt(task.getType());
			if(task.getType()==null){
				continue;
			}

			BaseUploader uploader = BaseUploader.getUploader(task);
			if (uploader != null) {
				if (uploader.Upload(task)) {
//					isform = (type == 3 || type == 5 || type == 6);
//					if (isform) {
//						this.publishProgress(100);
//					} else {
						uploaded = true;

				//	}
					task.setStatus(0);
					TaskUtils.onUpdateTaskById(task);
				}else{
					//task.setStatus(2);
					task.setFailNum(task.getFailNum()+1);
					Log.i("worker",task.getFailNum()+"");
					if(task.getFailNum()>=5){
						task.setStatus(4);
					}
					TaskUtils.onUpdateTaskById(task);
					this.publishProgress(100);
				}
			}
		}
		Log.i("upload", "run end");

		if (uploaded) {
			this.publishProgress(100);
		}
	}

	@Override
	protected String doInBackground(String... params) {
		if(NetWorkUtils.isWifiState(MyAppliaction.getContext())){
			run();
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			this.publishProgress(80);
			return null;
		}else{
			this.publishProgress(80);
			return null;
		}

	}

}
