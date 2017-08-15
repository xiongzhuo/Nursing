package com.deya.hospital.quality;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseDialog;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.TaskUtils;
import com.deya.hospital.workspace.priviewbase.RisitanCheckAdapter;
import com.deya.hospital.workspace.priviewbase.RisitanCheckAdapter2;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/2/18
 */
public class SDPriviewPop extends BaseDialog implements View.OnClickListener {//    选项类型
    //    1）文字标签
//    2）判断
//    3）评分
//    4）判断和评分
//    5）单选
//    6）多选
    Button submitBtn;
    ListView listView;
    Context context;
    Button cancleBtn;
    TextView titleTxt, pjaMethodTxt,moreTv;
    List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> childrenBeanList = new ArrayList<>();
    List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> mainList = new ArrayList<>();
    RisistantVo.TempListBean.ItemListBean itemListBean;
    RisistantVo.TempListBean.ItemListBean newItemListBean = new RisistantVo.TempListBean.ItemListBean();
    SumbmitInter dialogInterface;
    private ViewHolder viewHolder;
    private TextView pingjia;

    /**
     * Creates a new instance of MyDialog.
     *
     * @param context
     */
    public SDPriviewPop(Context context) {
        super(context);
        this.context = context;
    }


    public void setData(RisistantVo.TempListBean.ItemListBean bean) {
        newItemListBean = bean;
        this.itemListBean = new RisistantVo.TempListBean.ItemListBean();
        itemListBean.setItem_score(bean.getItem_score());
        itemListBean.setResult(bean.getResult());
        itemListBean.setTitle(bean.getTitle());
        itemListBean.setType(bean.getType());
        itemListBean.setScore(bean.getScore());
        itemListBean.setShow_type(bean.getShow_type());
        this.mainList = bean.getChildren();
        childrenBeanList.clear();
        this.mainList = bean.getChildren();
        if (mainList.size() > 0) {
            for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBeen : mainList) {
                String str = TaskUtils.gson.toJson(childrenBeen);
                RisistantVo.TempListBean.ItemListBean.ChildrenBean newbean = TaskUtils.gson.fromJson(str, RisistantVo.TempListBean.ItemListBean.ChildrenBean.class);
                childrenBeanList.add(newbean);
            }
        }
        itemListBean.setChildren(childrenBeanList);
//        if (null != viewHolder) {
//            setViews();
//        }else{
         //   initItems();
      //  }

    }

    public void setDialogInterface(SumbmitInter dialogInterface) {
        this.dialogInterface = dialogInterface;

    }

    public void saveData(RisistantVo.TempListBean.ItemListBean bean) {
        double score = Double.parseDouble(bean.getScore());
        switch (bean.getType()) {
            case 2:
                if (bean.getResult().equals("1")) {
                    bean.setItem_score("0");
                } else if (bean.getResult().equals("2")) {
                    bean.setItem_score((0 - score) + "");
                } else {
                    bean.setItem_score("0");
                }
                break;
            case 5:
                for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : bean.getChildren()) {
                    if (childrenBean.getIs_yes().equals("1")) {
                        if (childrenBean.getResult().equals("1")) {
                            bean.setItem_score("0");
                        } else {
                            bean.setItem_score((0 - score) + "");
                        }
                        break;
                    }
                }
                break;
            case 6:
                for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : bean.getChildren()) {
                    if (childrenBean.getIs_yes().equals("1")) {
                        if (!childrenBean.getResult().equals("1")) {
                            bean.setItem_score(childrenBean.getScore());
                            break;
                        }
                    }
                }
                if (!bean.getItem_score().equals("0")) {
                    bean.setItem_score((0 - score) + "");
                } else {
                    bean.setItem_score("0");
                }
                break;
            case 7:
                for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean : bean.getChildren()) {
                        if (childrenBean.getResult().equals("1")) {
                            double chidScore = Double.parseDouble(childrenBean.getScore());
                            bean.setItem_score((chidScore-score)+"");
                            break;
                        }

                }
                break;
            default:

                break;
        }

        newItemListBean.setItem_score(bean.getItem_score());
        newItemListBean.setResult(bean.getResult());
        newItemListBean.setTitle(bean.getTitle());
        newItemListBean.setType(bean.getType());
        newItemListBean.setName(tools.getValue(Constants.NAME));
        newItemListBean.setSave_time(TaskUtils.getLoacalDate());
        mainList.clear();
        if (childrenBeanList.size() > 0) {
            for (RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBeen : childrenBeanList) {
                String str = TaskUtils.gson.toJson(childrenBeen);

                RisistantVo.TempListBean.ItemListBean.ChildrenBean newbean = TaskUtils.gson.fromJson(str, RisistantVo.TempListBean.ItemListBean.ChildrenBean.class);

                mainList.add(newbean);
            }
        }
        dialogInterface.onsumbmit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sd_priview_dialog);
        listView = findView(R.id.listView);
        titleTxt = findView(R.id.titleTv);
        titleTxt.setMaxLines(4);
        moreTv=findView(R.id.moreTv);
        moreTv.setOnClickListener(this);
        submitBtn = findView(R.id.submitBtn);
        pjaMethodTxt = findView(R.id.pjaMethodTxt);
        submitBtn.setOnClickListener(this);
        cancleBtn = findView(R.id.cancleBtn);
        cancleBtn.setOnClickListener(this);
        initItems();
        pingjia = findView(R.id.pingjia);
        pingjia.setOnClickListener(this);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        int wh[] = AbViewUtil.getDeviceWH(context);
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        titleTxt.setOnClickListener(this);
        dialogWindow.setAttributes(lp);
    }

    private void initItems() {
        viewHolder = new ViewHolder();
        title = findView(R.id.title);
        itemList = findView(R.id.itemList);
        itemList2 = findView(R.id.itemList2);
        itemRulesLay = findView(R.id.itemRulesLay);
        itemRulesLayVertical = findView(R.id.itemRulesLayVertical);
        singleCheckLay = findView(R.id.singleCheckLay);
        scoreEdtLay = findView(R.id.scoreEdtLay);
        title_check = findView(R.id.title_check);
        numTv = findView(R.id.numTv);
        subtractionBtn = findView(R.id.btn_subtraction);
        plusBtn = findView(R.id.btn_add);
        indexTv = findView(R.id.indexTv);
        scoreTv = findView(R.id.scoreTv);
        nameTv = findView(R.id.nameTv);
        timeTv = findView(R.id.timeTv);
        titleLay = findView(R.id.titleLay);
        setViews();
    }

    private void setViews() {
        final RisistantVo.TempListBean.ItemListBean rti = itemListBean;
        title.setText(rti.getTitle());
        scoreEdtLay.setVisibility(View.GONE);
        singleCheckLay.setVisibility(View.GONE);
        title.setTextColor(context.getResources().getColor(R.color.black));
        itemRulesLayVertical.setVisibility(View.GONE);
        itemRulesLay.setVisibility(View.GONE);
        switch (rti.getType()) {
            case 1:
                title.setVisibility(View.VISIBLE);
                title.setText(rti.getShow_no() + rti.getTitle());
                title.setTextColor(context.getResources().getColor(R.color.top_color));
                break;
            case 2:
                title.setVisibility(View.VISIBLE);
                singleCheckLay.setVisibility(View.VISIBLE);
                setCheckLay(rti, 2);
                break;

            case 3:
                title.setVisibility(View.VISIBLE);
                scoreEdtLay.setVisibility(View.VISIBLE);
                setScoreLay(rti);

                break;
            case 4:
                title.setVisibility(View.VISIBLE);
                singleCheckLay.setVisibility(View.VISIBLE);
                setCheckLay(rti, 4);
                setScoreLay(rti);
                break;
            case 5:
//                title.setVisibility(View.GONE);
//                RisitanCheckAdapter itemAdapter1 = new RisitanCheckAdapter(context, rti.getChildren(), true);
//                RisitanCheckAdapter2 itemAdapter2 = new RisitanCheckAdapter2(context, rti.getChildren(), true);
//                if (rti.getShow_type() == 3||checkTextLength(rti.getChildren())) {//多行
//                    itemRulesLayVertical.setVisibility(View.VISIBLE);
//                    itemList2.setAdapter(itemAdapter1);
//                } else {//单行
//                    itemRulesLay.setVisibility(View.VISIBLE);
//                    itemList.setAdapter(itemAdapter2);
//                }
//                break;
            case 7:
                title.setVisibility(View.GONE);
                RisitanCheckAdapter itemAdapter1 = new RisitanCheckAdapter(context, rti.getChildren(), true);
                RisitanCheckAdapter2 itemAdapter2 = new RisitanCheckAdapter2(context, rti.getChildren(), true);
                if (rti.getShow_type() == 3||checkTextLength(rti.getChildren())) {//多行
                    itemRulesLayVertical.setVisibility(View.VISIBLE);
                    itemList2.setAdapter(itemAdapter2);
                } else {//单行
                    itemRulesLay.setVisibility(View.VISIBLE);
                    itemList.setAdapter(itemAdapter1);
                }
                break;
            case 6:
                title.setVisibility(View.GONE);
                RisitanCheckAdapter itemAdapter3= new RisitanCheckAdapter(context, rti.getChildren(), false);
                RisitanCheckAdapter2 itemAdapter4 = new RisitanCheckAdapter2(context, rti.getChildren(), false);
                if (rti.getShow_type() == 3||checkTextLength(rti.getChildren())) {//多行
                    itemRulesLayVertical.setVisibility(View.VISIBLE);
                    itemList2.setAdapter(itemAdapter4);
                } else {//单行
                    itemRulesLay.setVisibility(View.VISIBLE);
                    itemList.setAdapter(itemAdapter3);
                }
                break;
            case 8:
                break;
            default:
                break;


        }
    }

    public boolean checkTextLength(List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> list){
        for(RisistantVo.TempListBean.ItemListBean.ChildrenBean bean:list){
            if(bean.getTitle().length()>10){
                return true;
            }
        }
        return false;

    }

    boolean isOpen;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                saveData(itemListBean);
                newItemListBean.setIsSaved(1);
                dialogInterface.onsumbmit();
               cancel();
                break;
            case R.id.cancleBtn:
                cancel();
                break;
            case R.id.pingjia:
                pjaMethodTxt.setVisibility(pjaMethodTxt.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case R.id.moreTv:

                if(!isOpen){
                    titleTxt.setMaxLines(100);
                    moreTv.setText("点击收起");
                    isOpen=true;
                }else{
                    titleTxt.setMaxLines(4);
                    moreTv.setText("查看更多");
                    isOpen=false;
                }

                break;

        }

    }


    private class JugeTypeAdapter extends DYSimpleAdapter<RisistantVo.TempListBean.ItemListBean.ChildrenBean> {

        public JugeTypeAdapter(Context context, List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView title = findView(convertView, R.id.title);
            LinearLayout singleCheckLay = findView(convertView, R.id.singleCheckLay);
            final ImageView title_check = findView(convertView, R.id.title_check);
            final RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean = childrenBeanList.get(position);
            String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
            title.setText(childrenBean.getTitle());
            final int chooseState = Integer.parseInt(str);
            switch (chooseState) {
                case 0:
                    title_check.setVisibility(View.GONE);
                    break;
                case 1:
                    title_check.setVisibility(View.VISIBLE);
                    title_check.setImageResource(R.drawable.check_box_right);
                    break;
                case 2:
                    title_check.setVisibility(View.VISIBLE);
                    title_check.setImageResource(R.drawable.check_box_wrong);
                default:
                    break;
            }
            singleCheckLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();

                    final int chooseState = Integer.parseInt(str);
                    switch (chooseState) {
                        case 0:
                            title_check.setVisibility(View.VISIBLE);
                            title_check.setImageResource(R.drawable.check_box_right);
                            childrenBean.setResult("1");
                            childrenBean.setGet_score(childrenBean.getScore());
                            break;
                        case 1:
                            title_check.setVisibility(View.VISIBLE);
                            title_check.setImageResource(R.drawable.check_box_wrong);
                            childrenBean.setResult("2");

                            break;
                        case 2:
                            title_check.setVisibility(View.GONE);
                            childrenBean.setResult("0");
                            childrenBean.setGet_score(childrenBean.getScore());
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        @Override
        public int getLayoutId() {
            return R.layout.sd_juge_type_adapter;
        }
    }

    public void showPOp(RisistantVo.TempListBean.ItemListBean itemListBean) {
        setData(itemListBean);
        super.show();
        isOpen=false;
        titleTxt.setMaxLines(4);
        moreTv.setText("查看更多");
        titleTxt.setText(itemListBean.getTitle());
        titleTxt.post(new Runnable() {
            @Override
            public void run() {
                int lines=titleTxt.getLineCount();
                if(lines>4){
                    moreTv.setVisibility(View.VISIBLE);
                }else{
                    moreTv.setVisibility(View.GONE);
                }

            }
        });
        pingjia.setVisibility(itemListBean.getItem_desc().length()>0?View.VISIBLE:View.GONE);
        pjaMethodTxt.setVisibility(View.GONE);
        pjaMethodTxt.setText(itemListBean.getItem_desc());

    }



    public interface SumbmitInter {
        abstract void onsumbmit();
    }

    public class ViewHolder {
      
    }
    public Button subtractionBtn, plusBtn;
    public GridView itemList;
    public TextView title, numTv;
    public LinearLayout itemRulesLay;//子项
    public ImageView title_check;
    public LinearLayout singleCheckLay;//标题判断题类型
    public LinearLayout scoreEdtLay;//积分操作类型布局
    public ImageView groupTileImg;
    public LinearLayout itemRulesLayVertical;
    public ListView itemList2;
    public TextView indexTv;
    public LinearLayout detailLay;
    public TextView scoreTv;
    public TextView nameTv;
    public TextView timeTv;
    public LinearLayout titleLay;
    private void setScoreLay(final RisistantVo.TempListBean.ItemListBean rti) {
        final double score = Double.parseDouble(rti.getScore());
        String text = rti.getItem_score();
        if (AbStrUtil.isEmpty(text)) {
            text = "0";
        }
        Double itemScore = Double.parseDouble(text);
        numTv.setText((score +itemScore) + "");
        if (AbStrUtil.isEmpty(rti.getItem_score())) {
            rti.setItem_score("0");

        }
        double doubleScore1 = Double.parseDouble(rti.getItem_score());
        subtractionBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (AbStrUtil.isEmpty(rti.getItem_score())) {
                            rti.setItem_score("0");

                        }
                        double doubleScore = Double.parseDouble(rti.getItem_score());
                        double unitscore = 1;
                        if (doubleScore +score<=0) {
                            return;
                        }
                        doubleScore -= unitscore;
                        rti.setItem_score(doubleScore + "");
                        numTv.setText(AbStrUtil.reMoveUnUseNumber((score + doubleScore)
                                + ""));
                        setScoreBtn(score,doubleScore);

                    }
                });
        plusBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (AbStrUtil.isEmpty(rti.getItem_score())) {
                            rti.setItem_score("0");

                        }
                        double doubleScore = Double.parseDouble(rti.getItem_score());
                        double unitscore = 1;
                        if (doubleScore>=0) {
                            return;
                        }
                        doubleScore += unitscore;
                        rti.setItem_score(doubleScore + "");
                        numTv.setText(AbStrUtil.reMoveUnUseNumber((score + doubleScore)
                                + ""));
                        setScoreBtn(score,doubleScore);
                    }


                });
        setScoreBtn(score,doubleScore1);
    }

  private void setScoreBtn(double score,double subScore){
      if(subScore>=0){
          plusBtn.setEnabled(false);
          subtractionBtn.setEnabled(true);
      }else if(subScore +score<=0){
          plusBtn.setEnabled(true);
          subtractionBtn.setEnabled(false);
      }else{
          plusBtn.setEnabled(true);
          subtractionBtn.setEnabled(true);
      }

  }


    private void setCheckLay(final RisistantVo.TempListBean.ItemListBean rti, final int type) {

        String str = AbStrUtil.isEmpty(rti.getResult()) ? "0" : rti.getResult();
        final int chooseState = Integer.parseInt(str);
        switch (chooseState) {
            case 0:
                title_check.setVisibility(View.GONE);
                scoreEdtLay.setVisibility(View.GONE);
                break;
            case 1:
                title_check.setVisibility(View.VISIBLE);
                title_check.setImageResource(R.drawable.check_box_right);
                scoreEdtLay.setVisibility(View.GONE);

                break;
            case 2:
                title_check.setVisibility(View.VISIBLE);
                title_check.setImageResource(R.drawable.check_box_wrong);
                if (type == 4) {
                    scoreEdtLay.setVisibility(View.VISIBLE);
                } else {
                    scoreEdtLay.setVisibility(View.GONE);
                }
                break;
            default:
                break;

        }

        singleCheckLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = AbStrUtil.isEmpty(rti.getResult()) ? "0" : rti.getResult();
                double score = Double.parseDouble(rti.getScore());
                final int chooseState = Integer.parseInt(str);
                switch (chooseState) {
                    case 0:
                        title_check.setVisibility(View.VISIBLE);
                        title_check.setImageResource(R.drawable.check_box_right);
                        rti.setResult("1");
                        scoreEdtLay.setVisibility(View.GONE);
                        rti.setProblemState(0);
                        rti.setItem_score("0");
                        break;
                    case 1:
                        title_check.setVisibility(View.VISIBLE);
                        title_check.setImageResource(R.drawable.check_box_wrong);
                        rti.setResult("2");
                        if (type == 4) {
                            scoreEdtLay.setVisibility(View.VISIBLE);
                        } else {
                            scoreEdtLay.setVisibility(View.GONE);
                        }
                        // sendBordCast();
                        rti.setItem_score("0");
                        break;
                    case 2:
                        title_check.setVisibility(View.GONE);
                        rti.setResult("0");
                        scoreEdtLay.setVisibility(View.GONE);
                        rti.setProblemState(0);
                        rti.setItem_score((0 - score) + "");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
