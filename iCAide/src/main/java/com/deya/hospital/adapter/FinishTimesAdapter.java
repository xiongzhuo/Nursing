package com.deya.hospital.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.RulesVo;
import com.deya.hospital.vo.dbdata.subTaskVo;

import java.util.ArrayList;
import java.util.List;

public class FinishTimesAdapter extends BaseAdapter {
	List<subTaskVo> list = new ArrayList<subTaskVo>();
	LayoutInflater inflater;
	Context context;
	public int colors[] = { R.color.line1_corlor, R.color.line2_corlor,
			R.color.line3_corlor, R.color.line4_corlor, R.color.line5_corlor,
			R.color.line6_corlor };
	List<JobListVo> jobList;

	public FinishTimesAdapter(Context context, List<subTaskVo> list,
			List<JobListVo> jobList) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.jobList = jobList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setdata(List<subTaskVo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHoder;
		if (null == convertView) {
			viewHoder = new ViewHolder();
			convertView = inflater.inflate(R.layout.finish_list_item, null);
			viewHoder.person = (TextView) convertView.findViewById(R.id.person);
			viewHoder.times = (TextView) convertView.findViewById(R.id.times);
			viewHoder.method = (TextView) convertView.findViewById(R.id.method);
			viewHoder.result = (TextView) convertView.findViewById(R.id.result);
			viewHoder.indicationTv = (TextView) convertView
					.findViewById(R.id.indicationTv);
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHolder) convertView.getTag();
		}
		// int result=Integer.parseInt(list.get(position).getResults());
		String str = list.get(position).getCol_type();

		int result = Integer.parseInt(list.get(position).getResults());
		switch (result) {
		case 0:
			 viewHoder.method.setText("未采取措施");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_right);

			break;
		case 1:
			 viewHoder.method.setText("洗手");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_wrong);
			break;
		case 2:

			viewHoder.method.setText("卫生手消毒");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_wrong);
			break;
		case 3:

			viewHoder.method.setText("戴手套");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_wrong);
			break;
		case 4:
			viewHoder.method.setText("洗手");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_right);
			break;
		case 5:
			viewHoder.method.setText("卫生手消毒");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_right);
			break;
		case 6:
			viewHoder.method.setText("戴手套");
			viewHoder.result.setBackgroundResource(R.drawable.finishs_right);
			break;
		default:
			break;
		}
		String methodStr = viewHoder.method.getText().toString();
		viewHoder.method.setVisibility(View.GONE);
		String timesStr = "";
		for (int i = 0; i < str.length(); i++) {
			char item = str.charAt(i);

			int timetype = Integer.parseInt(item + "");

			switch (timetype) {
			case 0:
				timesStr = timesStr + "接触患者前  ";
				break;
			case 1:
				timesStr = timesStr + "接触患者后 ";
				break;
			case 2:
				timesStr = timesStr + "无菌操作前  ";
				break;
			case 3:
				timesStr = timesStr + "接触血液体液后  ";
				break;
			case 4:
				timesStr = timesStr + "接触患者周边环境后  ";
				break;
			default:
				break;
			}
		}
		viewHoder.indicationTv.setText(  "(" +timesStr  + ")"+methodStr);

		List<RulesVo> ruls = list.get(position).getUnrules();

		String rulesStr = "";
		if (null != ruls) {
			Log.i("1111111111111unrule22", ruls.size() + "");
			for (RulesVo rv : ruls) {
				rulesStr += rv.getName() + " ";
			}

		}
		String ppoName = list.get(position).getPpoName();
		if (AbStrUtil.isEmpty(ppoName)) {
			ppoName = "";
		}

		String workName = list.get(position).getWorkName();
		if (AbStrUtil.isEmpty(workName)) {
			workName = "";
		}
		String name = list.get(position).getPname();
		viewHoder.person.setText(workName + " " + ppoName + " " + name);
		if (!AbStrUtil.isEmpty(ppoName)) {
			for (int i = 0; i < jobList.size(); i++) {
				if (ppoName.equals(jobList.get(i).getName())) {
					viewHoder.person.setTextColor(context.getResources()
							.getColor(colors[i % 6]));
				}
			}

		} else {
			viewHoder.person.setTextColor(context.getResources().getColor(
					colors[0]));
		}
		viewHoder.times.setText(rulesStr);

		return convertView;
	}

	public class ViewHolder {
		TextView person;
		TextView times;
		TextView method;
		TextView result;
		TextView indicationTv;
	}

}
