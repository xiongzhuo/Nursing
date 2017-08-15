package com.deya.hospital.workspace.supervisorquestion;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.timepicker.ScreenInfo;
import com.baoyz.timepicker.WheelMain;
import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseRecordFileLisAdapter;
import com.deya.hospital.adapter.DetailImageListAdapter;
import com.deya.hospital.adapter.DetailImageListAdapter.ImageAdapterInter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseAddFileActivity;
import com.deya.hospital.base.BaseListDialog;
import com.deya.hospital.base.NewPhotoMultipleActivity;
import com.deya.hospital.base.img.CompressImageUtil;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.supervisor.PartTimeStaffDialog.PDialogInter;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.SupervisorEditorLay;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.DepartVos;
import com.deya.hospital.vo.ResistantMutiTextVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.deya.notification.NotificationReceiver;
import com.example.calendar.CalendarMainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SupervisorQueCreatActivity extends BaseAddFileActivity implements
        OnClickListener,FileActions {
    EditText checkContentEdt;
    EditText problemEdt;
    EditText sugesstEdt;
    GridView photoGv;
    DetailImageListAdapter imgAdapter;
    TaskVo tv = new TaskVo();
    List<Attachments> imgList = new ArrayList<Attachments>();
    List<Attachments> fileList = new ArrayList<Attachments>();
    private Button submit;
    private ListView recordGv;
    private BaseRecordFileLisAdapter fileAdapter;
    EditText changeEdt;
    TextView departTv;
    Button shareBtn;
    private CommonTopView topView;
    private TextView timeTv;
    private RelativeLayout timelay;
    SupervisorEditorLay commonEditorLay;
    LinearLayout suggestlay;
    TextView img_headview, record_headView;

    TextView queSrc;//来源
    SupSrcDialog origDialog;
    //    int [] srcId={1,5,4,3,10,8,11,9,12,13};
//    String [] srcName={"WHO手卫生观察","外科手卫生操作考核","手消毒消耗量","临床质控","环境物表清洁","多重耐药感染防控","安全注射"
//    ,"三管感染防控","手术部位感染防控","医疗废物"};
    List<ResistantMutiTextVo.ResultListBean> srcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superversion_question_detail);
        tools = new Tools(mcontext, Constants.AC);
        initHandler();

        tv = (TaskVo) getIntent().getSerializableExtra("data");
        if(null!=tv){
            getData();
        } else {
            tv = new TaskVo();
        }
        intTopView();
        initView();
    }


    private void intTopView() {
        srcList=new ArrayList<>();
        String str= SharedPreferencesUtil.getString(mcontext, "get_dudao_types", "");
        ResistantMutiTextVo rmtv=TaskUtils.gson.fromJson(str,ResistantMutiTextVo.class);
        if(null!=rmtv){
            srcList.addAll(rmtv.getResultList());}
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.setTitle(AbStrUtil.isEmpty(tv.getTitle()) ? "通用督导本" : tv.getTitle());
        topView.onbackClick(this, new OnClickListener() {

            @Override
            public void onClick(View v) {
                onback();


            }


        });
        topView.onRightClick(this, new OnClickListener() {

            @Override
            public void onClick(View v) {
                showRecordPopWindow(0,false,false);
            }
        });

    }


    public void onback(){
        String sugesstion = sugesstEdt.getText().toString();
        String checkConten = checkContentEdt.getText().toString();
        String problen = problemEdt.getText().toString();
        if (checkContentEdt == null) {
            return;
        }
        if (!tv.getCheck_content().equals(checkConten) || !tv.getDeal_suggest().equals(sugesstion)
                || !problen
                .equals(tv.getExist_problem())
                ) {
            isChanged = true;

        }
        if (isChanged) {
            showTips();
        } else {
            finish();
        }
    }
    PartTimeStaffDialog tipdialog;

    public void showTips() {

        tipdialog.show();
    }



    boolean isChanged = false;

    private void initView() {
        img_headview = (TextView) this.findViewById(R.id.img_headview);
        record_headView = (TextView) this.findViewById(R.id.record_headView);
        commonEditorLay = (SupervisorEditorLay) this.findViewById(R.id.commonEditorLay);
        checkContentEdt = commonEditorLay.getCheckContentEdt();
        SimpleSwitchButton feedbackSwitch = (SimpleSwitchButton) this.findViewById(R.id.feedbackSwitch);
        feedbackSwitch.setText("反馈到科室");
        feedbackSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                tv.setIs_feedback_department(ischeck ? 1 : 0);

            }
        });
        queSrc=findView(R.id.queSrc);

        for(ResistantMutiTextVo.ResultListBean hotVo:srcList){
            if(hotVo.getData_id()==tv.getOrigin()){
                queSrc.setText(hotVo.getData_name()+"");
            }
        }

        queSrc.setOnClickListener(this);
        problemEdt = commonEditorLay.getProblemEdt();
        sugesstEdt = commonEditorLay.getSugesstEdt();
        photoGv = (GridView) this.findViewById(R.id.photoGv);
        timeTv = (TextView) this.findViewById(R.id.timeTv);
        timeTv.setText(tv.getRemind_date());
        timeTv.setOnClickListener(this);
        timelay = (RelativeLayout) this.findViewById(R.id.timelay);
        SimpleSwitchButton reSupSwitch = (SimpleSwitchButton) this.findViewById(R.id.reSupSwitch);
        reSupSwitch.setText("再次督导");

        reSupSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                tv.setIs_again_supervisor(ischeck ? 1 : 0);
                timelay.setVisibility(ischeck?View.VISIBLE:View.GONE);
                timeTv.setText(TaskUtils.getLoaclTime());

            }
        });
        reSupSwitch.setCheck(tv.getIs_again_supervisor() == 1 ? true : false);
        if(tv.getDbid()<=0&&tv.getTask_id()<=0){
            feedbackSwitch.setCheck(true);
        }

        suggestlay = (LinearLayout) this.findViewById(R.id.suggestlay);
        imgAdapter = new DetailImageListAdapter(mcontext, imgList,
                new ImageAdapterInter() {

                    @Override
                    public void ondeletImage(int position) {
                        imgList.remove(position);
                        imgAdapter.setImageList(imgList);

                    }
                });

        photoGv.setAdapter(imgAdapter);
        recordGv = (ListView) this.findViewById(R.id.recordGv);
        fileAdapter = new BaseRecordFileLisAdapter(mcontext, fileList);
        fileAdapter.setOnDeletListener(this);
        recordGv.setAdapter(fileAdapter);
        changeEdt = (EditText) this.findViewById(R.id.sugesst_need_change_Edt);
        if (null != tv.getImprove_suggest()) {
            changeEdt.setText(tv.getImprove_suggest());
        }
        departTv = (TextView) this.findViewById(R.id.departTv);
        departTv.setText(tv.getDepartmentName());
        departTv.setOnClickListener(this);

        String hospitalJob = tools.getValue(Constants.JOB);
        String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
        if (null!=hospitalJob&&hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
            // 兼职感控人员在设置了默认科室后可以直接跳过选择部分
            if (!AbStrUtil.isEmpty("defultDepart")&&AbStrUtil.isEmpty(tv.getDepartment())) {

                tv.setDepartmentName(tools.getValue(Constants.DEFULT_DEPART_NAME));
                tv.setDepartment(defultDepart);
                departTv.setText(tools.getValue(Constants.DEFULT_DEPART_NAME));
            }
        }
        setImagHeadAndRecordHead();
        submit = (Button) this.findViewById(R.id.sumbmitBtn);
        // if(!tv.isUpdatedTask()){
        // submit.setEnabled(false);
        // }
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("1111111", "什么玩意");
                // if (checkContentEdt.getText().toString().trim().length() >=
                // 1) {

                resetMainRemark();
                if (AbStrUtil.isEmpty(tv.getDepartment())) {
                    ToastUtils.showToast(mcontext, "请先选择科室");
                    onChooseDepart();
                    return;
                }

                if(checkContentEdt.getText().toString().trim().length()<1){
                    ToastUtil.showMessage("检查内容不能为空");
                    return;
                }

                starUpload();
            }
            //finish();
        });

        tipdialog = new PartTimeStaffDialog(mcontext, false, "是否保存本次编辑？",
                new PDialogInter() {
                    @Override
                    public void onEnter() {
                        if(tv.getDbid()<=0){
                            resetMainRemark();
                            tv.setStatus(2);
                            TaskUtils.onAddTaskInDb(tv);
                            finish();
                        }else{
                            resetMainRemark();
                            tv.setStatus(2);
                            TaskUtils.onUpdateTaskById(tv);
                            finish();
                        }

                        Intent brodcastIntent = new Intent();
                        brodcastIntent
                                .setAction(CalendarMainActivity.UPDATA_ACTION);
                        SupervisorQueCreatActivity.this
                                .sendBroadcast(brodcastIntent);
                        finish();
                    }

                    @Override
                    public void onCancle() {
                        finish();
                    }
                });
        setdata();
        shareBtn = (Button) this.findViewById(R.id.shareBtn);

        if (tv.getTask_id()>0) {
            shareBtn.setVisibility(View.VISIBLE);
            shareBtn.setOnClickListener(this);
        }else {
            shareBtn.setVisibility(View.GONE);
        }
        origDialog=new SupSrcDialog(mcontext, srcList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv.setOrigin(srcList.get(position).getData_id());
                queSrc.setText(srcList.get(position).getData_name());
                origDialog.dismiss();

            }
        });


    }


    public void deletRecord(int position) {
        fileList.remove(position);
        fileAdapter.notifyDataSetChanged();
    }





    public void starUpload() {
        showprocessdialog();
        TaskUtils.onCommitAfterTask(tv);
        if (NetWorkUtils.isConnect(mcontext)) {
            if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {
                tv.setStatus(1);
                onSendFinish(FormCommitSucTipsActivity.ONLY_WIFI);
            } else {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if( !BaseUploader.getUploader(7).Upload(tv)){
                            tv.setFailNum(tv.getFailNum()+1);
                            tv.setStatus(1);
                            if(tv.getFailNum()>=5){
                                tv.setStatus(4);
                            }

                            onSendFinish(FormCommitSucTipsActivity.UPLOAD_FIAL);
                        }else{
                            onSendFinish(FormCommitSucTipsActivity.UPLOAD_SUC);
                        }
                    }
                }).start();

            }

        } else {
            tv.setStatus(1);
            onSendFinish(FormCommitSucTipsActivity.NET_WORK_DISCONECT);
            return;
        }


    }

    private void  onSendFinish(int state){
        dismissdialog();
        if(tv.getDbid()>0){
            TaskUtils.onUpdateTaskById(tv);
        }else {
            TaskUtils.onAddTaskInDb(tv);
        }
        Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
        it.putExtra("data", tv);
        it.putExtra("commit_status", state);
        startActivity(it);
        finish();
    }
    public static final int ADD_PRITRUE_CODE = 9009;
    // 压缩图片的msg的what


    private Tools tools;
    private List<Attachments> supervisorFileList = new ArrayList<>();
    private Gson gson = new Gson();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoLay:
                showRecordPopWindow(0,false,false);
                break;
            case R.id.shareBtn:
                if (tv.getStatus() == 0) {
                    showShare(WebUrl.LEFT_URL + "/supervisor/supervisorShare?id="
                            + tv.getId());
                    Log.i("11111111", WebUrl.LEFT_URL
                            + "/supervisor/supervisorShare?id=" + tv.getId());
                } else {
                    Toast.makeText(mcontext, "请到工作间首页选择上传后的督导本进行分享", 2000).show();
                }
                break;
            case R.id.recordLay:

                break;

            case R.id.timeTv:
                showTimeDialog();
                break;
            case R.id.departTv:
                if(tv.getTask_id()>0){//再次编辑不让该科室
                    return;
                }
                onChooseDepart();
                break;
            case R.id.queSrc:
                origDialog.showCenter();
                break;
            default:
                break;
        }

    }

    Dialog timedialog;

    private void showTimeDialog() {
        timedialog = new MyDialog(mcontext, R.style.SelectDialog);
        timedialog.show();
    }


    @Override
    public  void onDestroy() {
        if (null != timedialog) {
            timedialog.dismiss();
        }
        super.onDestroy();

    }

    @Override
    public boolean getdefultState() {
        return false;
    }

    @Override
    public void onCheckAll(boolean ischeck) {

    }

    public void tokephote() {
        Intent takePictureIntent = new Intent(
                SupervisorQueCreatActivity.this,
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

    public void setImagHeadAndRecordHead() {
        img_headview.setVisibility(imgList.size() > 0 ? View.VISIBLE : View.GONE);
        photoGv.setVisibility(imgList.size() > 0 ? View.VISIBLE : View.GONE);
        record_headView.setVisibility(fileList.size() > 0 ? View.VISIBLE : View.GONE);
        recordGv.setVisibility(fileList.size() > 0 ? View.VISIBLE : View.GONE);

    }



    private int fileSize = 0;
    private int downloadedSize = 0;

    @Override
    public void onDeletFile(int position) {
        isChanged = true;
        deletRecord(position);
    }

    @Override
    public void onDeletMedia(int position) {

    }

    @Override
    public void onPlayMedia(String fileName, ImageView view) {

    }

    @Override
    public void onStopMedia(int position) {

    }



   void  initHandler(){
       myHandler = new MyHandler(this) {
           @Override
           public void handleMessage(Message msg) {
               Activity activity = myHandler.mactivity.get();
               if (null != activity) {
                   switch (msg.what) {


                       case ADD_SUCESS:
                           if (null != msg && null != msg.obj) {
                               Log.i("1111msg", msg.obj + "");
                               try {
                                   setAddRes(new JSONObject(msg.obj.toString()),
                                           activity);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                           break;
                       case ADD_FAILE:
                           Log.i("1111msg", msg.obj + "");
                           // ToastUtils.showToast(getActivity(), "");
                           break;
                       case COMPRESS_IMAGE:
                           if (null != msg && null != msg.obj) {
                               File file = new File(msg.obj + "");
                               Log.i("1111", file.exists() + "");
                               // if (file.exists() && file.length() > 6.5 * 1024) {
                               setFile(file.toString(), 1, "");
                               // } else {
                               // ToastUtils.showToast(mcontext, "非法图片");
                               // }
                           }
                           break;

                   }
               }
           }
       };
    }


    private void setFile(String file, int type, String time) {
        Attachments att = new Attachments();
        att.setFile_name(file);
        att.setFile_type(type + "");
        att.setTime("");
        att.setState("1");
        imgList.add(att);

        imgAdapter.setImageList(imgList);
        isChanged = true;
        setImagHeadAndRecordHead();
    }


    @Override
    public void AddImgFile(String name) {
        startUploadImage(name);
    }

    @Override
    public void AddRecordFile(String name, double totalTime) {

    }

    @Override
    protected void onChooseSuc(DepartVos.DepartmentListBean bean) {
        departTv.setText(bean.getName());
        tv.setDepartmentName(bean.getName());
        tv.setDepartment(bean.getId()+"");
    }

    @Override
    protected void onChooseSuc(String names, String ids) {

    }

    private void startUploadImage(String name) {
        CompressImageUtil.getCompressImageUtilInstance().startCompressImage(
                myHandler, COMPRESS_IMAGE, name);
    }

    protected void setResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            JSONObject job = jsonObject.optJSONObject("info");
            TaskVo tv2 = gson.fromJson(job.toString(), TaskVo.class);
            Intent it = new Intent(mcontext, FormCommitSucTipsActivity.class);
            if (tv2.getState() == 3) {

                it.putExtra("tipIndex", 1);

            } else if (tv2.getState() == 2) {
                it.putExtra("tipIndex", 1);
            }
            it.putExtra("id", tv2.getId() + "");
            startActivity(it);
            tv.setId(tv2.getId());
            tv.setType("7");
            tv.setStatus(0);
            tv.setTask_id(tv2.getId());
            Intent brodcastIntent2 = new Intent();
            brodcastIntent2.setAction(CalendarMainActivity.UPDATA_ACTION);
            sendBroadcast(brodcastIntent2);
            finish();
        } else {

            submit.setEnabled(true);
        }


        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

    public void resetMainRemark() {
        if (timeTv.getText().length() > 0) {
            int isshow = tv.getIs_again_supervisor();
            setReminder(timeTv.getText().toString(), isshow);
        }
        if (tv.getTask_id()<=0) {
            tv.setMission_time(TaskUtils.getLoaclTime() + "");
        }
        tv.setCheck_content(checkContentEdt.getText().toString().trim());
        tv.setDeal_suggest(sugesstEdt.getText().toString());
        tv.setExist_problem(problemEdt.getText().toString());
        tv.setImprove_suggest(changeEdt.getText().toString());
        List<Attachments> attachments = new ArrayList<Attachments>();
        attachments.addAll(imgList);
        attachments.addAll(fileList);
        tv.setRemind_date(timeTv.getText().toString());
        tv.setRemind_time(timeTv.getText().toString());
        tv.setAgain_supervisor_time(timeTv.getText().toString());
        tv.setFileList(gson.toJson(attachments).toString());
        tv.setMobile(tools.getValue(Constants.MOBILE));
        tv.setType("7");
        tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));
        // tv.setUpdatedTask(tv.getStatus() == 0 ? true : false);
        // tv.setStatus(1);

    }

    // 发送通知
    private void setReminder(String time, int isSend) {

        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        // create a PendingIntent that will perform a broadcast
        PendingIntent pi = PendingIntent.getBroadcast(mcontext, 0, new Intent(
                this, NotificationReceiver.class), 0);

        if (isSend==1) {
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
        SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.SMS, SHARE_MEDIA.SINA,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN_CIRCLE};
        // initCustomPlatforms(shareMedia);
        // showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
        showShareDialog(
                tv.getMain_remark_name(), tv.getCheck_content(), url);
    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;

    public void getAddScore(String id) {
        Log.i("share_umeng", "111111111111111");
        tools = new Tools(mcontext, Constants.AC);
        JSONObject job = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler,
                SupervisorQueCreatActivity.this, ADD_SUCESS, ADD_FAILE, job,
                "goods/actionGetIntegral");
    }

    protected void setAddRes(JSONObject jsonObject, Activity activity) {
        Log.i("share_umeng", "返回次数");
        Log.i("11111111", jsonObject.toString());
        if (jsonObject.optString("result_id").equals("0")) {
            int score = jsonObject.optInt("integral");
            String str = tools.getValue(Constants.INTEGRAL);
            if (null != str) {
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str)
                        + score + "");
            } else {
                tools.putValue(Constants.INTEGRAL, score + "");
            }

        }
    }



    // 时间选择器

    public class MyDialog extends Dialog {

        private Button showBtn;
        protected String orderTime;

        /**
         * Creates a new instance of MyDialog.
         */
        public MyDialog(Context context, int theme) {
            super(context, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO 自动生成的方法存根
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.timepicker);

            LinearLayout timepickerview = (LinearLayout) this
                    .findViewById(R.id.timePicker1);
            ScreenInfo screenInfo = new ScreenInfo(
                    SupervisorQueCreatActivity.this);
            final WheelMain wheelMain = new WheelMain(timepickerview, true);
            wheelMain.screenheight = screenInfo.getHeight();
            Time curTime = new Time(); // or Time t=new Time("GMT+8"); 加上Time
            // Zone资料
            curTime.setToNow(); // 取得系统时间。
            int year = curTime.year;
            int month = curTime.month;
            int day = curTime.monthDay;
            int hour = curTime.hour; // 0-23
            int minute = curTime.minute;
            wheelMain.initDateTimePicker(year, month, day, hour, minute);
            showBtn = (Button) this.findViewById(R.id.btn_ok);

            showBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    orderTime = wheelMain.getTime().toString();
                    timeTv.setText(orderTime);
                    dismiss();

                }
            });

        }
    }

    private class SupSrcDialog extends BaseListDialog<ResistantMutiTextVo.ResultListBean>{

        public SupSrcDialog(Context context, List<ResistantMutiTextVo.ResultListBean> list, AdapterView.OnItemClickListener listener) {
            super(context, list, listener);
        }

        @Override
        protected void intUi() {

            View line=  this.findViewById(R.id.line);
            line.setBackgroundColor(getResources().getColor(R.color.top_color));
            titleTv.setText("问题类别");
            right_txt.setVisibility(View.GONE);

        }

        @Override
        public void setListDta(ViewHolder viewHolder, int position) {
            viewHolder.listtext.setText(srcList.get(position).getData_name());
            viewHolder.listtext.setGravity(Gravity.CENTER);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            onback();
            return false;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
