package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.vo.dbdata.subTaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class HandTaskUploader extends BaseUploader{

	@Override
	public boolean Upload(TaskVo task) {
		return uploadLoacalTask(task);
	}

	boolean uploadLoacalTask(TaskVo tavo) {
		List<planListDb> pdblist = tavo.subTasks();
		Log.i("sendhandtask", tavo.getDbid() + "-----");
		if(null==pdblist){
			return false;
		}
		for (planListDb pdb : pdblist) {
			pdb.setMission_time(tavo.getMission_time());
			List<subTaskVo> ls = pdb.getSubTasks();
			if (null == ls) {
				continue;
			}
			for (subTaskVo sv : pdb.getSubTasks()) {
				if (null != sv.getAttachments()) {
					for (Attachments att : sv.getAttachments()) {
						if (!att.getState().equals("2")) {
							if(!uploadAttachment(att)){
								return false;
							}

						}
					}
				}
			}
		}

		//upload task
		return uploadHandTask(pdblist, tavo);
	}

	boolean uploadHandTask(List<planListDb> pdblist, TaskVo pripartavo) {
		Log.i("upload", "upload hand task");
		try {
			JSONObject job = new JSONObject();
			//转换成json array
			String str = gson.toJson(pdblist);
			pripartavo.setFiveTasks(str);
			DataBaseHelper
					.getDbUtilsInstance(MyAppliaction.getContext())
					.update(pripartavo, WhereBuilder.b("dbid", "=",pripartavo.getDbid()));//防止文件重复上传
			JSONArray jarr2 = new JSONArray(str);
			job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
			job.put("fiveTasks", jarr2);
			if (null != pripartavo.getRemark()) {
				job.put("remark", pripartavo.getRemark());
			}
//			if (null != pripartavo.getTraining_recycle()) {
//				job.put("training_recycle", pripartavo.getTraining_recycle());
//			}
//			if (null != pripartavo.getIs_training()) {
//				job.put("is_training", pripartavo.getIs_training());
//			}
//			if (null != pripartavo.getEquip_examine()) {
//				job.put("equip_examine", pripartavo.getEquip_examine());
//			}
//			if (null != pripartavo.getFeedback_obj()) {
//				job.put("getFeedback_obj", pripartavo.getFeedback_obj());
//			}
			String spStr=TaskUtils.gson.toJson(pripartavo);
			TaskVo.SupervisorQuestion supervisorQuestion =TaskUtils.gson.fromJson(spStr,TaskVo.SupervisorQuestion.class);
			supervisorQuestion.setOrigin(1);
			supervisorQuestion.setCreate_time(pripartavo.getMission_time());
			if (pripartavo.getTask_id() != 0) {
				job.put("task_id", pripartavo.getTask_id() + "");
			}
			if(!AbStrUtil.isEmpty(pripartavo.getCheck_content())){
			job.put("supervisor_question",TaskUtils.gson.toJson(supervisorQuestion));
			}
			job.put("department",pripartavo.getDepartment());
			if(TaskUtils.isOtherJob(MyAppliaction.getContext())){
				job.put("check_type","20");
			}else if(TaskUtils.mysticalJob(MyAppliaction.getContext())){
				job.put("check_type","30");
			}else{
				job.put("check_type","10");
			}
			Log.i("upload-------",job.optString("check_type"));
			job.put("hospital", MyAppliaction.getConstantValue(Constants.HOSPITAL_ID));
			job.put("mission_time",pripartavo.getMission_time());
			job.put("task_type",pripartavo.getTask_type());
			JSONObject map = MainBizImpl.getInstance().sendSyncRequest("task/saveFiveTaskInfo", job);
			if(map.has("result_id") && map.optString("result_id").equals("0")){
				pripartavo.setTask_id(map.getInt("task_id"));
				pripartavo.setStatus(0);
				TaskUtils.onUpdateTaskById(pripartavo);
				return true;
			}

		} catch (Exception e) {
			Log.e("upload", e.getMessage()+"");
			e.printStackTrace();
			return false;
		}

		return false;
	}

}
