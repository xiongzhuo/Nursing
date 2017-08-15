package com.deya.hospital.setting;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.deya.acaide.R;
import com.deya.hospital.application.MyAppliaction;
import com.deya.hospital.base.BaseTableFragment;
import com.deya.hospital.base.BaseViewHolder;
import com.deya.hospital.base.DYSimpleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2017/6/14
 */
public class MyAcToolsFragment extends BaseTableFragment {
    GridView gv;
    ModuAdapter moduAdapter;
    String modueStr[] = {"手卫生", "环境清洁","医废管理","职业防护","临床质控", "督查反馈", "学习考试", "感染会诊" };
    int modueImg[] = {R.drawable.me_tool_hw,   R.drawable.me_tool_envir,R.drawable.me_tool_wast,R.drawable.me_tool_zyfh,
            R.drawable.me_tool_quality,R.drawable.me_tool_ddb,R.drawable.me_tool_study,
            R.drawable.me_tool_huizhen
    };
    JSONObject toolState;
    String keys[] = {"hw", "envior","wast" ,"zyfh","quality","dudao", "kaohe",  "hz"  };
    @Override
    public void initView() {
        gv=findView(R.id.gv);
        moduAdapter=new ModuAdapter(getActivity(), Arrays.asList(modueStr));
        gv.setAdapter(moduAdapter);
        toolState=MyAppliaction.getToolsState();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    String key=keys[position];
                    Boolean check=toolState.optBoolean(key);
                    check=!check;
                    toolState.put(key,check);
                    MyAppliaction.saveToolsState();
                    ImageView img= (ImageView) view.findViewById(R.id.switchImg);
                    img.setImageResource(check?R.drawable.me_open:R.drawable.me_close);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_user_tools;
    }

    private  class ModuAdapter extends DYSimpleAdapter {

        public ModuAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        protected void setView(int position, View convertView) {
            ImageView img = BaseViewHolder.get(convertView, R.id.img);
            ImageView img2= BaseViewHolder.get(convertView,R.id.switchImg);
            TextView txt = BaseViewHolder.get(convertView, R.id.txt);
            String str = (String) list.get(position);
            txt.setText(str);
          String key=keys[position];;
            try {

                img2.setImageResource(toolState.getBoolean(key)?R.drawable.me_open:R.drawable.me_close);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            img.setImageResource(modueImg[position]);
        }


        @Override
        public int getLayoutId() {
            return R.layout.my_tools_setting_adapter;
        }
    }
}
