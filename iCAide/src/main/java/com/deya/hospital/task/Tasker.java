package com.deya.hospital.task;

import android.content.Context;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class Tasker {

	static Context mcontext = MyAppliaction.getContext();

	/**
	 * 获取所有的task
	 *
	 * @return
	 */
	public static List<TaskVo> getUnUploadedTask() {
		List<TaskVo> list = null;
		try {
			int status = 1;
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where("status", "=", status)
									.and(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)))
									.orderBy("dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
		if (list == null) {
			list = new ArrayList<TaskVo>();
		}

		return list;
	}
	public static List<TaskVo> getNotInserviceTask() {
		List<TaskVo> list = null;
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where("status", "!=", 0)
									.and(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)))
									.orderBy("dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
		if (list == null) {
			list = new ArrayList<TaskVo>();
		}

		return list;
	}
	public static List<TaskVo> getAllLocalTaskByType(String type) {
		List<TaskVo> list = null;
		Log.i("info",
				"mobile: " + MyAppliaction.getConstantValue(Constants.MOBILE));
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE))).and(WhereBuilder.b("status", "!=", 0))
									.and(WhereBuilder.b("type", "=", type)).orderBy(
									"status desc, mission_time desc, dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<TaskVo>();
		}
		return list;
	}
	public static List<TaskVo> getFailUploadTaskByType(String type) {
		List<TaskVo> list = null;
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where("status", "=", 1)
									.and(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE))).and(WhereBuilder.b("type","=",type)).orderBy("dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
		if (list == null) {
			list = new ArrayList<TaskVo>();
		}

		return list;
	}
	public static List<TaskVo> getUploadedTask() {
		List<TaskVo> list = null;
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where("status", "=", 0));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
		if (list == null) {
			list = new ArrayList<TaskVo>();
		}

		return list;
	}

	public static List<TaskVo> getAllLocalTask() {
		List<TaskVo> list = null;
		Log.i("info",
				"mobile: " + MyAppliaction.getConstantValue(Constants.MOBILE));
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)))
									.orderBy(
											"status desc, mission_time desc, dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<TaskVo>();
		}
		return list;
	}
	public static List<TaskVo> getLocalTaskByDate(String time1,String time2,String type) {
		List<TaskVo> list = null;
		Log.i("info",
				"mobile: " + MyAppliaction.getConstantValue(Constants.MOBILE));
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)))
									.and(WhereBuilder.b(
											"mission_time",
											">=",
											time1)).and(WhereBuilder.b(
									"mission_time",
									"<=",
									time2)).and(WhereBuilder.b(
									"type",
									"=",
									type)).and(WhereBuilder.b(
									"status", "!=", 0)).orderBy(
											"status desc, mission_time desc, dbid"));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<TaskVo>();
		}
		return list;
	}
	static Gson gson = new Gson();

	public static void syncNetworkTask(List<TaskVo> tasks) {
		try {
			int status = 0; // 已上传
			List<TaskVo> list=getUploadedTask();
			if(null==list){

				list=new ArrayList<TaskVo>();
			}
//			DataBaseHelper
//					.getInstance()
//					.getDbUtilsInstance(mcontext)
//					.delete(TaskVo.class, WhereBuilder.b("status", "=", 0));


			for (TaskVo task : tasks) {
				task.setMobile(MyAppliaction.getConstantValue(Constants.MOBILE));
				if ("2".equals(task.getType())) {
					List<Attachments> attachments = task.getAttachments();
					if (attachments.size() > 0) {
						task.setFileList(gson.toJson(attachments));
					}
				} else if ("1".equals(task.getType())) {
					if (task.getFiveTasksInfo().size() > 0) {
						task.setFiveTasks(gson.toJson(task.getFiveTasksInfo()));
					}
				}else if("4".equals(task.getType())){
					if (task.getConsume_info().size() > 0) {
						task.setTypes_info(gson.toJson(task.getConsume_info()));
					}
				}else if("8".equals(task.getType())){
					if (task.getBusiness()!=null) {
						task.setId((task.getBusiness().getCase_id()));
						//task.setTaskListBean(gson.toJson(task));
					}
				}else if("9".equals(task.getType())){
					if (task.getBusiness()!=null) {
						task.setId((task.getBusiness().getCase_id()));
						//task.setTaskListBean(gson.toJson(task));
					}
				}else if("10".equals(task.getType())){
					if (task.getBusiness()!=null) {
						task.setId((task.getBusiness().getCase_id()));
						//task.setTaskListBean(gson.toJson(task));
					}
				}
				boolean needSave=true;

				for(TaskVo vo:list){

					if(task.getTask_id()==vo.getTask_id()){
						task.setDbid(vo.getDbid());
						Log.i("task_save", "need"+task.getTask_id());
						needSave=false;
						break;
					}
				}

				if(needSave){
					DataBaseHelper.getDbUtilsInstance(mcontext)
							.save(task);
				}else{
					DataBaseHelper.getDbUtilsInstance(mcontext)
							.update(task);
				}
			}
//			DataBaseHel`per.getInstance().getDbUtilsInstance(mcontext)
//					.saveAll(tasks);

		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}

	}
	public static void AddNetworkTask(List<TaskVo> tasks) {//上拉加载服务器数据到本地
		try {
			for (TaskVo task : tasks) {
				task.setMobile(MyAppliaction.getConstantValue(Constants.MOBILE));
				if ("2".equals(task.getType())) {
					List<Attachments> attachments = task.getAttachments();
					if (attachments.size() > 0) {
						task.setFileList(gson.toJson(attachments));
					}
				} else if ("1".equals(task.getType())) {
					if (task.getFiveTasksInfo().size() > 0) {
						task.setFiveTasks(gson.toJson(task.getFiveTasksInfo()));
					}
				}else if("4".equals(task.getType())){
					if (task.getConsume_info().size() > 0) {
						task.setTypes_info(gson.toJson(task.getConsume_info()));
					}
				}
			}

			DataBaseHelper.getDbUtilsInstance(mcontext)
					.saveAll(tasks);

		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
	}


	public static void deletAllFinishedTask() {//上拉加载服务器数据到本地
		try {
			DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.delete(TaskVo.class, WhereBuilder.b("status", "=", 0));
		} catch (DbException e) {
			Log.e("upload", e.getMessage() + "");
			e.printStackTrace();
		}
	}
	public static void deleteTask(TaskVo task){
		try{
		DataBaseHelper
				.getDbUtilsInstance(mcontext)
				.delete(task);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	public static List<TaskVo> getAllDbTask() {
		List<TaskVo> list = null;
		try {
			list = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class).orderBy(
									"dbid"));
		} catch (DbException e) {
			e.printStackTrace();
		}

		if (list == null) {
			list = new ArrayList<TaskVo>();
		}
		return list;
	}
	public static TaskVo findDbTaskByKeyValue(String k, String v){
		try {
			TaskVo tv= DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findFirst(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)).and(k,"=",v)));
			return tv;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return  null;
	}
	public static List<TaskVo>  findListByKeyValue(String k, String v){
		List<TaskVo> list;

		try {
			list= DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)).and(k,"=",v)));
			return list;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return  new ArrayList<>();
	}

	public static List<TaskVo> findListByStatusType(String status, String type){
		List<TaskVo> list;

		try {
			list= DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(TaskVo.class)
									.where(WhereBuilder.b(
											"mobile",
											"=",
											MyAppliaction
													.getConstantValue(Constants.MOBILE)).and("status","=",status).and("type","=",type)));
			return list;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return  new ArrayList<>();
	}
}
