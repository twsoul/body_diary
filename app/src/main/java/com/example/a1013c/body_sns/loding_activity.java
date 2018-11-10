package com.example.a1013c.body_sns;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class loding_activity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loding);


        ImageView loding =findViewById(R.id.loding);
        Animation anim = AnimationUtils.loadAnimation
                (getApplicationContext(), // 현재화면 제어권자
                        R.anim.fade_out);      // 에니메이션 설정한 파일
        loding.startAnimation(anim);
        startLoading();

    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 700);
    }
}
