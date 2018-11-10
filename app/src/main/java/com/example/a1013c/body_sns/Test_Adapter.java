package com.example.a1013c.body_sns;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Test_Adapter extends RecyclerView.Adapter<Test_Adapter.TestViewHolder>  {

    private Cursor mCursor;
    private Context mContext;


    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected TextView title;
        protected TextView contents;
        protected TextView testDate;



        public TestViewHolder(View view) {
            super(view);


            title = view.findViewById(R.id.title);
            contents = view.findViewById(R.id.contents);
            testDate = view.findViewById(R.id.test_date);


          view.setOnCreateContextMenuListener(this); //2. 리스너 등록

        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U


//            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "크게보기");
//            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
//
//            Edit.setOnMenuItemClickListener(onEditMenu);
//            Delete.setOnMenuItemClickListener(onEditMenu);


        }

//        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
//        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                // 메뉴 클릭
//                switch (item.getItemId()) {
//                    case 1001://수정
//
//                        mContext.startActivity(new Intent(mContext,Test_edit.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//
//                        break;
//
//                    case 1002: //삭제
//
//
//                        break;
//
//
//                }
//                return true;
//            }
//        };


    }



    public Test_Adapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;

    }



    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.test_rc_row, viewGroup, false);

        TestViewHolder viewHolder = new TestViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder viewholder_2, final int position) {
        if(!mCursor.moveToNext()){
            return;
        }

        long id = mCursor.getLong(mCursor.getColumnIndex(Test_Contract.TestEntry._ID));
        String bf = mCursor.getString(mCursor.getColumnIndex(Test_Contract.TestEntry.COLUMN_TITLE));
        String ln = mCursor.getString(mCursor.getColumnIndex(Test_Contract.TestEntry.COLUMN_CONTENTS));
        String dn = mCursor.getString(mCursor.getColumnIndex(Test_Contract.TestEntry.COLUMN_DATE));


        viewholder_2.itemView.setTag(id);
        viewholder_2.title.setText(bf); //제목
        viewholder_2.contents.setText(ln); //내용
        viewholder_2.testDate.setText(dn); //날짜


        //메모 접었다 폈다 하는 기능.
        viewholder_2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewholder_2.contents.getVisibility()==View.GONE){
                    viewholder_2.contents.setVisibility(View.VISIBLE);

                }else{
                    viewholder_2.contents.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }

    }

}
