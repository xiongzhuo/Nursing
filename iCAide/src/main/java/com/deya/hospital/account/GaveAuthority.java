package com.deya.hospital.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseRequestActivity;
import com.deya.hospital.base.MainActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.UploadBizImpl;
import com.deya.hospital.setting.UploadMessage;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.JobEntity;
import com.deya.hospital.vo.UserInfoVo;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * 用户信息公共类
 */
public abstract class GaveAuthority extends BaseRequestActivity implements View.OnClickListener {
    public static final int ADD_PRITRUE_CODE = 9009;
    public static final int UPDATE_USERINFO = 0;
    public static final int UPDATE_USERINFO_SEX = 1;
    public static final int UPDATE_USERINFO_DEPARTMENT = 2;
    public static final int UPDATE_USERINFO_NAME = 3;
    public static final int UPDATE_USERINFO_EMAIL = 4;
    public static final int GET_USER_DEPARTLIST = 5;
    public static final int COMPRESS_IMAGE = 0x17;
    public static final int JOB_SUCESS = 6;
    public Tools tools;
    public UserInfoVo userInfo;
    public List<JobEntity.ResultListBean> joblist = new ArrayList<JobEntity.ResultListBean>();
    public CommonTopView topView;
    public LinearLayout layout;
    public ImageView avatar;
    public RelativeLayout rel_name;
    public TextView name;
    public TextView sexTv;
    public TextView sex;
    public TextView tv_email;
    public TextView defultdeparTv;
    public TextView supervision;
    public Button refuseBtn, accept_btn;
    boolean isEditor;//true 为修改资料 false为认证
    public RelativeLayout rel_supervision, defultdepartlay;


