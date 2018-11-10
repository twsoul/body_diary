package com.example.a1013c.body_sns;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Alarm_main extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private Alarm_Notification mNoti;

    public TextView time;
    ToggleButton Mon,Tue,Wed,Thu,Fri,Sat,Sun;
    Switch on_off;

    int time_flag ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_main);

        time_flag = 0; // 타임피커에서 시간 설정 했을 때 바로 알람 세팅 안되게

        mNoti = new Alarm_Notification(this);

        //시간 눌렀을때 --> 타임 피커로 시간 설정..
        time = findViewById(R.id.time_set);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //타임 피커 띄워짐
                Alarm_TimePicker timePicker = new Alarm_TimePicker();
                timePicker.show(getFragmentManager(),"time_picker");
            }
        });

        Mon = findViewById(R.id.Monday);
        Tue = findViewById(R.id.Tuesday);
        Wed = findViewById(R.id.Wednesday);
        Thu = findViewById(R.id.Thursday);
        Fri = findViewById(R.id.Friday);
        Sat = findViewById(R.id.Saturday);
        Sun = findViewById(R.id.Sunday);

        //알람 해제 했을 때 --> 알람 취소
        on_off = findViewById(R.id.on_off_switch);
        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    cancelAlarm();
                    Toast.makeText(Alarm_main.this,"알람이 해제 됐습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //전에 저장 됐던 시간/요일/스위칭 세팅
        SharedPreferences pref1 = getSharedPreferences("alarm", MODE_PRIVATE);

        if(time.getText()== null){
            time.setText("시간 설정");
        }else
            time.setText(pref1.getString("time","시간"));

        Log.d("시간 좀가라 시발로마", String.valueOf(time));
        Mon.setChecked(pref1.getBoolean("월",false));
        Tue.setChecked(pref1.getBoolean("화",false));
        Wed.setChecked(pref1.getBoolean("수",false));
        Thu.setChecked(pref1.getBoolean("목",false));
        Fri.setChecked(pref1.getBoolean("금",false));
        Sat.setChecked(pref1.getBoolean("토",false));
        Sun.setChecked(pref1.getBoolean("일",false));

        on_off.setChecked(pref1.getBoolean("스위치",false));



        // 저장 버튼을 눌렀을때, (플로팅 버튼) --> 쉐어드에 값 저장.
        FloatingActionButton add = findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(Alarm_main.this);
                alt_bld.setMessage("알람 상태를 저장 할까요?").setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                // 요일 반복 인텐트로 보내기
//                                boolean[] week ={false, Mon.isChecked(), Tue.isChecked(), Wed.isChecked(), Thu.isChecked(), Fri.isChecked(), Sat.isChecked(), Sun.isChecked()};

                                Log.d("월", String.valueOf(Mon.isChecked()));
                                Log.d("화", String.valueOf(Tue.isChecked()));
                                Log.d("수", String.valueOf(Wed.isChecked()));
                                Log.d("목", String.valueOf(Thu.isChecked()));
                                Log.d("금", String.valueOf(Fri.isChecked()));
                                Log.d("토", String.valueOf(Sat.isChecked()));
                                Log.d("일", String.valueOf(Sun.isChecked()));

                                // 쉐어드에 값 저장
                                SharedPreferences pref1 = getSharedPreferences("alarm", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref1.edit();
                                editor.putString("time", (String) time.getText());

                                editor.putBoolean("월", Mon.isChecked());
                                editor.putBoolean("화", Tue.isChecked());
                                editor.putBoolean("수", Wed.isChecked());
                                editor.putBoolean("목", Thu.isChecked());
                                editor.putBoolean("금", Fri.isChecked());
                                editor.putBoolean("토", Sat.isChecked());
                                editor.putBoolean("일", Sun.isChecked());

                                editor.putBoolean("스위치", on_off.isChecked());
                                editor.commit();



                                boolean mon = pref1.getBoolean("월",false);
                                boolean tue = pref1.getBoolean("화",false);
                                boolean wed = pref1.getBoolean("수",false);
                                boolean thu = pref1.getBoolean("목",false);
                                boolean fri = pref1.getBoolean("금",false);
                                boolean sat =pref1.getBoolean("토",false);
                                boolean sun = pref1.getBoolean("일",false);

                                // 요일 반복 인텐트로 보내기
                                boolean[] week ={false, sun, mon, tue, wed, thu, fri, sat};

                                //알람 리시버로 팬딩인텐트 보냄
                                Intent i = new Intent(Alarm_main.this,Alarm_Receiver.class);
                                i.putExtra("re_week",week); //요일 반복 보내기

                                Toast.makeText(Alarm_main.this,"알람 저장 완료.",Toast.LENGTH_SHORT).show();
                                startAlarm(calendar);


                            }
                        }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("알람");
                // Icon for AlertDialog
                alert.setIcon(R.drawable.alarm);
                alert.show();





            }
        });




        //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm

        Button a = findViewById(R.id.test1);
        Button b = findViewById(R.id.test2);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder nb = mNoti.getChannel1Notification("몸","사진 찍으셨나요");
