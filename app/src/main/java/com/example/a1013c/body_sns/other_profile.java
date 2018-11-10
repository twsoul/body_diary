package com.example.a1013c.body_sns;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Camera;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class other_profile extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile);

//                Button button = findViewById(R.id.search);//인터넷 검색
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com"));
//
//                        startActivity(i);
//                    }
//                });
//                TextView main_ex1 = findViewById(R.id.main_ex);
//                main_ex1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i1 = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com"));
//                i1.putExtra(SearchManager.QUERY,"헬");
//                startActivity(i1);
//            }
//        });


    }
    @Override
    public void onBackPressed(){ //프래그먼트 메인 다시 시작시키기.
//        Intent i = new Intent(other_profile.this,contents_main.class);
//        startActivity(i);
        finish(); //나오면서 다른사람 프로필은 끄기
    }

    @Override
    public void onResume() {
        Log.e("다른 사람 프로필", "onResume()");
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("다른 사람 프로필", "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("다른 사람 프로필", "onStop()");
    }

}