    RefusDialog refusDialog;
    public int sexId;
    public BootomSelectDialog sexDilog;
    private int uploadStatus;
    private MyHandler myHandler;
    DisplayImageOptions optionsSquare;
    public LinearLayout frambg;
    private RelativeLayout rl_email;
    public TextView mobileTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tools = new Tools(this, Constants.AC);
        setContentView(getLayoutId());
        optionsSquare = AbViewUtil.getOptions(R.drawable.ic_checkimg);
        initMyHandler();
        bindViews();
    }


    public abstract int getLayoutId();

    private void bindViews() {
        rel_supervision = (RelativeLayout) this.findViewById(R.id.rel_supervision);
        rel_supervision.setOnClickListener(this);
        defultdepartlay = (RelativeLayout) this.findViewById(R.id.defultdepartlay);
        defultdepartlay.setOnClickListener(this);


        topView = (CommonTopView) findViewById(R.id.topView);
        topView.init(this);
        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(this);
        rel_name = (RelativeLayout) findViewById(R.id.rel_name);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        sex.setOnClickListener(this);
        rl_email = (RelativeLayout) findViewById(R.id.rl_email);
        rl_email.setOnClickListener(this);
        tv_email = (TextView) findViewById(R.id.tv_email);
        defultdeparTv = (TextView) findViewById(R.id.defultdeparTv);
        defultdeparTv.setOnClickListener(this);
        supervision = (TextView) findViewById(R.id.supervision);
        refuseBtn = (Button) findViewById(R.id.refuseBtn);
        refuseBtn.setOnClickListener(this);
        mobileTv = (TextView) this.findViewById(R.id.mobileTv);
        findViewById(R.id.rel_sex).setOnClickListener(this);
        accept_btn = (Button) findViewById(R.id.accept_btn);
        accept_btn.setOnClickListener(this);
        intSexDilog();
    }


    public void setEditorRes(JSONObject job) {
        tools.putValue(Constants.SEX, sexId+"");
        tools.putValue(Constants.NAME, name.getText().toString());
        UserUtis.setEditorRes(tools, job);
        ToastUtils.showToast(mcontext, "提交成功");
        if (!isEditor) {//初次认证
            Intent it = new Intent(mcontext, MainActivity.class);
            startActivity(it);
            finish();
        }

    }

    public void intSexDilog() {
        String titles[] = {"男", "女"};
        sexDilog = new BootomSelectDialog(mcontext, titles,
                new BootomSelectDialog.BottomDialogInter() {

                    @Override
                    public void onClick3() {

                    }

                    @Override
                    public void onClick2() {
                        sexId = 1;
                        sex.setText("女");
                        userInfo.setSex("1");
                        if(isEditor){
                            updateInfo(UPDATE_USERINFO_SEX, "sex", sexId+"");
                        }
                    }

                    @Override
                    public void onClick1() {
                        sexId = 0;
                        userInfo.setSex("0");
//                        if(isEditor){
//                        updateInfo("0","","");
//                        }
                        sex.setText("男");
                        if (isEditor) {
                            updateInfo(UPDATE_USERINFO_SEX, "sex", sexId+"");
                        }
                    }

                    @Override
                    public void onClick4() {

                    }
                });
    }

    /**
     * 修改信息
     */

    public void updateInfo(int code,String key,String value){
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));

            if (!AbStrUtil.isEmpty(value)) {
                job.put(key, value);
            }else{
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, this, code, job, "user/updateUser");
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {

        switch (code) {
            case UPDATE_USERINFO:
                dismissdialog();
                JSONObject job = jsonObject.optJSONObject("member");
                setEditorRes(job);
                break;
            case UPDATE_USERINFO_SEX:
                tools.putValue(Constants.SEX, sexId+"");
                break;
            case UPDATE_USERINFO_EMAIL:
                JSONObject job2 = jsonObject.optJSONObject("member");
                tools.putValue(Constants.EMAIL, job2.optString("email"));
                break;
            case UPDATE_USERINFO_DEPARTMENT:
                JSONObject job3 = jsonObject.optJSONObject("member");
                if (!AbStrUtil.isEmpty(job3.optString("department"))){
                    tools.putValue(Constants.DEFULT_DEPARTID, jsonObject.optString("department"));
                } else {
                    tools.putValue(Constants.DEFULT_DEPARTID, "");
                }

                if (!AbStrUtil.isEmpty(job3.optString("departmentName"))) {
                    tools.putValue(Constants.DEFULT_DEPART_NAME, jsonObject.optString("departmentName"));
                } else {
                    tools.putValue(Constants.DEFULT_DEPART_NAME, "");
                }
                break;
            case UPDATE_USERINFO_NAME:
                JSONObject job4 = jsonObject.optJSONObject("member");
                tools.putValue(Constants.NAME, job4.optString("name"));
                break;
            default:
                break;
        }

    }

    @Override
    public void onRequestFail(int code) {
        dismissdialog();
        ToastUtils.showToast(mcontext, "亲，您的网络不顺畅哦！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != refusDialog && refusDialog.isShowing()) {
            refusDialog.dismiss();
        }
    }

    //拍照
    public void tokephote() {
        Intent takePictureIntent = new Intent(this,
                AuthorityPhotoDialog.class);
        takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 1);
        takePictureIntent.putExtra("type", "1");
        takePictureIntent.putExtra("size", "0");
        takePictureIntent.putExtra("activity", getLocalClassName() + "");
        startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRITRUE_CODE && null != data) {
            Log.i("1111111111", data.getExtras() + "");
            uploadStatus = 1;
            for (int i = 0; i < data.getStringArrayListExtra("picList").size(); i++) {
                CompressImageUtil.getCompressImageUtilInstance()
                        .startCompressImage(myHandler, COMPRESS_IMAGE,
                                data.getStringArrayListExtra("picList").get(i));
                Log.i("1111111111", data.getStringArrayListExtra("picList")
                        .get(i));
            }
        }
    }

    private void initMyHandler() {
        myHandler = new MyHandler(this) {
            @Override
            public void handleMessage(Message msg) {
                Activity activity = myHandler.mactivity.get();
                if (null != activity) {
                    switch (msg.what) {
                        case UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS:// 上传图片成功
                            if (null != msg && null != msg.obj) {
                                Log.i("1111", msg.obj + "");
                                try {
                                    setHeadImg(new JSONObject(msg.obj.toString()));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case UploadMessage.SHOP_UPLOAD_PICTURE_FAIL:
                            uploadStatus = 0;
                            ToastUtils.showToast(mcontext,
                                    "您的网络不顺畅，暂时无法上传图片");
                            break;
                        case COMPRESS_IMAGE:// 压缩从选择图片界面返回的图片
                            uploadStatus = 1;
                            Log.i("1111", msg.obj + "");
                            File file = new File(msg.obj + "");
                            Log.i("1111", file.exists() + "");
                            if (null != msg && null != msg.obj) {
                                UploadBizImpl.getInstance().propertyUploadPicture(
                                        myHandler, msg.obj.toString(),
                                        UploadMessage.SHOP_UPLOAD_PICTURE_SUCCESS,
                                        UploadMessage.SHOP_UPLOAD_PICTURE_FAIL);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        };
    }

    private void setHeadImg(JSONObject jsonObject) {
        ImageLoader.getInstance().displayImage(
                WebUrl.FILE_LOAD_URL
                        + jsonObject.optString("data").toString(),
                avatar, optionsSquare);
        tools.putValue(Constants.CERTIFY_PHOTO,
                (jsonObject.optString("data").toString()));
        tools.putValue(Constants.HEAD_PIC,
                (jsonObject.optString("data").toString()));


    }

    public SpannableStringBuilder getSpanString(Context context, String src1, String src2, String src3, int style1, int style2, int style3) {
        SpannableStringBuilder spanStr = null;
        String src = src1 + src2;
        String srclength = src + src3;
        int length1 = src1.length();
        int length2 = src.length();
        int length3 = srclength.length();
        if (context != null) {
            spanStr = new SpannableStringBuilder(srclength);
            if (0 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style1), 0, length1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (length2 != length1) {
                spanStr.setSpan(new TextAppearanceSpan(context, style2), length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanStr.setSpan(new UnderlineSpan(), length1, length2, 0);
            }
            if (length3 != length1 && style3 != 0) {
                spanStr.setSpan(new TextAppearanceSpan(context, style3), length2, length3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }

        return spanStr != null ? spanStr : null;
    }


    /**
     * 获取科室
     */
    public void getDepartMentList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("hospital_id", tools.getValue(Constants.HOSPITAL_ID));
            job.put("authent", tools.getValue(Constants.AUTHENT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onComomReq(this, mcontext,
                        GET_USER_DEPARTLIST, job,
                        "department/getDepartment");
    }
}
