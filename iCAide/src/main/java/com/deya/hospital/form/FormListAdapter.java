package com.deya.hospital.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.FormVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FormListAdapter extends BaseAdapter {
	private static final int SAVE_SUCESS = 0x7010;
	private static final int SAVE_FAILE = 0x7011;
	private LayoutInflater inflater;
	private MyHandler myHandler;
	List<FormVo> list;
	Context context;
	public static int sava_position = -1;
	int type;

	public FormListAdapter(Context context, List<FormVo> list, int type) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.type = type;
		tools = new Tools(context, Constants.AC);
	}

	public FormListAdapter(Context context, List<FormVo> list, int type, MyHandler myHandler) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list = list;
		this.type = type;
		this.myHandler = myHandler;
		tools = new Tools(context, Constants.AC);
	}

	public int getSavePosition() {
		return sava_position;

	}

	public void setdata(List<FormVo> list) {
	this.list=list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.form_list_item, null);
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.readNumTv = (TextView) convertView
					.findViewById(R.id.tv_readnum);
			viewHolder.publishTimeTv = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.author);
			viewHolder.btn_save = (Button) convertView
					.findViewById(R.id.btn_save);
			viewHolder.ll_opne = (LinearLayout) convertView
					.findViewById(R.id.ll_opne);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (list.get(position).getIs_save() == 0) {
			//未保存

			viewHolder.btn_save.setPressed(false);
			viewHolder.btn_save.setEnabled(true);
			viewHolder.btn_save.setText("保存到本医院");
		} else {
			viewHolder.btn_save.setPressed(true);
			viewHolder.btn_save.setEnabled(false);
			viewHolder.btn_save.setText("已保存");
		}
		if (list.get(position).getIs_open() == 0) {
			viewHolder.author.setVisibility(View.VISIBLE);
			viewHolder.ll_opne.setVisibility(View.GONE);
		} else {
			viewHolder.author.setVisibility(View.GONE);
			viewHolder.ll_opne.setVisibility(View.VISIBLE);
		}


		if (list.get(position).getTypes() == 6) {
			viewHolder.img.setImageResource(R.drawable.xy_choose_img);
			viewHolder.img.setVisibility(View.VISIBLE);
		} else {
			switch (list.get(position).getType()) {
				case 1:
					viewHolder.img.setImageResource(R.drawable.score_img);
					viewHolder.img.setVisibility(View.VISIBLE);
					break;
				case 4:
				case 2:
					viewHolder.img.setImageResource(R.drawable.juge);
					viewHolder.img.setVisibility(View.VISIBLE);
					break;
				default:
					viewHolder.img.setVisibility(View.GONE);
					break;
			}
		}
		viewHolder.publishTimeTv.setText("创建时间:" + list.get(position).getCreate_time());
		viewHolder.readNumTv.setText(list.get(position).getUse_cnt());
		viewHolder.author.setText("创建人：" + list.get(position).getUname() + "(" + list.get(position).getHospital() + ")");
		viewHolder.titleTv.setText(list.get(position).getName());
		viewHolder.btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (tools.getValue(Constants.JOB).equals("1") || tools.getValue(Constants.JOB).equals("2")) {
				addFormList(list.get(position).getId());
				sava_position = position;
//				} else {
//					ToastUtils.showToast(context,"您不能做此操作哟！请联系院感科专职人员操作吧！");
//				}
			}
		});
		return convertView;
	}

	public Tools tools;

	private void addFormList(String id) {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("tmp_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,
				context, SAVE_SUCESS, SAVE_FAILE, job,
				"grid/addTemplateToHospital");
	}

	public class ViewHolder {
		public ImageView img;
		public TextView titleTv;
		public TextView readNumTv;
		public TextView publishTimeTv;
		public TextView author;
		public TextView company;
		public TextView creatCompany;
		public LinearLayout ll_opne;
		public Button btn_save;
	}
}
