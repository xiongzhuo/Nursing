package com.deya.hospital.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/12/28
 */
public abstract class BaseTableFragment extends BaseFragment {
    public  LayoutInflater inflater;
    private Context mcontext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {

        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container,
                    false);
            this.inflater = inflater;
            mcontext = getActivity();
            tools = new Tools(getActivity(), Constants.AC);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public abstract void initView();

    public abstract int getLayoutId();
    public <T extends View> T findById(int id) {
        return (T) rootView.findViewById(id);
    }
    public <T extends View> T findView(int id) {
        return (T) rootView.findViewById(id);
    }

}


