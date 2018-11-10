package com.example.a1013c.body_sns;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class Alarm_Notification extends ContextWrapper{
    public static final String channel_id_1 = "body_channel_id";
    public static final String channel_name_1 ="body_img";
    public static final String channel_id_2 = "eat_channel_id";
    public static final String channel_name_2 ="eat_img";

    private  NotificationManager mManager;

    public Alarm_Notification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        createChannel();
        }
    }

    public NotificationManager getmManager() {
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    // 몸 갤러리
    public NotificationCompat.Builder getChannel1Notification(String title,String message){
        // 노티 클릭했을때,
        Intent i = new Intent(this,login_body.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        return new NotificationCompat.Builder(getApplicationContext(),channel_id_1)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_wanna)
                .setContentIntent(pi)
                .setAutoCancel(true);


    }
    // 음식 갤러리
    public NotificationCompat.Builder getChannel2Notification(String title,String message){
        return new NotificationCompat.Builder(getApplicationContext(),channel_id_1)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.eat);
    }


    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel(){
        // 채널 1 --> 몸 갤러리
        NotificationChannel body_img = new NotificationChannel(channel_id_1, channel_name_1, NotificationManager.IMPORTANCE_DEFAULT);
        body_img.enableLights(true);
        body_img.enableVibration(true); //진동 오도록
        body_img.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); // 잠금화면에서 보이도록

        getmManager().createNotificationChannel(body_img);

        // 채널 2 --> 음식 갤러리
        NotificationChannel eat_img = new NotificationChannel(channel_id_2, channel_name_2, NotificationManager.IMPORTANCE_DEFAULT);
        eat_img.enableLights(true);
        eat_img.enableVibration(true); //진동 오도록
        eat_img.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE); // 잠금화면에서 보이도록

        getmManager().createNotificationChannel(eat_img);
    }

}
