package com.deya.hospital.baseui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.util.CommonTopView;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ListViewWithButtonActivity extends BaseActivity {
   public ListView listView;
    public LinearLayout sumbmitlay;
    Button button,leftButton;
    private CommonTopView topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lisview_bottom_button);
        topView = (CommonTopView) this.findViewById(R.id.topView);
        topView.init(this);
        listView= (ListView) this.findViewById(R.id.listView);
        sumbmitlay=(LinearLayout)this.findViewById(R.id.sumbmitlay);
        button=(Button) this.findViewById(R.id.sumbmitBtn);
        leftButton= (Button) this.findViewById(R.id.shareBtn);
    }


    public ListView getListView(){
        return listView;
    }
    public LinearLayout getSumbmitLay(){
        return sumbmitlay;
    }

    public Button getButton(){
        return  (Button) findViewById(R.id.sumbmitBtn);
    }
    public Button getLeftButton(){
        return  leftButton;
    }
}
