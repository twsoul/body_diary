package com.example.a1013c.body_sns;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Body_Img extends AppCompatActivity {
    private static String TAG = "recyclerview_example";

    private ArrayList<Body_Img_Dictionary> mArrayList;
    private Body_Img_Adapter mAdapter;
    private RecyclerView mRecyclerView_1;
    private int count = 0;

    public ImageView mimageView;
    private View header;

    Context mContext;

    FloatingActionMenu FM; //플로팅 메뉴
    com.github.clans.fab.FloatingActionButton add,slide,fab_resume,fab_pause,cancel,fab_search;

    TextView all_tx;
    TextView now_tx;

    autoScroll mAuto;

    public class autoScroll extends AsyncTask<Integer, Integer, Void>{
        boolean resume = true;
        boolean pause = false;
        private String WATCH_DOG = "barabulka";
        TextView all;

        @Override
        protected Void doInBackground(Integer... params) {
            int counter = 0;

            while (resume) {
                if(mAuto.isCancelled()){ //취소 누르면 스레드 종료
                   break;
                }
                // --- show progress in text field --
                publishProgress(counter); // 현재 슬라이드 값 전달
                mRecyclerView_1.smoothScrollToPosition(counter);
                // --- when the counter reaches the end, change the loop flag --
//                resume = (counter++ == params[0]) ? false : true; // 카운트가 꽉차면 true에서 false로 간다.   -->  이거 없으면 무한으로 재생 아닌가 -->아님

                resume = (counter++ == params[0]);
                Log.d("para", String.valueOf(params[0]));
                if(resume){ // 꽉차면 멈춤 <--> 안멈춤
//                    resume = false; //멈춤
                    resume = true;
                    counter = 0; // 무한 반복.
                }else{ //꽉차기 전까지..
                    resume = true;
                }

                try {
                    // --- put here any time expensive code --
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (pause) { // 일시정지면.
                    synchronized (WATCH_DOG) {
                        try {
                            // --- set text field status to 'paused' --
//                            publishProgress(-1);
                            // --- sleep tile wake-up method will be called --
                            WATCH_DOG.wait();

                        } catch (InterruptedException e) {e.printStackTrace();
                        }
                        pause = false;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {
            // --- update text with percentage --
            now_tx.setText(String.valueOf(params)); // 계속 현재 포지션 계속 보여주기. ex) 3/14
            if(params[0] == -1){ // 일시 정지 걸렸을 때,
                // --- show the 'pause' sight --
//                text.setText("PAUSED");

            }
        }

        @Override
        protected void onPostExecute(Void result) { // 끝났을 때
//            text.setText("FINISHED");
        }

        /**Pause task for a while*/
        public void pauseMyTask() {
            pause = true;
        }

        /**Wake up task from sleeping*/
        public void wakeUp() {
            synchronized(WATCH_DOG){
                WATCH_DOG.notify();
            }
        }

        /**Get a loop-flag*/
        public boolean getPause() {
            return pause;
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_body);
        checkPermissions(); //퍼미션 체크

        FM = findViewById(R.id.float_menu);// 플로팅 메뉴 찾기

        //검색창   검색기능
        final EditText search = findViewById(R.id.searching);
        search.setVisibility(View.GONE); //검색창 만들어 질때 안보이게 하기.
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { filter(s.toString()); }
        });


        //쉐어드에 저장된 데이터 불러오기.
        load_data();

        //인플레터
        header =  getLayoutInflater().inflate(R.layout.img_edit_body, null, false);

        //이미지 뷰
        mimageView = header.findViewById(R.id.body_img_cg);


        mRecyclerView_1 = (RecyclerView) findViewById(R.id.recycleview_main_list_2);
        mRecyclerView_1.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Body_Img_Adapter(this, mArrayList);
        mRecyclerView_1.setAdapter(mAdapter);


        //자동 스크롤

        all_tx =findViewById(R.id.all_count);
        now_tx =findViewById(R.id.now_count);

        slide = findViewById(R.id.fab_auto); //슬라이드 보기
        fab_pause= findViewById(R.id.fab_pause);//일시 정지
        fab_resume =findViewById(R.id.fab_resume);//다시 재생
        cancel = findViewById(R.id.fab_auto_cancel); // 취소

        fab_search =findViewById(R.id.fab_search); //검색창 띄우기

        //슬라이드 보기
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel.setVisibility(View.VISIBLE); //취소 창
                fab_pause.setVisibility(View.VISIBLE); // 일시 정지 창
                all_tx.setVisibility(View.VISIBLE); //진행
                now_tx.setVisibility(View.VISIBLE); //진행
                FM.close(true);
                mAuto = new autoScroll(); // 스레드 생성
                mAuto.execute(mArrayList.size()); // 리사이클러 뷰 개수 만큼 돌아가도록
                all_tx.setText(" / "+String.valueOf(mArrayList.size())); // 사이즈 만큼 보여주기

            }
        });

        // 일시정지
        fab_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            fab_resume.setVisibility(View.VISIBLE); // 다시 재생 버튼 보이기
            fab_pause.setVisibility(View.INVISIBLE); // 일시 정지 버튼 감추기
                mAuto.pauseMyTask(); // 일시 정지
            }
        });

        //다시 재생
        fab_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_resume.setVisibility(View.INVISIBLE); // 다시 재생 버튼 감추기
                fab_pause.setVisibility(View.VISIBLE); // 일시 정지 버튼 보이기
                mAuto.wakeUp(); //다시 재생
            }
        });

        //자동 스크롤 취소
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuto.cancel(true); //
                cancel.setVisibility(View.INVISIBLE); // 취소 버튼 감추기
                fab_resume.setVisibility(View.INVISIBLE); // 다시 재생 버튼 감추기
                fab_pause.setVisibility(View.INVISIBLE);
                all_tx.setVisibility(View.INVISIBLE); //진행
                now_tx.setVisibility(View.INVISIBLE); //진행

            }
        });


        //검색창 보이기
        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setVisibility(View.VISIBLE);
                FM.close(true);
            }
        });

