package com.deya.hospital.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.vo.JobListVo;

import java.util.List;
/**
 * 任务提交页面，被观察者自定义控件
* @author  yung
* @date 创建时间：2016年1月15日 上午11:42:18 
* @version 1.0
 */
public class UnTimeItem2 extends LinearLayout implements OnClickListener {
	private View view;
	RelativeLayout lay_left;
	LinearLayout lay_button_left;
	TextView text_name, text_counts;// , text_sl;
	EditText edt_name;
	DySpinner sp_sl;
	private Resources res;

	boolean Editorbal=true;
	String[] letters ={"A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z","AA","AB","AC","AD","AF","AG","AH","AJ","AK" };

	public UnTimeItem2(Context context) {
		super(context);
		init(context);
		
	}

	public UnTimeItem2(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		view = LayoutInflater.from(context).inflate(R.layout.view_untime_item2,
				null);
		text_name = (TextView) view.findViewById(R.id.text_name);
		text_counts = (TextView) view.findViewById(R.id.text_counts);

		lay_left = (RelativeLayout) view.findViewById(R.id.lay_left);
		lay_button_left = (LinearLayout) view
				.findViewById(R.id.lay_button_left);

		edt_name = (EditText) view.findViewById(R.id.edt_name);

		sp_sl = (DySpinner) view.findViewById(R.id.sp_sl);
		sp_sl.findViewById(R.id.lay_sl).setBackgroundResource(
				R.drawable.btn_broad_gray);
		if(!Editorbal){
			edt_name.setEnabled(false);
			sp_sl.setEnabled(false);
		}
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		res = getResources();

		this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	private int position;
	private String oldName="";

	/**
	 * 初始化数据
	 * @param position item index
	 * @param name 被观察者名称
	 * @param num 任务数
	 * @param edName 输入框文字
	 * @param spText spinner value
	 * @param adapter spinner adapter
	 */
	public void initData(int position, String name, String num, String edName,
			String spText, BaseAdapter adapter) {
		sp_sl.setAdapter(position, adapter);
		_initData(position, name, num, edName, spText);
	}

	/**
	 * 
	 * 初始化数据
	 * @param position item index
	 * @param name 被观察者名称
	 * @param num 任务数
	 * @param edName 输入框文字
	 * @param spText spinner value
	 * @param jobList 工作类型 list
	 * @param jobTypelist  工作性质list
	 * @param workId 工作性质ID
	 * @param jobId 工作类型ID
	 */
	public void initData(int position, String name, String num, String edName,
			String spText, List<JobListVo> jobList,List<JobListVo> jobTypelist,String workId, String jobId) {
		sp_sl.setAdapter(position, jobList,jobTypelist,workId,jobId);
		_initData(position, name, num, edName, spText);
	}

	/**
	 * 初始化数据
	 * @param position item index
	 * @param name 被观察者名称
	 * @param num 任务数
	 * @param edName 输入框文字
	 * @param spText spinner value
	 */
	private void _initData(int position, String name, String num,
			String edName, String spText) {
		this.oldName=name;
		this.position = position;
		text_name.setText(name);
		text_counts.setText("(" + num + ")");
		sp_sl.SetText(spText);

		DebugUtil.debug("untimeletters", "position>>" + position + "  let>>"
				+ letters[position] + " name>" + name);

		String reg = "[A-Z]";
		if (null != edName && !edName.matches(reg)) {
			edt_name.setText(edName);
		}

		edt_name.setHint(res.getString(R.string.untime_hit_name));
		if (!name.matches(reg)) {
			edt_name.setText(name);
		}

		// textWatcher=new MyTextWatcher();
		if (edt_name.getText().length() > 0) {
			edt_name.setSelection(edt_name.getText().length());// set cursor to
																// the end
		}
		edt_name.addTextChangedListener(textWatcher);

		edt_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

				}
			}
		});

		sp_sl.setOnSpinnerClik(new DySpinner.DySpinnerListener() {

			@Override
			public String OnItemClick(int position, int index, int index2) {
				// TODO Auto-generated method stub
				return listener.OnSpinnerChange(position, index, index2);
			}
		});
	}


	private UnTimeItemListener listener;

	/**
	 *  设置 监听
	 */
	public void setOnItemListener(UnTimeItemListener listener,
			UnTimeItemTextChangListener textChangListener) {
		this.listener = listener;
		this.textChangListener = textChangListener;
	}

	/**
	 * item 监听接口
	 */
	public interface UnTimeItemListener {
		public String OnSpinnerChange(int position, int index, int index2);
	}

	
	private UnTimeItemTextChangListener textChangListener;

	/**
	 * item 名字变化 监听接口
	 */
	public interface UnTimeItemTextChangListener {
		public void OnEditChange(int position, String text);
	}

	@Override
	public void onClick(View v) {

	}

	public void clear() {
		// TODO Auto-generated method stub
		edt_name.setText("");
	}

	TextWatcher textWatcher = new TextWatcher() {

		public void afterTextChanged(Editable arg0) {
			String text = arg0.toString();
			if (text.length() > 0&&!oldName.equals(text)) {
				DebugUtil.debug("untimeitem_settext", "position>>" + position
						+ "  name>>" + text);
				text_name.setText(text);
				textChangListener.OnEditChange(position, text);
				oldName=text;
			}
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			// editText.setSelection(editText.getText().length());
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	};
}
