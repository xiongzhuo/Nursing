package com.deya.hospital.workcircle.knowledge;

import com.deya.hospital.util.WebUrl;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/30
 */
public class DoucmentUitl {
    public static String getShareUrl(String id){
        return WebUrl.WEB_ARTICAL+id+"&article_src=2";

    }

    public static String getShareUrl(String id,String src){
        return WebUrl.WEB_ARTICAL+id+"&article_src="+src;

    }
}
