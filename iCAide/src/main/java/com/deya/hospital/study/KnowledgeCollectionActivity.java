package com.deya.hospital.study;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseClassifyAdapter;
import com.deya.hospital.base.BaseCommonTopActivity;
import com.deya.hospital.util.DataBaseHelper;
import com.deya.hospital.vo.ClassifyExciseVo;
import com.deya.hospital.vo.KnowledgeVo;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/21
 */
public class KnowledgeCollectionActivity extends BaseCommonTopActivity {
    ListView listView;
    ClassificationExerciseAdapter adapter;
    List<ClassifyExciseVo.ListBean> list;
    ClassifyExciseVo classifyExciseVo;
    
    int type;

    @Override
    public String getTopTitle() {
        return "分类练习";
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.paper_list_lay);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    void initdata(){
        list.clear();
        try {
            List<ClassifyExciseVo.ListBean> cache = DataBaseHelper
                    .getDbUtilsInstance(mcontext)
                    .findAll(
                            Selector.from(ClassifyExciseVo.ListBean.class).orderBy(
                                    "dbid"));

            String keys[]={"parent_id",type==1?"is_colected":"isWrong", "1"};

            if (cache != null) {
                list.addAll(cache);
                for (ClassifyExciseVo.ListBean listBean:list){
                    String values[]={listBean.getCateId(),"1"};
                    List<KnowledgeVo.ListBean> items=SubjectDbUtils.getSizelocalDataByKey(mcontext,keys,values);
                    for(KnowledgeVo.ListBean listBean1:items){
                        listBean1.setChooseAswer("");
                        listBean1.setAnswered(false);
                        for (KnowledgeVo.ListBean.ItemsBean itemsBean:listBean1.getItems()){
                            itemsBean.setResult("");
                        }

                    }
                    listBean.setCount(items.size());
                    listBean.setItems(items);
                }

                adapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initBaseData() {



    }

    @Override
    public void initView() {
        type=getIntent().getIntExtra("type",1);
        topView.setTitle(type==1?"我的收藏":"错题回顾");
        listView = (ListView) this.findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new ClassificationExerciseAdapter(mcontext);
        listView.setAdapter(adapter);
        listView.setDividerHeight(1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mcontext, LocalKnowledgePriview.class);
                intent.putExtra("position",position);
                intent.putExtra("type",type);
                intent.putExtra("data",list.get(position));
                intent.putExtra("parentlist", (Serializable) list);
                if(list.get(position).getCount()<=0){
                    ToastUtil.showMessage("该分类没有题目");
                    return;
                }
                startActivity(intent);

            }
        });
    }


    class ClassificationExerciseAdapter extends BaseClassifyAdapter<ClassifyExciseVo.ListBean> {

        public ClassificationExerciseAdapter(Context context) {
            super(context, list);
        }

        @Override
        public void setItem(ViewHolder viewHolder, int position) {
            ClassifyExciseVo.ListBean item = list.get(position);
            viewHolder.name.setText(item.getCateName());
            viewHolder.numTv.setText(item.getCount() + "");

        }
    }
}
