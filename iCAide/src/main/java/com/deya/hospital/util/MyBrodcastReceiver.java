package com.deya.hospital.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBrodcastReceiver extends BroadcastReceiver{

	@Override
	
	public void onReceive(Context context, Intent intent) {
		Log.i("111111", intent.getExtras()+"");
	}

}
