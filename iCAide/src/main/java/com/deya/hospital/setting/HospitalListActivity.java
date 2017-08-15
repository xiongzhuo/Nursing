package com.deya.hospital.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.CityVo;
import com.deya.hospital.vo.HospitalInfo;
import com.deya.hospital.vo.ProvinceVo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HospitalListActivity extends BaseActivity implements
		OnClickListener {
	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;
	public static final int HOSPITAL_RESULT_CODE = 0x18;
	ListView listView;
	private EditText searchEdt;
	private LayoutInflater inflater;
	List<HospitalInfo> list = new ArrayList<HospitalInfo>();
	List<HospitalInfo> autoList = new ArrayList<HospitalInfo>();
	Gson gson = new Gson();
	HospitalAdapter hosAdapter;
	private Tools tools;
	ImageView img_back, searchImg,clear;
	TextView btn_ok;
	private RelativeLayout provinceLay;
	TextView provinceTv, cityTv;
	private RelativeLayout cityLay;
	private TextView searchTv;
	private LinearLayout chooseLayout;
	LinearLayout empertyLay;
	 TextView   empertyTv,phoneViewTv ;
	String hospitalStr="";
	private Button addButton2;
	private LinearLayout addLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospitallist_activity);
		inflater = LayoutInflater.from(mcontext);
		listView = (ListView) this.findViewById(R.id.list);
		
		hosAdapter = new HospitalAdapter(mcontext, autoList);
		listView.setAdapter(hosAdapter);
		searchEdt = (EditText) this.findViewById(R.id.et_search);
		provinceLay = (RelativeLayout) this.findViewById(R.id.provinceLay);
		cityLay = (RelativeLayout) this.findViewById(R.id.cityLay);
		cityLay.setOnClickListener(this);
		provinceLay.setOnClickListener(this);
		searchTv = (TextView) this.findViewById(R.id.searchTv);
		searchTv.setOnClickListener(this);
		provinceTv = (TextView) this.findViewById(R.id.provinceTv);
		cityTv = (TextView) this.findViewById(R.id.cityTv);
		tools = new Tools(mcontext, Constants.AC);
		
		chooseLayout=(LinearLayout) this.findViewById(R.id.chooseLayout);
		empertyLay=(LinearLayout) this.findViewById(R.id.empertyLay);
		addLay=(LinearLayout) this.findViewById(R.id.addLay);
		
		empertyTv = (TextView) this.findViewById(R.id.empertyTv);
		addButton2=(Button) this.findViewById(R.id.addButton2);
		addButton2.setOnClickListener(this);
		clear=(ImageView) this.findViewById(R.id.clear);
		clear.setOnClickListener(this);
		setTextView();
		
		// btn_ok=(TextView) this.findViewById(R.id.btn_ok);
		// btn_ok.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent=new Intent(HospitalListActivity.this,
		// RegisterActivity.class);
		// intent.putExtra("hospital_name", imgSearch.getText().toString());
		// Log.i("11111",intent.getStringExtra("hospital_name"));
		// setResult(HOSPITAL_RESULT_CODE, intent);
		// finish();
		// }
		// });
		img_back = (ImageView) this.findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		searchEdt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				getautoList(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				search();
			}
		});
