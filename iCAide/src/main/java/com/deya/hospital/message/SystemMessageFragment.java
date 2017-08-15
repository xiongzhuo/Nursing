package com.deya.hospital.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.picMessageVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SystemMessageFragment extends BaseFragment implements
		OnClickListener {
	private static final int MSSAGE_SUCESS = 0x30001;
	private static final int MSSAGE_FAILE = 0x30002;
	private static final int PICMSSAGE_SUCESS = 0x30003;
	private static final int PICMSSAGE_FAILE = 0x30004;
	private Tools tools;
	private LayoutInflater inflater;
	private View view;
	private MyHandler myHandler;
	private ListView listView;
	
	private TextView txmessagetitle;
	private TextView txmessagestate;
	private TextView txmessagecontent;
	private TextView txmessagetime;
	private TextView picTitle,picContent,picTime;
	private RelativeLayout rel_message;
	RelativeLayout pic_messageLay;
	private static SystemMessageFragment contactBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstance) {
		this.inflater = inflater;
		tools = new Tools(getActivity(), Constants.AC);
		initMyHandler();
		if (view == null) {
			view = inflater.inflate(R.layout.activity_messagae, container,
					false);
		} else {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
		}

		initView();
		return view;
	}

	 /**
	   * getInstance:【获取ClassifyVo实例】. <br/>
	   * .@return.<br/>
	   */
	  public static SystemMessageFragment getInstance() {
	    if (contactBean == null) {
	      contactBean = new SystemMessageFragment();
	    }
	    return contactBean;
	  }

	private void initView() {
		txmessagetitle = (TextView) view.findViewById(R.id.message_title);
		txmessagestate = (TextView) view.findViewById(R.id.message_state);
		txmessagecontent = (TextView) view.findViewById(R.id.message_content);
		txmessagetime = (TextView) view.findViewById(R.id.message_time);
		listView = (ListView) view.findViewById(R.id.listView);
		pic_messageLay=(RelativeLayout) view.findViewById(R.id.pic_messageLay);
		pic_messageLay.setOnClickListener(this);

		rel_message = (RelativeLayout) view.findViewById(R.id.rel_message);
		rel_message.setOnClickListener(this);
		picTitle=(TextView) view.findViewById(R.id.pic_title);
		picContent=(TextView) view.findViewById(R.id.pic_content);
		picTime=(TextView) view.findViewById(R.id.pic_time);
		
		
		sendMessagerequst();
		sendPicMessagerequst();

	}

	private String content;
	private String msg_time;

	private void sendMessagerequst() {
		JSONObject job = new JSONObject();
		try {

			job.put("authent", tools.getValue(Constants.AUTHENT));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,getActivity(), MSSAGE_SUCESS,
				MSSAGE_FAILE, job,"msg/sendSysMsgInfo");
	}

	private void sendPicMessagerequst() {
		showprocessdialog();
		JSONObject job = new JSONObject();
		JSONObject json = new JSONObject();
		try {

			job.put("authent", tools.getValue(Constants.AUTHENT));
			job.put("id", "0");
			job.put("groups", "1");
			job.put("direct", "1");
			Log.i("1111", job.toString());
			String jobStr = AbStrUtil.parseStrToMd5L32(AbStrUtil
					.parseStrToMd5L32(job.toString()) + "msg/sendImageTextMsg");
			json.put("token", jobStr);
			json.put("msg_content", job.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MainBizImpl.getInstance().onComomRequest(myHandler,getActivity(),
				PICMSSAGE_SUCESS, PICMSSAGE_FAILE, job,"msg/sendImageTextMsg");
	}

	/**
	 * .本来所有消息的接收对象
	 */
	private void initMyHandler() {
		myHandler = new MyHandler(getActivity()) {
			@Override
			public void handleMessage(Message msg) {
				Activity activity = myHandler.mactivity.get();
				if (null != activity) {
					switch (msg.what) {
					case MSSAGE_SUCESS:
						if (null != msg && null != msg.obj) {
							Log.i("1111msg", msg.obj + "");
							try {
								setMessageRes(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case MSSAGE_FAILE:
						Log.i("1111msg", msg.obj + "");
						// ToastUtils.showToast(getActivity(), "");
						break;
					case PICMSSAGE_SUCESS:
						dismissdialog();
						if (null != msg && null != msg.obj) {
							Log.i("1111", msg.obj + "");
							try {
								setPicMessageRes(new JSONObject(
										msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case PICMSSAGE_FAILE:
						dismissdialog();
						break;
					default:
						break;
					}
				}
			}
		};
	}

	Gson gson = new Gson();
	private static List<picMessageVo> picMeaasgelist = new ArrayList<picMessageVo>();


	protected void setPicMessageRes(JSONObject jsonObject) {
		Log.i("111111", jsonObject.toString());
		JSONArray jarr = jsonObject.optJSONArray("msg_content");
		if(null!=jarr&&jarr.length()>0){
		JSONArray jarr2 = jarr.optJSONObject(0).optJSONArray("imageTextMsg");
		if(null!=jarr2){
		List<picMessageVo> list = gson.fromJson(jarr2.toString(),
				new TypeToken<List<picMessageVo>>() {
				}.getType());
		if (list.size() > 0) {
			picMeaasgelist.clear();
			picMeaasgelist.addAll(list);
			picContent.setText(list.get(0).getTopic());
			picTime.setText(list.get(0).getRecommend_time());
		}
		}
		}
	}

	protected void setMessageRes(JSONObject jsonObject) {
		Log.i("11111111111msg", jsonObject.toString());
		if (jsonObject.has("sysMsg")) {
			JSONObject job = jsonObject.optJSONObject("sysMsg");
			if(null!=job){
			content = job.optString("content");
			msg_time = job.optString("msg_time");
			txmessagetitle.setText("系统消息");
			txmessagestate = (TextView) view.findViewById(R.id.message_state);
			txmessagecontent.setText(job.optString("content"));
			txmessagetime.setText(job.optString("msg_time"));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_message:
			if(null!=content&&content.length()>1){
 		   Intent intent = new Intent(getActivity(), MessageListActivity.class);
			intent.putExtra("time", msg_time);
			intent.putExtra("content", content);
			startActivity(intent);
			}else{
				ToastUtils.showToast(getActivity(), "亲，您暂时没有系统消息哦！");
			}
			break;
		case R.id.pic_messageLay:
			Intent it = new Intent(getActivity(), ACMessageActivity.class);
		//	it.putExtra("list", (Serializable)picMeaasgelist);
			Log.i("1111111111adpter", picMeaasgelist.size()+"");
			startActivity(it);
			break;

		default:
			break;
		}

	}
}
