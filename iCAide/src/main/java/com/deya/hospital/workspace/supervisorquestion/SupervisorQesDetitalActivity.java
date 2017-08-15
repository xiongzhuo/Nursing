package com.deya.hospital.workspace.supervisorquestion;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseRecordFileLisAdapter;
import com.deya.hospital.adapter.DetailImageListAdapter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.FileDownloadThread;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.vo.SupervisorQestionVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.notification.NotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class SupervisorQesDetitalActivity extends BaseActivity implements View.OnClickListener,FileActions {

    private static final int SUMBMIT_SUC = 0x7108;
    private static final int SUMBMIT_FAIL = 0x7109;
    TextView departTv;
    TextView discover;//督察人
    TextView departComformer;//科室确认人
    TextView resupvsionTimeTv;//再次督导时间
    EditText reson_sugesstEdt;//原因分析与改进意见

    EditText checkContentEdt;
    EditText problemEdt;
    EditText sugesstEdt;
    GridView photoGv;
    DetailImageListAdapter imgAdapter;
    SupervisorQestionVo tv = new SupervisorQestionVo();
    List<Attachments> imgList = new ArrayList<Attachments>();
    List<Attachments> fileList = new ArrayList<Attachments>();
    private Button submit;
    private String creatTime = "";
    private String missonTime = "";
    RelativeLayout rlBack;
    private ListView recordGv;
    private BaseRecordFileLisAdapter fileAdapter;
    String recordName = "";
    EditText changeEdt;
    TextView shareTv;
    Button shareBtn;
    LinearLayout recordLay;
    private LinearLayout photoLay;
    private CommonTopView topView;
    LinearLayout suggestlay;
    String questhionId = "";
    public static final int ADD_PRITRUE_CODE = 9009;
    TaskVo sdv;
    Button continueBtn;
    // 压缩图片的msg的what

    public static final int COMPRESS_IMAGE = 0x17;

    private List<TaskVo> newTaskList = new ArrayList<TaskVo>();
    private String departmentName;
    private String departmentId;
    private List<Attachments> supervisorFileList = new ArrayList<Attachments>();
    private Gson gson;
    private boolean isLoading;
    int questionState;
    String creatorId = "";
    TextView question_type;
    LinearLayout reson_sugesstlay;
    private TextView img_headview, record_headView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superversion_question_detail2);
        questhionId = getIntent().getStringExtra("id");
        if (getIntent().hasExtra("state")) {
            questionState = getIntent().getIntExtra("state", 0);
            creatorId = getIntent().getStringExtra("creatorId");
        }

        gson = new Gson();
        intTopView();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void intTopView() {
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.setTitle(tv.getTitle());
        topView.onbackClick(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });
        topView.onRightClick(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sdv.getId() == -1) {
                    return;
                }

                Intent it = new Intent(mcontext, SupervisorQueCreatActivity.class);
