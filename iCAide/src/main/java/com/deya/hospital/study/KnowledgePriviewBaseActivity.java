package com.deya.hospital.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.adapter.MyDialogInterface;
import com.deya.hospital.base.BaseFragmentActivity;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.base.img.TipsDialogRigister;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.ClassifyExciseVo;
import com.deya.hospital.vo.KnowledgeVo;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/28
 */
public abstract class KnowledgePriviewBaseActivity extends BaseFragmentActivity implements View.OnClickListener {
    LinearLayout framView;
    KnowledgeVo knowledgeVo;
    public KnowledgeVo.ListBean listBean;
    ClassifyExciseVo.ListBean data;
    int GET_SUBJECT = 0x802;
    int pageIndex = 1;
    int itemIndex = 0;
    TextView rightNum, wrongNum;
    List<KnowledgeVo.ListBean> list;
    int rightCount, wrongCount;
    TextView queClassName;
    ViewPager order_pager;
    public MypagerFragmentAdapter myadapter;
    protected List<LearningModelFragment> listfragment;
    int modeState;//查看模式 0答题模式 1学习模式
    private RadioGroup mGroup;
    Map<Integer, Boolean> map;//随机加载集合 Key:页码 value  表示是否已加载
    int pageCount = 10;//每次加载题目个数
    int sqNo;//从题目序号弹出款选择的序号
    TextView pageNumber;
    ImageView collectImg;
    public AnswerPriviewAdapter answerPriviewAdapter;
    KnowledgeVo.ListBean dbVo;//数据库存放的对象
    SlidingDrawer slidingDrawer;
    private GridView gridView;
    public ImageView collectImgButton, deletImgButton, deletImg;
    int parentid;
    int type;//1、分类练习 2、本院题库


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        list = new ArrayList<>();
        listfragment = new ArrayList<>();
        getLoacleData();
        initView();
    }

    public void getLoacleData() {
        data = (ClassifyExciseVo.ListBean) getIntent().getSerializableExtra("data");
        parentid = AbStrUtil.isEmpty(data.getCateId()) ? 0 : Integer.parseInt(data.getCateId());
        type = getIntent().getIntExtra("type", 1);
    }

    protected void initView() {


        map = new HashMap<>();
        mGroup = (RadioGroup) this.findViewById(R.id.group);
        mGroup.setOnCheckedChangeListener(new CheckedChangeListener());
        rightNum = (TextView) findViewById(R.id.rightNum);
        wrongNum = (TextView) findViewById(R.id.wrongNum);
        queClassName = (TextView) findViewById(R.id.queClassName);
        order_pager = (ViewPager) this.findViewById(R.id.order_pager);
        order_pager.setOffscreenPageLimit(1);
        ViewPagerScroller scroller = new ViewPagerScroller(mcontext);
        scroller.setScrollDuration(1000);
        scroller.initViewPagerScroll(order_pager);//这个是设置切换过渡时间为2秒
        myadapter = new MypagerFragmentAdapter(getSupportFragmentManager());
        answerPriviewAdapter = new AnswerPriviewAdapter(mcontext, list);
        order_pager.setAdapter(myadapter);
        order_pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setAlpha(normalizedposition);

            }

        });
        pageNumber = (TextView) this.findViewById(R.id.pageNumber);
        framView= (LinearLayout) this.findViewById(R.id.framView);
        framView.setOnClickListener(this);
        findViewById(R.id.signInTv).setOnClickListener(this);
        collectImg = (ImageView) this.findViewById(R.id.collectImg);
        collectImgButton = (ImageView) findViewById(R.id.collectImgButton);
        findViewById(R.id.collectImgButton).setOnClickListener(this);
        slidingDrawer = (SlidingDrawer) this.findViewById(R.id.sliding);
        gridView = (GridView) this.findViewById(R.id.gridview);
        gridView.setAdapter(answerPriviewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCheckPopNuber(position);
            }
        });

        order_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onPageSelect(position);
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.moreImg).setOnClickListener(this);
        queClassName.setText(data.getCateName());
        getSubject();

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                collectImgButton.setVisibility(View.VISIBLE);
                framView.setVisibility(View.GONE);
                if (null != deletImgButton) {
                    deletImgButton.setVisibility(View.VISIBLE);
                }
            }
        });

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                collectImgButton.setVisibility(View.GONE);
                framView.setVisibility(View.VISIBLE);
                if (null != deletImgButton) {
                    deletImgButton.setVisibility(View.GONE);
                }
            }
        });
        if (!list.isEmpty()) {
            pageNumber.setText("(" + 1 + "/" + data.getCount() + ")");
            dbVo = list.get(0);
            setCollect();
        }

        baseTipsDialog = new TipsDialogRigister(mcontext, new MyDialogInterface() {//跳转下一类型题目
            @Override
            public void onItemSelect(int postion) {

            }

            @Override
            public void onEnter() {
                jumpToNextClassify();

            }

            @Override
            public void onCancle() {

            }
        });
        pageNumber.setText("(" + 1 + "/" + data.getCount() + ")");
    }


    public void jumpToNextClassify(){
        List<ClassifyExciseVo.ListBean> cache = null;
        try {
            cache = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(ClassifyExciseVo.ListBean.class).orderBy(
                                    "dbid"));
            if (list.size() <= 1) {
                return;
            }

            int j = 0;
            for (int i = 0; i < cache.size(); i++) {
                if (cache.get(i).getCateId().equals(data.getCateId())) {//获取当前分类ID 处于分类list的坐标
                    j = i;
                }

            }
            for (int i = j + 1; i < cache.size(); i++) {
                if (cache.get(i).getCount() > 0) {
                    Intent intent = new Intent(mcontext, StudyKnowledgeActivity.class);
                    intent.putExtra("data", cache.get(i));
                    intent.putExtra("type", type);
                    startActivity(intent);
                    finish();
                    break;
                }

            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public abstract void onCheckPopNuber(int position);

    public abstract void onPageSelect(int position);

    public void setCollect() {
        if (null != dbVo && dbVo.getIsColected() == 1) {
            collectImg.setImageResource(R.drawable.shoucang_select);
        } else {
            collectImg.setImageResource(R.drawable.shouchang_normal);
        }
    }

    public boolean notContainData(int page) {
        return !map.containsKey(page);
    }

    private boolean isCollected(KnowledgeVo.ListBean listBean) {
        return SubjectDbUtils.isAddedById(mcontext, listBean);
    }

    /**
     * fagment里面点击跳转下一题
     */
    TipsDialogRigister baseTipsDialog;

    public void selectNext() {
        if (isOutOfSize()) {
            baseTipsDialog.show();
            baseTipsDialog.setContent("是否进入下一类型题库");
            baseTipsDialog.setButton("进入");
            baseTipsDialog.setCancelable(false);
            return;
        }
        if (isWrongItem()) {
            return;
        }
        if (list.get(itemIndex).getSeqNo() >= data.getCount()) {
            return;
        }

        order_pager.setCurrentItem(itemIndex + 1,true);
        answerPriviewAdapter.notifyDataSetChanged();
    }

    public  boolean isOutOfSize(){
        return itemIndex >= data.getCount() - 1;
    }
    public int getTotalSize() {
        setRightNum();
        answerPriviewAdapter.notifyDataSetChanged();
        return data.getCount();
    }

    public abstract boolean isWrongItem();

    private class CheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_frist:
                    modeState = 0;
                    myadapter.getItem(order_pager.getCurrentItem()).refresh(0);

                    break;
                case R.id.radio_second:
                    modeState = 1;
                    myadapter.getItem(order_pager.getCurrentItem()).refresh(1);
                    //   searchGv.setVisibility(View.GONE);
                    break;

            }


        }

    }

    protected int getLayoutId() {
        return R.layout.activity_study_knowledge;
    }

    public abstract void getSubject();

    public void setJson(JSONObject job) {
        try {
            job.put("cateId", data.getCateId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collectImgButton:
                if (null == dbVo) {
                    listfragment.get(itemIndex).getBean().setIsColected(1);
                    listfragment.get(itemIndex).getBean().setParent_id(parentid);
                    if (SubjectDbUtils.saveData(mcontext, listfragment.get(itemIndex).getBean())) {
                        ToastUtil.showMessage("收藏成功");
                        collectImg.setImageResource(R.drawable.shoucang_select);
                    }
                    dbVo = SubjectDbUtils.getDataById(mcontext, listfragment.get(itemIndex).getBean());
                } else {
                    dbVo.setIsColected(dbVo.getIsColected() == 1 ? 0 : 1);
                    SubjectDbUtils.updateData(mcontext, dbVo);
                    setCollect();
                }

                break;
            case R.id.signInTv:
                finish();
                break;
            case  R.id.framView:
                slidingDrawer.animateClose();
                break;
            default:
                break;
        }

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


    public abstract void setAnswer();

    public void refreshPopAapter(){
        answerPriviewAdapter.notifyDataSetChanged();
    }
    public void refreshAdapter() {
        answerPriviewAdapter.notifyDataSetChanged();
        myadapter.notifyDataSetChanged();
    }

    /**
     * 点击跳转到某个序号对应的题目
     */
    public abstract void setAdapter();

    @Override
    protected void onDestroy() {
        listfragment.clear();
        listfragment = null;
        if (null != baseTipsDialog) {
            baseTipsDialog.dismiss();
        }
        super.onDestroy();
    }

    public int getState() {
        return modeState;
    }

    private class MypagerFragmentAdapter extends FragmentStatePagerAdapter {

        public MypagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public LearningModelFragment getItem(int position) {
            return listfragment.get(position);
        }

        @Override
        public int getCount() {
            return listfragment.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;//不保存tag
        }
    }

    private class AnswerPriviewAdapter extends DYSimpleAdapter<KnowledgeVo.ListBean> {

        public AnswerPriviewAdapter(Context context, List<KnowledgeVo.ListBean> list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        protected void setView(int position, View convertView) {
            TextView itemNum = BaseViewHolder.get(convertView, R.id.itemNum);
            LinearLayout lauout= BaseViewHolder.get(convertView, R.id.lauout);

            itemNum.setText((position + 1) + "");
            if (position <= list.size() - 1) {
                KnowledgeVo.ListBean listBean = list.get(position);
                Log.i("answer", listBean.getChooseAswer() + "-----" + listBean.getRightAswer());
                if (!AbStrUtil.isEmpty(listBean.getChooseAswer())) {
                    lauout.setBackgroundResource(R.drawable.circle_green);
                    itemNum.setTextColor(getResources().getColor(R.color.white));
                    if (listBean.getChooseAswer().equals(listBean.getRightAswer())) {
                        lauout.setEnabled(true);//绿色
                    } else {
                        lauout.setEnabled(false);//红色
                    }
                } else {
                    lauout.setBackgroundResource(R.drawable.circle_line_gray);
                    itemNum.setTextColor(getResources().getColor(R.color.black));
                }
            } else  {
                lauout.setBackgroundResource(R.drawable.circle_line_gray);
                itemNum.setTextColor(getResources().getColor(R.color.black));
            }


            setAdapterCHooseIndex(position, itemNum);
        }


        @Override
        public int getLayoutId() {
            return R.layout.gv_item_choose_adapter;
        }

    }

    public abstract void setAdapterCHooseIndex(int position, TextView itemNum);

}
