package com.deya.hospital.vo;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/11/14
 */
public class ShareVo {
    String shareTitle="";
    String shareContent="";
    String targetUrl="";
    String shareMan="";

    public String getShareMan() {
        return shareMan;
    }

    public void setShareMan(String shareMan) {
        this.shareMan = shareMan;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
