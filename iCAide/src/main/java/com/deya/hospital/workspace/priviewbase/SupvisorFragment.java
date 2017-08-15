package com.deya.hospital.workspace.priviewbase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.DetailImageListAdapter;
import com.deya.hospital.adapter.DetailRecordFileLisAdapter;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.TimerDialog;
import com.deya.hospital.quality.QualityPriviewActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.FileActions;
import com.deya.hospital.util.MyBrodcastReceiver;
import com.deya.hospital.util.SimpleSwitchButton;
import com.deya.hospital.util.Tools;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.vo.dbdata.Attachments;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.notification.NotificationReceiver;
import com.im.sdk.dy.common.utils.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */

@SuppressLint({"ValidFragment"})
public class SupvisorFragment extends BaseFragment implements View.OnClickListener {
    public EditText checkContentEdt;
    public EditText sugesstEdt;
    public SimpleSwitchButton feedbackSwitch;
    public SimpleSwitchButton reSupSwitch;
    LinearLayout imgLay, recordLay;
    List<Attachments> imgList = new ArrayList<Attachments>();
    List<Attachments> fileList = new ArrayList<Attachments>();
    GridView photoGv;
    ListView recordListView;
    View rootView;
    DetailImageListAdapter imgAdapter;
    RisistantVo.TaskInfoBean.SupervisorQuestionBean tv;
    private RelativeLayout timelay;
    private TextView timeTv;
    public  EditText problemEdt;
    private DetailRecordFileLisAdapter fileAdapter;
    Tools tools;
    private TextView discover;
    View editorLay;
    View detailEditorLay;
    boolean isDetail;
    TimerDialog timerDialog;
    RisistantVo rv;
    public static final String CHECKCONTENTNEEDCHANGE = "checkCOntentNeedChange";
    MyBrodcastReceiver myBrodcastReceiver;

    public static SupvisorFragment newInstance(RisistantVo.TaskInfoBean.SupervisorQuestionBean tv, RisistantVo rv) {
        SupvisorFragment newFragment = new SupvisorFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", tv);
        bundle.putSerializable("RisistantVo", rv);
        newFragment.setArguments(bundle);

        //bundle还可以在每个标签里传送数据


        return newFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.supervisor_fragment, container,
                    false);
            tools = new Tools(getActivity(), Constants.AC);
            tv = (RisistantVo.TaskInfoBean.SupervisorQuestionBean) args.getSerializable("data");
            rv = (RisistantVo) args.getSerializable("RisistantVo");
            init();

