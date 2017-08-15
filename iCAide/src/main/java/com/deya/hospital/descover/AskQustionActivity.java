package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DepartDialogListAdapter2;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.adapter.MyImageListAdapter2;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.ListDialog2;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.setting.UploadMessage;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ShowNetWorkImageActivity;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.QuestionVo;
import com.deya.hospital.widget.popu.TipsDialog;
import com.deya.hospital.widget.popu.TipsDialog.TipsInter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AskQustionActivity extends BaseActivity implements OnClickListener {
	private ImageView switchBtn;
	List<Attachments> imgList = new ArrayList<Attachments>();
	GridView photoGv;
	MyImageListAdapter2 imgAdapter;
	private EditText quesiton_title, question_information;
	private int isAnonymity = 0;
	private String q_type = "0";// 1：手卫生 2：多耐 3：职业暴露 4：三管 5：外科手术',
	private Button btn_questoin_type;
	DepartDialogListAdapter2 adapter;
	String[] strings;
	Button submit;
	TipsDialog dialog;
	SimpleSwitchButton switchButton;
	private int offerState;
	LinearLayout integralLay;
	EditText integralEdt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);

		initView();
		switchBtn = (ImageView) this.findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setTaskShowType();
			}
		});
		getTaskList();
		adapter = new DepartDialogListAdapter2(this, departlist);
	}

	public boolean isShowALLTask = true;

	private void setTaskShowType() {
		if (isShowALLTask) {
			isShowALLTask = false;
			switchBtn.setImageResource(R.drawable.dynamic_but2);
			isAnonymity = 1;

		} else {
			isShowALLTask = true;
			switchBtn.setImageResource(R.drawable.dynamic_but1);
			isAnonymity = 0;
		}

	}

	private PopupWindow popWindow;
	private TextView DepartKindTv;

	private void initView() {
		// TODO Auto-generated method stub
		TextView typeTxt = (TextView) this.findViewById(R.id.typeTxt);
		typeTxt.setText("匿名提问");
		quesiton_title = (EditText) findViewById(R.id.quesiton_title);
		question_information = (EditText) findViewById(R.id.question_information);

		findViewById(R.id.tv_top_location).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		findViewById(R.id.backText).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


		DepartKindTv = (TextView) this.findViewById(R.id.DepartKindTv);
		DepartKindTv.setOnClickListener(this);
		integralLay= (LinearLayout) this.findViewById(R.id.integralLay);
		integralEdt= (EditText) this.findViewById(R.id.integralEdt);

		switchButton =(SimpleSwitchButton) this.findViewById(R.id.isOfferSwitch);
		switchButton.setText("悬赏提问");
		switchButton.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
			@Override
			public void onCheckChange(boolean ischeck) {
				offerState=ischeck?1:0;
				integralLay.setVisibility(ischeck?View.VISIBLE:View.GONE);
				integralEdt.setText("");
			}
		});
		switchButton.setCheck(false);
		findViewById(R.id.photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imgList.size() < 9) {
					tokephote();
				} else {
					Toast.makeText(AskQustionActivity.this, "超过图片数量！", 0)
							.show();
				}

			}
		});

		photoGv = (GridView) this.findViewById(R.id.photoGv);
		imgAdapter = new MyImageListAdapter2(mcontext, imgList);
		photoGv.setAdapter(imgAdapter);

		photoGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				strings = new String[imgList.size()];
				for (int i = 0; i < imgList.size(); i++) {
					strings[i] = imgList.get(i).getFile_name();
				}
				Intent it = new Intent(mcontext, ShowNetWorkImageActivity.class);
				it.putExtra("urls", strings);
				it.putExtra("type", "1");
				it.putExtra("nowImage", position);
				mcontext.startActivity(it);
			}
		});

		submit = (Button) this.findViewById(R.id.publish);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// StartActivity(AnswerQuestionActivity.class);
				if ("0".equals(q_type)) {
					Toast.makeText(AskQustionActivity.this, "请选择问题分类！", 0)
							.show();
				} else if ("".equals(quesiton_title.getText().toString() + "")) {
					Toast.makeText(AskQustionActivity.this, "请输入标题！", 0).show();
				} else {
					if (imgList.size() > 0) {
						showprocessdialog();
						uploadImg(imgList);
						submit.setEnabled(false);
						submit.setText("发布中");
					} else {
						showprocessdialog();
						publishQueston();
						submit.setEnabled(false);
						submit.setText("发布中");
					}

				}
			}

		});

	}

	private Gson gson = new Gson();
	private static final int SEND_TASK_SUCESS = 0x20012;
	private static final int SEND_TASK_FIAL = 0x20013;

	private void publishQueston() {
		// TODO Auto-generated method stub
		JSONObject job = new JSONObject();

		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));

			job.put("q_type", q_type);
			job.put("title", quesiton_title.getText().toString() + "");
			job.put("content", question_information.getText().toString() + "");
			job.put("is_niming", isAnonymity);

			job.put("integral",integralEdt.getText().toString().equals("")?"0":integralEdt.getText().toString());
			if (imgList.size() > 0) {
				String str = gson.toJson(imgList);
				JSONArray jarr = new JSONArray(str);
				job.put("attachment", jarr);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				SEND_TASK_SUCESS, SEND_TASK_FIAL, job,
				"questions/submitQuestion");

	}

	/**
	 * 上传图片
	 */
	String imgId = "";

	private void uploadImg(List<Attachments> list) {
		imgId = "";
		for (Attachments att : list) {
			if (null == att.getState()) {
				att.setState("1");
			}
			if (!att.getState().equals("2")) {
				imgId = att.getFile_name();
				UploadBizImpl.getInstance().propertyUploadPicture(myHandler,
						att.getFile_name(),
						UploadMessage.UPLOAD_PICTURE_SUCCESS,
						UploadMessage.UPLOAD_PICTURE_FAIL);
				break;
			}
		}

	}

	public static final int ADD_PRITRUE_CODE = 9009;

	// 压缩图片的msg的what
	public void tokephote() {
		Intent takePictureIntent = new Intent(AskQustionActivity.this,
				NewPhotoMultipleActivity.class);
		int max_photo_num = 9;
		if (9 - imgList.size() > 0) {
			max_photo_num = 9 - imgList.size();
		}
		takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM,
				max_photo_num);
		takePictureIntent.putExtra("type", "1");
		takePictureIntent.putExtra("size", "0");
		startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
	}

	public static final int COMPRESS_IMAGE = 0x17;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ADD_PRITRUE_CODE && null != data) {
			Log.i("1111111111", data.getExtras() + "");

			for (int i = 0; i < data.getStringArrayListExtra("picList").size(); i++) {
				CompressImageUtil.getCompressImageUtilInstance()
						.startCompressImage(myHandler, COMPRESS_IMAGE,
								data.getStringArrayListExtra("picList").get(i));

			}

		}
	}

	private MyHandler myHandler = new MyHandler(this) {
		@Override
		public void handleMessage(Message msg) {
			Activity activity = myHandler.mactivity.get();
			if (null != activity) {
				switch (msg.what) {
					case COMPRESS_IMAGE:
						if (null != msg && null != msg.obj) {
							File file = new File(msg.obj + "");
							Log.i("1111", file.exists() + "");
//							if (file.exists() && file.length() > 6.5 * 1024) {
								setFile(file.toString(), 1, "");
//							} else {
//								ToastUtils.showToast(mcontext, "非法图片");
//							}
						}
						break;

					case UploadMessage.UPLOAD_PICTURE_SUCCESS:// 上传图片成功

						if (null != msg && null != msg.obj) {
							Log.i("111111111MSG", msg.obj + "");
							try {
								setImagReq(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						break;
					case UploadMessage.UPLOAD_PICTURE_FAIL:// 上传图片失败
						uploadImg(imgList);
						break;

					case UploadMessage.FORM_UPLOAD_PICTURE_SUCCESS:// 上传图片成功

						if (null != msg && null != msg.obj) {
							Log.i("111111111MSG", msg.obj + "");
							// try {
							// setFormImagReq(new JSONObject(
							// msg.obj.toString()));
							// } catch (JSONException e) {
							// e.printStackTrace();
							// }
						}
						break;

					case UploadMessage.SUP_PICTURE_SUCCESS:// 提交问题成功

						if (null != msg && null != msg.obj) {
							Log.i("111111111MSG", msg.obj + "");
							// try {
							// setsupImagReq(new JSONObject(msg.obj.toString()));
							// } catch (JSONException e) {
							// e.printStackTrace();
							// }

						}

						break;

					case SEND_TASK_SUCESS:// 提交问题失败

						onSendSucess(msg);

						break;
					case SEND_TASK_FIAL:
						ToastUtils.showToast(AskQustionActivity.this,
								"提交问题失败，请检查网络！");
						dismissdialog();
						submit.setEnabled(true);
						submit.setText("发布");
						break;

					case TASKLIST_SUCESS:// 获取问题详情成功
						if (null != msg && null != msg.obj) {
							Log.i("gettask", msg.obj + "");
							try {
								resoveData(new JSONObject(msg.obj.toString()));
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						break;
					case TASKLIST_FAILE:// 获取问题详情失败
						ToastUtils
								.showToast(AskQustionActivity.this, "亲！您的网络不给力哦！");
						break;
					default:
						break;
				}
			}
		}
	};

	private void onSendSucess(Message msg) {
		try {
			JSONObject jsonObject = new JSONObject(msg.obj.toString());

			String result_msg = jsonObject.optString("result_msg");
			String result_id = jsonObject.optString("result_id");
			if ("0".equals(result_id)) {
				if (jsonObject.has("question")) {
					QuestionVo Qvo = gson.fromJson(
							jsonObject.optJSONObject("question").toString(),
							QuestionVo.class);
					senAddQuestionBrod(Qvo);
				}
				dismissdialog();
				if (jsonObject.has("integral")
						&& jsonObject.optInt("integral") > 0) {
					//showTipsDialog(jsonObject.optString("integral"));
					dialog = new TipsDialog(mcontext,
							jsonObject.optString("integral"));
					dialog.setdismissListener(new TipsInter() {

						@Override
						public void onDismiss() {
							finish();

						}
					});
					dialog.show();
				}else{
				finish();
				}
				ToastUtils.showToast(AskQustionActivity.this, result_msg);
			} else {
				dismissdialog();
				ToastUtils.showToast(AskQustionActivity.this, result_msg);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if (null != dialog) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	private void resoveData(JSONObject jsonObject) {
		Log.d("aaa", jsonObject + "");
		try {
			JSONArray list = jsonObject.getJSONArray("list");
			for (int i = 0; i < list.length(); i++) {
				JSONObject o1 = list.getJSONObject(i);
				DepartLevelsVo departLevelsVo = new DepartLevelsVo();
				departLevelsVo.setQuestion_type_id(o1.getString("id"));
				departLevelsVo.setQuestion_type_name(o1.getString("name"));
				departlist.add(departLevelsVo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	List<String> sendImag = new ArrayList<String>();
	List<Attachments> localfiles = new ArrayList<Attachments>();// 本地任务中的图片

	public void setImagReq(JSONObject json) {
		localfiles.clear();
		for (Attachments att : imgList) {
			if (!AbStrUtil.isEmpty(att.getFile_name())) {
				if (att.getFile_name().equals(imgId)) {
					att.setFile_name(json.optString("data"));
					att.setState("2");
					att.setDate("");
					att.setImgId("");
					sendImag.add(json.optString("data"));
				}
			} else {
			}

			if (null == att.getState()) {
				att.setState("1");
			}
			if (!att.getState().equals("2")) {
				localfiles.add(att);
			}
		}

		if (localfiles.size() > 0) {
			uploadImg(localfiles);
		} else {
			showprocessdialog();
			publishQueston();
		}

	}

	private void setFile(String file, int type, String time) {
		Attachments att = new Attachments();
		att.setFile_name(file);
		att.setFile_type(type + "");
		if (!AbStrUtil.isEmpty(time) && type == 2) {
			att.setTime(time);

		} else {
			att.setTime("");
		}
		imgList.add(att);
		imgAdapter.notifyDataSetChanged();

	}

	public void deletPhoto(int position) {
		imgList.remove(position);
		imgAdapter.notifyDataSetChanged();

	}

	/**
	 * 显示popwindow
	 */
	public void showPopWindow(Context context, View parent) {
		int width = parent.getWidth();
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		final View vPopWindow = inflater.inflate(
				R.layout.supervisor_time_layout, null, true);
		popWindow = new PopupWindow(vPopWindow, width,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		int px = AbViewUtil.dip2px(mcontext, 10);
		popWindow.showAsDropDown(parent, AbViewUtil.dip2px(mcontext, 0), 0);

	}

	List<DepartLevelsVo> departlist = new ArrayList<DepartLevelsVo>();

	/**
	 * 问题分类
	 **/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

			case R.id.DepartKindTv:
				ListDialog2 dialog = new ListDialog2(mcontext,
						new MyDialogInterface() {

							@Override
							public void onItemSelect(int postion) {
								if (departlist.size() <= 1) {
									return;
								}
								DepartKindTv.setText(departlist.get(postion)
										.getQuestion_type_name());
								q_type = departlist.get(postion)
										.getQuestion_type_id();
							}

							@Override
							public void onEnter() {

							}

							@Override
							public void onCancle() {

							}
						}, adapter);
				dialog.show();
				break;
			default:
				break;
		}
	}

	/**
	 * 获取数据
	 */
	public void getTaskList() {
		JSONObject job = new JSONObject();
		try {
			job.put("authent", tools.getValue(Constants.AUTHENT));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("watchActivity", job.toString());
		MainBizImpl.getInstance().onCirclModeRequest(myHandler, this,
				TASKLIST_SUCESS, TASKLIST_FAILE, job,
				"questions/questionTypeList");

	}

	private static final int TASKLIST_SUCESS = 0x20010;
	private static final int TASKLIST_FAILE = 0x20011;

	public void senAddQuestionBrod(QuestionVo vo) {
		Intent brodIntent = new Intent();
		brodIntent.setAction(QuestionUpdateBrodcast.ADD_QUESTION);
		brodIntent.putExtra("data", vo);
		AskQustionActivity.this.sendBroadcast(brodIntent);

	}



}
