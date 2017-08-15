package com.deya.hospital.descover;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyImageListAdapter3;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.setting.UploadMessage;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ShowNetWorkImageActivity;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.vo.QuestionVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.widget.popu.TipsDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AnswerQuestionActivity extends BaseActivity {
    private ImageView switchBtn;
    List<Attachments> imgList = new ArrayList<Attachments>();
    GridView photoGv;
    MyImageListAdapter3 imgAdapter;
    private int isAnonymity = 0;
    private EditText answer_content;
    private Button bottom_btn;
    String[] strings;
    TextView sumbmit;
    private TipsDialog dialog;
    String queId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
        queId = getIntent().getStringExtra("id");
        initView();
        getAnswerCache();
    }

    private void initView() {
        // TODO Auto-generated method stub
//		bottom_btn=(Button) findViewById(R.id.bottom_btn);
        findViewById(R.id.photo).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (imgList.size() < 9) {
                    tokephote();
                } else {
                    Toast.makeText(AnswerQuestionActivity.this, "超过图片数量！", 0)
                            .show();
                }

            }
        });

        photoGv = (GridView) this.findViewById(R.id.photoGv);
        imgAdapter = new MyImageListAdapter3(mcontext, imgList);
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

        switchBtn = (ImageView) this.findViewById(R.id.switchBtn);
        switchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setTaskShowType();
            }
        });

        findViewById(R.id.tv_top_location).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        saveJsonChache(true);
                        finish();
                    }
                });
        findViewById(R.id.backText).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                saveJsonChache(true);

                finish();
            }
        });

        /** 提交 */
        answer_content = (EditText) findViewById(R.id.answer_content);
        sumbmit = (TextView) this.findViewById(R.id.submit);
        sumbmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("".equals(answer_content.getText().toString() + "")) {
                    Toast.makeText(AnswerQuestionActivity.this, "请输入您的答案！", 0)
                            .show();
                } else {
                    showprocessdialog();
                    if (imgList.size() > 0) {
                        uploadImg(imgList);
                        sumbmit.setEnabled(false);
                    } else {
                        showprocessdialog();
                        sumbmit.setEnabled(false);
                        publishQueston();
                    }

                }
            }
        });


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

    public static final int ADD_PRITRUE_CODE = 9009;

    // 压缩图片的msg的what
    public void tokephote() {
        Intent takePictureIntent = new Intent(AnswerQuestionActivity.this,
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
                          //  if (file.exists() && file.length() > 6.5 * 1024) {
                                setFile(file.toString(), 1, "");
//                            } else {
//                                ToastUtils.showToast(mcontext, "非法图片");
//                            }
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

                    case SEND_TASK_SUCESS:// 提交问题失败

                        onSendSucess(msg);
                        // isUpdate = false;
                        break;
                    case SEND_TASK_FIAL:
                        sumbmit.setEnabled(true);
                        break;

                    default:
                        break;
                }
            }
        }

    };

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

    private Gson gson = new Gson();
    private static final int SEND_TASK_SUCESS = 0x20012;
    private static final int SEND_TASK_FIAL = 0x20013;

    private void publishQueston() {
        // TODO Auto-generated method stub
        JSONObject job = new JSONObject();

        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));

            job.put("q_id", getIntent().getStringExtra("id"));
            job.put("content", answer_content.getText().toString() + "");
            job.put("is_niming", isAnonymity);
            if (imgList.size() > 0) {
                String str = gson.toJson(imgList);
                JSONArray jarr = new JSONArray(str);
                job.put("attachment", jarr);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onCirclModeRequest(myHandler, this, SEND_TASK_SUCESS,
                        SEND_TASK_FIAL, job, "questions/submitAnswer");

    }

    public void saveJsonChache(boolean isBack) {
        if (isBack) {
            if (answer_content.getText().toString().trim().length() > 0 || imgList.size() > 0) {
                JSONObject job = new JSONObject();

                try {
                    job.put("content", answer_content.getText().toString() + "");
                    job.put("is_niming", isAnonymity);

                    if (imgList.size() > 0) {
                        String str = gson.toJson(imgList);
                        job.put("attachment", str);
                    }
                    chachejsonObject.put(queId, job.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferencesUtil.saveString(mcontext, "answerChache", chachejsonObject.toString());
            }
        } else {
            if (chachejsonObject.has(queId)) {
                chachejsonObject.remove(queId);
                SharedPreferencesUtil.saveString(mcontext, "answerChache", chachejsonObject.toString());
            }
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


    //	   @Override
//	    public void onLayoutChange(View v, int left, int top, int right,  
//	            int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {  
//	          
//	        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值  
//	          
////	      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);  
////	      System.out.println(left + " " + top +" " + right + " " + bottom);  
//	          
//	          
//	        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起  
//	        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){  
//	              
//	            Toast.makeText(MainActivity.this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();  
//	          
//	        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){  
//	              
//	            Toast.makeText(MainActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();  
//	          
//	        }  
//	        
//	   }
    private void onSendSucess(Message msg) {
        try {
            JSONObject jsonObject = new JSONObject(
                    msg.obj.toString());

            String result_msg = jsonObject.optString("result_msg");
            String result_id = jsonObject.optString("result_id");
            if ("0".equals(result_id)) {
                if (jsonObject.has("question")) {
                    QuestionVo Qvo = gson.fromJson(jsonObject.optJSONObject("question").toString(), QuestionVo.class);
                    senAddQuestionBrod(Qvo);

                    MyBrodcastReceiver reciReceiver = new MyBrodcastReceiver() {
                    };


                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(QuestionUpdateBrodcast.ADD_QUESTION);
                    intentFilter.addAction(QuestionUpdateBrodcast.UPDATE_QUESTION);
                    registerReceiver(reciReceiver, intentFilter);
                }

                saveJsonChache(false);
                if (jsonObject.has("integral")
                        && jsonObject.optInt("integral") > 0) {
                    //showTipsDialog(jsonObject.optString("integral"));
                    dialog = new TipsDialog(mcontext,
                            jsonObject.optString("integral"));
                    dialog.setdismissListener(new TipsDialog.TipsInter() {

                        @Override
                        public void onDismiss() {
                            finish();

                        }
                    });
                    dialog.show();
                }
                dismissdialog();

                //finish();
                ToastUtils.showToast(mcontext,
                        result_msg);
            } else {
                dismissdialog();
                ToastUtils.showToast(mcontext,
                        result_msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void senAddQuestionBrod(QuestionVo vo) {
        Intent brodIntent = new Intent();
        brodIntent.setAction(QuestionUpdateBrodcast.ADD_QUESTION);
        brodIntent.putExtra("data", vo);
        AnswerQuestionActivity.this.sendBroadcast(brodIntent);

    }

    JSONObject chachejsonObject;

    public void getAnswerCache() {
        String str = SharedPreferencesUtil.getString(mcontext, "answerChache", "");
        try {
            if (AbStrUtil.isEmpty(str)) {
                chachejsonObject = new JSONObject();
            } else {
                chachejsonObject = new JSONObject(str);
            }
            if (chachejsonObject.has(queId)) {
                String chacheStr = chachejsonObject.optString(queId);
                if (AbStrUtil.isEmpty(chacheStr)) {
                    return;
                }
                JSONObject job = new JSONObject(chacheStr);
                answer_content.setText(job.optString("content"));
                isAnonymity = job.optInt("is_niming");
                isShowALLTask = isAnonymity == 1 ? true : false;
                setTaskShowType();
                if (job.has("attachment")) {
                    String imgstr = job.optString("attachment");
                    List<Attachments> list = gson.fromJson(imgstr, new TypeToken<List<Attachments>>() {
                    }.getType());
                    imgList.addAll(list);
                    imgAdapter.notifyDataSetChanged();
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
