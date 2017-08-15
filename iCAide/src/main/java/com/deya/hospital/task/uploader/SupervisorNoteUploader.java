package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SupervisorNoteUploader extends BaseUploader {

	@Override
	public boolean Upload(TaskVo task) {
		return prepairSendSupervisorTask(task);
	}

	private boolean prepairSendSupervisorTask(TaskVo tavo) {
		String str = tavo.getFileList();
		List<Attachments> supervisorFileList = gson.fromJson(str,
				new TypeToken<List<Attachments>>() {
				}.getType());

		for (Attachments att : supervisorFileList) {
			att.setImgId(att.getFile_name());
			if (!att.getState().equals("2")) {

				if (!uploadAttachment(att)) {
				}
			}
		}

		return sendSupervisorTask(tavo, supervisorFileList);
	}

	boolean sendSupervisorTask(TaskVo tv, List<Attachments> supervisorFileList) {
		try {
			JSONObject job = new JSONObject();
			job.put("authent",
					MyAppliaction.getConstantValue(Constants.AUTHENT));
			job.put("check_content", tv.getCheck_content());
			job.put("exist_problem", tv.getExist_problem());
			job.put("deal_suggest", tv.getDeal_suggest());
			job.put("improve_suggest", tv.getImprove_suggest());
			if (tv.getTask_id() != 0) {
				job.put("task_id", tv.getTask_id() + "");
			}
			Log.i("111111", tv.getTask_id() + "--------------" + tv.getDbid());

			if (supervisorFileList.size() > 0) {
				String str = gson.toJson(supervisorFileList);
				tv.setFileList(str);
				JSONArray jarr = new JSONArray(str);
				job.put("attachments", jarr);
				DataBaseHelper
						.getDbUtilsInstance(MyAppliaction.getContext())
						.update(tv, WhereBuilder.b("dbid", "=", tv.getDbid()));// 防止文件重复上传
			}
			job.put("department", tv.getDepartment());
			job.put("hospital", tv.getHospital());
			job.put("mission_time", tv.getMission_time());

			JSONObject map = MainBizImpl.getInstance().sendSyncRequest(
					"supervisor/submitSupervisor", job);
			if (map != null && map.has("result_id")
					&& map.getString("result_id").equals("0")) {
				if (!AbStrUtil.isEmpty(map.getString("task_id"))) {
					tv.setTask_id(map.getInt("task_id"));
				}
				tv.setStatus(0);
				TaskUtils.onUpdateTaskById(tv);
				return true;
			}

		} catch (Exception e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
			return false;
		}

		return false;

	}
}
