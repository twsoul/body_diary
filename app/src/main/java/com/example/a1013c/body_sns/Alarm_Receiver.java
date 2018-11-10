package com.example.a1013c.body_sns;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;

public class Alarm_Receiver extends BroadcastReceiver{

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {


        // 요일 반복 인텐트로 보내기
        boolean[] week = intent.getBooleanArrayExtra("re_week");
        Log.i("리시버 요일 week", Arrays.toString(week));

        Log.i("AlarmReceiver.java | onReceive", "|일 : " + week[Calendar.SUNDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|월 : " + week[Calendar.MONDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|화 : " + week[Calendar.TUESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|수 : " + week[Calendar.WEDNESDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|목 : " + week[Calendar.THURSDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|금 : " + week[Calendar.FRIDAY]);
        Log.i("AlarmReceiver.java | onReceive", "|토 : " + week[Calendar.SATURDAY]);

        Calendar c = Calendar.getInstance();
        if (!week[c.get(Calendar.DAY_OF_WEEK)]){
            return;}

        Alarm_Notification notificationHelper = new Alarm_Notification(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("Body_Diary","사진 찍으셨나요?");
        long[] vb ={0,100,200,200};
        nb.setVibrate(vb); // 진동 설정

        notificationHelper.getmManager().notify(1,nb.build());
    }

}
