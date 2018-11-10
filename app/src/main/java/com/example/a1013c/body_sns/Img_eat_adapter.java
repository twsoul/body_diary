package com.example.a1013c.body_sns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

//음식사진 + 간단한 메모
public class Img_eat_adapter extends RecyclerView.Adapter<Img_eat_adapter.CustomViewHolder>  {

    private ArrayList<Img_eat_Dictionary> mList;
    private Context mContext;

    public Img_eat_adapter( Context context, ArrayList<Img_eat_Dictionary> list) {
        mList = list;
        mContext = context;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected ImageView mimg;
        protected TextView mmemo;
        protected TextView mDate;


        public CustomViewHolder(View view) {
            super(view);

            this.mimg = (ImageView) view.findViewById(R.id.list_img);
            this.mmemo = (TextView) view.findViewById(R.id.img_memo);
            this.mDate = view.findViewById(R.id.img_day);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록


        }




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            //메뉴
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "크게보기");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");

            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // 수정
                switch (item.getItemId()) {
                    case 1001:

                        //다른 액티비티
//                        mContext.startActivity(new Intent(mContext,Img.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.img_edit, null, false);
                        builder.setView(view);


                        final Button ButtonSubmit = (Button) view.findViewById(R.id.img_save); //수정 버튼
                        // 이미지,메모,날짜
                        final ImageView img = (ImageView) view.findViewById(R.id.img);
                        final EditText editTextEnglish = (EditText) view.findViewById(R.id.edit_img_memo);

                        //편집 할때는 날짜, 공유 버튼 보이게
                        final EditText editDate = view.findViewById(R.id.editText_date); //날짜
                        editDate.setVisibility(View.VISIBLE);
                        final Button share = view.findViewById(R.id.share); //공유 버튼
                        share.setVisibility(View.VISIBLE);

                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("편집 이미지 클릭","ㅇ"); //됨


                            }
                        });


                        //공유 버튼 클릭  --> 이미지 공유하기
                        share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mList.get(getAdapterPosition()).getImg_img()));
                                    v.getContext().startActivity(intent);
                                }
                        });


                        // 메인에 있던 데이터 옮겨 받기.
                        img.setImageURI(Uri.parse(mList.get(getAdapterPosition()).getImg_img()));

                        Log.d("이미지 오나?",mList.get(getAdapterPosition()).getImg_img());
                        //이까지 옴

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
                                Img_eat_Dictionary dict = new Img_eat_Dictionary(strID, strEnglish, strDate);

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




    @Override
    public Img_eat_adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.img_eat_row, viewGroup, false);

        Img_eat_adapter.CustomViewHolder viewHolder = new Img_eat_adapter.CustomViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBindViewHolder(@NonNull final Img_eat_adapter.CustomViewHolder viewholder_1, final int position) {


        viewholder_1.mmemo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder_1.mmemo.setGravity(Gravity.CENTER);

        viewholder_1.mmemo.setText(mList.get(position).getImg_memo());
        viewholder_1.mDate.setText(mList.get(position).getDate());


//        final Handler handler = new Handler(){
//         public void handleMessage(Message msg){
//             viewholder_1.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));
//         }
//        };
//        Thread img = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                Message msg = handler.obtainMessage();
//                handler.sendMessage(msg);
//            }
//        };
//        img.start();




        new AsyncTask(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            // 백 그라운드 작동.. 도중에 publishProgress()를 실행하면 onProgressUpdate 실행
            @Override
            protected Object doInBackground(Object[] objects) {

                Bitmap img = resize(mContext,Uri.parse(mList.get(position).getImg_img()),120);

                return img;
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
                viewholder_1.mimg.setImageBitmap((Bitmap) o);

//    viewholder_1.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));
            }
        }.execute();


//        viewholder_1.mimg.setImageURI(Uri.parse(mList.get(position).getImg_img()));


    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }


    private Bitmap resize(Context context,Uri uri,int resize){
        Bitmap resizeBitmap=null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); // 1번

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (true) {//2번
                if (width / 2 < resize || height / 2 < resize)
                    break;
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); //3번
            resizeBitmap=bitmap;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }



    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));



        cursor.close();
        return path;
    }




}
