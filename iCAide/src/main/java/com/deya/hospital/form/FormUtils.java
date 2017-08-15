package com.deya.hospital.form;

import android.app.Activity;
import android.content.Context;

import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.SupervisorQestionVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FormUtils {
	public static TaskVo getMainRemark(List<FormDetailListVo> list,
			TaskVo data, String title, Context mcontext) {

		String checkContent = "";
		String exist_problem = "";
		String deal_suggest = "";
		List<Attachments> remarkFileList = new ArrayList<Attachments>();
		int k = 1;
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getSub_items().size(); j++) {
				if (!AbStrUtil.isEmpty(list.get(i).getSub_items().get(j)
						.getRemark())) {
					list.get(i).getSub_items().get(j).setIs_remark(1);
					TaskVo remarkVo = list.get(i).getSub_items().get(j)
							.getRemarkVo();
					checkContent += k + "、" + remarkVo.getCheck_content()
							+ "\n";
					exist_problem += k + "、" + remarkVo.getExist_problem()
							+ "\n";
					deal_suggest += k + "、" + remarkVo.getDeal_suggest() + "\n";
					remarkFileList.addAll(remarkVo.getAttachments());
					k++;
				}

			}
		}

		TaskVo remarkData = new TaskVo();
		remarkData.setDepartmentName(data.getDepartmentName());
		remarkData.setDepartment(data.getDepartment());
		remarkData.setMission_time(data.getMission_time());
		remarkData.setCheck_content(checkContent);
		remarkData.setExist_problem(exist_problem);
		remarkData.setDeal_suggest(deal_suggest);
		remarkData.setAttachments(remarkFileList);
		remarkData.setTitle(title);
		remarkData.setStatus(1);

		if (!AbStrUtil.isEmpty(checkContent)) {
			// Intent it = new Intent(mcontext,
			// FormSupervisoryBookAcitivity.class);
			// it.putExtra("data", remarkData);
			// mcontext.startActivity(it);
		}
		return remarkData;

	}

	public static SupervisorQestionVo getRemarkDetail(TaskVo data, Context mcontext) {
		SupervisorQestionVo remarkTv = new Gson().fromJson(data.getMain_remark(),
				SupervisorQestionVo.class);
		if (null == remarkTv) {
			remarkTv = new SupervisorQestionVo();
			remarkTv.setDepartment(data.getDepartment());
			remarkTv.setDepartmentName(data.getDepartmentName());
		}
		return remarkTv;
	}

	public static TaskVo onAddMainRemark(TaskVo tv, List<TaskVo> monthList) {// 工作间首页点进去的时候根据Id匹配督导本
		if (!AbStrUtil.isEmpty(tv.getMain_remark_id())) {
			for (TaskVo tvo : monthList) {
				if (tvo.getType().equals("2")
						&& (tvo.getTask_id() + "").equals(tv
								.getMain_remark_id())) {
					return tvo;
				}
			}
		}
		return new TaskVo();
	}

	public static TaskVo addTask(Activity ac, TaskVo tv, boolean upload,
			List<FormDetailListVo> list) {
			tv.setItems(TaskUtils.gson.toJson(list));

		return tv;

	}
}
