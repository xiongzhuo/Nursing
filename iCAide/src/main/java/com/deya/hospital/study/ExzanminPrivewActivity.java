package com.deya.hospital.study;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.CommonTopView;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.vo.ExzanminVo;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workcircle.knowledge.KnowledgeAdapter;
import com.deya.hospital.workspace.TaskUtils;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sup
 * @date 2016/9/26
 */
public class ExzanminPrivewActivity extends BaseCommonTopActivity implements View.OnClickListener {
    private static final int GET_DATA_SUCESS = 0x20032;
    private static final int GET_DATA_FAIL = 0x20033;
    private static final int SUBMIT_DATA_SUCESS = 0x20034;
    private static final int SUBMIT_DATA_FAIL = 0x20035;
    ListView pagerList;
    TextView typeTv, titleTv,knowlegeType;
    KnowledgeAdapter adapter;
    KnowledgeVo vo;
    int index;
    Button submitBtn;
    ExzanminVo.ListBean data;
    boolean isMuti = false;
    ImageView collectImg;
    TipsDialogRigister baseTipsDialog;
    List<KnowledgeVo.ListBean.ItemsBean> list = new ArrayList<>();
    String json = "{\"status\":1,\"list\":[{\"id\":27,\"title\":\"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置\",\"note\":\"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置\",\"img\":\"44bbc152-2445-4f3c-b524-fd7426f35a28.jpg\",\"sub_type\":1,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":2286,\"title\":\"T\",\"is_yes\":\"2\",\"note\":\"医疗卫生机构收治的传染病病人或者疑似传染病病人产生的生活垃圾，按照医疗废物进行管理和处置医疗卫生\",\"img\":\"c761b36a-3da9-408c-8da6-32fa0f22a251.jpg\"},{\"id\":2287,\"title\":\"F\",\"is_yes\":\"1\",\"note\":\"错误答案22\",\"img\":\"5d7eb308-450c-4808-bea6-e8027a38b6a8.jpg\"}]},{\"id\":46,\"title\":\"哪些情况应该洗手\",\"note\":\"\",\"img\":null,\"sub_type\":2,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":159,\"title\":\"A\",\"is_yes\":\"1\",\"note\":\"接触患者黏膜、破损皮肤或伤口后 \",\"img\":\"\"},{\"id\":160,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"接触患者的血液、体液、分泌物、排泄物、伤口敷料等之后 \",\"img\":\"\"},{\"id\":161,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"直接接触每个患者前后\",\"img\":\"\"},{\"id\":162,\"title\":\"D\",\"is_yes\":\"1\",\"note\":\"直接为传染病患者进行检查、治疗、护理后\",\"img\":\"\"}]},{\"id\":45,\"title\":\"手消毒指征\",\"note\":\"\",\"img\":null,\"sub_type\":2,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":155,\"title\":\"A\",\"is_yes\":\"1\",\"note\":\"进入和离开隔离病房、穿脱隔离衣前后\",\"img\":\"\"},{\"id\":156,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"接触特殊感染病原体后\",\"img\":\"\"},{\"id\":157,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"接触血液、体液和被污染的物品后\",\"img\":\"\"},{\"id\":158,\"title\":\"D\",\"is_yes\":\"1\",\"note\":\"接触未消毒仪器和设备后\",\"img\":\"\"}]},{\"id\":44,\"title\":\"手卫生设施是指用于洗手与手消毒的设施，包括洗手池、水龙头、流动水、（    ）等\",\"note\":\"\",\"img\":null,\"sub_type\":2,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":151,\"title\":\"A\",\"is_yes\":\"1\",\"note\":\"肥皂（液）\",\"img\":\"\"},{\"id\":152,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"干手用品\",\"img\":\"\"},{\"id\":153,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"手消毒剂\",\"img\":\"\"},{\"id\":154,\"title\":\"D\",\"is_yes\":\"2\",\"note\":\"手套\",\"img\":\"\"}]},{\"id\":43,\"title\":\"速干手消毒剂是指含有醇类和护肤成分的手消毒剂，包括\",\"note\":\"\",\"img\":null,\"sub_type\":2,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":147,\"title\":\"A\",\"is_yes\":\"1\",\"note\":\"水剂  \",\"img\":\"\"},{\"id\":148,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"凝胶    \",\"img\":\"\"},{\"id\":149,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"泡沫型   \",\"img\":\"\"},{\"id\":150,\"title\":\"D\",\"is_yes\":\"2\",\"note\":\"油剂\",\"img\":\"\"}]},{\"id\":42,\"title\":\"医务人员工作期间手部要求哪句话描述正确\",\"note\":\"\",\"img\":null,\"sub_type\":1,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":143,\"title\":\"A\",\"is_yes\":\"2\",\"note\":\"只要手套没有破就不用担心有害微生物会污染到手，摘手套后可以不必洗手\",\"img\":\"\"},{\"id\":144,\"title\":\"B\",\"is_yes\":\"2\",\"note\":\"易挥发性的醇类手消毒剂开瓶后的使用期限应大于30d\",\"img\":\"\"},{\"id\":145,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"医护人员工作期间指甲不应超过指尖，不应戴假指甲、戒指、手表、手镯或其它指甲产品\",\"img\":\"\"},{\"id\":146,\"title\":\"D\",\"is_yes\":\"2\",\"note\":\"洗手的目的是保护医务人员自身不受病原微生物的污染\",\"img\":\"\"}]},{\"id\":41,\"title\":\"《医务人员手卫生规范》中卫生手消毒是指\",\"note\":\"\",\"img\":null,\"sub_type\":1,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":139,\"title\":\"A\",\"is_yes\":\"2\",\"note\":\"用速干手消毒剂揉搓双手，以杀灭手部暂居菌的过程\",\"img\":\"\"},{\"id\":140,\"title\":\"B\",\"is_yes\":\"2\",\"note\":\"用速干手消毒剂揉搓双手，以减少手部常居菌的过程\",\"img\":\"\"},{\"id\":141,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"用速干手消毒剂揉搓双手，以减少手部暂居菌的过程\",\"img\":\"\"},{\"id\":142,\"title\":\"D\",\"is_yes\":\"2\",\"note\":\"用速干手消毒剂揉搓双手，以杀灭手部常居菌的过程\",\"img\":\"\"}]},{\"id\":40,\"title\":\"关于手卫生的作用，包括\",\"note\":\"\",\"img\":null,\"sub_type\":2,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":135,\"title\":\"A\",\"is_yes\":\"1\",\"note\":\"预防患者发生医院感染\",\"img\":\"\"},{\"id\":136,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"阻断微生物在不同患者之间的交叉传染\",\"img\":\"\"},{\"id\":137,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"预防潜在病原体对医院环境的污染 \",\"img\":\"\"},{\"id\":138,\"title\":\"D\",\"is_yes\":\"1\",\"note\":\"帮助医务人员预防职业疾病\",\"img\":\"\"}]},{\"id\":39,\"title\":\"手消毒效果应达到的要求：外科手消毒监测的细菌数应\",\"note\":\"\",\"img\":null,\"sub_type\":1,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":131,\"title\":\"A\",\"is_yes\":\"2\",\"note\":\"≤10cfu/cm2 \",\"img\":\"\"},{\"id\":132,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\" ≤5cfu/cm2 \",\"img\":\"\"},{\"id\":133,\"title\":\"C\",\"is_yes\":\"2\",\"note\":\"≤15cfu/cm2 \",\"img\":\"\"},{\"id\":134,\"title\":\"D\",\"is_yes\":\"2\",\"note\":\"≤8cfu/cm2\",\"img\":\"\"}]},{\"id\":47,\"title\":\"关于手卫生，下列说法正确的是\",\"note\":\"\",\"img\":null,\"sub_type\":1,\"s_score\":\"10\",\"listorder\":0,\"type\":\"0\",\"items\":[{\"id\":163,\"title\":\"A\",\"is_yes\":\"2\",\"note\":\"手部有肉眼可见污垢时，可以不洗手，进行卫生手消毒。\",\"img\":\"\"},{\"id\":164,\"title\":\"B\",\"is_yes\":\"1\",\"note\":\"若手部没有肉眼可见污染时宜使用速干手消毒剂消毒双手代替洗手。\",\"img\":\"\"},{\"id\":165,\"title\":\"C\",\"is_yes\":\"1\",\"note\":\"接触患者的血液、体液和分泌物后， 应先洗手，然后卫生手消毒。\",\"img\":\"\"},{\"id\":166,\"title\":\"D\",\"is_yes\":\"1\",\"note\":\"直接为传染病患者进行检查、治疗、护理或处理传染患者污物之后，应先洗手，然后卫生手消毒。\",\"img\":\"\"}]}]}";
    private String article_id = "";
    private String article_src;
    private CountDownTimer timer;
    TextView topRightTxt;
    private KnowledgeVo.ListBean dbVo;
    int r;
    Animation animation;