//                nb.setSound(ringtoneUri); 소리
                long[] vb ={0,100,200,200};
                nb.setVibrate(vb); // 진동 설정
                mNoti.getmManager().notify(1,nb.build());
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder nb = mNoti.getChannel2Notification("음식","사진 찍으셨나요");
                mNoti.getmManager().notify(2,nb.build());
                cancelAlarm();
            }
        });
        //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm


    }


    public static Calendar calendar = Calendar.getInstance();
    // 타임 피커
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        Calendar calendar = Calendar.getInstance();

        // 타임 피커의 시간을 받아서 텍스트 뷰 셋 시켜주고 그 시간에 알람 맞춰달라고 리시버에 요청.
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);

        Log.d("ontimeset", String.valueOf(hourOfDay));

//        calendar.set(Calendar.DAY_OF_WEEK,1); //월

        updateTimeText(calendar); // 텍스트만 바꿔주기..
        Log.d("캘린더", String.valueOf(calendar));


        // 저장 버튼을 누르면 알람 등록이 되도록 만들어야 함.
        // 저장 버튼 쪽에 flag 찍으면 될 듯.-->생각대로 안됨
        // 쉐어드에 저장 하자 -->스트링으로 변환해서 저장했다가.풀어줄 때 문제가 생길 것 같음.

//        startAlarm(calendar);//알람 등록

    }
    private  void updateTimeText(Calendar c){
        String timeText = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()); // 타임피커에서 받은 시간형식을 스트링에 저장


        time.setText(timeText); // 알람 설정한 시간 텍스트 뷰로 보여주기.
    }


    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        SharedPreferences pref1 = getSharedPreferences("alarm", MODE_PRIVATE);

        boolean mon = pref1.getBoolean("월",false);
        boolean tue = pref1.getBoolean("화",false);
        boolean wed = pref1.getBoolean("수",false);
        boolean thu = pref1.getBoolean("목",false);
        boolean fri = pref1.getBoolean("금",false);
        boolean sat = pref1.getBoolean("토",false);
        boolean sun = pref1.getBoolean("일",false);

        // 요일 반복 인텐트로 보내기
        boolean[] week ={false, sun, mon, tue, wed, thu, fri, sat};

        //알람 리시버로 팬딩인텐트 보냄
        Intent i = new Intent(Alarm_main.this,Alarm_Receiver.class);
        i.putExtra("re_week",week); //요일 반복 보내기

        Log.i("요일 week", Arrays.toString(week));

        //시발 팬딩인텐트 시발  flag_update current
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,i,PendingIntent.FLAG_UPDATE_CURRENT);

        //알람 매니져 세팅
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.d("알람 시작 시간", String.valueOf(c.getTimeInMillis()));

    }

    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this,Alarm_Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,i,0);

        alarmManager.cancel(pendingIntent);
    }



}

