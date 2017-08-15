package com.deya.hospital.workspace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.form.FormListActivity;
import com.deya.hospital.form.FormUtils;
import com.deya.hospital.form.PreViewActivity;
import com.deya.hospital.form.PreViewActivityDetial;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.form.handantisepsis.HandPreViewActivityDetial;
import com.deya.hospital.form.xy.XiangyaPrivewActivity;
import com.deya.hospital.form.xy.XyFormListActivity;
import com.deya.hospital.form.xy.XyPreViewActivityDetial;
import com.deya.hospital.quality.QualityPriviewActivity;
import com.deya.hospital.supervisor.CreatPlansAcitivity;
import com.deya.hospital.supervisor.SupervisionActivity;
import com.deya.hospital.supervisor.SupervisionDetailActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.util.ReportWebViewDemo;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CaseListVo;
import com.deya.hospital.vo.ChildsVo;
import com.deya.hospital.vo.DepartLevelsVo;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.environment.EnviromentFormActivity;
import com.deya.hospital.workspace.environment.EnvironmentMainActivity;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.multidrugresistant.MutiRisitantPriviewActivity;
import com.deya.hospital.workspace.other_forms.OtherFormActivity;
import com.deya.hospital.workspace.other_forms.OtherFormtMainActivity;
import com.deya.hospital.workspace.priviewbase.ResistantListActivity;
import com.deya.hospital.workspace.safeinjection.safeInjectionActivity;
import com.deya.hospital.workspace.site_infection.SiteInfectionPriviewAcitivty;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQesDetitalActivity;
import com.deya.hospital.workspace.supervisorquestion.SupervisorQueCreatActivity;
import com.deya.hospital.workspace.threepips.ThreePipsPriviewActivity;
import com.deya.hospital.workspace.waste_disposal.WastFormActivity;
import com.deya.hospital.workspace.workspacemain.TodayDynamicFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskUtils {
    public static Gson gson = new Gson();

    public static void onStaractivity(Activity activity, TaskVo tv,
                                      List<TaskVo> monthTaskList) {
        int status = tv.getStatus();

        String type = tv.getType();
        if (status == 5) {
            addPripairTask(activity, tv);
            return;
        }
        Intent commonIntent = new Intent();
        switch (type) {
            case "1":
                if (status == 2 || status == 3) {
                    Intent it = new Intent(activity,
                            HandWashTasksActivity.class);
                    it.putExtra("type", tv.getTask_type());
                    it.putExtra("time", tv.getMission_time());
                    it.putExtra(CreatPlansAcitivity.departName,
                            tv.getDepartmentName());
                    it.putExtra(CreatPlansAcitivity.departId, tv.getDepartment());
                    it.putExtra("data", tv);
                    activity.startActivity(it);

                } else if (status == 0) {
                    Intent toWeb = new Intent(activity, ReportWebViewDemo.class);
                    toWeb.putExtra("task_url",
                            WebUrl.TASKDETAILURL + tv.getTask_id());
                    toWeb.putExtra("title", "督导统计");
                    toWeb.putExtra("showbottom", "show");
                    toWeb.putExtra("task_id", tv.getTask_id() + "");
                    toWeb.putExtra("data", tv);
                    Log.i("11111111", tv.getDbid() + "----");
                    activity.startActivity(toWeb);
                }
                break;
            case "2":
                if (tv.getStatus() == 2 || tv.getStatus() == 3) {
                    Intent it = new Intent(activity, SupervisionActivity.class);
                    it.putExtra("time", tv.getMission_time());
                    it.putExtra(CreatPlansAcitivity.departName,
                            tv.getDepartmentName());
                    it.putExtra(CreatPlansAcitivity.departId, tv.getDepartment());
                    it.putExtra("data", tv);
                    Log.i("111111111111", tv.getTask_id() + "");
                    activity.startActivity(it);

                } else if (tv.getStatus() == 0) {
                    if (tv.getIs_main_remark().equals("1")) {
                        Intent toWeb = new Intent(activity,
                                SupervisorQesDetitalActivity.class);
                        toWeb.putExtra("questhionId", tv.getTask_id());
                        activity.startActivityForResult(toWeb, 0x136);
                    } else {
                        Intent toWeb = new Intent(activity,
                                SupervisionDetailActivity.class);
                        toWeb.putExtra("data", tv);
                        activity.startActivityForResult(toWeb, WorkSpaceFragment.ADD_SUPQUESTION);
                    }
                }
                break;
            case "3":

                if (tv.getStatus() == 0) {
                    Intent toWeb = new Intent(activity, PreViewActivityDetial.class);
                    tv.setMain_remark(gson.toJson(FormUtils.onAddMainRemark(tv,
                            monthTaskList)));
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                } else if (tv.getStatus() == 2) {
                    Intent toWeb = new Intent(activity, PreViewActivity.class);
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                }
                break;
            case "4":
                break;
            case "5":
                if (tv.getStatus() == 0) {
                    Intent toWeb = new Intent(activity,
                            HandPreViewActivityDetial.class);
                    tv.setMain_remark(gson.toJson(FormUtils.onAddMainRemark(tv,
                            monthTaskList)));
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                } else if (tv.getStatus() == 2) {
                    Intent toWeb = new Intent(activity,
                            HandDisinfectionPrivewActivity.class);
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                }
                break;
            case "6":
                if (tv.getStatus() == 0) {
                    Intent toWeb = new Intent(activity,
                            XyPreViewActivityDetial.class);
                    tv.setMain_remark(gson.toJson(FormUtils.onAddMainRemark(tv,
                            monthTaskList)));
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                } else if (tv.getStatus() == 2) {
                    Intent toWeb = new Intent(activity, XiangyaPrivewActivity.class);
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                }
                break;
            case "7":
                if (tv.getStatus() == 0) {
                    Intent toWeb = new Intent(activity,
                            SupervisorQesDetitalActivity.class);
                    toWeb.putExtra("id", tv.getId() + "");
                    activity.startActivityForResult(toWeb, 0x136);
                } else if (tv.getStatus() == 2) {
                    Intent toWeb = new Intent(activity, SupervisorQueCreatActivity.class);
                    toWeb.putExtra("data", tv);
                    activity.startActivityForResult(toWeb, 0x136);
                }
                break;
            case "8":
                commonIntent.setClass(activity, MutiRisitantPriviewActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "9":
                commonIntent.setClass(activity, ThreePipsPriviewActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;

            case "10":
                commonIntent.setClass(activity, EnviromentFormActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "11":
                commonIntent.setClass(activity, safeInjectionActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "12":
                commonIntent.setClass(activity, SiteInfectionPriviewAcitivty.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "13":
                commonIntent.setClass(activity, WastFormActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "14":
                commonIntent.setClass(activity, OtherFormActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "16":
                commonIntent.setClass(activity, OtherFormActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "17":
                commonIntent.setClass(activity, QualityPriviewActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            case "15":
                commonIntent.setClass(activity, ConsumptionFormActivity.class);
                activity.startActivity(commonIntent);
                break;
            case "21":
                commonIntent.setClass(activity, QualityPriviewActivity.class);
                addFromTask(activity, tv, commonIntent);
                break;
            default:
                break;

        }

    }


    /**
     * 点击预设任务，跳转
     *
     * @param activity
     * @param tv
     */
    private static void addPripairTask(Activity activity, TaskVo tv) {
        final Intent it = new Intent();
        it.putExtra("time", tv.getMission_time() + "");
        int type = Integer.parseInt(tv.getType());
        if (type != 4) {
            it.putExtra("type", type + "");
        }
        it.putExtra("data", tv);
        startAfterTaskActivity(activity, tv, tv.getMission_time(), it);
    }

    private static void addFromTask(Activity activity, TaskVo tv, Intent commonIntent) {
        if (tv.getStatus() == 0) {
            CaseListVo.CaseListBean bean = gson.fromJson(tv.getTaskListBean(), CaseListVo.CaseListBean.class);
            if (null != bean && bean.getCase_id() <= 0) {
                tv.setWho(true);//没有病例的现场督察
            }

            commonIntent.putExtra("data", tv);
            activity.startActivity(commonIntent);
        } else if (tv.getStatus() == 2) {
            commonIntent.putExtra("data", tv);
            tv.setFinished(0);
            activity.startActivityForResult(commonIntent, 0x136);
        }
    }

    public static void onUpdateTaskById(TaskVo task) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .update(task, WhereBuilder.b("dbid", "=", task.getDbid()));
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static TaskVo onFindTaskById(int dbid) {
        TaskVo tv;
        try {
            tv = DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .findById(TaskVo.class, WhereBuilder.b("dbid", "=", dbid));
        } catch (DbException e) {
            tv = new TaskVo();
            e.printStackTrace();
        }
        return tv;
    }

    public static TaskVo onFindTaskByCaseDbId(int dbid) {
        TaskVo tv = null;
        try {

            tv = DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext()).findFirst(Selector.from(TaskVo.class).where("case_dbid", "=", dbid));
        } catch (DbException e) {
            e.printStackTrace();
        } finally {
            return tv;
        }

    }

    public static TaskVo onFindTaskByKeyAndValue(String key, int value) {
        TaskVo tv = null;
        try {

            tv = DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext()).findFirst(Selector.from(TaskVo.class).where(key, "=", value));
        } catch (DbException e) {
            e.printStackTrace();
        } finally {
            return tv;
        }

    }

    public static void onAddTaskInDb(TaskVo task) {
        try {
            DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext()).save(task);
        } catch (DbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getTaskMissionTime(String creatTime) {
        Calendar calendar = null;
        calendar = new GregorianCalendar();//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (AbStrUtil.isEmpty(creatTime)) {//获取当前时间
            Date date = calendar.getTime();
            date.setMinutes(date.getMinutes() + 10);
            return sdf.format(date);
        } else {
            String time = creatTime + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":"
                    + calendar.get(Calendar.SECOND);
            Date date2 = null;
            try {
                date2 = sdf.parse(time);
                return sdf.format(date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date2.toString();
        }
    }

    public static String getLoaclTime() {
        Calendar calendar = null;
        calendar = new GregorianCalendar();//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    public static String getLoacalDate() {
        Calendar calendar = null;
        calendar = new GregorianCalendar();//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static void getDepartList(Context mcontext, List<DepartLevelsVo> departlist) {
        String str = SharedPreferencesUtil.getString(mcontext, "depart_levels",
                "");
        String childsStr = SharedPreferencesUtil.getString(mcontext,
                "departmentlist", "");
        if(AbStrUtil.isEmpty(childsStr)&&AbStrUtil.isEmpty(str)){
            return;
        }
        List<ChildsVo> list2 = gson.fromJson(childsStr,
                new TypeToken<List<ChildsVo>>() {
                }.getType());
        List<ChildsVo> otherList = new ArrayList<ChildsVo>();
        if (!AbStrUtil.isEmpty(str)) {
            for (ChildsVo cv : list2) {
                if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
                    otherList.add(cv);
                }
            }
            List<DepartLevelsVo> list = gson.fromJson(str,
                    new TypeToken<List<DepartLevelsVo>>() {
                    }.getType());
            departlist.addAll(list);
            for (DepartLevelsVo dlv : departlist) {
                if (dlv.getRoot().getId().equals("0")) {
                    if (dlv.getChilds().size() == 0) {
                        dlv.getChilds().addAll(otherList);
                        break;
                    }
                }
            }
            // TODO Auto-generated catch block
        } else {

            DepartLevelsVo dlv = new DepartLevelsVo();
            dlv.getRoot().setId("0");
            dlv.getRoot().setName("全部");
            dlv.getChilds().addAll(list2);
            departlist.add(dlv);
        }
    }

    public static List<DepartLevelsVo> GetDepartList(Context mcontext, String str, String childsStr) {
        List<DepartLevelsVo> departlist = new ArrayList<>();
        SharedPreferencesUtil.saveString(mcontext, "depart_levels",
                str);
        SharedPreferencesUtil.saveString(mcontext,
                "departmentlist", childsStr);
        List<ChildsVo> list2 = gson.fromJson(childsStr,
                new TypeToken<List<ChildsVo>>() {
                }.getType());
        List<ChildsVo> otherList = new ArrayList<ChildsVo>();
        if (!AbStrUtil.isEmpty(str)) {
            for (ChildsVo cv : list2) {
                if (cv.getParent().equals("0") || cv.getParent().equals("1")) {
                    otherList.add(cv);
                }
            }
            List<DepartLevelsVo> list = gson.fromJson(str,
                    new TypeToken<List<DepartLevelsVo>>() {
                    }.getType());
            departlist.addAll(list);
            for (DepartLevelsVo dlv : departlist) {
                if (dlv.getRoot().getId().equals("0")) {
                    if (dlv.getChilds().size() == 0) {
                        dlv.getChilds().addAll(otherList);
                        break;
                    }
                }
            }
        } else {
            DepartLevelsVo dlv = new DepartLevelsVo();
            dlv.getRoot().setId("0");
            dlv.getRoot().setName("全部");
            dlv.getChilds().addAll(list2);
            departlist.add(dlv);
        }

        return departlist;
    }

    public static void addTask(final Activity activity, int type, String localToday) {
        final Intent it = new Intent();
        it.putExtra("time", localToday + "");
        if (type != 4) {
            it.putExtra("type", type + "");
        }

//        if (iswho) {
//            it.putExtra("isWho", "1");
//        }
       it.putExtra("isAfter", false);
//        if (!hasTaskTypes(type)) {//判断是否已订购
//            TipsDialogRigister tipsDialogRigister = new TipsDialogRigister(activity, new MyDialogInterface() {
//                @Override
//                public void onItemSelect(int postion) {
//
//                }
//
//                @Override
//                public void onEnter() {
//                    it.setClass(activity, WebViewDemo.class);
//                    it.putExtra("url", WebUrl.LEFT_URL + "/gkgzj-help/question_detail.html");
//                    it.putExtra("title", "如何");
//                    activity.startActivity(it);
//                    ;
//                }
//
//                @Override
//                public void onCancle() {
//
//                }
//            });
//            tipsDialogRigister.show();
//            tipsDialogRigister.setContent("您没有订购此功能，如果需要使用，请联系院感负责人订购” ，可以选择“取消”，“了解如何订购” 点击如何订购，跳转到新手帮助");
//            tipsDialogRigister.setButton("如何订购");
//            return;
//        }
        startTaskActivity(activity, type, localToday, it);


    }

    private static void startTaskActivity(Activity activity, int type, String localToday, Intent it) {
        switch (type) {
            case 1:
                it.setClass(activity, HandWashTasksActivity.class);
                activity.startActivity(it);
                break;
            case 2:
                it.setClass(activity, SupervisorQueCreatActivity.class);
                activity.startActivityForResult(it, TodayDynamicFragment.ADD_SUPQUESTION);
                break;
            case 4:
                it.setClass(activity, ConsumptionFormActivity.class);
                TaskVo tv = new TaskVo();
                tv.setMission_time(localToday + "");
                tv.setStatus(2);
                it.putExtra("data", tv);
                activity.startActivity(it);
                break;
            case 3:
                it.setClass(activity, FormListActivity.class);
                it.putExtra("time", localToday + "");
                activity.startActivity(it);
                break;
            case 5:
                it.setClass(activity, HandDisinfectionPrivewActivity.class);
                activity.startActivity(it);
                break;

            case 6://湘雅模版
                it.setClass(activity, XyFormListActivity.class);

                it.putExtra("time", localToday + "");
                activity.startActivity(it);
                break;
            case 8://多耐
                it.setClass(activity, ResistantListActivity.class);
                it.putExtra("code", "DN");
                activity.startActivity(it);
                break;

            case 9://三管
                it.setClass(activity, ResistantListActivity.class);
                it.putExtra("code", "SG");
                activity.startActivity(it);
                break;
            case 10:
                it.setClass(activity, EnvironmentMainActivity.class);
                activity.startActivity(it);
                break;
            case 11:
                it.setClass(activity, safeInjectionActivity.class);
                activity.startActivity(it);
                break;
            case 12:
                it.setClass(activity, ResistantListActivity.class);
                it.putExtra("code", "SIP");
                activity.startActivity(it);
                break;
            case 13:
                it.setClass(activity, WastFormActivity.class);
                activity.startActivity(it);
                break;
            case 14:
                it.setClass(activity, OtherFormtMainActivity.class);
                activity.startActivity(it);
                break;
            case 15:
                it.setClass(activity, ConsumptionFormActivity.class);
                activity.startActivity(it);
                break;
            default:
                break;
        }
    }

    private static void startAfterTaskActivity(Activity activity, TaskVo taskVo, String localToday, Intent it) {
        switch (taskVo.getType()) {
            case "1":
                it.setClass(activity, HandWashTasksActivity.class);
                activity.startActivity(it);
                break;
            case "2":
                it.setClass(activity, SupervisorQueCreatActivity.class);
                activity.startActivityForResult(it, TodayDynamicFragment.ADD_SUPQUESTION);
                break;
            case "4":
                it.setClass(activity, ConsumptionFormActivity.class);
                TaskVo tv = new TaskVo();
                tv.setMission_time(localToday + "");
                tv.setStatus(2);
                it.putExtra("data", tv);
                activity.startActivity(it);
                break;
            case "3":
                it.setClass(activity, FormListActivity.class);
                it.putExtra("time", localToday + "");
                activity.startActivity(it);
                break;
            case "5":
                it.setClass(activity, HandDisinfectionPrivewActivity.class);
                activity.startActivity(it);
                break;

            case "6"://湘雅模版
                it.setClass(activity, XyFormListActivity.class);

                it.putExtra("time", localToday + "");
                activity.startActivity(it);
                break;
            case "8"://多耐
                it.setClass(activity, MutiRisitantPriviewActivity.class);
                it.putExtra("isInspector", 1);
                it.putExtra("code", "DN");
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;

            case "9"://三管
                it.setClass(activity, ThreePipsPriviewActivity.class);
                it.putExtra("isInspector", 1);
                it.putExtra("code", "SG");
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;
            case "10":
                it.setClass(activity, EnvironmentMainActivity.class);
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;
            case "11":
                it.setClass(activity, safeInjectionActivity.class);
                activity.startActivity(it);
                break;
            case "12":
                it.setClass(activity, ResistantListActivity.class);
                it.putExtra("data", taskVo);
                it.putExtra("isInspector", 1);
                it.putExtra("code", "SIP");
                activity.startActivity(it);
                break;
            case "13":
                it.setClass(activity, WastFormActivity.class);
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;
            case "14":
                it.setClass(activity, OtherFormtMainActivity.class);
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;
            case "15":
                it.setClass(activity, ConsumptionFormActivity.class);
                it.putExtra("data", taskVo);
                activity.startActivity(it);
                break;
            default:
                break;
        }
    }

    private static boolean hasTaskTypes(int type) {
        try {
            List<TaskTypePopVo> popVoList = DataBaseHelper
                    .getDbUtilsInstance(MyAppliaction.getContext())
                    .findAll(
                            Selector.from(TaskTypePopVo.class).orderBy("dbid"));
            if (null == popVoList) {
                return false;
            }
            for (TaskTypePopVo vo : popVoList) {
                if (type == vo.getId()) {
                    return true;
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isOpenNetwork(Context ctx) {
        ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null) {//2.获取当前网络连接的类型信息
            int networkType = networkInfo.getType();
            if (ConnectivityManager.TYPE_WIFI == networkType) {//当前为wifi网络
            } else if (ConnectivityManager.TYPE_MOBILE == networkType) {//当前为mobile网络
            }
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    public static boolean isPartTimeJob(Context ctx) {

        Tools tools = MyAppliaction.getTools();
        String hospitalJob = tools.getValue(Constants.JOB);
        String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
        if (null != hospitalJob && hospitalJob.equals("3") && !AbStrUtil.isEmpty(defultDepart)) {//
            // 兼职感控人员在设置了默认科室后可以直接跳过选择部分
            return true;
        }
        return false;
    }
    public static boolean  mysticalJob(Context ctx) {

        Tools tools = MyAppliaction.getTools();
        String hospitalJob = tools.getValue(Constants.JOB);
        String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
        if (null != hospitalJob && hospitalJob.equals("5")) {//
            // 兼职感控人员在设置了默认科室后可以直接跳过选择部分
            return true;
        }
        return false;
    }
    public static String getLoacalTextDate() {
        Calendar calendar = null;
        calendar = new GregorianCalendar();//
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(calendar.getTime());
    }
    public static boolean isOtherJob(Context ctx) {

        Tools tools = MyAppliaction.getTools();
        String hospitalJob = tools.getValue(Constants.JOB);
        String defultDepart = tools.getValue(Constants.DEFULT_DEPARTID);
        if (AbStrUtil.isEmpty(hospitalJob) || hospitalJob.equals("4") || hospitalJob.equals("3")) {//
            // 兼职感控人员在设置了默认科室后可以直接跳过选择部分
            return true;
        }
        return false;
    }

    public static String getDefultDepartId() {

        return MyAppliaction.getTools().getValue(Constants.DEFULT_DEPARTID);
    }

    public static String getDefultDepartName() {

        return MyAppliaction.getTools().getValue(Constants.DEFULT_DEPART_NAME);
    }

    /**
     * 预设任务提交时共同处理方式
     */
    public static void onCommitAfterTask(TaskVo taskVo) {
        Date newDay = null;
        if (taskVo.getIsAfterTask() == 1) {
            taskVo.setIsAfterTask(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dateCache = sdf.parse(taskVo.getMission_time());

                switch (taskVo.getRecycleState()) {
                    case 0:
                        break;
                    case 1:
                        newDay = new DateTime(dateCache).plusDays(1).toDate();
                        break;
                    case 2:
                        newDay = new DateTime(dateCache).plusDays(7).toDate();
                        break;
                    case 3:
                        newDay = new DateTime(dateCache).plusMonths(1).toDate();
                        break;
                }
                if (null != newDay) {
                    TaskVo taskVo1 = new TaskVo();
                    taskVo1.setMission_time(sdf.format(newDay));
                    taskVo1.setDepartmentName(taskVo.getDepartmentName());
                    taskVo1.setDepartment(taskVo.getDepartment());
                    taskVo1.setStatus(5);
                    taskVo1.setRecycleState(taskVo.getRecycleState());
                    taskVo1.setIsAfterTask(1);
                    taskVo1.setType(taskVo.getType());
                    taskVo1.setMobile(taskVo.getMobile());
                    onAddTaskInDb(taskVo1);
                }
                taskVo.setMission_time(TaskUtils.getTaskMissionTime(taskVo.getMission_time()));
            } catch (ParseException e) {
                e.printStackTrace();

            }


        }
    }

}
