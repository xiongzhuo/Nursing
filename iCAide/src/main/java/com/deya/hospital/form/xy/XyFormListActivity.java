package com.deya.hospital.form.xy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ReviewTipsDialog;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.form.FormDataCache;
import com.deya.hospital.form.FormListAdapter;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.FormDetailListVo;
import com.deya.hospital.vo.FormVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XyFormListActivity extends BaseActivity implements OnClickListener {
	protected static final int SEND_SUCESS = 0x7008;
	protected static final int SEND_FAILE = 0x7009;
	ListView formListView;
	ImageView creatbtn;
	FormListAdapter formAdapter;
	List<FormVo> formlist = new ArrayList<FormVo>();
	private RelativeLayout rlBack;
	Gson gson=new Gson();
	LinearLayout layout;
	TaskVo data=new TaskVo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_list);
		intTopView();
		initView();
		//getFormList();
		 getFormCache();
	}
	private void getFormCache() {
		
		try {
			formlist = DataBaseHelper
					.getDbUtilsInstance(mcontext)
					.findAll(
							Selector.from(FormVo.class)
									.where("types", "=", 6)
									.orderBy("dbid"));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		formAdapter.setdata(formlist);
		
	}
	private void intTopView() {
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("选择考核表模板");
		rlBack = (RelativeLayout) this.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(this);
		layout=(LinearLayout) this.findViewById(R.id.creatLay);
		layout.setVisibility(View.GONE);

	}

	private void initView() {
		formListView = (ListView) this.findViewById(R.id.formList);
		formAdapter = new FormListAdapter(mcontext, formlist,3);
		formListView.setAdapter(formAdapter);

//		creatbtn = (ImageView) this.findViewById(R.id.creatbtn);
//		creatbtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent it = new Intent(mcontext, CreatReviewFormActivity.class);
//				startActivityForResult(it, 0x111);
//			}
//		});

		formListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it = new Intent(XyFormListActivity.this,
						XiangyaPrivewActivity.class);
//				it.putExtra("id", formlist.get(position).getId());
//				it.putExtra("type", formlist.get(position).getType());
//				it.putExtra("totoalScore", formlist.get(position).getScore());
//				it.putExtra("title", formlist.get(position).getName());
				
				data.setFormType(3);
				data.setTotalscore(formlist.get(position).getScore());
				data.setName(formlist.get(position).getName());
				data.setFormId(formlist.get(position).getId());
				it.putExtra("data",data);
				it.putExtra("itemList",(Serializable) getFormItemCache(position));
				it.putExtra("time",getIntent().getStringExtra("time"));
				startActivityForResult(it, 0x116);
			}
		});
	}
	public List<FormDetailListVo> getFormItemCache(int position){
		String str=formlist.get(position).getCacheItems();
		List<FormDetailListVo> itemList=gson.fromJson(str,
				new TypeToken<List<FormDetailListVo>>() {
				}.getType());
		return itemList;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0x111) {
			ReviewTipsDialog dialog = new ReviewTipsDialog(mcontext);
			dialog.show();
		}else if(resultCode==0x116 ){
			finish();
		}
	}

	private void getFormList(){
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("template_type", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("111111111111", WebUrl.GET_FORM_LIST); 
		MainBizImpl.getInstance().onComomRequest(myHandler,XyFormListActivity.this, SEND_SUCESS,
				SEND_FAILE, job,"grid/queryTemplateInfos");
	}
	
	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SEND_SUCESS:
					if (null != msg && null != msg.obj) {
						try {
							setsListResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case SEND_FAILE:
					String str=FormDataCache.getFormList(mcontext);
					if(str.length()>0){
						try {
							setsListResult(new JSONObject(str));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					}
					break;

				default:
					break;
				}
			}
		}

	};

	protected void setsListResult(JSONObject jsonObject) {
		Log.i("queryTemplateInfos", jsonObject.toString());
		JSONArray jarr=jsonObject.optJSONArray("grid_templates");
		if(null!=jarr){
			formlist=gson.fromJson(jarr.toString(),
					new TypeToken<List<FormVo>>() {
					}.getType());
			formAdapter.setdata(formlist);
		}

		FormDataCache.saveFormList(mcontext, jsonObject.toString());
	}
}
