package com.deya.hospital.study;

import android.util.Log;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.vo.KnowledgeVo;
import com.deya.hospital.workspace.TaskUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class StudyKnowledgeActivity extends KnowledgePriviewBaseActivity implements RequestInterface {
    @Override
    public void onCheckPopNuber(int position) {
        int currentLoadPage = (position) / 10 + 1;
        sqNo = position + 1;
        slidingDrawer.animateClose();
        if (!map.containsKey(currentLoadPage)) {
            pageIndex = currentLoadPage;
            getSubject();

        } else {
            setAdapter();
        }
    }

    @Override
    public void onPageSelect(int position) {
        int currCount = listfragment.get(position).getPageTage();//当面页码
        int r = currCount % pageCount;//加载页 的第几页
        int d = currCount / pageCount + 1;//当前页
        if (position > itemIndex) {//向右翻
            if (r == pageCount - 1 && currCount < data.getCount() - 2 && notContainData(d + 1)) {
                pageIndex = d + 1;
                getSubject();
            }
        } else if (position < itemIndex) {//向左翻
            if (r == 2 && currCount > 1 * pageCount && notContainData(d - 1)) {
                Log.i("pager", position + "----" + ((pageIndex - 1) * pageCount - 3));
                pageIndex = d - 1;
                getSubject();
            }
        }
        listBean = listfragment.get(itemIndex).getBean();
        pageNumber.setText("(" + listfragment.get(position).getPageTage() + "/" + data.getCount() + ")");
        setAnswer();
        setRightNum();
        itemIndex = position;
        dbVo = SubjectDbUtils.getDataById(mcontext, listfragment.get(position).getBean());
        setCollect();
        sqNo = listfragment.get(itemIndex).getPageTage();
        listfragment.get(position).refresh(modeState);
        refreshPopAapter();
    }

    @Override
    public boolean isOutOfSize() {
        return sqNo >= data.getCount();
    }

    @Override
    public boolean isWrongItem() {
        return false;
    }


    protected int getLayoutId() {
        return R.layout.activity_study_knowledge;
    }

    public void getSubject() {
        showprocessdialog();
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
            job.put("subjectOwner", type == 1 ? "public" : "hospital");
            setJson(job);
            job.put("pageIndex", pageIndex);
            job.put("pageCount", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this, mcontext, GET_SUBJECT, job, "subject/subjectListByCateId");
    }

    public void setJson(JSONObject job) {
        try {
            job.put("cateId", data.getCateId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {

        knowledgeVo = TaskUtils.gson.fromJson(jsonObject.toString(), KnowledgeVo.class);

        map.put(pageIndex, true);
        list.addAll(knowledgeVo.getList());
        if(pageIndex==1){
            if(list.size()>0){
            dbVo = SubjectDbUtils.getDataById(mcontext, list.get(0));
            setCollect();
            }
        }
        for (KnowledgeVo.ListBean listbean : knowledgeVo.getList()) {
            LearningModelFragment fragment = LearningModelFragment.newInstance(listbean, modeState);
            fragment.setPageTag(listbean.getSeqNo());
            listfragment.add(fragment);
        }
        Collections.sort(listfragment, comparator);
        refreshAdapter();
        setAdapter();
        dismissdialog();
    }

    /**
     * 对list进行排序
     */
    Comparator<LearningModelFragment> comparator = new Comparator<LearningModelFragment>() {
        @Override
        public int compare(LearningModelFragment bean1, LearningModelFragment bean2) {

            return bean1.getPageTage() - bean2.getPageTage();
        }
    };

    @Override
    public void onRequestErro(String message) {
        dismissdialog();

    }

    @Override
    public void onRequestFail(int code) {
        dismissdialog();

    }


    public void setRightNum() {
        wrongCount = 0;
        rightCount = 0;
        for (KnowledgeVo.ListBean bean : list) {
            if (!AbStrUtil.isEmpty(bean.getChooseAswer())) {
                if (!bean.getChooseAswer().equals(bean.getRightAswer())) {
                    wrongCount++;
                } else {
                    rightCount++;

                }
            }
        }
        rightNum.setText("正确:" + rightCount);
        wrongNum.setText("错误" + wrongCount);
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
        if (!AbStrUtil.isEmpty(listBean.getChooseAswer()) && !listBean.getRightAswer().equals(listBean.getChooseAswer())) {
            if (dbVo != null) {
                dbVo.setIsWrong(1);
                SubjectDbUtils.updateData(mcontext, dbVo);
            } else {
                listBean.setIsWrong(1);
                listBean.setParent_id(parentid);
                if (SubjectDbUtils.saveData(mcontext, listBean)) {

                }
                dbVo = SubjectDbUtils.getDataById(mcontext, listBean);
            }

        }
    }


    /**
     * 点击跳转到某个序号对应的题目
     */
    @Override
    public void setAdapter() {
        order_pager.removeAllViews();
        refreshAdapter();

        for (int i = 0; i < listfragment.size(); i++) {
            if (listfragment.get(i).getPageTage() == sqNo) {
                order_pager.setCurrentItem(i);
                /**
                 * 如果当前页刚好在加载页，因为select方法未执行 所以不会加载 所以必须主动加载
                 */
                int currCount = sqNo;//当面页码
                int r = currCount % pageCount;//加载页 的第几页
                int d = currCount / pageCount + 1;//当前页
                if (r >= pageCount - 1 && currCount < data.getCount() - 2 && notContainData(d + 1)) {
                    pageIndex = d + 1;
                    getSubject();
                } else if (r <= 2 && currCount > 1 * pageCount && notContainData(d - 1)) {
                    pageIndex = d - 1;
                    getSubject();

                }
            }
        }

    }

    @Override
    public void setAdapterCHooseIndex(int position, TextView itemNum)  {
        if(listfragment.size()>0&&listfragment.get(itemIndex).getPageTage()==(position+1)){
            itemNum.setBackgroundResource(R.drawable.circle_gray_bg);
        }else{
            itemNum.setBackgroundResource(0);
        }
    }


}
