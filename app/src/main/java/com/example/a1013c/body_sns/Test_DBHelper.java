package com.example.a1013c.body_sns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.a1013c.body_sns.Test_Contract.*;

public class Test_DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "eat.db";
    public static final int DATABASE_VERSION = 1;

    public Test_DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EATLIST_TABLE ="CREATE TABLE " +
                TestEntry.TABLE_NAME + " ( " +
                TestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TestEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TestEntry.COLUMN_CONTENTS + " TEXT NOT NULL, " +
                TestEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                TestEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_EATLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TestEntry.TABLE_NAME);
        onCreate(db);
    }

}
