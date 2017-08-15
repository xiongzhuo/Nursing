package com.deya.hospital.task.uploader;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.util.Constants;
import com.deya.hospital.workspace.TaskUtils;

public class ConsumptionUploder extends BaseUploader {

	@Override
	public boolean Upload(TaskVo task) {
		return sendConsumptionTask(task);
	}

	boolean sendConsumptionTask(TaskVo tv) {
		try {
			JSONObject job = new JSONObject();
			job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
			job.put("hospital", tv.getHospital());
			String str = tv.getTypes_info().toString();
			JSONArray jarr = new JSONArray(str);
			job.put("types_info", jarr);
			job.put("hospital", tv.getHospital());
			job.put("department", tv.getDepartment());

			job.put("mission_time", tv.getMission_time());

			JSONObject map = MainBizImpl.getInstance().sendSyncRequest("consume/syncConsumeAmount", job);
			if(map.has("result_id") && map.getString("result_id").equals("0")){
				tv.setTask_id(map.getInt("task_id"));
				tv.setStatus(0);
				TaskUtils.onUpdateTaskById(tv);
				return true;
			}
			
		} catch (Exception e) {
			Log.e("upload", e.getMessage()+"");
			e.printStackTrace();
		}
		
		return false;

	}
}
