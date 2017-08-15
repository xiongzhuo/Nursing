package com.deya.hospital.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.account.SignAcitivity;
import com.deya.hospital.account.UserInfoEditorActivity;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.consumption.ConsumptionFormActivity;
import com.deya.hospital.dudao.SupvisorMainActivity;
import com.deya.hospital.form.handantisepsis.HandDisinfectionPrivewActivity;
import com.deya.hospital.handwash.HandMainActivity;
import com.deya.hospital.quality.QualityMainActivity;
import com.deya.hospital.study.StudyMainActivity;
import com.deya.hospital.supervisor.PartTimeStaffDialog;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonUtils;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.dbdata.TaskVo;
import com.deya.hospital.workspace.BootomSelectDialog;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.environment.EnviromentWorkActivty;
import com.deya.hospital.workspace.handwash.HandWashTasksActivity;
import com.deya.hospital.workspace.waste_disposal.WastWorkSpaceActivty;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TabBaseFragment extends BaseFragment {
    public static final String TOWORKCIRCLEFRAGMENT = "onToWorkCircleFragment";
    public static final String TOSETTINGFRAGMENT= "tosettingfragment";
    UnSignDialog isSignedDialog;
    private TipsDialogRigister isSignedDialog2;
    ModueIntruDialog modueIntruDialog;
    String handItems[] = {"WHO手卫生观察", "手卫生操作考核","手卫生消毒消耗量"};
    String intruduceStr[]={"实现院科两级手卫生月报管理，分科室提供科室自查、院感科抽查、暗访三个维度数据采集与分析及月报汇总，逐步涵盖WHO手卫生观察、外科手消毒操作观察与考核、手卫生设施与用品管理、手卫生用品消耗量，以及供应室、检验科、食品安全等非接触患者区域工作人员手卫生观察多个场景。",

   "根据最新指南对医疗机构建筑物表与设备表面清洁与消毒质控管理的要求，实现月度保洁公司自查与医院科室和职能部门督查情况同系统实时呈现，方便重点跟踪高风险区域与易纰漏环节，形成数据为基础的自动线上反馈，随时提醒加强当月薄弱环节的人员操作培训和干预，支持过程中的PDCA督查，实现持续改进（建议物业公司购买，配置专属pad",

    "全院各科室医疗废物详细分类以及丢弃、暂存、交接、转运全流程记录、危险因素督查和数量登记",

    "支持院方以职业暴露潜在人群为中心，定期提供针对院内各类感染风险和感染患者的防护用品选用，针刺伤预防和疫苗接种教育和知识测评和暴露后的评估与咨询服务；\n" +
            "支持院方对于暴露案例进行总结统计、归因分析和PDCA督导，实现全员职业防护预防能力的持续提升；在保障个人隐私的前提下，为院内公益志愿者提供帮助他人的机会；",

            "实现所有临床与医技部门院级感控质量指标库管理，并按科室生成感染管理质量月报模板；提供科室自查与院感抽查两个维度的月报数据采集与汇总；提供全院指标库指标当月自查与抽查排序；支持评价过程中的PDCA督查；评价方式可以支持对错、评分以及ABCD等级。",

    "不限定内容的院级科室对科室的PDCA督导本，支持拍照；附带全部督查提醒备忘录功能；",

    "支持全员感控在线知识培训和操作视频示范、可快速完成全员在线知识考核，支持感控专干现场操作观察评价以及生成考试二维码在督查现场做员工知识评估。题库有平台更新并支持各医院在后台进行本院学习内容和考试试题添加。",

            "病例简单描述与资料拍照后48小时即可得到工作间感染诊断专家回复，改变大部分感控中心无医生或感控医生较少现状，联合国内多位医院感染管理专家、感染病科诊疗专家提供感染性疾病与抗菌药物合理使用会诊与咨询。联合医脉通提供国内、美国、英国等医院感染标准、临床诊疗指南查询和感染诊断决策（自动）"};
    String modueStr[] = {"手卫生", "环境清洁","医废管理","职业防护","临床质控", "督查反馈", "学习考试", "感染会诊" };
    String keys[] = {"hw", "envior","wast" ,"zyfh","quality","dudao", "kaohe",  "hz"  };
    public BootomSelectDialog handItemSelectDialog;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);


        isSignedDialog = new UnSignDialog(getActivity());
        handItemSelectDialog=new BootomSelectDialog(getActivity(), handItems, new BootomSelectDialog.BottomDialogInter() {
            @Override
            public void onClick1() {
                StartActivity(HandMainActivity.class);
            }

            @Override
            public void onClick2() {
                Intent in=new Intent(getActivity(),QualityMainActivity.class);
                in.putExtra("task_type",21);
                startActivity(in);
            }

            @Override
            public void onClick3() {
                addTask(4);
            }

            @Override
            public void onClick4() {

            }
        });



        isSignedDialog2 = new TipsDialogRigister(getActivity(), new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                Intent it = new Intent(getActivity(), SignAcitivity.class);
                startActivity(it);
            }

            @Override
            public void onCancle() {
//                    Intent it = new Intent(getActivity(), WebViewDemo.class);
//                    it.putExtra("url", "http://studio.gkgzj.com/download/indexhos.html");
//                    it.putExtra("title", "下载");
//                    startActivity(it);

                Intent intent = new Intent(TOWORKCIRCLEFRAGMENT);
                getActivity().sendBroadcast(intent);
                tipsDialog.dismiss();
            }
        });
        tipsDialog = CommonUtils.getTipsDialiog(getActivity(), new PartTimeStaffDialog.PDialogInter() {
            @Override
            public void onCancle() {
                if (CommonUtils.isSignedSucUser(tools) < 5) {//判断是否为认证中用户
                    Intent intent = new Intent(TOWORKCIRCLEFRAGMENT);
                    getActivity().sendBroadcast(intent);
                    tipsDialog.dismiss();
                } else if (CommonUtils.isSignedSucUser(tools) == 6) {
                    Intent intent = new Intent(activity, UserInfoEditorActivity.class);
                    intent.setAction(TOWORKCIRCLEFRAGMENT);
                    getActivity().sendBroadcast(intent);
                    startActivity(intent);
                }


            }

            @Override
            public void onEnter() {

            }
        });

        modueIntruDialog=new ModueIntruDialog(getActivity());

    }
    public void addTask(int type) {
        Intent it = new Intent();
        if (type != 4) {
            it.putExtra("type", type + "");
        }
        switch (type) {
            case 1:
                it.setClass(getActivity(), HandWashTasksActivity.class);
                startActivity(it);
                break;
            case 5:
                it.setClass(getActivity(), HandDisinfectionPrivewActivity.class);
                startActivity(it);
                break;
            case 4:
                it.setClass(getActivity(), ConsumptionFormActivity.class);
                TaskVo tv = new TaskVo();
                tv.setMission_time(TaskUtils.getLoacalDate() + "");
                tv.setStatus(2);
                it.putExtra("data", tv);
                startActivity(it);
                break;
            default:
                break;
        }


    }
    public boolean isSigned(String key){
        int position = 0;
     for (int i=0;i<modueStr.length;i++){
         if(key.equals(keys[i])){
             position=i;
             break;
         }
     }

        String is_Vip=MyAppliaction.getTools().getValue(Constants.IS_VIP_HOSPITAL);
        if(AbStrUtil.isEmpty(is_Vip)||is_Vip.equals("1")){
            isSignedDialog2.show();
            isSignedDialog2.setContent(getString(R.string.app_name)+"已有超过2000家医院使用,产生督导任务超过160万次。");
            isSignedDialog2.setButton("申请预约");
            isSignedDialog2.setCancleButton("浏览其他内容");
            isSignedDialog2.setContentTiltle("\u3000\u3000"+intruduceStr[position]);

            isSignedDialog2.setCancelable(false);
            isSignedDialog2.setContentTiltleTxtGravity(Gravity.START,getActivity().getResources().getColor(R.color.list_content));
            isSignedDialog2.setContentTxtGravity(Gravity.START,getActivity().getResources().getColor(R.color.black));
            return false;

        }else if(tools.getValue_int(key,0)==0){
            modueIntruDialog.show();
            modueIntruDialog.setContent(intruduceStr[position]);
            modueIntruDialog.setContent2("护理工作间已有超过2000家医院使用,产生督导任务超过160万次。");
            modueIntruDialog.setTitleTxt(modueStr[position]);
            modueIntruDialog.setKeys(key);

            tools.putValue(key,1);
            return false;
        }else{
            return true;
        }

    }

 public void   isSignUser(){
     String is_Vip=MyAppliaction.getTools().getValue(Constants.IS_VIP_HOSPITAL);
     if(AbStrUtil.isEmpty(is_Vip)||is_Vip.equals("1")){//未签约用户
         isSignedDialog.show();
         isSignedDialog.setCancelable(false);
         return;
     }
    }
    public void isSignedSucUser(Tools tools) {
        String is_Vip=MyAppliaction.getTools().getValue(Constants.IS_VIP_HOSPITAL);
        if(AbStrUtil.isEmpty(is_Vip)||is_Vip.equals("1")){//未签约用户
//            isSignedDialog.show();
//            isSignedDialog.setCancelable(false);
            return;
        }
        if (CommonUtils.isSignedSucUser(tools)!=0&& CommonUtils.isSignedSucUser(tools)< 5) {//判断是否为认证中用户
            tipsDialog.show();
            tipsDialog.setContentTv("正在认证中...若想加快认证速度,请联系院感科专职人员认证通过！", getActivity().getResources().getColor(R.color.light_yellow));
            tipsDialog.setCancleBtn("浏览其他内容");
            tipsDialog.setCancelable(false);
        } else if (CommonUtils.isSignedSucUser(tools) == 6) {
            tipsDialog.show();
            tipsDialog.setContentTv("认证失败...请重新填写认证信息！", getActivity().getResources().getColor(R.color.red));
            tipsDialog.setCancleBtn("重新认证");
            tipsDialog.setCancelable(false);
        }
    }

    private class UnSignDialog extends BaseDialog{

        /**
         * Creates a new instance of MyDialog.
         *
         * @param context
         */
        public UnSignDialog(Context context) {
            super(context);

        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_unsign);
            findView(R.id.enterBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toWeb = new Intent(getActivity(), WebViewDemo.class);
                    toWeb.putExtra("title", "介绍");
                    toWeb.putExtra("url",
                            "http://test.shediaojianghu.com/ywwx/wxact/toActInfoPage/3e05d46d-9cab-4d0f-847d-52a1082907b2/cb495ba6-fa94-4930-acef-69c77ef32937");
                    //toWeb.putExtra("type", "1");
                    startActivity(toWeb);
                  dismiss();

                }
            });
        }

         }

    private class ModueIntruDialog extends BaseDialog{
        TextView contentTxt,content2;
        TextView titleTxt;
        String keys;

        /**
         * Creates a new instance of MyDialog.
         *
         * @param context
         */
        public ModueIntruDialog(Context context) {
            super(context);

        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.mode_intruduce);
            contentTxt=findView(R.id.content);
            titleTxt=findView(R.id.title);
            content2=findView(R.id.content2);
            findView(R.id.submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    switch (keys) {
                        case "hw":
                            handItemSelectDialog.show();
                            break;
                        case "dudao":
                            onStartActivity(SupvisorMainActivity.class);
                            break;
                        case "kaohe":
                            onStartActivity(StudyMainActivity.class);
                            break;
                        case "hz":
                            showDialogToast("","正在排队上线中!");
                            break;
                        case "zyfh":
                            showDialogToast("","正在排队上线中!");
                            break;
                        case "quality":
                            Intent it2 = new Intent(getActivity(),
                                    QualityMainActivity.class);
                            startActivity(it2);
                            break;
                        case "envior":
                            onStartActivity(EnviromentWorkActivty.class);
                            break;
                        case "wast":
                            onStartActivity(WastWorkSpaceActivty.class);
                            break;
                        default:
                            break;

                    }
                }
            });
        }

        public void setKeys(String keys){
            this.keys=keys;
        }
        public void setContent(String content){
            if(null!=contentTxt){
            contentTxt.setText("\u3000\u3000"+content);
            }
        }
        public void setContent2(String txt){
            if(null!=content2){
                content2.setText("\u3000\u3000"+txt);
            }
        }
        public void setTitleTxt(String title) {
            if(null!=titleTxt){
                titleTxt.setText(title);
            }
        }
    }
    @Override
    public void onDestroy() {
        if(isSignedDialog!=null){
            isSignedDialog.dismiss();
        }
        if(tipsDialog!=null){
            tipsDialog.dismiss();
        }
        if(isSignedDialog2!=null){
            isSignedDialog2.dismiss();
        }
        super.onDestroy();
    }
}
