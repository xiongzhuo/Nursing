package com.deya.hospital.shop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DebugUtil;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.Goods;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 奖品详细页
* @author  yung
* @date 创建时间：2016年1月15日 下午2:56:13 
* @version 1.0
 */
public class ShopGoodsInfoActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout rl_back;
	private ImageView img_back, img_top;

	private PullToRefreshListView listView;
	private TextView backText, title, text_credit, text_goods_title,
			text_goods_content;
	private Button btn_exchange, btn_enter;
	private EditText edt_name, edt_phone, edt_addr;
	private LinearLayout lay_bottom, lay_enter;
	private ScrollView scroll;
	private View line1;
	DisplayImageOptions optionsSquare;
	private Tools tools;
	int myInteral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_info);
		tools = new Tools(mcontext, Constants.AC);
		optionsSquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.defult_img)
				.showImageForEmptyUri(R.drawable.defult_img)
				.showImageOnFail(R.drawable.defult_img)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.considerExifParams(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		initView();
		getdata();
		
		try {
			String interal_str = tools.getValue(Constants.INTEGRAL);
			myInteral = Integer.parseInt(interal_str);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 获取用户信息
	 * 获取积分
	 */
	private void getUserInfo(){
		showprocessdialog();
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,ShopGoodsInfoActivity.this, SUCCESS_USER,
				FAIL, job,"member/getUserInfo");
	}
	
	/**
	 * 获取用户积分后，处理相应页面的显示
	 * @param jsonObject
	 */
	protected void setInfoRes(JSONObject jsonObject) {
		Log.i("111111111", jsonObject.toString());
		
		JSONObject job=jsonObject.optJSONObject("user");
		
		int integra = 0;
		String integral=job.optString("integral");
		if (!AbStrUtil.isEmpty(integral)) {
			integra = Integer.parseInt(integral);
		}
		
		 tools.putValue(Constants.INTEGRAL, integra+"");
		 
		if (goods.getIntegral() <= integra) {
			exchange();
		} else {
			Intent it = new Intent(mcontext, ScoreRuleActivity.class);
			startActivity(it);
		}
		String isVip=job.optString("is_sign");
		if(!AbStrUtil.isEmpty(isVip)&&isVip.equals("0")){
			tools.putValue(Constants.IS_VIP_HOSPITAL, "0");
			
			}else{
				tools.putValue(Constants.IS_VIP_HOSPITAL, "1");
			}
	}

	private void initView() {
		rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		img_back = (ImageView) this.findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		img_top = (ImageView) this.findViewById(R.id.img_top);

		backText = (TextView) this.findViewById(R.id.backText);
		backText.setOnClickListener(this);
		title = (TextView) this.findViewById(R.id.title);
		text_credit = (TextView) this.findViewById(R.id.text_credit);
		text_goods_title = (TextView) this.findViewById(R.id.text_goods_title);
		text_goods_content = (TextView) this
				.findViewById(R.id.text_goods_content);

		btn_exchange = (Button) this.findViewById(R.id.btn_exchange);
		btn_enter = (Button) this.findViewById(R.id.btn_enter);
		btn_exchange.setOnClickListener(this);
		btn_enter.setOnClickListener(this);

		edt_name = (EditText) this.findViewById(R.id.edt_name);
		edt_phone = (EditText) this.findViewById(R.id.edt_phone);
		edt_addr = (EditText) this.findViewById(R.id.edt_addr);

		lay_bottom = (LinearLayout) this.findViewById(R.id.lay_bottom);
		lay_enter = (LinearLayout) this.findViewById(R.id.lay_enter);
		line1 = (View) this.findViewById(R.id.line1);

		lay_bottom.setVisibility(View.GONE);
		lay_enter.setVisibility(View.GONE);
		line1.setVisibility(View.GONE);

		scroll = (ScrollView) this.findViewById(R.id.scroll);
	}

	private Goods goods;

	/**
	 * 获取奖品信息
	 */
	private void getdata() {
		// TODO Auto-generated method stub
		goods = getIntent().getParcelableExtra("data");
		if (null != goods) {
			title.setText(goods.getName());
			text_credit.setText(goods.getIntegral() + "");
			text_goods_title.setText(goods.getName());
			text_goods_content.setText(goods.getDescription());
		}
		if (!AbStrUtil.isEmpty(goods.getPicture())) {
			ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL+goods.getPicture(), img_top, optionsSquare);
			
	    } else {
	    	ImageLoader.getInstance().displayImage("",
	    			img_top, optionsSquare);
	    }
	}

	/**
	 * 展开隐藏控件
	 * 兑换时要填写信息 的 控件
	 */
	private void exchange() {
		line1.setVisibility(View.VISIBLE);
		lay_bottom.setVisibility(View.VISIBLE);
		lay_enter.setVisibility(View.VISIBLE);
		btn_exchange.setVisibility(View.GONE);
		
		edt_phone.setText(tools.getValue(Constants.MOBILE));
		edt_name.setText(tools.getValue(Constants.NAME));
		edt_addr.setText(tools.getValue(Constants.HOSPITAL_NAME));
		
		edt_addr.setFocusable(true);
		edt_addr.setFocusableInTouchMode(true);
		edt_addr.requestFocus();
		Selection.setSelection(edt_addr.getText(), edt_addr.getText().length());

		new Handler().post(new Runnable() {
			public void run() {
				scroll.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	String phone;
	String name;
	String addr;

	/**
	 *  确认兑换处理
	 */
	private void enter() {
		phone = edt_phone.getText().toString().trim();
		name = edt_name.getText().toString().trim();
		addr = edt_addr.getText().toString().trim();
		String regex = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(phone);

		DebugUtil.debug("shopping", phone + " " + name + " " + addr);

		if (!m.find()) {
			ToastUtils.showToast(mcontext,
					res.getString(R.string.edt_check_phone));
			return;
		}
		if (name.length() < 1) {
			ToastUtils.showToast(mcontext,
					res.getString(R.string.edt_check_linkman));
			return;
		}
		if (addr.length() < 1) {
			ToastUtils.showToast(mcontext,
					res.getString(R.string.edt_check_addr));
			return;
		}

		addData();
	}

	private static final int SUCCESS = 0x1055;
	private static final int FAIL = 0x1056;
	
	private static final int SUCCESS_USER = 0x1057;

	/**
	 * 兑换
	 */
	private void addData() {

		showprocessdialog();
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("goods_id", goods.getId());
			job.put("address", addr);
			job.put("name", name);
			job.put("mobile", phone);
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "goods/convertGoods");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
			Log.i("11111111", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,ShopGoodsInfoActivity.this, SUCCESS, FAIL,
				job,"goods/convertGoods");
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
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(msg.obj.toString());
							DebugUtil.debug("goods", "msg.obj.toString()>>"
									+ msg.obj.toString());
							String r = jsonObject.getString("result_id");
							String mymsg = jsonObject.getString("result_msg");
							DebugUtil.debug("goods", "r>" + r);
							ToastUtils.showToast(mcontext, mymsg);
							if (null != r && r.equals("0")) {
								tools.putValue(Constants.ADDR, edt_addr.getText().toString());
								
									// 减积分
									try {
										String interal_str = jsonObject
												.getString("integral");
										if (null != interal_str
												&& interal_str.length()> 0) {
										
											Pattern pattern = Pattern.compile("[0-9]*"); 
											   Matcher isNum = pattern.matcher(interal_str);
											   if( isNum.matches() ){
													tools.putValue(Constants.INTEGRAL,interal_str);
													
													Intent intent=new Intent(mcontext,PrizListActivity.class);
													startActivity(intent);
													finish();
													return;
											   } 
										} 
									} catch (Exception e) {
										// TODO: handle exception
										
									}
									
									tools.putValue(Constants.INTEGRAL,
											(myInteral - goods
													.getIntegral())+"");

									Intent intent=new Intent(mcontext,PrizListActivity.class);
									startActivity(intent);
									finish();
								
							}
							return;
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					ToastUtils.showToast(mcontext, "兑换失败！");
					break;
				case FAIL:
					dismissdialog();
					ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
					break;
				case SUCCESS_USER:
					dismissdialog();
					if (null != msg && null != msg.obj) {
						Log.i("1111msg", msg.obj + "");
						try {
							setInfoRes(new JSONObject(msg.obj.toString()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					break;
				default:
					break;
				}
			}
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_back:
		case R.id.rl_back:
		case R.id.backText:
			finish();
			break;
		case R.id.btn_exchange:
			DebugUtil.debug("shopping",
					"goods.getIntegral()>>" + goods.getIntegral()
							+ " myInteral>>" + myInteral);
			if(goods.getIs_sign()==0){
				if(!tools.getValue(Constants.IS_VIP_HOSPITAL).equals("0")){
					Intent it=new Intent(mcontext,VipTipsActivity.class);
					startActivity(it);
					return;
				}
			}
			getUserInfo();
			
			// Intent it = new Intent(ShopGoodsListActivity.this,
			// SearchActivity.class);
			// it.putExtra("types", typesId);
			// startActivityForResult(it, 0x105);
			break;
		case R.id.btn_enter:
			enter();
			break;
		default:
			break;
		}

	}
}
