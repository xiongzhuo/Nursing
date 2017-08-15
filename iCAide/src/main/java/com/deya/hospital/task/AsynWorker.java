package com.deya.hospital.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.vo.dbdata.Attachments;

import org.json.JSONObject;

import java.util.List;

public class AsynWorker extends AsyncTask<String, Integer, String> {
	List<Attachments> supervisorFileList;
	Context mcontext = MyAppliaction.getContext();
	FinishInter nofity;

	public AsynWorker( List<Attachments> supervisorFileList, FinishInter nofity) {

		super();
		this.nofity = nofity;
		this.supervisorFileList=supervisorFileList;
	}

	// onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {
		int status = progresses[0];
		Log.i("upload", "onProgressUpdate(Progress... progresses) called");

	}

	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result) {
		if(result.equals("0")){
			nofity.onWorkFinish(supervisorFileList);
		}else{
			nofity.onWorkFail();
		}

	}



	@Override
	protected String doInBackground(String... params) {


		for (Attachments att : supervisorFileList) {
			att.setImgId(att.getFile_name());
			if (!att.getState().equals("2")) {

				if (!uploadAttachment(att)) {
					return "1";
				}
			}
		}
		return "0";
	}
	boolean uploadAttachment(Attachments att) {
		String remoteFileName = null;
		try {
			JSONObject map = UploadBizImpl.getInstance().syncUploadPicture(att.getFile_name());
			if (null != map && map.has("data")) {
				remoteFileName = map.getString("data");
			}
		} catch (Exception e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}

		if (remoteFileName == null) {
			return false;
		} else {
			att.setFile_name(remoteFileName);
			att.setState("2");
			att.setDate("");
			att.setImgId("");
			return true;
		}
	}

	public  interface FinishInter{
		public void onWorkFinish(List<Attachments> supervisorFileList);
		public void onWorkFail();
	}
}
