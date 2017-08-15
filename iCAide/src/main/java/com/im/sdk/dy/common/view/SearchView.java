package com.im.sdk.dy.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.deya.acaide.R;

public class SearchView extends LinearLayout {
    private Context mContext ;

    /**
     * @param context
     */
    public SearchView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search, this, true);
    }

}