//		searchEdt.setOnEditorActionListener(new OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView view, int actionId,
//					KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//					// 先隐藏键盘
//					((InputMethodManager) searchEdt.getContext()
//							.getSystemService(Context.INPUT_METHOD_SERVICE))
//							.hideSoftInputFromWindow(getCurrentFocus()
//									.getWindowToken(),
//									InputMethodManager.HIDE_NOT_ALWAYS);
//					if (searchEdt.getText().toString().trim().length() > 0) {
//						getHosdata(searchEdt.getText().toString().trim());
//					}
//					return true;
//				}
//				return false;
//			}
//		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(HospitalListActivity.this,
						MyEditorActivity.class);
				intent.putExtra("hospital_name", autoList.get(position)
						.getName());
				intent.putExtra("hospital_id", autoList.get(position).getId());
				Log.i("11111", intent.getStringExtra("hospital_name"));
				setResult(HOSPITAL_RESULT_CODE, intent);
				finish();
			}
		});

		getHosdata("");
	}

	public void getautoList(String s){
		if (s.toString().trim().length() > 0) {
			provinceTv.setText("");
			cityTv.setText("");
			//provicelist.clear();
			empertyLay.setVisibility(View.GONE);
			chooseLayout.setVisibility(View.GONE);
			addLay.setVisibility(View.GONE);
			
			autoList.clear();
			for (HospitalInfo dv : list) {
				if (dv.getName().contains(s.toString())
						|| dv.getCityName().contains(s.toString())
						|| dv.getProvinceName().contains(s.toString())) {
					autoList.add(dv);
					Log.i("111111111", dv.getName());
				}
			}
		} else {
			chooseLayout.setVisibility(View.VISIBLE);
			autoList.clear();
			autoList.addAll(list);

		}
		hosAdapter.notifyDataSetChanged();
	}
	
	
	private void getHosdata(String text) {
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("keywords", text);
			job.put("device_type", "2");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,HospitalListActivity.this, SUCCESS, FAIL,
				job,"comm/getAllHospitals");

	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
				case SUCCESS:
					dismissdialog();
					if (null != msg && null != msg.obj) {
						try {
							setHosResult(new JSONObject(msg.obj.toString()));
						} catch (JSONException e5) {
							e5.printStackTrace();
						}
					}
					break;
				case FAIL:
					dismissdialog();
					ToastUtils.showToast(HospitalListActivity.this,
							"亲，您的网络不顺畅哦！");
					break;

				default:
					break;
				}
			}
		}
	};

	public class provinceAdapter extends BaseAdapter {
		Context context;

		public provinceAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return provicelist.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.list_item, null);
			TextView listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			listtext.setText(provicelist.get(position).getName());
			return convertView;
		}

	}

	public class CityAdapter extends BaseAdapter {
		Context context;

		public CityAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return cityList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.list_item, null);
			TextView listtext = (TextView) convertView
					.findViewById(R.id.listtext);
			listtext.setText(cityList.get(position).getName());
			return convertView;
		}

	}

	public class HospitalAdapter extends BaseAdapter {
		Context context;

		public HospitalAdapter(Context context, List<?> list) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return autoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.hospital_item, null);
				viewHolder.hospitalTv = (TextView) convertView
						.findViewById(R.id.hospitalname);
				viewHolder.proTv = (TextView) convertView
						.findViewById(R.id.proTv);
				viewHolder.cityTv = (TextView) convertView
						.findViewById(R.id.cityTv);
				convertView.setTag(viewHolder);
			} else {
           viewHolder=(ViewHolder) convertView.getTag();
			}
			viewHolder.hospitalTv.setText(autoList.get(position).getName());
			viewHolder.proTv.setText(autoList.get(position).getProvinceName());
			viewHolder.cityTv.setText(autoList.get(position).getCityName());
			return convertView;
		}

	}

	public class ViewHolder {
		public TextView proTv;
		public TextView cityTv;
		public TextView hospitalTv;

	}

	List<ProvinceVo> provicelist = new ArrayList<ProvinceVo>();

	protected void setHosResult(JSONObject jsonObject) {

		list.clear();
		autoList.clear();
		JSONObject jsonArray = jsonObject.optJSONObject("hospitals");
		Iterator keys = jsonArray.keys();
		
		Log.i("11111111111", jsonArray.keys().toString());
		while (keys.hasNext()) {
			Object key = (Object) keys.next();
			ProvinceVo pVo = new ProvinceVo();
			pVo.setName(key.toString());
			JSONObject provinceJson = jsonArray.optJSONObject(key.toString());
			Iterator cityKeys = provinceJson.keys();
			List<CityVo> cList = new ArrayList<CityVo>();
			while (cityKeys.hasNext()) {
				CityVo cv = new CityVo();
				Object key2 = (Object) cityKeys.next();
				cv.setName(key2.toString());
				JSONArray hospitalArr = provinceJson.optJSONArray(key2
						.toString());
				List<HospitalInfo> hlist = new ArrayList<HospitalInfo>();
				for (int i = 0; i < hospitalArr.length(); i++) {
					JSONObject job = hospitalArr.optJSONObject(i);
					HospitalInfo hv = new HospitalInfo();
					
					DebugUtil.debug("getjson","name>>"+job.optString("name")+"  NAME>>"+job.optString("fullpinyin"));
					
					hv.setName(job.optString("name"));
					hv.setNAME(job.optString("fullpinyin"));
					hv.setCityName(key2.toString());
					hv.setProvinceName(key.toString());
					hv.setId(job.optString("id"));
					hlist.add(hv);
					list.add(hv);
				}
				cv.setHospitals(hlist);
				cList.add(cv);
			}
			pVo.setCityes(cList);
			provicelist.add(pVo);
		}
		Log.i("111111111", provicelist.get(3).getCityes().get(0).getName());


		autoList.addAll(list);
		hosAdapter.notifyDataSetChanged();
	}

	provinceAdapter proviceAdapter;
	List<CityVo> cityList = new ArrayList<CityVo>();

	public class MyDialog extends Dialog {
		

		public MyDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_list);
			ListView listView = (ListView) this.findViewById(R.id.list);
			proviceAdapter = new provinceAdapter(mcontext);
			listView.setAdapter(proviceAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					provinceTv.setText(provicelist.get(position).getName());
					cityList.clear();
					cityList.addAll(provicelist.get(position).getCityes());
					
					cityTv.setText(cityList.get(0).getName());
					autoList.clear();
					autoList.addAll(cityList.get(0).getHospitals());
					hosAdapter.notifyDataSetChanged();
					addLay.setVisibility(View.VISIBLE);
					dismiss();
				}
			});

		}
	}

	CityAdapter cAdapter;

	public class MyDialog2 extends Dialog {
		

		public MyDialog2(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_list);
			ListView listView = (ListView) this.findViewById(R.id.list);
			cAdapter = new CityAdapter(mcontext);
			listView.setAdapter(cAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					cityTv.setText(cityList.get(position).getName());
					autoList.clear();
					autoList.addAll(cityList.get(position).getHospitals());
					Log.i("1111111111", autoList.size() + "");
					hosAdapter.notifyDataSetChanged();
					dismiss();
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addButton2:
			Dialog adddialog = new AddDialog(mcontext, R.style.SelectDialog);
			adddialog.show();
			showKeyBord(hospitalEdt);
			break;
			
		
		case R.id.provinceLay:
			Dialog dialog = new MyDialog(mcontext, R.style.SelectDialog);
			dialog.show();
			break;
		case R.id.cityLay:
			if (provinceTv.getText().length()<1) {
				ToastUtils.showToast(mcontext, "请先选择省份");
			} else {
				Dialog dialog2 = new MyDialog2(mcontext, R.style.SelectDialog);
				dialog2.show();
			}
			break;
		case R.id.searchTv:
			search();
			break;
		case R.id.clear:
			if(searchEdt.getText().toString().trim().length()>0){
			searchEdt.setText("");
			
			chooseLayout.setVisibility(View.VISIBLE);
			empertyLay.setVisibility(View.GONE);
			}
			Log.i("111111111111", autoList.size()+"");
				break;
		default:
			break;
		}
	}
	
	private void search(){
		String s=searchEdt.getText().toString();
		if (s.toString().trim().length() > 0) {
				autoList.clear();
				
			for (HospitalInfo dv : list) {
				if (dv.getName().contains(s.toString())
						|| dv.getCityName().contains(s.toString())
						|| dv.getProvinceName().contains(s.toString())||searchPinYin(s.toString().trim(),dv.getNAME())) {
					autoList.add(dv);
				}
			}
			if(autoList.size()<=0){
				empertyLay.setVisibility(View.VISIBLE);
				hospitalStr=s;
			}else{
				empertyLay.setVisibility(View.GONE);
			}
		}  
		hosAdapter.notifyDataSetChanged();
	}
	
	private boolean ispy(String text,String py){
		
		return false;
	}
	
	private EditText hospitalEdt;
	private TextView deletBtn;
	private TextView cancleBtn;
	public class AddDialog extends Dialog {



		/**
		 * Creates a new instance of MyDialog.
		 */
		public AddDialog(Context context, int theme) {
			super(context, theme);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);

			setContentView(R.layout.pop_addhospital);
			//让键盘可以自动弹出
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			hospitalEdt = (EditText) this.findViewById(R.id.hospitalEdt);
			hospitalEdt.requestFocus();  
			deletBtn = (TextView) this.findViewById(R.id.yes);
			deletBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(hospitalEdt.getText().toString().trim().length()>0){
					Intent intent = new Intent(HospitalListActivity.this,
							MyEditorActivity.class);
					intent.putExtra("hospital_name", hospitalEdt.getText().toString().trim());
					intent.putExtra("province", provinceTv.getText().toString());
					intent.putExtra("city", cityTv.getText().toString());
					setResult(HOSPITAL_RESULT_CODE, intent);
					finish();
					}else{
						ToastUtils.showToast(mcontext, "医院名字长度必须大于0");
					}		
				}
			});
			cancleBtn = (TextView) this.findViewById(R.id.cacle);
			cancleBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dismiss();
				}
			});
		}
	}
	
	public void showKeyBord(EditText  edt){
		InputMethodManager inputManager =  
	               (InputMethodManager) edt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	           inputManager.showSoftInput(edt, 0);  
	           inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	
	/*boolean  searchPinYin(String searchText, String pinyin){
        if (searchText.isEmpty() || pinyin.isEmpty()){
            return false;
        }
        DebugUtil.debug("pinying",searchText+" "+pinyin);
        searchText = searchText.toUpperCase();
        pinyin = pinyin.toUpperCase();

        int searchIndex = 0;
        int lastSearchIndex = -1;
        int pinyinLen = pinyin.length();
        int searchLen = searchText.length();

        for (int i =0; i<pinyinLen && searchIndex < searchLen; ++i) {
            char s = searchText.charAt(searchIndex);
            char s1 = pinyin.charAt(i);
            if (s == s1) {
                if (i==lastSearchIndex+1 || pinyin.charAt(i-1) == ' ') {
                    lastSearchIndex = i;
                    searchIndex += 1;
                    continue;
                }
            } else {
                if (lastSearchIndex >=0 && i>0 && (lastSearchIndex+1) != i && pinyin.charAt(i-1) == ' ' && pinyin.charAt(i)!=' ') {
                    break;
                }
            }
        }

        return searchIndex == searchText.length();
    }*/
	boolean searchPinYin(String searchText, String pinyin){
        if (searchText.isEmpty() || pinyin.isEmpty()){
            return false;
        }

        searchText = searchText.toUpperCase();
        pinyin = pinyin.toUpperCase();

        String[] pinyinAry = pinyin.split(" ");

        for (int i = 0; i <pinyinAry.length; ++i){
            if(matchPinYin(pinyinAry, i, searchText)){
                return true;
            }
        }
        return false;
    }


    boolean matchPinYin(String[]pinyinAry, int index, String searchText){
        //尾部了返回
        if (searchText.isEmpty()){
            return true;
        }

        if (index >= pinyinAry.length){
            return false;
        }
        
        String word = pinyinAry[index];
        //跳过空格
        if (word.isEmpty()){
            return matchPinYin(pinyinAry, index+1, searchText);
        }
        
        //全拼音匹配
        if (searchText.startsWith(word) ){
            return matchPinYin(pinyinAry, index+1, searchText.substring(word.length()));
        }

        //只有首字母匹配
        if (word.charAt(0) == searchText.charAt(0)){
            return matchPinYin(pinyinAry, index+1, searchText.substring(1));
        }

        return false;
    }
public void setTextView(){
    StringBuilder actionText = new StringBuilder();
	actionText
			.append("抱歉没有搜索到该结果，若要新增医院，请拨打客服电话："
					+ "<a style=\"color:blue;text-decoration:none;\" href='singstar'>  "
					+ getResources().getString(R.string.credit_tel) + "</a>");
	empertyTv.setText(Html.fromHtml(actionText.toString()));
	empertyTv.setMovementMethod(LinkMovementMethod
			.getInstance());
	CharSequence text = empertyTv.getText();
	int ends = text.length();
	Spannable spannable = (Spannable) empertyTv.getText();
	URLSpan[] urlspan = spannable.getSpans(0, ends, URLSpan.class);
	SpannableStringBuilder stylesBuilder = new SpannableStringBuilder(text);
	stylesBuilder.clearSpans(); // should clear old spans
	for (URLSpan url : urlspan) {
		TextViewURLSpan myURLSpan = new TextViewURLSpan(url.getURL());
		stylesBuilder.setSpan(myURLSpan, spannable.getSpanStart(url),
				spannable.getSpanEnd(url), spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	empertyTv.setText(stylesBuilder);
}

private class TextViewURLSpan extends ClickableSpan {
	private String clickString;

	public TextViewURLSpan(String clickString) {
		this.clickString = clickString;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    ds.setColor(getResources().getColor(R.color.top_color));
	    ds.setUnderlineText(false); //去掉下划线
	}
	
	@Override
	public void onClick(View widget) {
	if (clickString.equals("singstar")) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri
					.parse("tel:"
							+empertyTv.getText().toString()));
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mcontext, "该设备不支持通话功能",
					Toast.LENGTH_SHORT).show();
		}
		}
	}
}

}
