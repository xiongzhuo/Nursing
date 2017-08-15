package com.deya.hospital.bizimp;

import org.json.JSONObject;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public interface RequestInterface {
    public void onRequestSucesss(int code, JSONObject jsonObject);

    public void onRequestErro(String message);

    public void onRequestFail(int code);

}
