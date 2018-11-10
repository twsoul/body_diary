package com.example.a1013c.body_sns;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.a1013c.body_sns.Test_Contract.*;

public class Test_Main extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    public Test_Adapter mAdapter;
    public RecyclerView mRecyclerview;
    public static boolean edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_rc);

        Toast.makeText(this,"삭제  <<밀기>> 수정",Toast.LENGTH_SHORT).show();

        Test_DBHelper dbHelper = new Test_DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mRecyclerview = findViewById(R.id.test_rc);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Test_Adapter(this,getAll()); // 모든 데이터 꺼내오기.
        mRecyclerview.setAdapter(mAdapter);

        //[추가] 버튼 눌렀을 때, 에딧 액티비티로 감
        Button add =findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Test_Main.this,Test_edit.class);
                startActivity(i);
            }
        });

        //왼쪽 삭제
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }

        }).attachToRecyclerView(mRecyclerview);

        //오른쪽  편집
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Intent i = new Intent(Test_Main.this,Test_edit.class);
                // 아이디 값 넘기기.
                edit = true;
                i.putExtra("id", (long) viewHolder.itemView.getTag());
//                i.putExtra("title",viewHolder.)


                Log.d("디비 id", String.valueOf((long) viewHolder.itemView.getTag()));
                startActivity(i);
            }

        }).attachToRecyclerView(mRecyclerview);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.swapCursor(getAll()); // 데이터 체인지 셋
    }

    // 삭제
    private void removeItem(long id) {
        mDatabase.delete(TestEntry.TABLE_NAME,
                TestEntry._ID + "=" + id,null);
        mAdapter.swapCursor(getAll());
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
