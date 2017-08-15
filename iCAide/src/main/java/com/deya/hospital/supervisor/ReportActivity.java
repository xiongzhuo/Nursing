package com.deya.hospital.supervisor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.adapter.JobListAdapter;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.vo.dbdata.planListDb;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.SoftarticleViewFlipper;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.JobListVo;
import com.deya.hospital.vo.TipsVo;
import com.deya.hospital.widget.popu.PopuUnTimeReport;
import com.deya.hospital.widget.popu.PopuUnTimeReport.OnPopuClick;
import com.deya.hospital.widget.web.DYWebView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.DbException;

public class ReportActivity extends BaseActivity implements
		OnClickListener {
	private ImageView img_back;
	private TextView text_back,text_title,text_share;
	private DYWebView webview;
	private Button btn_email;
	private String str_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		initViews();
		tools = new Tools(mcontext, Constants.AC);
		findDbJobList();
	}

	private Tools tools;

	private void initViews() {
		img_back=(ImageView)findViewById(R.id.img_back);
		text_back=(TextView)findViewById(R.id.text_back);
		text_title=(TextView)findViewById(R.id.text_title);
		text_share=(TextView)findViewById(R.id.text_share);
		
		btn_email=(Button)findViewById(R.id.btn_email);
		webview=(DYWebView)findViewById(R.id.webview);
		
		img_back.setOnClickListener(this);
		text_back.setOnClickListener(this);
		text_share.setOnClickListener(this);
		btn_email.setOnClickListener(this);
		url = getIntent().getStringExtra("url");
		setWeb(getIntent().getStringExtra("json"));
	}
	
	String baseUrl    = "file:///android_asset/";
	String encoding   = "UTF-8";
	String historyUrl = "";
	String text="" ;
	String url="";
	/**
	 * 
	 * @param url
	 */
	private void setWeb(String jsonStr){
		
		
        try {  
//Return an AssetManager instance for your application's package  
            InputStream is = getAssets().open("report.html");  
            int size = is.available();  
  
            // Read the entire asset into a local byte buffer.  
            byte[] buffer = new byte[size];  
            is.read(buffer);  
            is.close();  
            text= new String(buffer, "utf-8");  
        } catch (IOException e) {  
            e.printStackTrace();
        }  
        
        if(!AbStrUtil.isEmpty(text)){
		 url=text.replace("__BODY__", "<script type=\"text/javascript\">window.ReportData ="+ jsonStr+";</script>");
		
	}
		webview.loadUrlBase(url);
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	private void setTitle(String title){
		text_title.setText(title);
	}
	
	private SoftarticleViewFlipper articleviewFlipper;
	private LinearLayout llsoftarticle;
	private ImageView imgclosearticle;
	List<TipsVo> tipsList;
	public void getTipsData(){
		  String jsonStr = SharedPreferencesUtil.getString(mcontext, "tips_json", null);
		  JSONObject job;
		try {
			job = new JSONObject(jsonStr);
			JSONObject	json=job.optJSONObject("tipsMsgHints");
			JSONArray jarr=json.optJSONArray("select-timer");
			  tipsList = gson.fromJson(jarr
						.toString(), new TypeToken<List<TipsVo>>() {
				}.getType());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	String pname;

	 private void disposeAdvertorial() {
        if (tipsList.size()>0) {
        	llsoftarticle.setVisibility(View.VISIBLE);
            articleviewFlipper.setData(tipsList);
            imgclosearticle.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View view) {
                hideArticleAnimation();
                }
            });
          } else {
            llsoftarticle.setVisibility(View.GONE);
          }
	  }
	 
	 
		//关闭软文
	 private void hideArticleAnimation() {
		    AlphaAnimation alphaaniin = new AlphaAnimation(1.0f, 0.0f);
		    alphaaniin.setDuration(600);
		    llsoftarticle.startAnimation(alphaaniin);
		    llsoftarticle.getAnimation().setAnimationListener(new AnimationListener() {
		      @Override
		      public void onAnimationStart(Animation animation) {}

		      @Override
		      public void onAnimationRepeat(Animation animation) {}

		      @Override
		      public void onAnimationEnd(Animation animation) {
		        llsoftarticle.setVisibility(View.GONE);
		      }
		    });
		  }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
		case R.id.text_back:
			this.finish();
			break;

		case R.id.text_share:
			//show popu