//                SupervisorQestionVo sv = new SupervisorQestionVo();
//                sv.setDepartment(sdv.getDepartment());
//                sv.setDepartmentName(sdv.getDepartmentName());
//                sv.getAttachments().addAll(fileList);
//                sv.getAttachments().addAll(imgList);
//                sv.setIs_feedback_department(sdv.getIs_feedback_department());
//                sv.setExist_problem(sdv.getExist_problem());
//                sv.setCheck_content(sdv.getCheck_content());
//                sv.setDeal_suggest(sdv.getDeal_suggest());
//                sv.setId(sdv.getId());
//                sv.setRemind_time(sdv.getRemind_time());
                it.putExtra("data", sdv);
                it.putExtra("reEditor", true);
                startActivity(it);
                finish();
            }
        });
        topView.showRightView(View.GONE);
    }

    PartTimeStaffDialog tipdialog;

    public void showTips() {
        tipdialog = new PartTimeStaffDialog(mcontext, false, "是否保存本次编辑？",
                new PartTimeStaffDialog.PDialogInter() {
                    @Override
                    public void onEnter() {
                        finish();
                    }

                    @Override
                    public void onCancle() {
                        finish();
                    }
                });
        tipdialog.show();
    }

    public void resetMainRemark() {
        tv.setImprove_suggest(changeEdt.getText().toString());
        List<Attachments> attachments = new ArrayList<Attachments>();
        attachments.addAll(imgList);
        attachments.addAll(fileList);
        tv.setFileList(gson.toJson(attachments).toString());
        tv.setMobile(tools.getValue(Constants.MOBILE));
        tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        // tv.setUpdatedTask(tv.getStatus() == 0 ? true : false);
        // tv.setStatus(1);
        tv.setIs_main_remark(1);
    }

    boolean isChanged = false;

    private void initView() {
        discover = (TextView) this.findViewById(R.id.discover);
        departComformer = (TextView) this.findViewById(R.id.departComformer);
        resupvsionTimeTv = (TextView) this.findViewById(R.id.resupvsionTimeTv);
        reson_sugesstEdt = (EditText) this.findViewById(R.id.reson_sugesstEdt);


        checkContentEdt = (EditText) this.findViewById(R.id.checkContentEdt);
        problemEdt = (EditText) this.findViewById(R.id.problemEdt);
        sugesstEdt = (EditText) this.findViewById(R.id.sugesstEdt);
        photoGv = (GridView) this.findViewById(R.id.photoGv);
        question_type=findView(R.id.question_type);

        suggestlay = (LinearLayout) this.findViewById(R.id.suggestlay);
        reson_sugesstlay = (LinearLayout) this.findViewById(R.id.reson_sugesstlay);


        imgAdapter = new DetailImageListAdapter(mcontext, imgList,
                new DetailImageListAdapter.ImageAdapterInter() {

                    @Override
                    public void ondeletImage(int position) {
                        imgList.remove(position);
                        imgAdapter.setImageList(imgList);

                    }
                });
        imgAdapter.setDeletVisible(View.GONE);
        img_headview = (TextView) this.findViewById(R.id.img_headview);
        record_headView = (TextView) this.findViewById(R.id.record_headView);
        photoGv.setAdapter(imgAdapter);
        recordGv = (ListView) this.findViewById(R.id.recordGv);
        fileAdapter=new BaseRecordFileLisAdapter(mcontext,fileList);
        fileAdapter.setOnDeletListener(this);
        recordGv.setAdapter(fileAdapter);
        changeEdt = (EditText) this.findViewById(R.id.sugesst_need_change_Edt);
        if (null != tv.getImprove_suggest()) {
            changeEdt.setText(tv.getImprove_suggest());
        }
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setText(tv.getDepartmentName());

        submit = (Button) this.findViewById(R.id.sumbmitBtn);
        // if(!tv.isUpdatedTask()){
        // submit.setEnabled(false);
        // }
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("1111111", "什么玩意");
                // if (checkContentEdt.getText().toString().trim().length() >=
                // 1) {
                submit.setEnabled(false);
                resetMainRemark();
                starUpload();
                //finish();
            }
        });
        setdata();
        shareBtn = (Button) this.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        continueBtn=findView(R.id.continueBtn);
        continueBtn.setOnClickListener(this);
        //  setUI();//根据权限 配置UI
        getQuestionDetail();//获取问题详情
    }

    public void setImagHeadAndRecordHead() {
        img_headview.setVisibility(imgList.size() > 0 ? View.VISIBLE : View.GONE);
        record_headView.setVisibility(fileList.size() > 0 ? View.VISIBLE : View.GONE);

    }

    public void getQuestionDetail() {
        JSONObject job = new JSONObject();
        try {

            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("id", questhionId);
        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                this, SEND_SUCESS, SEND_FIAL, job,
                "supervisorQuestion/questionDetail");
    }

    public void deletRecord(int position) {
        fileList.remove(position);
        fileAdapter.notifyDataSetChanged();
    }


    /**
     * 根据权限设置UI
     */
    private void setUI() {
        setImagHeadAndRecordHead();
        topView.showRightView((questionState == 3 ||questionState == 2 || questionState == 1) && creatorId.equals(tools.getValue(Constants.USER_ID)) ? View.VISIBLE : View.GONE);

        if (questionState == 1) {
            if (creatorId.equals(tools.getValue(Constants.USER_ID))) {//本人不可确认
                suggestlay.setBackgroundResource(R.drawable.big_round_blue_type_style);
                changeEdt.setEnabled(true);
                questionState = 3;
                submit.setText("结束");
                continueBtn.setVisibility(View.VISIBLE);
            } else {
                submit.setVisibility(View.GONE);
            }
        } else if (questionState == 2) {
            if (tools.getValue(Constants.DEFULT_DEPARTID).equals(sdv.getDepartment())) {//本科室可确认
                reson_sugesstlay.setBackgroundResource(R.drawable.big_round_blue_type_style);
                submit.setText("确认");
                reson_sugesstEdt.setEnabled(true);
            } else {
                if (creatorId.equals(tools.getValue(Constants.USER_ID))) {//本人不可确认
                    suggestlay.setBackgroundResource(R.drawable.big_round_blue_type_style);
                    changeEdt.setEnabled(true);
                    questionState = 3;
                    submit.setText("结束");
                    continueBtn.setVisibility(View.VISIBLE);
                } else {
                    submit.setVisibility(View.GONE);
                }
            }

        } else if (questionState == 3) {
            if (creatorId.equals(tools.getValue(Constants.USER_ID))) {//本人可完成
                suggestlay.setBackgroundResource(R.drawable.big_round_blue_type_style);
                changeEdt.setEnabled(true);
                submit.setText("结束");
                continueBtn.setVisibility(View.VISIBLE);
            } else {
                submit.setVisibility(View.GONE);
            }

        } else if (questionState == 4) {
            submit.setVisibility(View.GONE);
            suggestlay.setBackgroundResource(R.drawable.big_round_gray_type_style);
            reson_sugesstlay.setBackgroundResource(R.drawable.big_round_gray_type_style);
            reson_sugesstEdt.setEnabled(false);
            changeEdt.setEnabled(false);
        }

    }


    public void setLoadingFinishUi() {
        if (sdv.getIs_feedback_department() == 0) {
            reson_sugesstlay.setVisibility(View.GONE);
        }

        if (sdv.getIs_again_supervisor() == 0) {
            suggestlay.setVisibility(View.GONE);
        }
    }

    public void starUpload() {
//        SupervisorQuestionUploader uploader = new SupervisorQuestionUploader();
//        boolean isSucess = uploader.Upload(tv);
//        if (isSucess) {
        dosubmit();

        //   }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoLay:
                tokephote();
                break;
            case R.id.shareBtn:
                if (tv.getStatus() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if(null==sdv){
                            sdv=new TaskVo();
                        }
                        jsonObject.put("id", sdv.getId() + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showShare(WebUrl.LEFT_URL
                            + "/supervisorQuestion/supervisorQuestionShare?u=" + AbStrUtil.getBase64(jsonObject.toString()));
                    Log.i("11111111", WebUrl.LEFT_URL
                            + "/supervisorQuestion/supervisorQuestionShare?u=" + AbStrUtil.getBase64(jsonObject.toString()));
                } else {
                    ToastUtil.showMessage( "请到工作间首页选择上传后的督导本进行分享");
                }
                break;
            case R.id.recordLay:

                break;

            case R.id.timeTv:
                //  showTimeDialog();
                break;
            case R.id.departTv:
                break;
            case R.id.continueBtn:
                resetMainRemark();
                doContinue();
                break;
            default:
                break;
        }

    }

    Dialog timedialog;

