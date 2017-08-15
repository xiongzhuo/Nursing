package com.deya.hospital.study;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.KnowledgeVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/22
 */
public class AnswerPriviewActivity extends BaseCommonTopActivity {
    List<KnowledgeVo.ListBean> list;
    GridView gridView;
    int totalSize;
    AnswerPriviewAdapter adapter;

    @Override
    public String getTopTitle() {
        return "答案预览";
    }

    @Override
    public int getLayoutId() {
        return R.layout.answer_priview;
    }

    @Override
    public void initBaseData() {
        adapter = new AnswerPriviewAdapter(mcontext, list);
        list = (List<KnowledgeVo.ListBean>) getIntent().getSerializableExtra("data");
        totalSize = getIntent().getIntExtra("total", 0);
        gridView.setAdapter(adapter);


    }

    @Override
    public void initView() {
        gridView = (GridView) this.findViewById(R.id.gridview);

    }

    private class AnswerPriviewAdapter extends DYSimpleAdapter<KnowledgeVo.ListBean> {

        public AnswerPriviewAdapter(Context context, List<KnowledgeVo.ListBean> list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            return totalSize;
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView itemNum = BaseViewHolder.get(convertView, R.id.itemNum);
            ImageView itemIcon = BaseViewHolder.get(convertView, R.id.itemIcon);
            itemNum.setText((position + 1) + "");
            if (position <= list.size() - 1) {
                KnowledgeVo.ListBean listBean = list.get(position);
                Log.i("answer",listBean.getChooseAswer()+"-----"+listBean.getRightAswer());
                if (!AbStrUtil.isEmpty(listBean.getChooseAswer())) {
                    itemIcon.setBackgroundResource(R.drawable.circle_green);
                    itemNum.setTextColor(getResources().getColor(R.color.white));
                    if (listBean.getChooseAswer().equals(listBean.getRightAswer())) {
                        itemIcon.setEnabled(true);//绿色
                    } else {
                        itemIcon.setEnabled(false);//红色
                    }
                } else {
                    itemIcon.setBackgroundResource(R.drawable.circle_line_gray);
                    itemNum.setTextColor(getResources().getColor(R.color.black));
                }
            } else {
                itemIcon.setBackgroundResource(R.drawable.circle_line_gray);
                itemNum.setTextColor(getResources().getColor(R.color.black));
            }
        }


        @Override
        public int getLayoutId() {
            return R.layout.item_today_dynamic_adapter;
        }
    }

}
