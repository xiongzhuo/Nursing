package com.deya.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.deya.acaide.R;

public class NotificationReceiver extends BroadcastReceiver {

    /**
     * called when the BroadcastReceiver is receiving an Intent broadcast.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        /* start another activity - MyAlarm to display the alarm 直接跳出activity*/
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//        intent.setClass(context, MyAlarm.class);  
//        context.startActivity(intent);

        NotificationManager  manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);


        //例如这个id就是你传过来的
        String id = intent.getStringExtra("id");
        id = "0";

        CharSequence contentTitle = "您有新的督导提醒"; // 通知栏标题
        CharSequence contentText = "点击查看我的督导任务"; // 通知栏内容

        Intent broadcastIntent = new Intent(context, AppAstartReciever.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(contentTitle).setContentText(contentText)
                .setTicker("您有新的督导任务提醒")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher);

        // FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
        // FLAG_NO_CLEAR 该通知不能被状态栏的清除按钮给清除掉
        // FLAG_ONGOING_EVENT 通知放置在正在运行
        // FLAG_INSISTENT 是否一直进行，比如音乐一直播放，知道用户响应
        builder.build().flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        builder.build().flags |= Notification.FLAG_SHOW_LIGHTS;
//        builder.setSound(Uri.withAppendedPath(
//                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5"));
        builder.setAutoCancel(true);
        // DEFAULT_ALL 使用所有默认值，比如声音，震动，闪屏等等
        // DEFAULT_LIGHTS 使用默认闪光提示
        // DEFAULT_SOUNDS 使用默认提示声音
        // DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission
        // android:name="android.permission.VIBRATE" />权限
//        builder.build().defaults = Notification.DEFAULT_LIGHTS;
//        // 叠加效果常量
//        builder.build().defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;

        //builder.setContentTitle("title").setContentText("您有新的任务提醒哦!").setSmallIcon(R.drawable.ic_launcher).setDefaults(Notification.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true).setSubText("二级text");
        manager.notify(1,  builder.build());

    }

}
