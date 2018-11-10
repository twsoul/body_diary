package com.example.a1013c.body_sns;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import junit.framework.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.a1013c.body_sns.Test_Contract.*;

public class Test_edit extends AppCompatActivity {

    public SQLiteDatabase mDatabase;
    public RecyclerView mRecyclerview;

    private EditText BF;
    private EditText LN;
    private EditText DN;
    String subject ;
    String content ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_rc_add);

        // 제목/본문/날짜 입력
        BF = findViewById(R.id.edit_bf);
        LN = findViewById(R.id.edit_ln);
        DN = findViewById(R.id.edit_dn);

        long now = System.currentTimeMillis();
        Date day = new Date(now);
        SimpleDateFormat a = new SimpleDateFormat("yy-MM-dd");
        String getTime = a.format(day);
        DN.setText(getTime);


        Button save = findViewById(R.id.test_save);

        Test_DBHelper dbHelper = new Test_DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        //저장 버튼
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addItem();
                    finish();
            }
        });


        //수정에서 왔다면 --> 그 컬럼 출력
        if(Test_Main.edit){

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            long id = getIntent().getLongExtra("id",0);
           Cursor cursor = db.rawQuery("SELECT * FROM eatList WHERE _id = " + id, null);

           // 제목, 본문 값 받아오기
            while(cursor.moveToNext()){
                 subject =  cursor.getString(cursor.getColumnIndex(Test_Contract.TestEntry.COLUMN_TITLE));
                content =  cursor.getString(cursor.getColumnIndex(TestEntry.COLUMN_CONTENTS));
            }
            // 받아온 값 세팅!
            BF.setText(subject);
            LN.setText(content);
            Log.d("db",subject);


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = getIntent().getLongExtra("id",0); //아이디 값 받기
                    updateItem(id);

                    Test_Main.edit =false; // 나갈때 수정에서 왔다는 것 꺼주기
                    finish();
                }
            });
            Test_Main.edit =false;
        }

    }

    private void addItem(){
        String bf = BF.getText().toString();
        String ln = LN.getText().toString();
        String dn = DN.getText().toString();

        //데이터 베이스에 저장.
        ContentValues cv = new ContentValues();
        cv.put(TestEntry.COLUMN_TITLE, bf);
        cv.put(TestEntry.COLUMN_CONTENTS, ln);
        cv.put(TestEntry.COLUMN_DATE, dn);


        mDatabase.insert(TestEntry.TABLE_NAME, null, cv);
    }

    private void updateItem(long id){

        String bf = BF.getText().toString();
        String ln = LN.getText().toString();
        String dn = DN.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TestEntry.COLUMN_TITLE, bf);
        cv.put(TestEntry.COLUMN_CONTENTS, ln);
        cv.put(TestEntry.COLUMN_DATE, dn);

        mDatabase.update(TestEntry.TABLE_NAME, cv, TestEntry._ID + "=" + id , null);
    }



    public Cursor getAll(){
        return mDatabase.query(
                TestEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TestEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }



}
