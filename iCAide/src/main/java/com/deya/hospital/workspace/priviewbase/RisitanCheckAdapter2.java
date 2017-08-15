package com.deya.hospital.workspace.priviewbase;

import android.content.Context;

import com.deya.acaide.R;
import com.deya.hospital.vo.RisistantVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class RisitanCheckAdapter2 extends RisitanCheckAdapter {


    public RisitanCheckAdapter2(Context context, List<RisistantVo.TempListBean.ItemListBean.ChildrenBean> list, boolean isSingleCheck) {
        super(context, list, isSingleCheck);
    }


    @Override
    public int getLayout() {
        return R.layout.item_check_layout2;
    }
}
