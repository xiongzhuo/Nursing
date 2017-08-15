package com.deya.hospital.handwash;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.adapter.TaskLisAdapter;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.task.Tasker;
import com.deya.hospital.task.uploader.BaseUploader;
import com.deya.hospital.task.uploader.FailLogUpload;
import com.deya.hospital.util.ComomDialog;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.NetWorkUtils;
import com.deya.hospital.util.ReportWebViewDemo;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.FormCommitSucTipsActivity;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/1/12
 */
public class NotUpLoadActivity extends BaseCommonTopActivity implements RequestInterface{
    private static final int  TASKLIST_SUCESS =0x02 ;
    ListView planLv;
    TaskLisAdapter adapter;
    List<TaskVo> list;
    LinearLayout empertyView;
    private String monthMinDate;
    private String monthMaxDate;

    @Override
    public String getTopTitle() {
        return "历史纪录";
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_not_upload;
    }

    @Override
    public void initBaseData() {

    }

    public void getTaskList() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("start_date", monthMinDate);
            job.put("end_date",  monthMaxDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,this,
                TASKLIST_SUCESS, job, "task/syncTaskInfos");
    }
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(Tasker.getAllLocalTask());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        monthMinDate = dateFormater.format(cal.getTime()) + "";
        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthMaxDate = dateFormater.format(cal.getTime());
        list=new ArrayList<>();
        planLv=findView(R.id.planLv);
        empertyView=findView(R.id.empertyView);
        adapter=new TaskLisAdapter(mcontext,list);
        planLv.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        if(list.size()<=0){
            planLv.setEmptyView(empertyView);
        }
        planLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final TaskVo tv = list.get(position);
                if (null == tv) {
                    return;
                }

                TipsDialogRigister dialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {
                    @Override
                    public void onItemSelect(int postion) {
                    }

                    @Override
                    public void onEnter() {
                        if (tv.getStatus() == 1) {//上传中的任务 去编辑
                            tv.setStatus(2);
                            TaskUtils.onUpdateTaskById(tv);
                            TaskUtils.onStaractivity(NotUpLoadActivity.this, tv, list);
                            adapter.notifyDataSetChanged();
                        } else {//上传失败的任务 去上报日志
                            showprocessdialog();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (FailLogUpload.getUpload().upload(TaskUtils.gson.toJson(tv))) {
                                        dismissdialog();
                                    } else {
                                        dismissdialog();
                                        ToastUtil.showMessage("日志上传失败");

                                    }
                                }
                            }).start();


                        }
                    }

                    @Override
                    public void onCancle() {
                        if (tv.getStatus() == 1) {
                            if (NetWorkUtils.isConnect(mcontext)) {
                                if (!NetWorkUtils.isWifiState(MyAppliaction.getContext())) {

                                    Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                                    intent.putExtra("data", tv);
                                    intent.putExtra("commit_status", FormCommitSucTipsActivity.ONLY_WIFI);
                                    startActivity(intent);
                                } else {
                                    showprocessdialog();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int type = Integer.parseInt(tv.getType());
                                            Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                                            if (!BaseUploader.getUploader(tv).Upload(tv)) {
                                                tv.setFailNum(tv.getFailNum() + 1);
                                                if (tv.getFailNum() >= 5) {
                                                    tv.setStatus(4);
                                                }
                                                intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_FIAL);
                                            } else {
                                                intent.putExtra("commit_status", FormCommitSucTipsActivity.UPLOAD_SUC);
                                            }
                                            dismissdialog();
                                            TaskUtils.onUpdateTaskById(tv);
                                            intent.putExtra("data", tv);
                                            startActivity(intent);
                                        }
                                    }).start();
                                }
                            } else {
                                Intent intent = new Intent(mcontext, FormCommitSucTipsActivity.class);
                                intent.putExtra("data", tv);
                                intent.putExtra("commit_status", FormCommitSucTipsActivity.NET_WORK_DISCONECT);
                                startActivity(intent);
                                return;
                            }
                        } else {
                            list.remove(tv);
                            Tasker.deleteTask(tv);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                if (tv.getStatus() == 1) {
                    dialog.show();
                    dialog.setContent("数据未提交,是否现在提交？");
                    dialog.setButton("去编辑");
                    dialog.setCancleButton("提交");
                    return;
                } else if (tv.getStatus() == 4) {
                    dialog.show();
                    dialog.setContent("非常抱歉，本条数据出现异常,请上报日志以便我们尽快解决这个问题！");
                    dialog.setButton("上报");
                    dialog.setCancleButton("删除");
                    return;
                }else if(tv.getStatus()==0){
                    Intent toWeb = new Intent(mcontext, ReportWebViewDemo.class);
                    toWeb.putExtra("task_url",
                            WebUrl.TASKDETAILURL + tv.getTask_id());
                    toWeb.putExtra("title", "督导统计");
                    toWeb.putExtra("showbottom", "show");
                    toWeb.putExtra("task_id", tv.getTask_id() + "");
                    toWeb.putExtra("data", tv);
                   startActivity(toWeb);
                }
                TaskUtils.onStaractivity(NotUpLoadActivity.this, tv, list);
            }

        });
        planLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            Dialog deletDialog;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                deletDialog = new ComomDialog(mcontext, "确认删除此任务？",
                        R.style.SelectDialog, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(list.get(position).getStatus()==1){
                            return;
                        }
                        Tasker.deleteTask(list.get(position));
                        list.remove(position);

                        deletDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                });
                deletDialog.show();
                return true;
            }
        });
        getTaskList();
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code) {
            case TASKLIST_SUCESS:
                setTaskList(jsonObject);
                break;
        }
    }

    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }

    protected void setTaskList(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            final JSONArray jarr = jsonObject.optJSONArray("syncTaskInfos");

            new Thread(new Runnable() {

                @Override
                public void run() {
                    List<TaskVo> list = null;
                    try {
                        list = TaskUtils.gson.fromJson(jarr.toString(),
                                new TypeToken<List<TaskVo>>() {
                                }.getType());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                    if (list != null) {
                        Tasker.syncNetworkTask(list);
                    }
                    handler.sendEmptyMessageDelayed(0, 2* 1000);
                }
            }).start();
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            dismissdialog();
            List<TaskVo> listChache = Tasker.getAllLocalTask();
            list.clear();
            list.addAll(listChache);
            adapter.notifyDataSetChanged();
        };
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
