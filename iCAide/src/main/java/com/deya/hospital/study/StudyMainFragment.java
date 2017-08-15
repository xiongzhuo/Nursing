package com.deya.hospital.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseFragment;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.HomePageBanner;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebViewDemo;
import com.deya.hospital.vo.AdvEntity;
import com.deya.hospital.vo.TaskTypePopVo;
import com.deya.hospital.workcircle.fragment.WorkCiecleChannelActivity;
import com.deya.hospital.workspace.TaskUtils;
import com.flyco.banner.widget.Banner.base.BaseBanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/18
 */
public class StudyMainFragment extends BaseFragment implements RequestInterface {
    private static final int GET_SUB_COUNT =0x811 ;
    GridView gridview;
    StudyMainAdapter adapter;
    List<TaskTypePopVo> list;
    String title[] = {"分类练习", "知识评测", "培训考核", "实战分享", "错题回顾", "我的收藏"};
    int imgId[] = {R.drawable.study_1, R.drawable.study_2, R.drawable.study_3, R.drawable.study_4, R.drawable.study_5, R.drawable.study_6};

    Class[] activitys = {ClassificationExerciseActivity.class,ExaminationPaperActivity.class,ExaminationPaperActivity.class,
            WorkCiecleChannelActivity.class,KnowledgeCollectionActivity.class,
            KnowledgeCollectionActivity.class};
    private HomePageBanner viewPager;
   static final int CAROUSELFIGURE=0x810;
    Tools tools;
    public static final String FIRST_NUM="first_num";
    public static final String SECOND_NUM="second_num";
    public static final String THIRD_NUM="third_num";
    public static final String FOUTH_NUM="fouth_num";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.study_main_fragment, container,
                    false);
            tools=new Tools(getActivity(), Constants.AC);
            initView();

        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }
    public void getCarouselFigure() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("module_id", "4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,getActivity(),
                CAROUSELFIGURE, job, "comm/getCarouselFigure");

    }

    public void getMainSubCount() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("hospitalId", tools.getValue(Constants.HOSPITAL_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomReq(this,getActivity(),
                GET_SUB_COUNT, job, "subject/getShowTotal");

    }
    private void initView() {
        list = new ArrayList<>();
        viewPager = (HomePageBanner) findViewById(R.id.viewPager);
        adapter = new StudyMainAdapter(getActivity(), list);
        gridview = (GridView) this.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( activitys[position]!=null) {
                    Intent intent = new Intent(getActivity(), activitys[position]);
                    if(position==4||position==2){
                        intent.putExtra("type",2);
                    }else if(position==3){
                        intent.putExtra("channelID", 6);
                        intent.putExtra("channelName", "公开课");
                    }else{
                        intent.putExtra("type",1);
                    }
                    startActivity(intent);
                }
            }
        });
        setAdvPic();
        getCarouselFigure();
    }
    private List<AdvEntity.ListBean> pagerList = new ArrayList<>();
    private void setAdvPic() {
        if (!(pagerList.size() > 0)) {
            AdvEntity.ListBean advEntity1 = new AdvEntity.ListBean();
            advEntity1.setDrawable(R.drawable.knowlege_banner1);
            advEntity1.setType(1);
            advEntity1.setName("");
            pagerList.add(advEntity1);
        }
        setHomeBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        getMainSubCount();
    }

    void initData(){
        list.clear();
        for (int i = 0; i < title.length; i++) {
            TaskTypePopVo vo = new TaskTypePopVo();
            vo.setImgid(imgId[i]);
            vo.setName(title[i]);
            switch (i){
                case 0:
                    vo.setCount(tools.getValue_int(FIRST_NUM));
                    break;
                case 1:
                    vo.setCount(tools.getValue_int(SECOND_NUM));
                    break;
                case 2:
                    vo.setCount(tools.getValue_int(THIRD_NUM));
                    break;
                case 3:
                    vo.setCount(tools.getValue_int(FOUTH_NUM));
                    break;
                case 4:
                    vo.setCount(SubjectDbUtils.getlocalDataByKey(getActivity(),"isWrong","1").size());
                    break;

                case 5:
                    vo.setCount(SubjectDbUtils.getlocalDataByKey(getActivity(),"is_colected","1").size());
                    break;
            }
            list.add(vo);

        }
        adapter.notifyDataSetChanged();
    }
    /**
     * setHomeBanner:【填充广告的数据】. <br/>
     */
    private void setHomeBanner() {
        if (1 == pagerList.size()) {
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).startScroll(); // 只有一张广告设置不能滑动可以点击
        } else {// 否则执行自动滚动并设置为可以手动滑动也可以点击
            viewPager.setDelay(3).setPeriod(3).setAutoScrollEnable(true)
                    .setTouchAble(true).setSource(pagerList).startScroll();
        }

        viewPager.setOnItemClickL(new BaseBanner.OnItemClickL() {

            @Override
            public void onItemClick(int position) {
                if (pagerList != null && pagerList.size() > 0) {
                    int type = pagerList.get(position).getType();
                    switch (type) {
                        case 0:
                            Intent intent = new Intent(getActivity(), WebViewDemo.class);
                            intent.putExtra("url", pagerList.get(position).getHref_url());
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {
        switch (code){
            case CAROUSELFIGURE:
                try {
                    AdvEntity entity = TaskUtils.gson.fromJson(jsonObject.toString(), AdvEntity.class);
                    if (entity != null) {
                        if (entity.getResult_id().equals("0")) {
                            setRequestData(entity);
                        } else {
                            ToastUtils.showToast(getActivity(), entity.getResult_msg());
                        }
                    } else {
                        ToastUtils.showToast(getActivity(), "亲，您的网络不顺畅哦！");
                    }
                } catch (Exception e5) {
                    e5.printStackTrace();
                } finally {
                    setAdvPic();
                }
                break;
            case GET_SUB_COUNT:
                if(jsonObject.has("publicSubjectCount")){//题库数
                    tools.putValue(FIRST_NUM,jsonObject.optInt("publicSubjectCount"));
                    list.get(0).setCount(jsonObject.optInt("publicSubjectCount"));
                }
                if(jsonObject.has("publicTestCount")){//试卷数
                    list.get(1).setCount(jsonObject.optInt("publicTestCount"));
                    tools.putValue(SECOND_NUM,jsonObject.optInt("publicTestCount"));
                }
                if(jsonObject.has("hospTestCount")){//本院
                    list.get(2).setCount(jsonObject.optInt("hospTestCount"));
                    tools.putValue(THIRD_NUM,jsonObject.optInt("hospTestCount"));
                }
                if(jsonObject.has("hospSubjectCount")){//本院
                    list.get(3).setCount(jsonObject.optInt("hospSubjectCount"));
                    tools.putValue(FOUTH_NUM,jsonObject.optInt("hospSubjectCount"));
                }
                adapter.notifyDataSetChanged();
                break;

        }

    }
    private void setRequestData(AdvEntity entity) {
        if (entity.getList() != null && entity.getList().size() > 0) {
            viewPager.setDelay(0).setPeriod(0).setAutoScrollEnable(false)
                    .setTouchAble(false).setSource(pagerList).pauseScroll();
            pagerList.clear();
            pagerList.addAll(entity.getList());
        }
    }
    @Override
    public void onRequestErro(String message) {

    }

    @Override
    public void onRequestFail(int code) {

    }

  class   StudyMainAdapter extends DYSimpleAdapter<TaskTypePopVo> {

      public StudyMainAdapter(Context context, List<TaskTypePopVo> list) {
          super(context, list);
      }

      @Override
      protected void setView(int position, View convertView) {
          ImageView icon = BaseViewHolder.get(convertView, R.id.itemIcon);
          TextView textView = BaseViewHolder.get(convertView, R.id.itemTxt);
          TextView itemNum = BaseViewHolder.get(convertView, R.id.itemNum);
          TaskTypePopVo hotVo = list.get(position);
          icon.setImageResource(hotVo.getImgid());
          textView.setText(hotVo.getName());
          itemNum.setText("("+hotVo.getCount()+")");
      }

      @Override
      public int getLayoutId() {
          return R.layout.layout_study_main;
      }
  }
}
