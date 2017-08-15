package com.deya.hospital.workspace.multidrugresistant;

import android.content.Context;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.RisistantVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ResitantPriviewAadapter extends BasePriviewAdapter<RisistantVo.TempListBean.ItemListBean> {
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
                setCheckLay(rti, viewHolder, 0);
                break;

            case 3:
                viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
                setScoreLay(rti, viewHolder, 0);

                break;
            case 4:
                viewHolder.scoreEdtLay.setVisibility(View.VISIBLE);
                viewHolder.singleCheckLay.setVisibility(View.VISIBLE);
                setCheckLay(rti, viewHolder, 0);
                setScoreLay(rti, viewHolder, 1);
                break;
            case 5:

            case 6:
                RisitanCheckAdapter itemAdapter = new RisitanCheckAdapter(context, rti.getChildren(), true);
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

    private void setScoreLay(RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, int childPos) {
        String text = AbStrUtil.isEmpty(rti.getChildren().get(childPos).getResult()) ? "0" : rti.getChildren().get(childPos).getResult();
        viewHolder.numTv.setText(text);
        viewHolder.subtractionBtn
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String num = viewHolder.numTv.getText().toString();
                        if (!AbStrUtil.isEmpty(num)) {
                            double score = Double.parseDouble(num);
                            double unitscore = 0.5;
                            score -= unitscore;
                            viewHolder.numTv.setText(AbStrUtil.reMoveUnUseNumber(score
                                    + ""));
                        }


                    }
                });
    }

    private void setCheckLay(final RisistantVo.TempListBean.ItemListBean rti, final ViewHolder viewHolder, int childPos) {
        final RisistantVo.TempListBean.ItemListBean.ChildrenBean childrenBean = rti.getChildren().get(childPos);

        String str = AbStrUtil.isEmpty(childrenBean.getResult()) ? "0" : childrenBean.getResult();
        final int chooseState = Integer.parseInt(str);
        switch (chooseState) {
            case 0:
                viewHolder.title_check.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.title_check.setVisibility(View.VISIBLE);
                viewHolder.title_check.setImageResource(R.drawable.check_box_right);
                break;
            case 2:
                viewHolder.title_check.setVisibility(View.VISIBLE);
                viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
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
                        break;
                    case 1:
                        viewHolder.title_check.setVisibility(View.VISIBLE);
                        viewHolder.title_check.setImageResource(R.drawable.check_box_wrong);
                        childrenBean.setResult("2");
                        break;
                    case 2:
                        viewHolder.title_check.setVisibility(View.GONE);
                        childrenBean.setResult("0");
                        break;
                    default:
                        break;
                }
            }
        });
    }


}
