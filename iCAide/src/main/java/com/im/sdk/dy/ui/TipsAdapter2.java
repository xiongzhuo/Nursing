package com.im.sdk.dy.ui;

import android.content.Context;
import android.widget.TextView;

import com.deya.hospital.vo.IMTipsVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class TipsAdapter2 extends  TaskTipsAdapter {



    public TipsAdapter2(Context context, List<IMTipsVo> list) {
        super(context, new IMTipsVo());
        this.list=list;
    }

    @Override
    public void setItem(int position, TextView title, TextView typeTv, TextView timeTv) {
        IMTipsVo tipsVo= (IMTipsVo) list.get(position);
        title.setText(tipsVo.getMsgContent());
        typeTv.setText(Strings[tipsVo.getType()]);
        timeTv.setText(tipsVo.getCreate_time());
        typeTv.setBackgroundResource(drawables[tipsVo.getType()]);
    }
}