            initRecievier();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            setCheckContent();
        }else{
            if(null==problemEdt){
                return;
            }
            if (!firstQustion.equals(problemEdt.getText().toString())) {
                needAddQuetion = false;
                rv.setNeedChangeState(1);
            }
        }
        Log.i("isVisibleToUser",isVisibleToUser+"");
    }

    @Override
    public void onResume() {
        super.onResume();
        setCheckContent();
    }

    private void initRecievier() {
        myBrodcastReceiver=new MyBrodcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if(intent.getAction().equals(CHECKCONTENTNEEDCHANGE)){
                    setCheckContent();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CHECKCONTENTNEEDCHANGE);
        getActivity().registerReceiver(myBrodcastReceiver, intentFilter);
    }


    String firstQustion = "";

    boolean needAddQuetion = true;
    String listQueStr = "";

    public void setCheckContent() {

        if(null==problemEdt){
            return;
        }
        listQueStr="";

        if(null==rv){
            rv=new RisistantVo();
        }
        RisistantVo.TaskInfoBean.TempListBean rt=rv.getTask_info().getTemp_list();
        String text = checkContentEdt.getText().toString();
        if (tv.getId()<=0&&rt.getContentState() == 0) {
            checkContentEdt.setText(getHeadText(text) + rt.getTitle());
            rt.setContentState(1);
        }

        for (RisistantVo.TempListBean.ItemListBean rti : rt.getItem_list()) {
            String text2 ="";
            if (!AbStrUtil.isEmpty(rti.getItem_score())&&!rti.getItem_score().equals("0")) {
                listQueStr = getHeadText(listQueStr) + rti.getTitle();
            }

        }

        if(null!=tv&&null!=rv&&tv.getId()<=0&&rv.getNeedChangeState()==0){
            problemEdt.setText(listQueStr);
        }
        firstQustion = problemEdt.getText().toString();
        tv.setCheck_content(checkContentEdt.getText().toString());
        tv.setExist_problem(problemEdt.getText().toString());
    }

    @Override
    public void onPause() {

        super.onPause();


    }

    public String getContent(int id) {
        switch (id) {
            case 3:
                return "呼吸机";
            case 4:
                return "血管导管";
            case 5:
                return "导尿管";
            default:
                return "现场督察";
        }
    }

    String getHeadText(String str) {
        return AbStrUtil.isEmpty(str) ? str : str + "\n";

    }

    public void init() {
        editorLay = rootView.findViewById(R.id.editorLay);
        detailEditorLay = rootView.findViewById(R.id.detailEditorLay);
//        if(isDetail){
//            editorLay.setVisibility(View.GONE);
//            detailEditorLay.setVisibility(View.VISIBLE);
//        }else{
        editorLay.setVisibility(View.VISIBLE);
        detailEditorLay.setVisibility(View.GONE);
        //   }


        feedbackSwitch = (SimpleSwitchButton) rootView.findViewById(R.id.feedbackSwitch);
        reSupSwitch = (SimpleSwitchButton) rootView.findViewById(R.id.reSupSwitch);
        timelay = (RelativeLayout) rootView.findViewById(R.id.timelay);
        timeTv = (TextView) rootView.findViewById(R.id.timeTv);
        //timeTv.setText(tv.getRemind_date());
        timeTv.setOnClickListener(this);
        imgLay = (LinearLayout) rootView.findViewById(R.id.imgLay);
        recordLay = (LinearLayout) rootView.findViewById(R.id.recordLay);
        photoGv = (GridView) rootView.findViewById(R.id.photoGv);

        SimpleSwitchButton feedbackSwitch = (SimpleSwitchButton) rootView.findViewById(R.id.feedbackSwitch);
        feedbackSwitch.setText("反馈到科室");
        feedbackSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                tv.setIs_feedback_department(ischeck ? 1 : 0);

            }
        });
        feedbackSwitch.setCheck(tv.getIs_feedback_department() == 1 ? true : false);


        checkContentEdt = (EditText) rootView.findViewById(R.id.checkContentEdt);
        problemEdt = (EditText) rootView.findViewById(R.id.problemEdt);
        sugesstEdt = (EditText) rootView.findViewById(R.id.sugesstEdt);
        photoGv = (GridView) rootView.findViewById(R.id.photoGv);

        SimpleSwitchButton reSupSwitch = (SimpleSwitchButton) rootView.findViewById(R.id.reSupSwitch);
        reSupSwitch.setText("再次督导");

        if(QualityPriviewActivity.isDetail){
            checkContentEdt.setEnabled(false);
            problemEdt.setEnabled(false);
            sugesstEdt.setEnabled(false);
            photoGv.setEnabled(false);
            reSupSwitch.setEnabled(false);
        }
        reSupSwitch.setOncheckChangeListener(new SimpleSwitchButton.SimpleSwitchInter() {
            @Override
            public void onCheckChange(boolean ischeck) {
                tv.setIs_again_supervisor(ischeck ? 1 : 0);
                timelay.setVisibility(ischeck ? View.VISIBLE : View.GONE);

            }
        });
        timelay.setVisibility(tv.getIs_again_supervisor() == 1 ? View.VISIBLE : View.GONE);
        reSupSwitch.setCheck(tv.getIs_again_supervisor() == 1 ? true : false);

        imgAdapter = new DetailImageListAdapter(getActivity(), imgList,
                new DetailImageListAdapter.ImageAdapterInter() {

                    @Override
                    public void ondeletImage(int position) {
                        imgList.remove(position);
                        imgAdapter.setImageList(imgList);

                    }
                });

        photoGv.setAdapter(imgAdapter);
        recordListView = (ListView) rootView.findViewById(R.id.recordListView);
        fileAdapter = new DetailRecordFileLisAdapter(getActivity(), fileList,
                new FileActions() {

                    @Override
                    public void onStopMedia(int position) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onPlayMedia(String fileName, ImageView view) {
                        //playRecord(fileName, view);
                    }

                    @Override
                    public void onDeletMedia(int position) {

                    }

                    @Override
                    public void onDeletFile(int position) {
                        //isChanged = true;
                        fileList.remove(position);
                        fileAdapter.notifyDataSetChanged();

                    }
                });
        recordListView.setAdapter(fileAdapter);


        reSupSwitch.setCheck(tv.getIs_again_supervisor() == 1 ? true : false);
        timelay.setVisibility(View.GONE);
        imgLay.setVisibility(View.GONE);
        recordLay.setVisibility(View.GONE);


        //详情

        discover = (TextView) rootView.findViewById(R.id.discover);
        timerDialog = new TimerDialog(getActivity(), new TimerDialog.TimerInter() {
            @Override
            public void onChooseTime(String time) {
                timeTv.setText(time);

            }
        });

        initData();
    }

    public void initData() {

        checkContentEdt.setText(tv.getCheck_content());
        problemEdt.setText(tv.getExist_problem());
        sugesstEdt.setText(tv.getDeal_suggest());
        List<Attachments> atts = tv.getAttachments();
        for (Attachments att : atts) {
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
        if (imgList.size() > 0) {
            imgLay.setVisibility(View.VISIBLE);
        }
        if (fileList.size() > 0) {
            recordLay.setVisibility(View.VISIBLE);
        }
        imgAdapter.setImageList(imgList);
        fileAdapter.notifyDataSetChanged();
        sendFileBrodcast();

    }

    private List<Attachments> supervisorFileList = new ArrayList<Attachments>();

    public void setDetailData(RisistantVo.TaskInfoBean.SupervisorQuestionBean sv) {
        checkContentEdt.setText(sv.getCheck_content());
        problemEdt.setText(sv.getExist_problem());
        sugesstEdt.setText(sv.getDeal_suggest());
        discover.setText(sv.getUser_name() + " " + sv.getUser_regis_job() + " " + sv.getCreate_time());
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
        sendFileBrodcast();
        this.tv = sv;
        setUI(sv);

    }

    public void addImg(String fileName) {
        Attachments att = new Attachments();
        att.setFile_name(fileName);
        att.setDate(DateUtil.getCurrentTime() + "");
        att.setFile_type("1");
        att.setState("1");
        imgList.add(att);
        imgAdapter.setImageList(imgList);
        imgLay.setVisibility(View.VISIBLE);
        sendFileBrodcast();
        tv.setAttachments(imgList);
    }

    public void addRecord(String fileName, String time) {
        Attachments att = new Attachments();
        att.setFile_name(fileName);
        att.setTime(time + "");
        att.setFile_type("2");
        att.setState("1");
        fileList.add(att);
        fileAdapter.setRecordList(fileList);
        recordLay.setVisibility(View.VISIBLE);
        sendFileBrodcast();
    }


    void sendFileBrodcast() {
        Intent intent = new Intent(BasePriviewActivity.IMG_CHANGED);
        int size = imgList.size() + fileList.size();
        intent.putExtra("fileSize", size);
        getActivity().sendBroadcast(intent);

    }

    public RisistantVo.TaskInfoBean.SupervisorQuestionBean getMainRemark() {
        if (null == tv) {
            return new RisistantVo.TaskInfoBean.SupervisorQuestionBean();
        }
        int isshow = tv.getIs_again_supervisor();
        setReminder(timeTv.getText().toString(), isshow);

        tv.setCheck_content(checkContentEdt.getText().toString().trim());
        tv.setDeal_suggest(sugesstEdt.getText().toString());
        tv.setExist_problem(problemEdt.getText().toString());
        List<Attachments> attachments = new ArrayList<Attachments>();
        attachments.addAll(imgList);
        attachments.addAll(fileList);
        tv.setAttachments(attachments);
        tv.setRemind_time(timeTv.getText().toString());
        tv.setAgain_supervisor_time(timeTv.getText().toString());
        tv.setFileList(TaskUtils.gson.toJson(attachments).toString());
        tv.setHospital(tools.getValue(Constants.HOSPITAL_ID));
//        if (isFormdata) {
//            formData.setMain_remark(gson.toJson(tv));
//            tv.setIs_main_remark("1");
//        }
        // tv.setUpdatedTask(tv.getStatus() == 0 ? true : false);
        // tv.setStatus(1);
        return tv;

    }


    // 发送通知
    private void setReminder(String time, int isSend) {
        if(AbStrUtil.isEmpty(time)){
            return;
        }
        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
        // create a PendingIntent that will perform a broadcast
        PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, new Intent(getActivity(), NotificationReceiver.class), 0);

        if (isSend == 1) {
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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.timeTv:
                timerDialog.show();
                break;
        }

    }


    /**
     * 根据权限设置UI
     */
    private void setUI(RisistantVo.TaskInfoBean.SupervisorQuestionBean sdv) {

    }

    @Override
    public void onDestroy() {
        if (null != myBrodcastReceiver) {
            getActivity().unregisterReceiver(myBrodcastReceiver);
        }
        super.onDestroy();

    }
}
