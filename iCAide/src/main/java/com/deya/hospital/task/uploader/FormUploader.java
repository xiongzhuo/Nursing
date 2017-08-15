package com.deya.hospital.task.uploader;

import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormDetailVo;
import com.deya.hospital.vo.FormItemsVo;
import com.deya.hospital.vo.SubItemlVo;
import com.deya.hospital.vo.Subrules;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormUploader extends BaseUploader {

	@Override
	public boolean Upload(TaskVo task) {
		return prepairSendFormTask(task);
	}
	
	private boolean prepairSendFormTask(TaskVo vo) {
		TaskVo remarktTv = null;
		List<FormDetailListVo> formList = gson.fromJson(vo
				.getItems().toString(),
				new TypeToken<List<FormDetailListVo>>() {
				}.getType());
			
			String remark = vo.getMain_remark();
			if(!AbStrUtil.isEmpty(remark)){
				remarktTv = gson.fromJson(remark, TaskVo.class);
				List<Attachments> atts=remarktTv.getAttachments();;
				if(null==atts){
					atts=new ArrayList<Attachments>();
				}
					for (Attachments att:atts) {
						if (!att.getState().equals("2")) {
							if(!super.uploadAttachment(att)){
								return false;
							}
						}
					}
				remarktTv.setAttachments(atts);
			}
			
			return sendformTaskList(formList, vo,  remarktTv);
		}

	
	private boolean sendformTaskList(List<FormDetailListVo> formSendList, TaskVo formTaskVo,TaskVo remarktTv) {
		JSONObject job = new JSONObject();
		String str = gson.toJson(getItems(formTaskVo,formSendList));
		JSONArray jarr2 = null;
		try {
			jarr2 = new JSONArray(str);
		} catch (JSONException e1) {
			Log.e("upload", e1.getMessage());
			e1.printStackTrace();
		}

		try {
			job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
			job.put("items", jarr2);
			job.put("tmp_id", formTaskVo.getTmp_id());
			job.put("grid_type", formTaskVo.getGrid_type());
			job.put("department", formTaskVo.getDepartment());
			job.put("mission_time", formTaskVo.getMission_time());
			job.put("hospital", MyAppliaction.getConstantValue(Constants.HOSPITAL_ID));
			String type = formTaskVo.getType();
			job.put("type", type);
			if (type.equals("5")) {
				job.put("ppost", formTaskVo.getPpost());
				job.put("uname", formTaskVo.getUname());
				job.put("work_type", formTaskVo.getWork_type());
				job.put("uteam", formTaskVo.getUteam());
				job.put("address", formTaskVo.getAddress());
			}
			if(remarktTv!=null){
				String remarkstr=gson.toJson(remarktTv);
				formTaskVo.setMain_remark(remarkstr);
				DataBaseHelper
				.getDbUtilsInstance(MyAppliaction.getContext())
				.update(formTaskVo, WhereBuilder.b("dbid", "=",formTaskVo.getDbid()));//防止文件重复上传
				job.put("main_remark",remarkstr);
			}
			JSONObject map = MainBizImpl.getInstance().sendSyncRequest("grid/submitGridInfos", job);
			if(map.has("result_id") && map.getString("result_id").equals("0")){
				formTaskVo.setTask_id(map.getInt("task_id"));
				formTaskVo.setMain_remark_id(map.optString("main_remark_id"));
				formTaskVo.setStatus(0);
				TaskUtils.onUpdateTaskById(formTaskVo);
				return true;
			}
			
		} catch (Exception e) {
			Log.e("upload", e.getMessage()+"");
			e.printStackTrace();
		}
		return false;
	}

	public List<FormItemsVo> getItems(TaskVo data,List<FormDetailListVo> list){
		List<FormItemsVo> itemList = new ArrayList<FormItemsVo>();
		if(!data.getType().equals("6")){

		for (FormDetailListVo fdl : list) {
			FormItemsVo itemVo = new FormItemsVo();
			itemVo.setId(fdl.getId());
			itemVo.setScore(fdl.getScore());
			List<SubItemlVo> sblist = new ArrayList<SubItemlVo>();
			for (FormDetailVo fdv : fdl.getSub_items()) {
				SubItemlVo sbv = new SubItemlVo();
				sbv.setId(fdv.getId());
				sbv.setResult(fdv.getResult());
				if (!AbStrUtil.isEmpty(fdv.getRemark())) {
					sbv.setIs_remark(1);
				}
				sbv.setRemark("");
				sbv.setScore(fdv.getScores());
				sblist.add(sbv);
			}
			itemVo.setSub_items(sblist);
			itemList.add(itemVo);
		}
		}else{
			for (FormDetailListVo fdl : list) {
				FormItemsVo itemVo = new FormItemsVo();
				itemVo.setId(fdl.getId());
				//itemVo.setScore(fdl.getScore());
				List<SubItemlVo> sblist = new ArrayList<SubItemlVo>();
				for (FormDetailVo fdv : fdl.getSub_items()) {
					SubItemlVo sbv = new SubItemlVo();
					sbv.setId(fdv.getId());
					sbv.setResult(fdv.getResult());
					if (!AbStrUtil.isEmpty(fdv.getRemark())) {
						sbv.setIs_remark(1);
					}
					sbv.setRemark("");
					sbv.setScore(fdv.getScores());
					if(fdv.getChooseItem()==-1){
						fdv.setChooseItem(0);
						fdv.getSub_rule().get(0).setChoosed(true);
					}
					for(Subrules subrules:fdv.getSub_rule()){
						if(subrules.isChoosed()){
							itemVo.setScore(itemVo.getScore()+subrules.getScore());
						}
					}
					sbv.setR_id(fdv.getSub_rule().get(fdv.getChooseItem())
							.getR_id()
							+ "");
					sblist.add(sbv);
				}
				itemVo.setSub_items(sblist);
				itemList.add(itemVo);
			}
		}
		return  itemList;
	}
}
