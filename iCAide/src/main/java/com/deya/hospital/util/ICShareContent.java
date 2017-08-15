package com.deya.hospital.util;

import android.os.Parcel;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.BaseShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.net.utils.SocializeNetUtils;
import com.umeng.socialize.utils.Log;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class ICShareContent extends BaseShareContent {
    public static final Creator<ICShareContent> CREATOR = new Creator() {
        public ICShareContent createFromParcel(Parcel in) {
            return new ICShareContent(in);
        }

        public ICShareContent[] newArray(int size) {
            return new ICShareContent[size];
        }
    };

    public ICShareContent() {
    }

    public ICShareContent(String text) {
        super(text);
    }

    public ICShareContent(UMImage image) {
        super(image);
    }

    public ICShareContent(UMusic music) {
        super(music);
    }

    public ICShareContent(UMVideo video) {
        super(video);
    }

    protected ICShareContent(Parcel in) {
        super(in);
    }

    public void setTargetUrl(String targetUrl) {
        if (!TextUtils.isEmpty(targetUrl) && SocializeNetUtils.startWithHttp(targetUrl)) {
            this.mTargetUrl = targetUrl;
        } else {
            Log.e(this.TAG, "### targetUrl必须以http://或者https://开头");
        }

    }

    public String toString() {
        return super.toString() + "QQShareContent [mTitle=" + this.mTitle + ", mTargetUrl =" + this.mTargetUrl + "]";
    }

    public SHARE_MEDIA getTargetPlatform() {
        return SHARE_MEDIA.FACEBOOK;
    }
}
