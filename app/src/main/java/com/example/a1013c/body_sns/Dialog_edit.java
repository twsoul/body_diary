package com.example.a1013c.body_sns;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Dialog_edit extends Dialog {


        private ImageView big_img;
        private EditText edit_text;
        private Context context;
        private Button save_button;

    ArrayList<rc_content_1> body_ArrayList = new ArrayList<>();


    public Dialog_edit(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //다이얼로그 제목창x
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_edit);

        big_img =findViewById(R.id.big_img);
        edit_text =findViewById(R.id.edit_text);
        save_button=findViewById(R.id.edit_Button);



        //이미지 셋
//        big_img.setImageURI();

//        Intent i = new Intent();
////        i.getStringExtra("sub");
//        big_img.setImageURI(Uri.parse(i.getStringExtra("sub")));

//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        edit_text.setText(pref.getString("t",""));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
