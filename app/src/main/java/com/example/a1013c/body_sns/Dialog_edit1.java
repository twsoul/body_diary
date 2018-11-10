package com.example.a1013c.body_sns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Dialog_edit1 extends AppCompatActivity {

    public static int edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);

        // 몸사진, 메모 받아오기
        final String uri = getIntent().getStringExtra("img_st");
        final String memo_tx = getIntent().getStringExtra("memo");
        final String date = getIntent().getStringExtra("day");
        final int pos = getIntent().getIntExtra("pos",0);

        ImageView img = findViewById(R.id.big_img);
        final EditText memo =findViewById(R.id.edit_text);

        memo.setText(memo_tx);
        img.setImageURI(Uri.parse(uri));

        //수정 버튼 눌렀을 때 --> 다시 저장
        Button save = findViewById(R.id.edit_Button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=1;
                Intent i = new Intent(Dialog_edit1.this,Navigation_activity.class);
                i.putExtra("uri_edit",uri);
                i.putExtra("memo_edit",memo.getText().toString());
                i.putExtra("date_edit",date);
                i.putExtra("pos_edit",pos);
                startActivity(i);


                finish();
            }
        });
    }
}
