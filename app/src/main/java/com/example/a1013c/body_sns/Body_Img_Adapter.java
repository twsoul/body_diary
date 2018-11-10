package com.example.a1013c.body_sns;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//몸사진 + 간단한 메모
public class
Body_Img_Adapter extends RecyclerView.Adapter<Body_Img_Adapter.CustomViewHolder>  {

    private ArrayList<Body_Img_Dictionary> mList;
    private Context mContext;


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected ImageView mimg;
        protected TextView mmemo;
        protected TextView mDate;


        public CustomViewHolder(View view) {
            super(view);

            this.mimg = (ImageView) view.findViewById(R.id.body_list_img);
            this.mmemo = (TextView) view.findViewById(R.id.body_img_memo);
            this.mDate = view.findViewById(R.id.body_img_day);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록


        }




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            //공유하기 여기 넣어도 되겠네
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "크게보기");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");

            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);


//            Intent i = new Intent(android.content.Intent.ACTION_SEND);
//            i.setType("image/png");
//            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(mList.get(getAdapterPosition()).getImg_img()));
//            v.getContext().startActivity(i);

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // 수정
                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.img_edit_body, null, false);
                        builder.setView(view);


                        final Button ButtonSubmit = (Button) view.findViewById(R.id.body_img_save); //수정 버튼
                        // 이미지,메모,날짜
                        final ImageView img = (ImageView) view.findViewById(R.id.body_img_cg);
                        final EditText editTextEnglish = (EditText) view.findViewById(R.id.body_edit_img_memo);

                        // 수정할 때는 날짜와 공유 버튼 보이도록
                        final EditText editDate = view.findViewById(R.id.body_editText_date); //날짜
                        editDate.setVisibility(View.VISIBLE);
                        final Button share = view.findViewById(R.id.body_share); //공유 버튼
                        share.setVisibility(View.VISIBLE);

                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Body_Img camera = new Body_Img();

                            }
                        });


                        //공유 버튼 클릭
                        share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/png");
                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mList.get(getAdapterPosition()).getImg_img()));
                                v.getContext().startActivity(intent);
                            }
                        });


                        //이미지 받아서 크게 보여주기
                        img.setImageURI(Uri.parse(mList.get(getAdapterPosition()).getImg_img()));
                        Log.d("이미지 오나?",mList.get(getAdapterPosition()).getImg_img());
                        //이까지 옴

                        //스트링 받아서 보여주기
                        editTextEnglish.setText(mList.get(getAdapterPosition()).getImg_memo());

                        editDate.setText(mList.get(getAdapterPosition()).getDate());

                        //수정 다이얼로그
                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() { //수정 버튼
                            public void onClick(View v) {

                                String strID = mList.get(getAdapterPosition()).getImg_img();
                                Log.d("이미지 수정은?",strID);
                                String strEnglish = editTextEnglish.getText().toString();
                                String strDate = editDate.getText().toString();

                                // 이미지, 메모, 날짜 순서
                                Body_Img_Dictionary dict = new Body_Img_Dictionary(strID, strEnglish, strDate);

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;


                }
                return true;
            }
        };


    }



    public Body_Img_Adapter(Context context, ArrayList<Body_Img_Dictionary> list) {
        mList = list;
        mContext = context;

    }



    @Override
    public Body_Img_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.img_body_row, viewGroup, false);

        Body_Img_Adapter.CustomViewHolder viewHolder = new Body_Img_Adapter.CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Body_Img_Adapter.CustomViewHolder viewholder_2, final int position) {


        viewholder_2.mmemo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder_2.mmemo.setGravity(Gravity.CENTER);

//        new AsyncTask(){
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            // 백 그라운드 작동.. 도중에 publishProgress()를 실행하면 onProgressUpdate 실행
//            @Override
//            protected Object doInBackground(Object[] objects) {
////                viewholder_2.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));
//                publishProgress();
//                return null;
//            }
//
//            //도중에 진행을 알릴 때,
//            @Override
//            protected void onProgressUpdate(Object[] values) {
//                super.onProgressUpdate(values);
//                viewholder_2.mimg.setImageResource(R.drawable.body_logo);
//            }
//
//            // 결과 값
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                viewholder_2.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));
//            }
//        }.execute();


        viewholder_2.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));
        viewholder_2.mmemo.setText(mList.get(position).getImg_memo());
        viewholder_2.mDate.setText(mList.get(position).getDate());

    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


   //검색기능
    public void filterList(ArrayList<Body_Img_Dictionary> filterList){
        mList = filterList;
        notifyDataSetChanged();

    }


    // 이미지 처리
    public class autoScroll extends AsyncTask {

        // 메인 스레드  --> 준비 작업  ex) 네트워크 준비, 객체 new
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 백 그라운드 작동.. 도중에 publishProgress()를 실행하면 onProgressUpdate 실행
        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }

        //도중에 진행을 알릴 때,
        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        // 결과 값
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }


}
