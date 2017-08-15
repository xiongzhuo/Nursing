package com.deya.hospital.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.widget.view.UnTimeItem2;
import com.deya.hospital.widget.view.UnTimeItem2.UnTimeItemTextChangListener;

import java.util.List;

public class UnTimeAdapter extends BaseAdapter implements OnClickListener {
	public static final String TAG = "UnTimeAdapter";
	private LayoutInflater mInflater;
	private Context mContext;
	private List<planListDb> mModels;
	TaskVo tv;

	public UnTimeAdapter(Context context, List<planListDb> mModels, TaskVo tv) {

		mContext = context;
		this.mModels = mModels;
		this.tv = tv;
	}

	private BaseAdapter spinnerAdapter;

	public void setSpinnerAdapter(BaseAdapter spinnerAdapter) {
		this.spinnerAdapter = spinnerAdapter;
	}

	private List<JobListVo> jobList;
	List<JobListVo> jobTypelist;

	public void setSpinnerAdapter(List<JobListVo> jobList, List<JobListVo> jobTypelist) {
		this.jobList = jobList;
		this.jobTypelist=jobTypelist;
	}

	private void clean() {
		mModels.clear();
	}

	@Override
	public int getItemViewType(int position) {
		return mModels.get(position).getView_type();
	}

	@Override
	public int getCount() {
		if (null != mModels)
			return mModels.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (position >= getCount()) {
			return null;
		}
		return mModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ViewHodler hodler = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DebugUtil.debug("adapter", "go view");
		mInflater = LayoutInflater.from(mContext);

		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = mInflater.inflate(R.layout.adapter_item_time2, null);

			hodler.item = (UnTimeItem2) convertView.findViewById(R.id.item);


			convertView.setTag(hodler);

		} else {
			hodler = (ViewHodler) convertView.getTag();
			resetViewHolder(hodler);
		}

		planListDb model = mModels.get(position);
		String pname = model.getPpostName();
		pname = null == pname || "".equals(pname) ? "岗位" : pname;
		int num = model.getSubTasks().size();
		
		hodler.item.initData(position, model.getPname(), num + "",
				model.getTemp_pname(), pname, jobList,jobTypelist,model.getWork_type(),model.getPpost());
		if(!tv.isTranning()){
			hodler.item.setOnItemListener(new UnTimeItem2.UnTimeItemListener() {

				@Override
				public String OnSpinnerChange(int position, int index,
						int index2) {
					// TODO Auto-generated method stub
					return listener.OnSpinnerClick(position, index, index2);
				}
			},textChangListener);
		}

		return convertView;
	}

	private String remark;

	public String remark() {
		return remark;
	}

	/**
	 * 返回样式数
	 */
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	private class ViewHodler {
		UnTimeItem2 item;
	}

	// private class ViewHodler2 {
	// TextView text_time_res, text_dependence_res, text_correct_res,
	// text_rate_res,text_depart;
	// }
	//
	// private class ViewHodler3 {
	// LinearLayout lay_remark;
	// TextView text_remark_res;
	// }

	protected void resetViewHolder(ViewHodler hodler) {
		hodler.item.clear();
	}

	// protected void resetViewHolder2(ViewHodler2 hodler2) {
	// hodler2.text_time_res.setText("");
	// hodler2.text_dependence_res.setText("");
	// hodler2.text_correct_res.setText("");
	// hodler2.text_rate_res.setText("");
	// hodler2.text_depart.setText("");
	// }
	//
	// protected void resetViewHolder3(ViewHodler3 hodler3) {
	// hodler3.text_remark_res.setText("");
	// }

	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	public interface UnTimeAdapterListener {
		public void OnClick();

		public String OnSpinnerClick(int position, int index, int index2);

//		public void OnChangeName(int position, String name);
//
//		public void OnChangeTextName(int position, String name);

	}

	private UnTimeAdapterListener listener;

	public void setListener(UnTimeAdapterListener listener,UnTimeItemTextChangListener textChangListener) {
		this.listener = listener;
		this.textChangListener=textChangListener;
	}
	
	private UnTimeItemTextChangListener textChangListener;
	

	public Handler getHandler() {
		return mHandler;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				EditText editText = (EditText) msg.obj;
				String text = editText.getText().toString();
				editText.requestFocus();
				if (null != text && !"".equals(text)) {
					try {
						int p = Integer.parseInt(editText.getTag().toString());
					//	listener.OnChangeName(p, text);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			default:
				break;
			}
		}
	};

}