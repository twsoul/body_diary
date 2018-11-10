package com.example.a1013c.body_sns.Board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1013c.body_sns.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class board_main extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private board_adapter mAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;

    private  int nick_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_main);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads"); // "uploads" 키를 가진 것들 받아오기

        //처음 설정할 때 들어오게 만들기
        //한번이라도 저장 --> 다이얼로그 생성 x
        final SharedPreferences pref = getSharedPreferences("gellery", Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("choice_nick",true);
//        editor.apply();

        if(pref.getBoolean("choice_nick",true)){ // 닉네임 설정  --> x

            //처음 들어 왔을 때, 닉네임 설정.. //중복 확인
            AlertDialog.Builder builder = new AlertDialog.Builder(board_main.this);
            final View view = LayoutInflater.from(board_main.this)
                    .inflate(R.layout.activity_nickname_dialog, null, false);
            builder.setView(view);
            builder.setCancelable(false);

            final EditText nickname = view.findViewById(R.id.Nick_Name);
            Button confirm_overlap = view.findViewById(R.id.confirm_overlap);
            final Button save = view.findViewById(R.id.nick_save);

            final AlertDialog dialog = builder.create();

            //중복확인 버튼 /
            confirm_overlap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                Log.i("파이어", child.next().child("nickname").getValue());
                                Log.i("파이어 nick", nickname.getText().toString());
                                if (nickname.getText().toString().equals(dataSnapshot1.child("nickname").getValue())) {
                                    Toast.makeText(getApplicationContext(), "중복된 닉네임", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            Toast.makeText(getApplicationContext(),"사용 가능한 닉네임",Toast.LENGTH_LONG).show();
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });

            // 저장(가입) 버튼 / 눌렀을때, 쉐어드에 닉네임 저장 --> 중복 확인 체크안했으면 리턴 & 하나도 안눌렀을 때도 리턴
            save.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ShowToast")
                @Override
                public void onClick(View v) {
                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                Log.i("파이어", child.next().child("nickname").getValue());
                                Log.i("파이어 nick", nickname.getText().toString());
                                if (nickname.getText().toString().equals(dataSnapshot1.child("nickname").getValue())) {
                                    Toast.makeText(getApplicationContext(), "중복된 닉네임은 저장이 안됩니다.", Toast.LENGTH_LONG).show();
                                    nick_flag = 1; // 1 이면 통과 x
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("choice_nick",true);
                                    editor.apply();
                                    finish(); // 중복된 닉네임 저장하려고 하면 꺼버리기.
                                    Log.i("nm",pref.getString("gell_nick",null));

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    if(nickname.getText().toString().trim().equals("")){ //닉네임을 아무 것도 안적었을때.
                        Toast.makeText(board_main.this,"닉네임을 설정하세요",Toast.LENGTH_SHORT);
                        return;
                    }
                    if(nick_flag ==1){
                       return;
                    }
                    if(nick_flag==0) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("gell_nick", nickname.getText().toString()); //닉네임 저장
                        editor.putBoolean("choice_nick", false); // 저장했을 때 닉네임 설정 안뜨 도록 처리.
                        editor.apply();

                        dialog.dismiss();
                    }


                }
            });


            //취소 버튼
//            dialog.setCanceledOnTouchOutside(false);

            dialog.show();// 다이얼로그 보여줌

        }
//        else{ //닉네임 설정 --> o
//
//        }


        //mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm

        mRecyclerView = findViewById(R.id.board_rc);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploads = new ArrayList<>();
        mAdapter = new board_adapter(board_main.this,mUploads);
        mRecyclerView.setAdapter(mAdapter);



        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUploads.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(0,upload);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(board_main.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton add = findViewById(R.id.add_contents);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(board_main.this,board_add.class);
                startActivity(i);
//                finish();
            }
        });
    }


}
