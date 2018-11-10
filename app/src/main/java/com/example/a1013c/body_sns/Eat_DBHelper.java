package com.example.a1013c.body_sns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Eat_DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="eat_list.db";
    public static final int DATABASE_VERSION =1;

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public Eat_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EAT_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, breakfast TEXT, lunch TEXT, dinner TEXT, date TEXT);");
//        getResult();
    }

    //추가    아침 점심 저녁 날짜
    public void insert(String breakfast, String lunch, String dinner, String date) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + breakfast + "', '" + lunch + "', '" + dinner + "', '" + date + "');");
        db.close();
    }

}
