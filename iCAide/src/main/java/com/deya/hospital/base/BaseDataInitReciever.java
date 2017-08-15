package com.deya.hospital.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Copyright (C) dymd Technology Co.Ltd.All right reserved.
 *
 * @author : sunp
 * @date ${date}
 */
public class BaseDataInitReciever extends BroadcastReceiver{
    public static final String INIT_ALL_DATA="init_all_data";
    public static final String GET_USER_INFO_SC ="get_user_info_sc";//个人信息获取成功
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("reciever","-----");
        if(intent.getAction().equals(INIT_ALL_DATA)){

        }


    }
}
