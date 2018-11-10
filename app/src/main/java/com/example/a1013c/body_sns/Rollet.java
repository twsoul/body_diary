package com.example.a1013c.body_sns;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a1013c.body_sns.Board.board_main;


public class Rollet extends AppCompatActivity{

    private ImageView wheel; // 휠 이미지
    private ImageView info; //도움말

    private float init_angle = 0.0f; //초기 각도


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rollet);

        wheel = findViewById(R.id.wheel);
        info = findViewById(R.id.info);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info();
            }
        });

        //휠 돌리기
        wheel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onWheel();
            }
        });
    }
    private int getRandom(int max){
        return (int)(Math.random()*max);
    }

    private void onWheel(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float fromAngle = getRandom(360) + 720 +init_angle; // 2바퀴 + a
//                (Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                RotateAnimation rAnimate = new RotateAnimation(init_angle, fromAngle, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                //매번 초기각도를 현재 각도로 초기화
                init_angle = fromAngle;

                rAnimate.setDuration(3000); // 지속

                // 종료된 후 상태를 고정해주는 옵션
                rAnimate.setFillEnabled(true);
                rAnimate.setFillAfter(true);

                wheel.startAnimation(rAnimate);
            }
        });
    }

    private void Info(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Rollet.this);
        final View view = LayoutInflater.from(Rollet.this)
                .inflate(R.layout.rollet_info, null, false);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
