package com.example.a1013c.body_sns;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Eat extends AppCompatActivity {
    private static String TAG = "recyclerview_example";

    private ArrayList<Eat_Dictionary> mArrayList;
    private Eat_adapter mAdapter;
    private RecyclerView mRecyclerView;
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_main_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

      load_data();
//      mArrayList = new ArrayList<>();   // 데이터 로드 때문에 지워준거


        mAdapter = new Eat_adapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);



        //추가 버튼
        Button buttonInsert = (Button)findViewById(R.id.add_eat);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Eat.this);
                View view = LayoutInflater.from(Eat.this)
                        .inflate(R.layout.eat_edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_id);
                final EditText editTextEnglish = (EditText) view.findViewById(R.id.edittext_dialog_endlish);
                final EditText editTextKorean = (EditText) view.findViewById(R.id.edittext_dialog_korean);

                // 날짜 입력 자동
                final EditText editDate = view.findViewById(R.id.edittext_date);
                editDate.setVisibility(View.INVISIBLE);

                ButtonSubmit.setText("추가");


                //저장(추가) 버튼 눌렀을 때
                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String Breakfast = editTextID.getText().toString();
                        String Lunch = editTextEnglish.getText().toString();
                        String Dinner = editTextKorean.getText().toString();

                        //날짜 입력
                        long now = System.currentTimeMillis();
                        Date day = new Date(now);
                        SimpleDateFormat a = new SimpleDateFormat("yy-MM-dd");
                        String getTime = a.format(day);

                        Eat_Dictionary dict = new Eat_Dictionary(getTime, Breakfast, Lunch, Dinner);

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        //mArrayList.add(dict); //마지막 줄에 삽입
                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영


                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //데이터 저장
        save_data();
    }

    private void save_data(){
        //json 저장
        SharedPreferences pref = getSharedPreferences("Eat",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mArrayList);
        editor.putString("eat_list",json);
        editor.apply();
    }

    private void load_data(){
        SharedPreferences pref = getSharedPreferences("Eat",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("eat_list",null);
        Type type = new TypeToken<ArrayList<Eat_Dictionary>>() {}.getType();
        mArrayList = gson.fromJson(json, type);

        if(mArrayList==null){
            mArrayList = new ArrayList<>();
        }
    }
}