//			ComomListPopWindow hos = new ComomListPopWindow(mcontext);
//			hos.showAtLocation(
//					ReportActivity.this.findViewById(R.id.main),
//					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			
			
			break;
		case R.id.btn_email:
			//提交
			new PopuUnTimeReport(mcontext,_activity,false, ReportActivity.this.findViewById(R.id.main), str_email, new OnPopuClick() {
				
				@Override
				public void enter(String text) {
					// TODO Auto-generated method stub
					str_email=text;
					Toast.makeText(mcontext, text, 500).show();
				}
				
				@Override
				public void cancel() {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		default:
			break;
		}

	}

	List<JobListVo> jobList = new ArrayList<JobListVo>();
	String jobs[];

	public void findDbJobList() {
		try {
			if (null != DataBaseHelper
					.getDbUtilsInstance(mcontext).findAll(JobListVo.class)) {
				jobList = DataBaseHelper
						.getDbUtilsInstance(mcontext).findAll(JobListVo.class);
				Log.i("11111111111joblist", jobList.size() + "");
				jobs = new String[jobList.size()];
				for (int i = 0, j = jobList.size(); i < j; i++) {
					jobs[i] = jobList.get(i).getName();
				}

			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	JobListAdapter popAdapter;

	public void showPopWindow(Context context, View parent) {
		int width = parent.getWidth();
		;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View vPopWindow = inflater.inflate(R.layout.sweetpopwindow, null,
				true);
		final PopupWindow popWindow = new PopupWindow(vPopWindow, width,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		int px = AbViewUtil.dip2px(mcontext, 10);
		popWindow.showAsDropDown(parent, AbViewUtil.dip2px(mcontext, 0), 0);
		ListView popListView = (ListView) vPopWindow.findViewById(R.id.poplist);
		if (null == popAdapter) {
			popAdapter = new JobListAdapter(mcontext, jobList);
			popListView.setAdapter(popAdapter);
		} else {
			popListView.setAdapter(popAdapter);
		}
		popListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				popWindow.dismiss();

			}
		});
	}

	TaskVo tv;
	List<planListDb> list = new ArrayList<planListDb>();
	private Gson gson = new Gson();


	// 更多弹出框

	boolean isStartRecord=true;
	public int recordStadio=0;
	private View mMenuView;
	LayoutInflater inflater;
	private TextView yesTv, cancelTv;
	LinearLayout recordLay;
	public class ComomListPopWindow extends PopupWindow {

		private TextView yesBtn;
		private EditText edt_email_res;

		public ComomListPopWindow(Context contex) {
			super(contex);
			mcontext = contex;
			inflater = LayoutInflater.from(mcontext);
			mMenuView = inflater.inflate(R.layout.popwindow_email, null);
			
			recordLay=(LinearLayout)mMenuView.findViewById(R.id.recordLay);
			
			edt_email_res = (EditText) mMenuView.findViewById(R.id.edt_email_res);
			
			edt_email_res.setText(str_email);
			
			cancelTv = (TextView) mMenuView.findViewById(R.id.cancle);

			cancelTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
							dismiss();
				}
			});
			yesBtn=(TextView) mMenuView.findViewById(R.id.yes);
			yesBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String email=edt_email_res.getText().toString();
					if(email.equals("")){
						ToastUtils.showToast(mcontext, "请输入邮箱！");
						return;
					}else{
						String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
						Pattern regex = Pattern.compile(check);    
						Matcher matcher = regex.matcher("dffdfdf@qq.com");    
						boolean isMatched = matcher.matches();    
						if(isMatched){
							str_email=edt_email_res.getText().toString();
							dismiss();
						}else{
							ToastUtils.showToast(mcontext, "请正确输入邮箱！");
							return;
						}
					}
				}
			});
			// 设置按钮监听
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.popupAnimation);
			// 实例化一个ColorDrawable颜色为半透明
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(mcontext.getResources().getDrawable(
					android.R.color.transparent));
			setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {

				}
			});

		}
	
	}
}
