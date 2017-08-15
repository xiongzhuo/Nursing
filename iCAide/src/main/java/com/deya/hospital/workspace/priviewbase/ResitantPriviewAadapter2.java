package com.deya.hospital.workspace.priviewbase;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.RisistantVo;
import com.deya.hospital.workspace.multidrugresistant.BasePriviewAdapter;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ResitantPriviewAadapter2 extends BasePriviewAdapter<RisistantVo.TempListBean.ItemListBean> {
    public ResitantPriviewAadapter2(Context context, List<RisistantVo.TempListBean.ItemListBean> list) {
        super(context, list);
        this.list = list;
    }

//    选项类型
//    1）文字标签
//    2）判断
//    3）评分
//    4）判断和评分
//    5）单选
//    6）多选


    @Override
    public void setItem(int position, final ViewHolder viewHolder) {

        RisistantVo.TempListBean.ItemListBean rti = list.get(position);
        viewHolder.title.setText(rti.getTitle());
        viewHolder.groupTileImg.setVisibility(View.GONE);
        viewHolder.scoreEdtLay.setVisibility(View.GONE);
        viewHolder.singleCheckLay.setVisibility(View.GONE);
        viewHolder.title.setTextColor(context.getResources().getColor(R.color.black));
        viewHolder.itemRulesLayVertical.setVisibility(View.GONE);
        viewHolder.itemRulesLay.setVisibility(View.GONE);
        switch (rti.getType()) {
            case 1:
                viewHolder.groupTileImg.setVisibility(View.VISIBLE);
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.top_color));
                break;
            case 2:

                viewHolder.singleCheckLay.setVisibility(View.VISIBLE);
                setCheckLay(rti, viewHolder, 0, 2);
                break;

            case 3:
                viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
                setScoreLay(rti, viewHolder, 0);

                break;
            case 4:
                viewHolder.singleCheckLay.setVisibility(View.VISIBLE);
                setCheckLay(rti, viewHolder, 0, 4);
                setScoreLay(rti, viewHolder, 1);
                break;
            case 5:
                RisitanCheckAdapter itemAdapter1 = new RisitanCheckAdapter(context, rti.getChildren(), true);
                if (rti.getShow_type() == 3) {//多行
                    viewHolder.itemRulesLayVertical.setVisibility(View.VISIBLE);
                    viewHolder.itemList2.setAdapter(itemAdapter1);
                } else {//单行
                    viewHolder.itemRulesLay.setVisibility(View.VISIBLE);
                    viewHolder.itemList.setAdapter(itemAdapter1);
                }
                break;
            case 6:
                RisitanCheckAdapter itemAdapter = new RisitanCheckAdapter(context, rti.getChildren(), false);
                if (rti.getShow_type() == 3) {//多行
                    viewHolder.itemRulesLayVertical.setVisibility(View.VISIBLE);
                    viewHolder.itemList2.setAdapter(itemAdapter);
                } else {//单行
                    viewHolder.itemRulesLay.setVisibility(View.VISIBLE);
                    viewHolder.itemList.setAdapter(itemAdapter);
                }
                break;
            default:
                break;


        }


    }

    private void  sendBordCast(){
        Intent intent=new Intent(SupvisorFragment.CHECKCONTENTNEEDCHANGE);
        context.sendBroadcast(intent);

    }
    private void setScoreLay(final RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, final int childPos) {
        String text = AbStrUtil.isEmpty(rti.getChildren().get(childPos).getResult()) ? rti.getChildren().get(childPos).getScore() : rti.getChildren().get(childPos).getResult();
        if(AbStrUtil.isEmpty(text)){
            text="0";
        }
        viewHolder.numTv.setText(text);
        final double totalScore = Double.parseDouble(text);
        viewHolder.subtractionBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String num = viewHolder.numTv.getText().toString();
                        if (!AbStrUtil.isEmpty(num)) {

                            double score = Double.parseDouble(num);
                            if(score<=0){
                                return;
                            }
                            double unitscore = 0.5;
                            score -= unitscore;
                            rti.getChildren().get(childPos).setResult(score+"");
                            viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
                                    + ""));
                        }


                    }
                });
        viewHolder.plusBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String num = viewHolder.numTv.getText().toString();
                        if (!AbStrUtil.isEmpty(num)) {
                            double score = Double.parseDouble(num);
                            double unitscore = 0.5;
                            if(score>=totalScore){
                                return;
                            }
                            score += unitscore;
                            rti.getChildren().get(childPos).setResult(score+"");
                            viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
                                    + ""));
                        }


                    }
                });
    }

    private void setCheckLay(final RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, int childPos, final int type) {
        final RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean = rti.getChildren().get(childPos);

        String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
        final int chooseState = Integer.parseInt(str);
        switch (chooseState) {
            case 0:
                viewHolder.title_check.setVisibility(View.GONE);
                viewHolder.scoreEdtLay.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.title_check.setVisibility(View.VISIBLE);
                viewHolder.title_check.setImageResource(R.drawable.check_box_right);
                viewHolder.scoreEdtLay.setVisibility(View.GONE);

                break;
            case 2:
                viewHolder.title_check.setVisibility(View.VISIBLE);
                viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
                if (type == 4) {
                    viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.scoreEdtLay.setVisibility(View.GONE);
                }
                break;
            default:
                break;

        }

        viewHolder.singleCheckLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
                final int chooseState = Integer.parseInt(str);
                switch (chooseState) {
                    case 0:
                        viewHolder.title_check.setVisibility(View.VISIBLE);
                        viewHolder.title_check.setImageResource(R.drawable.check_box_right);
                        childrenBean.setResult("1");
                        viewHolder.scoreEdtLay.setVisibility(View.GONE);
                        rti.setProblemState(0);
                        break;
                    case 1:
                        viewHolder.title_check.setVisibility(View.VISIBLE);
                        viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
                        childrenBean.setResult("2");
                        if (type == 4) {
                            viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.scoreEdtLay.setVisibility(View.GONE);
                        }
                        sendBordCast();
                        break;
                    case 2:
                        viewHolder.title_check.setVisibility(View.GONE);
                        childrenBean.setResult("0");
                        viewHolder.scoreEdtLay.setVisibility(View.GONE);
                        rti.setProblemState(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }


}
