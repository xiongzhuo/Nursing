package com.deya.hospital.study;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workcircle.knowledge.KnowledgeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 * 学习模式
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class LearningModelFragment extends BaseFragment {
    ListView pagerList;
    TextView typeTv, titleTv;
    LearningAdapter adapter;
    MyKnowledgeItemAdapter adapter2;
    boolean isMuti = false;
    int index;
    KnowledgeVo knowlegeVo;
    List<KnowledgeVo.ListBean.ItemsBean> list;
    KnowledgePriviewBaseActivity activity;
    TextView analysisTv;
    KnowledgeVo.ListBean vo;
    Button submitBtn;
    LinearLayout answerLay;

    int state;//0答题模式，1、学习模式
    private int pageTag;
    private ImageView priviewImg;

    public static LearningModelFragment newInstance(KnowledgeVo.ListBean vo, int type) {
        LearningModelFragment newFragment = new LearningModelFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", vo);
        bundle.putInt("state", type);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public int getPageTage() {

        return pageTag;
    }

    public void setPageTag(int pageTag) {

        this.pageTag = pageTag;
    }

    public KnowledgeVo.ListBean getBean() {

        return vo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_knowledge_priview, container,
                    false);

            initView();
            initBuesinisData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }


    private void initView() {
        if (null == list) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        activity = (KnowledgePriviewBaseActivity) getActivity();
        pagerList = (ListView) this.findViewById(R.id.pagerList);
        pagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (state == 1 || vo.isAnswered()) {
                    return;
                }

                adapter2.setChooseIndex(position);
                if (isMuti) {
                    setSubmitVBtn();
                } else {
                    vo.setAnswered(true);
                    if (isWrongItem()) {
                        answerLay.setVisibility(View.VISIBLE);
                        if (vo.getSeqNo() >= activity.getTotalSize()) {
                            activity.selectNext();
                        }

                    } else {
                        activity.selectNext();
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });
        typeTv = (TextView) this.findViewById(R.id.typeTv);
        titleTv = (TextView) this.findViewById(R.id.titleTv);

        LinearLayout analysisLay = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.learning_modle_analysislay, null);
        submitBtn = (Button) analysisLay.findViewById(R.id.submitBtn);
        answerLay = (LinearLayout) analysisLay.findViewById(R.id.answerLay);
        pagerList.addFooterView(analysisLay);
        analysisLay.setVisibility(View.VISIBLE);
        adapter = new LearningAdapter(getActivity(), list);
        adapter2 = new MyKnowledgeItemAdapter(getActivity(), list);
        analysisTv = (TextView) findViewById(R.id.analysisTv);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vo.setAnswered(true);
                submitBtn.setVisibility(View.GONE);
                if (isWrongItem()) {
                    answerLay.setVisibility(View.VISIBLE);
                    if (vo.getSeqNo() >= activity.getTotalSize()) {

                        activity.selectNext();
                    }
                } else {
                    activity.selectNext();
                }
                adapter2.notifyDataSetChanged();
            }
        });
        priviewImg = (ImageView) this.findViewById(R.id.priviewImg);
    }

    public void setSubmitVBtn() {
        int i = 0;
        for (int j = 0; j < list.size(); j++) {
            KnowledgeVo.ListBean.ItemsBean bean = list.get(j);
            if (bean.getResult().equals("1")) {
                i++;
            }
        }
        if (i >= 1) {
            submitBtn.setEnabled(true);
            submitBtn.setTextColor(getResources().getColor(R.color.white));
        } else {
            submitBtn.setEnabled(false);
            submitBtn.setTextColor(getResources().getColor(R.color.gray));
        }
        setanswerlay();
    }

    private void initBuesinisData() {

        Bundle args = getArguments();
        vo = (KnowledgeVo.ListBean) args.getSerializable("data");
        state = args.getInt("state", 0);
        list.clear();
        list.addAll(vo.getItems());
        titleTv.setText("\u3000\u3000\u3000\u3000" + vo.getTitle());
        typeTv.setText(vo.getSub_type() == 1 ? "单选题" : "多选题");
        vo.setRightAswer("");
        for (KnowledgeVo.ListBean.ItemsBean itemsBean : vo.getItems()) {
            if (itemsBean.getIs_yes().equals("1")) {
                vo.setRightAswer(vo.getRightAswer() + itemsBean.getTitle());
            }
        }
        analysisTv.setText("正确答案:" + vo.getRightAswer() + "  " + vo.getNote());
        isMuti = vo.getSub_type() == 2;
        if (state == 0) {
            submitBtn.setVisibility(isMuti ? View.VISIBLE : View.GONE);
        } else {
            submitBtn.setVisibility(View.GONE);
        }
        if (!AbStrUtil.isEmpty(vo.getImg())) {
            priviewImg.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(WebUrl.FILE_LOAD_URL +vo.getImg(),priviewImg, AbViewUtil.getOptions(R.drawable.defult_img));
        }else{
            priviewImg.setVisibility(View.GONE);
        }
        adapter.setIsMuti(isMuti);
        adapter2.setIsMuti(isMuti);
        pagerList.setAdapter(state == 0 ? adapter2 : adapter);
        setanswerlay();
    }

    private void setanswerlay() {
        answerLay.setVisibility(state == 1 ? View.VISIBLE : View.GONE);
        if (!AbStrUtil.isEmpty(vo.getChooseAswer())) {
            answerLay.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);
        }
    }

    public boolean isWrongItem() {

        vo.setChooseAswer("");
        vo.setRightAswer("");
        for (KnowledgeVo.ListBean.ItemsBean itemsBean : vo.getItems()) {
            if (itemsBean.getIs_yes().equals("1")) {
                vo.setRightAswer(vo.getRightAswer() + itemsBean.getTitle());
            }
            if (itemsBean.getResult().equals("1")) {
                vo.setChooseAswer(vo.getChooseAswer() + itemsBean.getTitle());
            }
        }
        if (!AbStrUtil.isEmpty(vo.getChooseAswer()) && !vo.getRightAswer().equals(vo.getChooseAswer())) {
            return true;
        }
        return false;
    }

    public void refresh(int state) {
        this.state = state;
        if (null != pagerList) {
            pagerList.setAdapter(state == 0 ? adapter2 : adapter);
            if (state == 0) {
                submitBtn.setVisibility(isMuti ? View.VISIBLE : View.GONE);
            } else {
                submitBtn.setVisibility(View.GONE);
            }
            analysisTv.setText("正确答案:" + vo.getRightAswer() + "  " + getAnswerNote());
            setanswerlay();

        }

    }

    String getAnswerNote() {
        return AbStrUtil.isEmpty(vo.getNote()) ? "" : vo.getNote();
    }

    private class LearningAdapter extends KnowledgeAdapter {

        public LearningAdapter(Context context, List<KnowledgeVo.ListBean.ItemsBean> list) {
            super(context, list);
        }

        @Override
        public void setview(int position, KnowledgeAdapter.ViewHolder viewHolder) {
            if (list.get(position).getIs_yes().equals("1")) {
                viewHolder.chooseImg.setVisibility(View.VISIBLE);
                viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                viewHolder.indextTv.setEnabled(true);
                viewHolder.indextTv.setTextColor(getResources().getColor(R.color.white));
            } else {
                viewHolder.chooseImg.setVisibility(View.INVISIBLE);
                viewHolder.indextTv.setEnabled(false);
                viewHolder.indextTv.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }


    public class MyKnowledgeItemAdapter extends BaseAdapter {
        List<KnowledgeVo.ListBean.ItemsBean> list;
        Context context;
        LayoutInflater layoutInflater;

        public MyKnowledgeItemAdapter(Context context, List<KnowledgeVo.ListBean.ItemsBean> list) {
            this.context = context;
            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        public void setChooseIndex(int index) {
            if (isMuti) {
                if (list.get(index).getResult().equals("")||list.get(index).getResult().equals("0")) {
                    list.get(index).setResult("1");
                } else {
                    list.get(index).setResult("0");
                }
            } else {
                list.get(index).setResult("1");
            }
            notifyDataSetChanged();

        }

        public void setIsMuti(boolean isMuti) {
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.adapter_knowledge_privew, null);
                viewHolder.indextTv = (TextView) convertView.findViewById(R.id.indexTv);
                viewHolder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
                viewHolder.chooseImg = (ImageView) convertView.findViewById(R.id.chooseImg);
                viewHolder.chooseLay = (LinearLayout) convertView.findViewById(R.id.chooseLay);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            KnowledgeVo.ListBean.ItemsBean itemsBean = list.get(position);
            viewHolder.indextTv.setText(list.get(position).getTitle());
            viewHolder.titleTv.setText(list.get(position).getNote());
            if (vo.isAnswered()) {
                if (!list.get(position).getResult().equals("")) {
                    if (itemsBean.getIs_yes().equals(itemsBean.getResult())) {
                        if (itemsBean.getIs_yes().equals("1")) {
                            viewHolder.chooseImg.setVisibility(View.VISIBLE);
                            viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                        } else {
                            viewHolder.chooseImg.setVisibility(View.GONE);
                        }
                    } else {
                        viewHolder.chooseImg.setVisibility(View.VISIBLE);
                        viewHolder.chooseImg.setImageResource(R.drawable.img_no_p);
                    }
                    viewHolder.indextTv.setEnabled(true);
                    viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.white));

                } else {
                    if (!itemsBean.getIs_yes().equals("1")) {
                        viewHolder.chooseImg.setVisibility(View.INVISIBLE);
                        viewHolder.indextTv.setEnabled(false);
                        viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
                    } else {
                        viewHolder.chooseImg.setVisibility(View.VISIBLE);
                        viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                        viewHolder.indextTv.setEnabled(false);
                        viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }
            } else {
                if (list.get(position).getResult().equals("1")) {
                    viewHolder.chooseImg.setVisibility(View.VISIBLE);
                    viewHolder.chooseImg.setImageResource(R.drawable.adopt_select);
                    viewHolder.indextTv.setEnabled(true);
                    viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    viewHolder.chooseImg.setVisibility(View.INVISIBLE);
                    viewHolder.indextTv.setEnabled(false);
                    viewHolder.indextTv.setTextColor(context.getResources().getColor(R.color.black));
                }
            }


            return convertView;
        }

        private class ViewHolder {
            TextView indextTv;
            TextView titleTv;
            ImageView chooseImg;
            LinearLayout chooseLay;

        }
    }
}