//        mArrayList = new ArrayList<>();  //데이터 로딩때문에 지워준 것.

        mimageView = findViewById(R.id.body_img_cg);


        //추가 버튼
        add = findViewById(R.id.fab_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FM.close(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(Body_Img.this);
                View view = LayoutInflater.from(Body_Img.this)
                        .inflate(R.layout.img_edit_body, null, false);
                builder.setView(view);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.body_img_save);


                final ImageView img_uri = (ImageView) view.findViewById(R.id.body_img_cg);
                final TextView editmemo =  (TextView)view.findViewById(R.id.body_edit_img_memo);
                //인플레터 먹임.
                mimageView = (ImageView) view.findViewById(R.id.body_img_cg);


                //이미지 버튼 눌렀을 때 이미지 바꿀 수 있도록.
                img_uri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoDialog();
                    }
                });


                // 날짜 입력 자동
//                final EditText editDate = view.findViewById(R.id.edittext_date);
//                editDate.setVisibility(View.INVISIBLE);

                ButtonSubmit.setText("추가");


                //저장 버튼 눌렀을 때
                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            //사진 없으면 사진을 선택하세요.


                            public void onClick(View v) {
                                if(photoUri==null){

                                    return;
                                }
                        String strID = photoUri.toString();
                        String strEnglish = editmemo.getText().toString();


                        //날짜 입력
                        long now = System.currentTimeMillis();
                        Date day = new Date(now);
                        SimpleDateFormat a = new SimpleDateFormat("yy년MM월dd일");
                        String getTime = a.format(day);

                        //이미지 uri 받아서 넣기.

                        Body_Img_Dictionary dict = new Body_Img_Dictionary(strID,strEnglish, getTime);

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




    //검색 기능
    private void filter(String text){
        ArrayList<Body_Img_Dictionary> filterList = new ArrayList<>();

        for(Body_Img_Dictionary item : mArrayList){
            if(item.getImg_memo().toLowerCase().contains(text.toLowerCase()) || item.getDate().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        mAdapter.filterList(filterList);
    }



    @Override
    protected void onPause() {
        super.onPause();
        save_data();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
        if(mAuto.getStatus() == autoScroll.Status.RUNNING) {
            mAuto.cancel(true); //스레드 끄고 나가기.
        }
        }catch(Exception e){
        }
    }

    private void save_data(){
            //json 저장
            SharedPreferences pref = getSharedPreferences("Body_Img",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mArrayList);
            editor.putString("body_img_list",json);
            editor.apply();
    }

    private void load_data() {
        SharedPreferences pref = getSharedPreferences("Body_Img", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("body_img_list", null);
        Type type = new TypeToken<ArrayList<Body_Img_Dictionary>>() {
        }.getType();
        mArrayList = gson.fromJson(json, type);

        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
    }





    //카메라
    public void PhotoDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Body_Img.this);
        alt_bld.setMessage("몸 사진을 선택합니다.").setCancelable(
                false).setPositiveButton("사진 찍기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        takePhoto();
                    }
                }).setNegativeButton("갤러리",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        goToAlbum();

                    }
                }).setNeutralButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("몸 사진");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.body_logo);
        alert.show();
    }

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;

    private Uri photoUri;


    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;


    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(Body_Img.this, "이미지 처리 오류!", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Body_Img.this,
                    "com.example.a1013c.body_sns.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "BodyImg_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Body_Img/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();
            cropImage();
        } else if (requestCode == PICK_FROM_CAMERA) {
            cropImage();
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(Body_Img.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {

            mimageView.setImageURI(null);
            mimageView.setImageURI(photoUri);
        }
    }

    //Android N crop image
    public void cropImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.grantUriPermission("com.android.camera", photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            grantUriPermission(list.get(0).activityInfo.packageName, photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 3);
//            intent.putExtra("aspectY", 4);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/Body_Img/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(Body_Img.this,
                    "com.example.a1013c.body_sns.provider", tempFile);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }

            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                grantUriPermission(res.activityInfo.packageName, photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }




}
