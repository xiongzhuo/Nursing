package com.deya.hospital.study;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.DeletDialog;
import com.deya.hospital.vo.ClassifyExciseVo;
import com.deya.hospital.vo.KnowledgeVo;
import com.im.sdk.dy.common.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/28
 */
public class LocalKnowledgePriview extends KnowledgePriviewBaseActivity {
    DeletDialog dialog;
    List<ClassifyExciseVo.ListBean> parentlist;
    int pos;//当前展示数据在所有分类列表的坐标

    @Override
    public void getLoacleData(){
        parentlist= (List<ClassifyExciseVo.ListBean>) getIntent().getSerializableExtra("parentlist");
        pos=getIntent().getIntExtra("position",0);
        if(null!=parentlist){
            list.addAll(parentlist.get(pos).getItems());
        }
        data = (ClassifyExciseVo.ListBean) getIntent().getSerializableExtra("data");
        data.setCount(list.size());
        type=getIntent().getIntExtra("type",1);
        initChildView();
    }

    private void initChildView() {
        deletImgButton= (ImageView) this.findViewById(R.id.deletImgButton);
        deletImg= (ImageView) this.findViewById(R.id.deletImg);
        dialog=new DeletDialog(mcontext, R.style.SelectDialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbVo!=null){
                    if(dbVo.getIsColected()==1){
                        dbVo.setIsWrong(0);
                        SubjectDbUtils.updateData(mcontext,dbVo);
                    }else{
                        SubjectDbUtils.deleteData(mcontext,dbVo);
                    }
                }
                finish();
            }
        });
        if(type==2){
            deletImgButton.setVisibility(View.VISIBLE);
            deletImg.setVisibility(View.VISIBLE);
        }
        deletImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(null!=dialog){
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onCheckPopNuber(int position) {
        order_pager.setCurrentItem(position);

        slidingDrawer.animateClose();
    }

    @Override
    public void onPageSelect(int position) {
        listBean = list.get(itemIndex);
        pageNumber.setText("(" + (position+1) + "/" + data.getCount() + ")");
        setAnswer();
        setRightNum();
        itemIndex = position;
        dbVo =list.get(position);
        setCollect();
        listfragment.get(position).refresh(modeState);
        refreshPopAapter();

    }

    @Override
    public boolean isWrongItem() {
        return false;
    }

    @Override
    public void getSubject() {

        pageCount=data.getCount();
        for (KnowledgeVo.ListBean listbean : list) {
            LearningModelFragment fragment = LearningModelFragment.newInstance(listbean, modeState);
            fragment.setPageTag(listbean.getSeqNo());
            listfragment.add(fragment);
        }

        refreshAdapter();
        dismissdialog();
        order_pager.setCurrentItem(itemIndex);
    }

    @Override
    public void setAnswer() {
        listBean.setChooseAswer("");
        listBean.setRightAswer("");
        for (KnowledgeVo.ListBean.ItemsBean itemsBean : listBean.getItems()) {
            if (itemsBean.getIs_yes().equals("1")) {
                listBean.setRightAswer(listBean.getRightAswer() + itemsBean.getTitle());
            }
            if (itemsBean.getResult().equals("1")) {
                listBean.setChooseAswer(listBean.getChooseAswer() + itemsBean.getTitle());
            }
        }
        if (!listBean.getRightAswer().equals(listBean.getChooseAswer())) {
            if (dbVo != null) {
                dbVo.setIsWrong(1);
                SubjectDbUtils.updateData(mcontext, dbVo);
            }

        }
    }

    @Override
    public void setAdapter() {
        refreshAdapter();
        order_pager.setCurrentItem(itemIndex+1);

    }

    @Override
    public void setAdapterCHooseIndex(int position, TextView itemNum) {
        Log.i("111111",itemIndex+"=="+position);
        if(itemIndex==position){
            itemNum.setBackgroundResource(R.drawable.circle_gray_bg);
        }else{
            itemNum.setBackgroundResource(0);
        }
    }

    @Override
    public void jumpToNextClassify() {
            for (int i = pos+1; i < parentlist.size(); i++) {
                if (parentlist.get(i).getItems() != null && parentlist.get(i).getItems().size() > 0) {
                    Intent intent = new Intent(mcontext, LocalKnowledgePriview.class);
                    intent.putExtra("position", i);
                    intent.putExtra("type", type);
                    intent.putExtra("data", parentlist.get(i));
                    intent.putExtra("parentlist", (Serializable) parentlist);
                    startActivity(intent);
                    finish();
                    break;
                }else if(i==parentlist.size()-1){//已到最后一个分类 并且没有题目
                    ToastUtil.showMessage("没有找到符合条件的分类");
                    finish();

                }

            }
    }
}
