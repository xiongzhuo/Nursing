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
public class ResitantPriviewAadapter extends BasePriviewAdapter<RisistantVo.TempListBean.ItemListBean> {
    OnPopChildInter onPopChildInter;
    int color[] = {R.color.top_color, R.color.white};

    public ResitantPriviewAadapter(Context context, List<RisistantVo.TempListBean.ItemListBean> list) {
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


    public void setChildPopLis(OnPopChildInter onPopChildInter) {

        this.onPopChildInter = onPopChildInter;

    }

    @Override
    public void setItem(int position, final ViewHolder viewHolder) {

        final RisistantVo.TempListBean.ItemListBean rti = list.get(position);
        viewHolder.title.setText(rti.getTitle());
        viewHolder.groupTileImg.setVisibility(View.GONE);
        viewHolder.scoreEdtLay.setVisibility(View.GONE);
        viewHolder.singleCheckLay.setVisibility(View.GONE);
        viewHolder.title.setTextColor(context.getResources().getColor(R.color.black));
        viewHolder.itemRulesLayVertical.setVisibility(View.GONE);
        viewHolder.itemRulesLay.setVisibility(View.GONE);
        viewHolder.detailLay.setVisibility(View.GONE);

        double score = Double.parseDouble(rti.getScore());
        double itemScore;
        if (AbStrUtil.isEmpty(rti.getItem_score())) {
            itemScore = 0;
        } else {
            itemScore = Double.parseDouble(rti.getItem_score());
        }
        switch (rti.getType()) {
            case 1:
                viewHolder.title.setText(rti.getShow_no() + rti.getTitle());
                viewHolder.indexTv.setVisibility(View.GONE);
                viewHolder.title.setTextColor(context.getResources().getColor(R.color.top_color));
                break;

            default:

                viewHolder.titleLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPopChildInter.OnPop(rti);
                    }
                });
                viewHolder.title_check.setVisibility(View.GONE);
                viewHolder.indexTv.setVisibility(View.VISIBLE);
                viewHolder.indexTv.setText(rti.getShow_no());
                break;


        }
        viewHolder.indexTv.setBackgroundResource(!AbStrUtil.isEmpty(rti.getSave_time()) ? R.drawable.circle_gray : R.drawable.circle_sharp_blue);
        viewHolder.indexTv.setTextColor(!AbStrUtil.isEmpty(rti.getSave_time()) ? context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.top_color));
        viewHolder.nameTv.setText(rti.getName());
        viewHolder.timeTv.setText(rti.getSave_time());

        if (AbStrUtil.isEmpty(rti.getSave_time())) {
            viewHolder.scoreTv.setText("得分"+ AbStrUtil.reMoveUnUseNumber(rti.getScore())+"分");
            viewHolder.detailLay.setVisibility(View.GONE);
        } else {
            viewHolder.scoreTv.setText("得分"+ AbStrUtil.reMoveUnUseNumber((score + itemScore) + "")+"分");
            viewHolder.detailLay.setVisibility(View.VISIBLE);
        }


    }

    private void sendBordCast() {
        Intent intent = new Intent(SupvisorFragment.CHECKCONTENTNEEDCHANGE);
        context.sendBroadcast(intent);

    }

    private void setScoreLay(final RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, final int childPos) {
//        String text = AbStrUtil.isEmpty(rti.getChildren().get(childPos).getResult()) ? rti.getChildren().get(childPos).getScore() : rti.getChildren().get(childPos).getResult();
//        if (AbStrUtil.isEmpty(text)) {
//            text = "0";
//        }
//        viewHolder.numTv.setText(text);
//        final int totalScore;
//        if (!AbStrUtil.isEmpty(rti.getChildren().get(childPos).getScore())) {
//            totalScore = Integer.parseInt(rti.getChildren().get(childPos).getScore());
//        } else {
//            totalScore = 0;
//        }
//        viewHolder.subtractionBtn
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        String num = viewHolder.numTv.getText().toString();
//                        if (!AbStrUtil.isEmpty(num)) {
//                            double doubleScore = Double.parseDouble(num);
//                            int score = (int) doubleScore;
//                            if (score <= 0) {
//                                return;
//                            }
//                            int unitscore = 1;
//                            score -= unitscore;
//                            rti.getChildren().get(childPos).setResult(score + "");
//                            viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
//                                    + ""));
//                        }
//
//
//                    }
//                });
//        viewHolder.plusBtn
//                .setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        String num = viewHolder.numTv.getText().toString();
//                        if (!AbStrUtil.isEmpty(num)) {
//                            double doubleScore = Double.parseDouble(num);
//                            int score = (int) doubleScore;
//                            int unitscore = 1;
//                            if (score >= totalScore) {
//                                return;
//                            }
//                            score += unitscore;
//                            rti.getChildren().get(childPos).setResult(score + "");
//                            viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
//                                    + ""));
//                        }
//
//
//                    }
//                });
    }

    private void setCheckLay(final RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, int childPos, final int type) {
//        final RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean = rti.getChildren().get(childPos);
//
//        String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
//        final int chooseState = Integer.parseInt(str);
//        switch (chooseState) {
//            case 0:
//                viewHolder.title_check.setVisibility(View.GONE);
//                viewHolder.scoreEdtLay.setVisibility(View.GONE);
//                break;
//            case 1:
//                viewHolder.title_check.setVisibility(View.VISIBLE);
//                viewHolder.title_check.setImageResource(R.drawable.check_box_right);
//                viewHolder.scoreEdtLay.setVisibility(View.GONE);
//
//                break;
//            case 2:
//                viewHolder.title_check.setVisibility(View.VISIBLE);
//                viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
//                if (type == 4) {
//                    viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.scoreEdtLay.setVisibility(View.GONE);
//                }
//                break;
//            default:
//                break;

        //   }

//        viewHolder.singleCheckLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
//                final int chooseState = Integer.parseInt(str);
//                switch (chooseState) {
//                    case 0:
//                        viewHolder.title_check.setVisibility(View.VISIBLE);
//                        viewHolder.title_check.setImageResource(R.drawable.check_box_right);
//                        childrenBean.setResult("1");
//                        viewHolder.scoreEdtLay.setVisibility(View.GONE);
//                        rti.setProblemState(0);
//                        break;
//                    case 1:
//                        viewHolder.title_check.setVisibility(View.VISIBLE);
//                        viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
//                        childrenBean.setResult("2");
//                        if (type == 4) {
//                            viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.scoreEdtLay.setVisibility(View.GONE);
//                        }
//                        sendBordCast();
//                        break;
//                    case 2:
//                        viewHolder.title_check.setVisibility(View.GONE);
//                        childrenBean.setResult("0");
//                        viewHolder.scoreEdtLay.setVisibility(View.GONE);
//                        rti.setProblemState(0);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
    }


    public interface OnPopChildInter {
        public abstract void OnPop(RisistantVo.TempListBean.ItemListBean chi);

    }
}