//    private void showTimeDialog() {
//        timedialog = new MyDialog(mcontext, R.style.SelectDialog);
//        timedialog.show();
//    }


    public void tokephote() {
        Intent takePictureIntent = new Intent(
                mcontext,
                NewPhotoMultipleActivity.class);
        takePictureIntent.putExtra(NewPhotoMultipleActivity.MAX_UPLOAD_NUM, 9);
        takePictureIntent.putExtra("type", "1");
        takePictureIntent.putExtra("size", "0");
        startActivityForResult(takePictureIntent, ADD_PRITRUE_CODE);
    }

    void setdata() {
        sugesstEdt.setText(tv.getDeal_suggest());
        problemEdt.setText(tv.getExist_problem());
        checkContentEdt.setText(tv.getCheck_content());
    }

    private void getData() {
        departmentName = tv.getDepartmentName();
        departmentId = tv.getDepartment();
        creatTime = tv.getMission_time();
        String atts = tv.getFileList();
        if (!AbStrUtil.isEmpty(atts)) {
            supervisorFileList = gson.fromJson(atts,
                    new TypeToken<List<Attachments>>() {
                    }.getType());
        }
        if (null == supervisorFileList || supervisorFileList.size() < 1) {
            supervisorFileList = tv.getAttachments();
        }
        for (Attachments att : supervisorFileList) {
            if (null == att.getState() || !att.getState().equals("1")) {
                att.setState("2");
            }
            if (att.getFile_type().equals("1")) {
                imgList.add(att);
                Log.i("super", att.getFile_name());
            } else if (att.getFile_type().equals("2")) {
                fileList.add(att);
            }
        }

    }


    public void onSaveData() {


    }


    public void setPower() {
    }

    // 播放语音
    ImageView view;

    public void playRecord(String fileName, ImageView view) {
        showprocessdialog();
        File pdffile = new File(getPath() + "/" + fileName);
        this.recordName = pdffile.toString();
        this.view = view;
        if (isLoading) {
            return;
        }
        if (pdffile.exists()) {
            // playRecord();
            pdffile.delete();
        }
        download(fileName);
    }

    // 下载部分
    private void download(String fileName) {
        showprocessdialog();
        isLoading = true;
        // 获取SD卡目录
        String dowloadDir = getPath();
        File file = new File(dowloadDir);
        Log.i("11111111", file.toString());
        // 创建下载目录
        if (!file.exists()) {
            file.mkdirs();
            Log.i("11111111", file.exists() + "");
        }

        // 读取下载线程数，如果为空，则单线程下载
        int downloadTN = 1;
        // 如果下载文件名为空则获取Url尾为文件名
        String downloadUrl = WebUrl.FILE_LOAD_URL + fileName;
        String pdfName = fileName;
        Log.i("11111111", downloadUrl);
        // 开始下载前把下载按钮设置为不可用
        // 进度条设为0
        // downloadProgressBar.setProgress(0);
        // 启动文件下载线程
        new downloadTask(downloadUrl, Integer.valueOf(downloadTN), dowloadDir
                + pdfName).start();
    }

    // 下载语音部分
    Handler handler = new Handler() {
        private boolean isLoading;

        @Override
        public void handleMessage(Message msg) {
            // 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
            int progress = (Double
                    .valueOf((downloadedSize * 1.0 / fileSize * 100)))
                    .intValue();
            if (progress == 100) {
                isLoading = false;
                dismissdialog();
              //  fileAdapter.playMusic(recordName, view);
                //
            } else {
                // ToastUtils.showToast(mcontext, "当前进度:" + progress + "%");
            }
            // downloadProgressBar.setProgress(progress);
        }

    };
    private int fileSize = 0;
    private int downloadedSize = 0;

    @Override
    public void onDeletFile(int position) {

    }

    @Override
    public void onDeletMedia(int position) {
       deletRecord(position);

    }

    @Override
    public void onPlayMedia(String fileName, ImageView view) {

    }

    @Override
    public void onStopMedia(int position) {

    }

    public class downloadTask extends Thread {
        private int blockSize, downloadSizeMore;
        private int threadNum = 1;
        String urlStr, threadNo, fileName;

        public downloadTask(String urlStr, int threadNum, String fileName) {
            this.urlStr = urlStr;
            this.threadNum = threadNum;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            FileDownloadThread[] fds = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(urlStr);
                URLConnection conn = url.openConnection();
                conn.getHeaderField("Content-Length");
                // 获取下载文件的总大小
                // fileSize = conn.getContentLength();
                fileSize = conn.getContentLength();
                Log.i("1111111",
                        fileSize + "" + conn.getHeaderField("Content-Length"));
                // 计算每个线程要下载的数据量
                blockSize = fileSize / threadNum;
                // 解决整除后百分比计算误差
                downloadSizeMore = (fileSize % threadNum);
                File file = new File(fileName);
                for (int i = 0; i < threadNum; i++) {
                    // 启动线程，分别下载自己需要下载的部分
                    FileDownloadThread fdt = new FileDownloadThread(url, file,
                            i * blockSize, (i + 1) * blockSize - 1);
                    fdt.setName("Thread" + i);
                    fdt.start();
                    fds[i] = fdt;
                }
                boolean finished = false;
                while (!finished) {
                    // 先把整除的余数搞定
                    downloadedSize = downloadSizeMore;
                    finished = true;
                    for (int i = 0; i < fds.length; i++) {
                        downloadedSize += fds[i].getDownloadSize();
                        if (!fds[i].isFinished()) {
                            finished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    handler.sendEmptyMessage(0);
                    // 休息1秒后再读取下载进度
                    sleep(1000);
                }
            } catch (Exception e) {

            }

        }
    }

    public static final String DOWNLOAD_FOLDER_NAME = "Deya/pdf";
    private static final int SEND_FIAL = 0x200045;
    private static final int SEND_SUCESS = 0x200046;

    public String getPath() {
        // TODO Auto-generated method stub
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            return new StringBuilder(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()).append(File.separator)
                    .append(DOWNLOAD_FOLDER_NAME).append(File.separator)
                    .toString();
        }
        return "";
    }

    public void dosubmit() {
        JSONObject job = new JSONObject();
        showUncacleBleProcessdialog();
        try {

            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("id", questhionId);
            if (questionState == 2) {
                job.put("improve_measures", reson_sugesstEdt.getText().toString());
                MainBizImpl.getInstance().onComomRequest(myHandler,
                        this, SUMBMIT_SUC, SUMBMIT_FAIL, job,
                        "supervisorQuestion/confirmQuestion");

            } else if (questionState == 3) {
                if (!creatorId.equals(tools.getValue(Constants.USER_ID))) {
                    return;
                }
                job.put("improve_result_assess", changeEdt.getText().toString());
                MainBizImpl.getInstance().onComomRequest(myHandler,
                        this, SUMBMIT_SUC, SUMBMIT_FAIL, job,
                        "supervisorQuestion/againSubmit");
            }

        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

    }

    public void doContinue() {
        JSONObject job = new JSONObject();
        showprocessdialog();
        try {

            job.put("authent", MyAppliaction.getConstantValue(Constants.AUTHENT));
            job.put("id", questhionId);
            job.put("state","2");
                if (!creatorId.equals(tools.getValue(Constants.USER_ID))) {
                    return;
                }
                job.put("improve_result_assess", changeEdt.getText().toString());
                MainBizImpl.getInstance().onComomRequest(myHandler,
                        this, SUMBMIT_SUC, SUMBMIT_FAIL, job,
                        "supervisorQuestion/againSubmit");

        } catch (Exception e) {
            Log.e("upload", e.getMessage() + "");
            e.printStackTrace();
        }

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
                                setResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case SEND_FIAL:
                        dismissdialog();
                        if (null != msg && null != msg.obj) {
//                            try {
//                                setResult(new JSONObject(msg.obj.toString()));
//                            } catch (JSONException e5) {
//                                e5.printStackTrace();
//                            }
                        }
                        ToastUtils.showToast(mcontext,
                                "亲，您的网络不顺畅哦！");
                        submit.setEnabled(true);
                        break;
                    case SUMBMIT_SUC:
                        dismissdialog();
                        if (null != msg && null != msg.obj) {
                            try {
                                setSumbmitResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case SUMBMIT_FAIL:
                        dismissdialog();
                        ToastUtils.showToast(mcontext,"网络链接异常请稍后再试");
                        break;
                    default:
                        break;


                }
            }
        }
    };

    private void setSumbmitResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            JSONObject job = jsonObject.optJSONObject("info");
            if (null == job) {
                return;
            }
            TaskVo sv = gson.fromJson(job.toString(), TaskVo.class);
            Intent it = new Intent(mcontext, SupQueTipsActivity.class);
            if (sv.getState() == 3||sv.getState()==2) {

                it.putExtra("tipIndex", 1);

            } else if (sv.getState() == 4) {
                it.putExtra("tipIndex", 2);
            }
            it.putExtra("id", questhionId+ "");
            sendUpdateBroadCast(sv.getId(), sv.getState(), false);
            startActivity(it);
            finish();
        }
    }


    protected void setResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            JSONObject job = jsonObject.optJSONObject("info");
            if (null == job) {
                return;
            }
            TaskVo sv = gson.fromJson(job.toString(), TaskVo.class);
            if (null == sv) {

                return;
            }
            sdv = sv;
            questionState = sdv.getState();

            creatorId = sdv.getUid();
            checkContentEdt.setText(sv.getCheck_content());
            problemEdt.setText(sv.getExist_problem());
            sugesstEdt.setText(sv.getDeal_suggest());
            question_type.setText(getTypeName(sv.getOrigin()));
            String depetrcomferm = sv.getDepartment_confirm_user_name();
            discover.setText(sv.getUser_name() + " " + sv.getUser_regis_job() + " " + sv.getCreate_time());
            if (sv.getIs_feedback_department() == 1) {
                departComformer.setText(AbStrUtil.isEmpty(depetrcomferm) ? "科室未确认" : depetrcomferm + " " + sv.getDepartment_confirm_user_regis_job() + " " + sv.getDepartment_confirm_time());
            } else {
                departComformer.setText(AbStrUtil.isEmpty(depetrcomferm) ? "未反馈到科室" : depetrcomferm + " " + sv.getDepartment_confirm_user_regis_job() + " " + sv.getDepartment_confirm_time());
            }

            departTv.setText(sv.getDepartmentName());
            reson_sugesstEdt.setText(sv.getImprove_measures());
            changeEdt.setText(sv.getImprove_result_assess());
            String aginTime = sv.getRemind_date();
            resupvsionTimeTv.setText(sv.getIs_again_supervisor() == 0 ? "未设再次督导" : aginTime);
            topView.setTitle(sv.getTitle());
            List<Attachments> atts = sv.getAttachments();
            if (null != atts) {
                supervisorFileList.clear();
                supervisorFileList.addAll(atts);
            }

            if (null == supervisorFileList) {
                supervisorFileList = new ArrayList<Attachments>();
            }
            for (Attachments att : supervisorFileList) {
                if (null == att.getState() || !att.getState().equals("1")) {
                    att.setState("2");
                }
                if (att.getFile_type().equals("1")) {
                    imgList.add(att);
                    Log.i("super", att.getFile_name());
                } else if (att.getFile_type().equals("2")) {
                    fileList.add(att);
                }
            }
            imgAdapter.setImageList(imgList);
            fileAdapter.notifyDataSetChanged();
            setUI();
            setLoadingFinishUi();

            sendUpdateBroadCast(sv.getId(), sv.getState(), false);
        } else {
            ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        }

    }

    /**
     * 获取来源
     */
    Map<Integer,String> map=new HashMap<>();
    void initTypes(){
        String str= SharedPreferencesUtil.getString(mcontext, "get_dudao_types", "");
        ResistantMutiTextVo rmtv= TaskUtils.gson.fromJson(str,ResistantMutiTextVo.class);
        if(null!=rmtv){
            for(ResistantMutiTextVo.ResultListBean resultListBean:rmtv.getResultList()){
                map.put(resultListBean.getData_id(),resultListBean.getData_name());

            }
        }
    }
    private String getTypeName(int origin) {
        if(map.size()<=0){
            initTypes();
        }
        return AbStrUtil.isEmpty(map.get(origin))?"":map.get(origin);
    }
    public void sendUpdateBroadCast(int id, int state, boolean isFinishFront) {
        Intent brodcastIntent = new Intent();
        brodcastIntent.setAction(isFinishFront ? SupQuestionSearchListActivity.FINISH_QUES_LIST : SupQuestionSearchListActivity.UPDATA_ACTION);
        brodcastIntent.putExtra("id", id);
        brodcastIntent.putExtra("state", state);
        sendBroadcast(brodcastIntent);

    }

    // 发送通知
    private void setReminder(String time, String isSend) {

        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // create a PendingIntent that will perform a broadcast
        PendingIntent pi = PendingIntent.getBroadcast(mcontext, 0, new Intent(
                this, NotificationReceiver.class), 0);

        if (isSend.equals("0")) {
            // just use current time + 10s as the Alarm time.
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            // 可以根据项目要求修改，秒、分钟、提前、延后
            c.add(Calendar.SECOND, 10);
            c.add(Calendar.DAY_OF_MONTH, 10);
            // schedule an alarm
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (time == null) {
                return;
            }
            try {
                date = sdf.parse(time);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
        } else {
            // cancel current alarm
            am.cancel(pi);
        }

    }

    private void showShare(String url) {

        showShareDialog("通用督导本问题反馈", sdv.getCheck_content(),url);

    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;


}