    @Override
    public String getTopTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_exzanmin_priview;
    }

    @Override
    public void initBaseData() {
        article_id = getIntent().getStringExtra("article_id");
        article_src = getIntent().getStringExtra("article_src");
        data = (ExzanminVo.ListBean) getIntent().getSerializableExtra("data");
        timer = new CountDownTimer(data.getMins() * 60 * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                r = (int) millisUntilFinished / 1000;//剩余秒
                int min = r / 60;
                int s = r % 60;
                topView.setRigtext(formatData(min + "") + ":" + formatData(s + ""));
            }

            @Override
            public void onFinish() {
                r=0;
                baseTipsDialog.show();
                topView.setRigtext("时间结束");
                baseTipsDialog.setButton("直接提交");
                baseTipsDialog.setCancelable(false);
                baseTipsDialog.setCancleButton("放弃考试");
                baseTipsDialog.setContent("时间结束，是否交卷？");

            }
        };
        timer.start();
        getData();
        knowlegeType.setText(data.getTitle());
    }

    private String formatData(String text) {


        return text.length() < 2 ? "0" + text : text;
    }

    public static final SimpleDateFormat format = new SimpleDateFormat("mm:ss");

    @Override
    protected void onDestroy() {
        if (null != timer) {
            timer.cancel();
        }
        if(null!=baseTipsDialog){
            baseTipsDialog.dismiss();
        }
        pagerList.clearAnimation();
        super.onDestroy();
    }

    @Override
    public void initView() {
        animation=AnimationUtils.loadAnimation(this, R.anim.slide_to_left);
        findViewById(R.id.submitBtn).setVisibility(View.GONE);
        findViewById(R.id.bottm_lay).setVisibility(View.VISIBLE);
        submitBtn = (Button) this.findViewById(R.id.nextBtn);
        submitBtn.setOnClickListener(this);
        topRightTxt = topView.getRightTextView();
        topRightTxt.setVisibility(View.VISIBLE);
        topRightTxt.setTextColor(getResources().getColor(R.color.red));
        pagerList = (ListView) this.findViewById(R.id.pagerList);
        collectImg = (ImageView) findViewById(R.id.collectImg);
        collectImg.setOnClickListener(this);
        knowlegeType= (TextView) this.findViewById(R.id.knowlegeType);
        baseTipsDialog=new TipsDialogRigister(mcontext, new MyDialogInterface() {
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                setData();

            }

            @Override
            public void onCancle() {
                finish();

            }
        });
        pagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                adapter.setChooseIndex(position);
                if (isMuti) {
                    setSubmitVBtn();
                } else {

                    setSubmitVBtn1();
                }
                adapter.notifyDataSetChanged();

            }
        });
        adapter = new KnowledgeAdapter(mcontext, list);
        pagerList.setAdapter(adapter);
        typeTv = (TextView) this.findViewById(R.id.typeTv);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        titleTv = (TextView) this.findViewById(R.id.titleTv);

    }


    void setSubmitVBtn() {
        int i = 0;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getResult().equals("1")) {
                i++;
            }
        }
        if (i >= 1) {
            submitBtn.setEnabled(true);
        }
    }

    void setSubmitVBtn1() {
        int i = 0;
        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getResult().equals("1")) {
                i++;
            }
        }
        if (i >= 1) {
            submitBtn.setEnabled(true);
        }
    }

    private void getData() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", article_id);
            job.put("article_src", article_src);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, this, GET_DATA_SUCESS, GET_DATA_FAIL, job, "subject/test/subjectTestList");

    }

    public void setAdapter(int groupPosition) {
        index = groupPosition;

        if (groupPosition >= vo.getList().size()) {
            setData();
            return;
        }
        pagerList.startAnimation(animation);//设置一个加载动画
        dbVo = SubjectDbUtils.getDataById(mcontext, vo.getList().get(index));
        setCollect();
        if (null != vo && null != vo.getList()) {


            int num = groupPosition + 1;
            topView.setTitle("知识评估（" + num + "/" + vo.getList().size() + ")");
            isMuti = vo.getList().get(groupPosition).getSub_type() == 2;
            typeTv.setText(isMuti ? "多选题" : "单选题");
            submitBtn.setEnabled(false);
            submitBtn.setText(groupPosition == vo.getList().size() ? "提交" : "下一题");
            titleTv.setText("\u3000\u3000\u3000\u3000" + vo.getList().get(groupPosition).getTitle());
            list.clear();
            list.addAll(vo.getList().get(groupPosition).getItems());
            adapter.setIsMuti(isMuti);

        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextBtn:
                setAdapter(index + 1);
                break;
            case R.id.collectImg:
                if (null == dbVo) {
                    vo.getList().get(index).setIsColected(1);
                    if (SubjectDbUtils.saveData(mcontext, vo.getList().get(index))) {
                        ToastUtil.showMessage("收藏成功");
                        collectImg.setImageResource(R.drawable.shoucang_select);
                    }
                    dbVo = SubjectDbUtils.getDataById(mcontext, vo.getList().get(index));
                } else {
                    dbVo.setIsColected(dbVo.getIsColected() == 1 ? 0 : 1);
                    SubjectDbUtils.updateData(mcontext, dbVo);
                    setCollect();
                }
                break;
        }

    }

    private void setCollect() {
        if (null != dbVo && dbVo.getIsColected() == 1) {
            collectImg.setImageResource(R.drawable.shoucang_select);
        } else {
            collectImg.setImageResource(R.drawable.shouchang_normal);
        }
    }

    public void sumbmitData() {
        showprocessdialog();

        timer.cancel();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("id", article_id + "");
            job.put("article_src", "3");
            job.put("channel_id", "1");
            job.put("item_id", item_id);
            job.put("item_true", item_true);
            job.put("item_result", item_result);
            job.put("scoreCount", score + "");
            job.put("scoreCount", score + "");
            job.put("second", data.getMins() * 60-r + "");
            MainBizImpl.getInstance().onComomRequest(myHandler, this, SUBMIT_DATA_SUCESS, SUBMIT_DATA_FAIL, job, "subject/test/records/add");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    String item_id = "";
    String item_true = "";
    String item_result = "";
    int score;
    int wrongNum;
    int rightNum;

    private void setData() {
        wrongNum = 0;
        rightNum = 0;
        list.clear();
        item_id = "";
        item_true = "";
        item_result = "";
        score=0;
        int indext = 0;
        for (KnowledgeVo.ListBean bean : vo.getList()) {
            String aswerStr = "";
            for (KnowledgeVo.ListBean.ItemsBean itemsBean : bean.getItems()) {
                if (itemsBean.getIs_yes().equals("1")) {
                    bean.setRightAswer(bean.getRightAswer() + itemsBean.getTitle());
                }
                if (itemsBean.getResult().equals("1")) {
                    bean.setChooseAswer(bean.getChooseAswer() + itemsBean.getTitle());
                    aswerStr = aswerStr + itemsBean.getTitle() + "|";
                }
            }

            if (!bean.getChooseAswer().equals(bean.getRightAswer())) {
                bean.setRight(false);
                wrongNum++;
            } else if (bean.getChooseAswer().equals(bean.getRightAswer())) {
                if (!AbStrUtil.isEmpty(bean.getS_score())) {
                    score += Integer.parseInt(bean.getS_score());
                }
                rightNum++;

            }
            if (indext == vo.getList().size() - 1) {
                item_result += aswerStr;
                item_true += bean.isRight();
                item_id += bean.getId();
            } else {
                item_result += aswerStr + "#";
                item_true += bean.isRight() + ",";
                item_id += bean.getId() + ",";
            }
            indext++;
        }
        sumbmitData();
    }

    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case GET_DATA_SUCESS:
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                setData(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case GET_DATA_FAIL:
                        break;
                    case SUBMIT_DATA_SUCESS:
                        dismissdialog();
                        if (null != msg.obj && !AbStrUtil.isEmpty(msg.obj.toString())) {
                            try {
                                setSubResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case SUBMIT_DATA_FAIL:
                        dismissdialog();
                        ToastUtil.showMessage("亲，您的网络不顺畅！");
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void setSubResult(JSONObject jsonObject) {
        Log.i("1111111111", jsonObject.toString());
        if (jsonObject.optString("result_id").equals("0")) {
            int record_id = jsonObject.optInt("record_id");


            Intent intent = new Intent(mcontext, ExzanmingShareActivity.class);
            data.setScore(score);
            data.setWrongNum(wrongNum);
            data.setRightNum(rightNum);
            data.setId(record_id);
            intent.putExtra("list", (Serializable) vo.getList());
            vo.setTest_id(article_id);
            vo.setArticle_src(article_src + "");
            intent.putExtra("vo", vo);
            intent.putExtra("data", data);
            intent.putExtra("title", getTopTitle());
            intent.putExtra("second", data.getMins() * 60-r);
            startActivity(intent);
            finish();
        } else {
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
        }
    }

    private void setData(JSONObject jsonObject) {
        vo = TaskUtils.gson.fromJson(jsonObject.toString(), KnowledgeVo.class);
        if (null == vo) {
            vo = new KnowledgeVo();
            return;
        }
        setAdapter(0);
    }
}
