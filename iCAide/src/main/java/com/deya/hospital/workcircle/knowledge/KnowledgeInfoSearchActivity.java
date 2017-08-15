package com.deya.hospital.workcircle.knowledge;

import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date 2016/9/29
 */

public abstract class KnowledgeInfoSearchActivity extends BaseActivity {

    public static final int KNOWLEDGEINFO_SUCESS=0x20036;
    public static final int KNOWLEDGEINFO_FAIL=0x20037;

    public void getKnowledgeInfo(MyHandler myHandler, String id, String article_src){
        JSONObject job=new JSONObject();
        try {
            job.put("authent",tools.getValue(Constants.AUTHENT));
            job.put("id",id);
            job.put("article_src",article_src);
            MainBizImpl.getInstance().onCirclModeRequest(myHandler, this, KNOWLEDGEINFO_SUCESS, KNOWLEDGEINFO_FAIL, job, "subject/test/testDetail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
