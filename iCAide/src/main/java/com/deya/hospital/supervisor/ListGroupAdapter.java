package com.deya.hospital.supervisor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.util.WebViewDemo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ListGroupAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private DisplayImageOptions optionsSquare;
	List<Map<String, List<TaskVo>>> groupList;
	Context context;
	private GroupClass groupVo;

	public ListGroupAdapter(Context context,
			List<Map<String, List<TaskVo>>> groupList) {
		inflater = LayoutInflater.from(context);
		this.groupList = groupList;
		this.context= context;

	}

	@Override
	public int getGroupCount() {
		Log.i("111111size", groupList.size() + "");
		return groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Log.i("111111323123", groupList.get(groupPosition).get("list").size()
				+ "");
		return groupList.get(groupPosition).get("list").size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition).get("list").get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.group_top, null);
			holder.stateImg = (ImageView) convertView
					.findViewById(R.id.state_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.i("111111groupview", groupList.get(groupPosition).get("list")
				.get(0).getStatus()
				+ "");
		int state = groupList.get(groupPosition).get("list").get(0).getStatus();
		if (state == 3 || state == 2) {
			holder.stateImg.setImageResource(R.drawable.unfinish);
		} else if (state == 1) {
			//holder.stateImg.setImageResource(R.drawable.unsycro);
		} else {
			holder.stateImg.setImageResource(R.drawable.finish_img);
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final ViewHolder2 mviewHolder;
		if (convertView == null) {
			mviewHolder = new ViewHolder2();
			convertView = inflater.inflate(R.layout.list_tasklist_child, null);
			mviewHolder.title = (TextView) convertView
					.findViewById(R.id.tv_title);
			mviewHolder.author = (TextView) convertView
					.findViewById(R.id.tv_author);
			mviewHolder.ycRate = (TextView) convertView
					.findViewById(R.id.yc_rete);
			mviewHolder.rtRate = (TextView) convertView
					.findViewById(R.id.rt_rate);
			mviewHolder.progress = (ProgressBar) convertView
					.findViewById(R.id.progressBar);
			mviewHolder.typeLayout = (LinearLayout) convertView
					.findViewById(R.id.type_layout);
			mviewHolder.finishtimeTv=(TextView) convertView.findViewById(R.id.finishtime);
			mviewHolder.timeLay=(LinearLayout) convertView.findViewById(R.id.timeLay);

			convertView.setTag(mviewHolder);
		} else {
			mviewHolder = (ViewHolder2) convertView.getTag();
		}
		final TaskVo tv = groupList.get(groupPosition).get("list")
				.get(childPosition);
		if (!AbStrUtil.isEmpty(tv.getTask_type())) {
			int type = Integer.parseInt(tv.getTask_type());
			switch (type) {
			case 0:
				mviewHolder.title.setText("5个手卫生时刻依从性调查-不限时机");
				break;
			case 1:
				mviewHolder.title.setText("5个手卫生时刻依从性调查-5个时机");
				break;
			case 2:
				mviewHolder.title.setText("5个手卫生时刻依从性调查-10个时机");
				break;
			case 3:
				mviewHolder.title.setText("5个手卫生时刻依从性调查-20个时机");
				break;
			case 4:
				mviewHolder.title.setText("5个手卫生时刻依从性调查-30个时机");
				break;
			default:
				break;
			}
		} else {
			mviewHolder.title.setText("");
		}
		if (null != tv.getDepartmentName()) {
			mviewHolder.author.setText(tv.getDepartmentName());
		} else {
			mviewHolder.author.setText("");
		}
		if (null != tv.getYc_rate()) {
			mviewHolder.ycRate.setText("依从率" + tv.getYc_rate()+"%");
		} else {
			mviewHolder.ycRate.setText("依从率" + "0%");
		}

		if (null != tv.getValid_rate()) {
			mviewHolder.rtRate.setText("正确率" + tv.getValid_rate()+"%");
		} else {
			mviewHolder.rtRate.setText("正确率" +"0%");
		}

		if (!AbStrUtil.isEmpty(tv.getProgress())) {
			Log.i("111111111122222", Integer.parseInt(tv.getProgress()) + "");
			mviewHolder.progress
					.setProgress(Integer.parseInt(tv.getProgress()));
		} else {
			mviewHolder.progress.setProgress(0);
		}
		if (!AbStrUtil.isEmpty(tv.getStatus() + "") && (tv.getStatus() == 0)) {
			mviewHolder.typeLayout
					.setBackgroundResource(R.color.type_title_finish);
			mviewHolder.timeLay.setVisibility(View.VISIBLE);
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		       Date date=null;
		       String str="";
		        try {
		        	date = sdf.parse(tv.getMission_time());
		        	str= sdf.getTimeInstance().format(date);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		       
		        System.out.println(str);
			mviewHolder.finishtimeTv.setText(str);
		} else {
			mviewHolder.timeLay.setVisibility(View.GONE);
			mviewHolder.typeLayout
					.setBackgroundResource(R.color.type_title_normal);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("1111111", tv.getStatus() + "");
				if (!AbStrUtil.isEmpty(tv.getStatus() + "")
						&& (tv.getStatus() == 2 || tv.getStatus() == 3)) {
					// 当任务状态为2时表示未完成
					Intent toDoIntent = new Intent(context,
							NewAccomplishTasksActivity.class);
					toDoIntent.putExtra("type", tv.getTask_type());
					toDoIntent.putExtra("data", (Serializable) tv);
					toDoIntent.putExtra("date", tv.getMission_time());
					toDoIntent.putExtra("task_id", tv.getDbid());
					toDoIntent.putExtra("departId",
							(Serializable) tv.getDepartment());
					// listSelection = position;
					context.startActivity(toDoIntent);
				} else if (!AbStrUtil.isEmpty(tv.getStatus() + "")
						&& (tv.getStatus() == 0)) {
					Intent toWeb = new Intent(context, WebViewDemo.class);
					toWeb.putExtra("url",
							WebUrl.TASKDETAILURL
									+ tv.getTask_id());
					toWeb.putExtra("title","任务统计");
					context.startActivity(toWeb);
				}

			}
		});
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewHolder2 {
		public TextView title;
		public TextView author;
		public TextView ycRate;
		public ImageView stateImg;
		public TextView rtRate;// 正确率
		public ProgressBar progress;
		LinearLayout typeLayout;
		private TextView finishtimeTv;
		LinearLayout timeLay;
	}

	class ViewHolder {
		public ImageView stateImg;
	}

	public int dp2Px(Context context, int dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + (int) 0.5f);
	}
}
