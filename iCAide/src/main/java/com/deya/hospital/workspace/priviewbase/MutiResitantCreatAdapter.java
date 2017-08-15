package com.deya.hospital.workspace.priviewbase;

import android.content.Context;
import android.view.View;

import com.deya.acaide.R;
import com.deya.hospital.adapter.BaseTaskAadpter;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.vo.CaseListVo;

import java.util.List;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class MutiResitantCreatAdapter extends BaseTaskAadpter<CaseListVo.CaseListBean> {
    private int statusImg[] = {R.drawable.unfinish, R.drawable.finish_img, R.drawable.unfinish};

    public MutiResitantCreatAdapter(Context context, List<CaseListVo.CaseListBean> list) {
        super(context, list);
        this.list = list;
    }

    @Override
    public void setItem(int position, TaskAdapterViewHolder mviewHolder) {
        CaseListVo.CaseListBean rt = list.get(position);
        mviewHolder.typeTv.setVisibility(View.GONE);

        if (null != rt) {
            String string = rt.getDepartmentName();
            string += AbStrUtil.isEmpty(rt.getBed_no()) ? "" : "   " + rt.getBed_no() + "床";
            string += AbStrUtil.isEmpty(rt.getPatient_name()) ? "" : "   " + rt.getPatient_name();
            mviewHolder.title.setText(string);
            mviewHolder.finishtime.setText(rt.getTime());
            mviewHolder.stateImg.setImageResource(statusImg[rt.getStatus()]);
            mviewHolder.stateTv.setText(rt.getStatus() == 1 ? "已完成" : "未完成");
        } else {
            mviewHolder.title.setText("");
            mviewHolder.finishtime.setText("");
            mviewHolder.stateImg.setImageResource(statusImg[0]);
            mviewHolder.stateTv.setText("未完成");
        }
    }
}

