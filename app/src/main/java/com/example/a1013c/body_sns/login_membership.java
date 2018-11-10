package com.example.a1013c.body_sns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login_membership extends AppCompatActivity {

    private EditText user_id;
    private EditText user_pw;
    private EditText user_pw_ag;
    private EditText nickname;
    private EditText mainexercise;
    private EditText career;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_the_body_membership);


            user_id = findViewById(R.id.user_id); // 현재 패스워드 입력란
            user_pw = findViewById(R.id.user_pw); // 패스워드 입력란
            user_pw_ag = findViewById(R.id.user_pw_agree); // 패스워드 확인 입력란



        Button cancel = findViewById(R.id.cancel); //취소 버튼
        Button join = findViewById(R.id.join); //저장 버튼

        //비밀번호 일치 검사
        user_pw_ag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            //틀리면 색변화.
            @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String ed_pw = user_pw.getText().toString();
                        String ed_pw1= user_pw_ag.getText().toString();

                        if(ed_pw.equals(ed_pw1)){
                            TextView  a =findViewById(R.id.ox);
                            a.setText("비밀 번호가 일치 합니다.");
                            a.setBackgroundColor(Color.GREEN);
                        }else{
                            TextView  a =findViewById(R.id.ox);
                            a.setText("비밀 번호가 일치 하지 않습니다.");
                            a.setBackgroundColor(Color.RED);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });



//     저장 버튼 눌렀을 때 동작
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id.getText().toString().length() ==0){
                    Toast.makeText(login_membership.this, "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    user_id.requestFocus();
                    return;
                }if(user_pw.getText().toString().length() ==0){
                    Toast.makeText(login_membership.this, "PW를 입력하세요", Toast.LENGTH_SHORT).show();
                    user_pw.requestFocus();
                    return;
                }if(user_pw_ag.getText().toString().length()==0){
                    Toast.makeText(login_membership.this, "PW 확인을 입력하세요", Toast.LENGTH_SHORT).show();
                    user_pw_ag.requestFocus();
                    return;
                }if(!user_pw.getText().toString().equals(user_pw_ag.getText().toString())){
                    Toast.makeText(login_membership.this, "PW가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    user_pw.setText("");
                    user_pw_ag.setText("");
                    user_pw.requestFocus();
                    return;
                }

                // ID 값 넘기기
                Intent result = new Intent();
                result.putExtra("ID",user_id.getText().toString());
//                result.putExtra("NICK",nickname.getText().toString()); //닉네임
                setResult(RESULT_OK,result);
//                Toast.makeText(getApplicationContext(),"가입 완료",Toast.LENGTH_SHORT).show();
                savePreferences();

                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("flag", "1");
                editor.commit();

                finish(); //회원가입 종료.
            }
        });

//     취소 버튼 눌렀을 때 동작
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"취소 됐습니다.",Toast.LENGTH_SHORT).show();
//취소 되면 flag 2
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("flag", "2");
                editor.commit();

                finish(); //회원 가입 끝내기

            }
        });


    }

    private void savePreferences(){ // 닉네임 값 저장하기
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

//        editor.putString("id")
        editor.putString("nick", nickname.getText().toString());
        editor.putString("main", mainexercise.getText().toString());
        editor.putString("career", career.getText().toString());

        editor.commit();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }
}
