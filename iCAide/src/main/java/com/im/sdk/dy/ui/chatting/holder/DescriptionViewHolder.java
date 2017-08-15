/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.im.sdk.dy.ui.chatting.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deya.acaide.R;
import com.im.sdk.dy.common.base.CCPTextView;
import com.im.sdk.dy.ui.chatting.base.EmojiconTextView;


/**
 * @author 容联•云通讯
 * @version 4.0
 * @date 2014-12-9
 */
public class DescriptionViewHolder extends BaseHolder {

    public View chattingContent;
    private int contactType;
    RelativeLayout shareLayout;
    /**
     * TextView that display IMessage description.
     */
    private EmojiconTextView descTextView;

    /**
     * @param type
     */
    public DescriptionViewHolder(int type) {
        super(type);

    }

    public DescriptionViewHolder(int type, int contactType) {
        super(type);
        this.contactType = contactType;

    }

    public BaseHolder initBaseHolder(View baseView, boolean receive) {
        super.initBaseHolder(baseView);


        chattingTime = (TextView) baseView.findViewById(R.id.chatting_time_tv);
        chattingUser = (TextView) baseView.findViewById(R.id.chatting_user_tv);
        descTextView = (EmojiconTextView) baseView.findViewById(R.id.chatting_content_itv);
        checkBox = (CheckBox) baseView.findViewById(R.id.chatting_checkbox);
        chattingMaskView = baseView.findViewById(R.id.chatting_maskview);
        chattingContent = baseView.findViewById(R.id.chatting_content_area);
        layout1 = (LinearLayout) baseView.findViewById(R.id.layout1);
        layout2 = (RelativeLayout) baseView.findViewById(R.id.shareLay_chatting);
        titleTv = (TextView) baseView.findViewById(R.id.title);
        typeTv = (TextView) baseView.findViewById(R.id.typeTv);
        finishtime = (TextView) baseView.findViewById(R.id.finishtime);
        if (receive) {
            type = 7;
            return this;
        }

        uploadState = (ImageView) baseView.findViewById(R.id.chatting_state_iv);
        progressBar = (ProgressBar) baseView.findViewById(R.id.uploading_pb);
        type = 8;
        return this;
    }

    public void setShareRow(){
        getShareView().setVisibility(View.VISIBLE);
        getTextLayout().setVisibility(View.GONE);

    }
    public void setnormalRow(){
        getShareView().setVisibility(View.GONE);
        getTextLayout().setVisibility(View.VISIBLE);

    }
    /**
     * {@link CCPTextView} Display imessage text
     *
     * @return
     */
    public EmojiconTextView getDescTextView() {
        if (descTextView == null) {
            descTextView = (EmojiconTextView) getBaseView().findViewById(R.id.chatting_content_itv);
        }
        return descTextView;
    }

    public RelativeLayout getShareView(){
        if (shareLayout == null) {
            shareLayout = (RelativeLayout) getBaseView().findViewById(R.id.shareLay_chatting);
        }
        return shareLayout;

    }
    public TextView getFinishTimeTextView() {
        if (finishtime == null) {
            finishtime = (TextView) getBaseView().findViewById(R.id.finishtime);
        }
        return finishtime;
    }

    public TextView getTipTitleTextView() {
        if (titleTv == null) {
            titleTv = (TextView) getBaseView().findViewById(R.id.title);
        }
        return titleTv;
    }
    public TextView getTipTypesTextView() {
        if (typeTv == null) {
            typeTv = (TextView) getBaseView().findViewById(R.id.typeTv);
        }
        return typeTv;
    }
   public LinearLayout getTextLayout(){
        if (layout1 == null) {
            layout1 = (LinearLayout) getBaseView().findViewById(R.id.layout1);
        }
        return layout1;
    }
    public RelativeLayout getClickLayout(){
        if (layout2 == null) {
            layout2 = (RelativeLayout) getBaseView().findViewById(R.id.shareLay_chatting);
        }
        return layout2;
    }
    /**
     * @return
     */
    public ImageView getChattingState() {
        if (uploadState == null) {
            uploadState = (ImageView) getBaseView().findViewById(R.id.chatting_state_iv);
        }
        return uploadState;
    }

    /**
     * @return
     */
    public ProgressBar getUploadProgressBar() {
        if (progressBar == null) {
            progressBar = (ProgressBar) getBaseView().findViewById(R.id.uploading_pb);
        }
        return progressBar;
    }

}
