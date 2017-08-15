package com.deya.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deya.acaide.R;


public class MyAlarm extends Activity {  
  
    /** 
     * An identifier for this notification unique within your application 
     */  
    public static final int NOTIFICATION_ID=1;   
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         setContentView(R.layout.alarm);  
          
        // create the instance of NotificationManager  
        final NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        
        // create the instance of Notification  
        final Notification n=new Notification();
        /* set the sound of the alarm. There are two way of setting the sound */  
          // n.sound=Uri.parse("file:///sdcard/alarm.mp3");  
       // n.sound=Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "20");
        // Post a notification to be shown in the status bar
        nm.notify(NOTIFICATION_ID, n);  
          
        /* display some information */  
        TextView tv=(TextView)findViewById(R.id.tv);  
        tv.setText("Hello, it's time to bla bla...");
        /* the button by which you can cancel the alarm */
        Button btnCancel=(Button)findViewById(R.id.btn);  
        btnCancel.setOnClickListener(new View.OnClickListener() {  
              
            @Override  
            public void onClick(View arg0) {  
                nm.cancel(NOTIFICATION_ID);  
                finish();  
            }  
        });  
    }

    public PendingIntent getDefalutIntent(int flags){  
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);  
        return pendingIntent;  
    }  
      
    
}  
