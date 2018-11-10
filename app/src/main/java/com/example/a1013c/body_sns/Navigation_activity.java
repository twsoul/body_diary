package com.example.a1013c.body_sns;

import android.Manifest;
import android.app.Activity;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1013c.body_sns.Board.board_main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Navigation_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private int ad=0; //광고

    //리사이클러뷰
    RecyclerView mRecyclerView;
    RecyclerView mRecyclerView_eat;

    Context mContext;
//    RecyclerView.LayoutManager mLayoutManager;
    private Body_Img_Adapter mAdapter;
    private Img_eat_adapter mAdapter_eat;

    static int num=2;



    //사진
    private ImageView mimageView;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private static final int CROP_FROM_CAMERA = 3;

    private Uri photoUri;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int MULTIPLE_PERMISSIONS = 101;

    private String mCurrentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);

        checkPermissions();


//        load_data();
//        mRecyclerView = findViewById(R.id.recycler_view1); //리사이클러 id 받음.
//        mRecyclerView.setHasFixedSize(true);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setReverseLayout(true);  //역순으로 저장
//        mLayoutManager.setStackFromEnd(true);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
//
//        mAdapter = new Body_Img_Adapter(this, mArrayList);
//        mRecyclerView.setAdapter(mAdapter);

        //전체 삭제 버튼
//        Button del = findViewById(R.id.all_delete_button);
//        del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               for(int i =0; i<body_ArrayList.size();i++){
//                   body_ArrayList.remove(i);
//                   i--;
//               }
//                Adapter1 myAdapter = new Adapter1(body_ArrayList);
//                mRecyclerView.setAdapter(myAdapter);
//
//            }
//        });

        //삭제 버튼 (체크박스)
//        Button del1 = findViewById(R.id.delete_button);
//        del1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for(int i=0; i<body_ArrayList.size();i++){
//                    rc_content_1 arr = body_ArrayList.get(i);
//                    if(arr.isSelected()==true){
//                        body_ArrayList.remove(i);
//                        i--;
//                    }
//                }
//                Adapter1 myAdapter = new Adapter1(body_ArrayList);
//                mRecyclerView.setAdapter(myAdapter);
//
//            }
//        });



//        ArrayAdapter<rc_content_1> adapter;
//        ArrayList<rc_content_1> body_ArrayList = new ArrayList<>();
//
//        //어뎁터 새로 정렬
//        Adapter1 myAdapter = new Adapter1(body_ArrayList);
//        mRecyclerView.setAdapter(myAdapter);


        // 쩌리
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    // 첫 화면 저장.
    private ArrayList<Body_Img_Dictionary> mArrayList;
    private ArrayList<Img_eat_Dictionary> mArrayList_eat;
    private void load_data() {
        //몸 갤러
        SharedPreferences pref = getSharedPreferences("Body_Img", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("body_img_list", null);
        Type type = new TypeToken<ArrayList<Body_Img_Dictionary>>() {
        }.getType();
        mArrayList = gson.fromJson(json, type);

        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }

        //음식 갤러리
        SharedPreferences pref_eat = getSharedPreferences("Img", MODE_PRIVATE);
        Gson gson_eat = new Gson();
        String json_eat = pref_eat.getString("eat_img_list", null);
        Type type_eat = new TypeToken<ArrayList<Img_eat_Dictionary>>() {
        }.getType();
        mArrayList_eat = gson_eat.fromJson(json_eat, type_eat);

        if (mArrayList_eat == null) {
            mArrayList_eat = new ArrayList<>();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();

        // 운동 갤러리 보여주기
        // 데이터 불러옴
        load_data();
        mRecyclerView = findViewById(R.id.recycler_view1); //리사이클러 id 받음.
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);  //역순으로 저장
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

        mAdapter = new Body_Img_Adapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        //음식 갤러리
        mRecyclerView_eat = findViewById(R.id.recycler_view2); //리사이클러 id 받음.
        mRecyclerView_eat.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager_eat = new LinearLayoutManager(this);
        mLayoutManager_eat.setReverseLayout(true);  //역순으로 저장
        mLayoutManager_eat.setStackFromEnd(true);
        mRecyclerView_eat.setLayoutManager(new GridLayoutManager(this,4));

        mAdapter_eat = new Img_eat_adapter(this, mArrayList_eat);
        mRecyclerView_eat.setAdapter(mAdapter_eat);



        //광고
        ad++;
        if(ad%10==0){
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(Navigation_activity.this);
            alt_bld.setMessage("[훈킴 GYM] 훈킴 보충제 확실합니다. 010-0000-7387").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alt_bld.create();
            // Title for AlertDialog
            alert.setTitle("광고");
            // Icon for AlertDialog
            alert.setIcon(R.drawable.body_logo);
            alert.show();

        }


        //      저장한거 어뎁터


        //add1 클래스에서 저장 버튼 눌렀을 때,
//        if(add1.save==1){
//            // 사진,메모,날짜 저장
//            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//            body_ArrayList.add(new rc_content_1(pref.getString("img",String.valueOf(photoUri)),
//                    pref.getString("t",""), pref.getString("date",""),false));
//
//            //어뎁터 새로 정렬
//        Adapter1 myAdapter = new Adapter1(body_ArrayList);
//        mRecyclerView.setAdapter(myAdapter);
////            adapter.notifyDataSetChanged();
//             add1.save=0;
//        }

    }


    private int long_click=0;
    private Dialog_edit dialog_edit;
    //리스타트 되면서 사진 저장!
    @Override
    protected void onRestart() {
        super.onRestart();
//        프로필 사진 저장!
//        mimageView = findViewById(R.id.profile_photo);
//        SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
//        mimageView.setImageURI(Uri.parse(pref1.getString("photo",String.valueOf(photoUri))));

//        if(num<1){ // 한번만 실행 될수 있게.
//            num=2;
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerView,
//                new RecyclerItemClickListener.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Log.d("원","클릭"+position);
//
//                        String img = body_ArrayList.get(position).getBody_img(); // arraylist 안에 있는 이미지 uri
//                        String memo = body_ArrayList.get(position).getmemo();// arraylist 안에 있는 메모 String
//
//
//
//                        Intent i = new Intent(Navigation_activity.this,Dialog_edit1.class);
//                        i.putExtra("img_st",img);
//                        i.putExtra("memo",memo);
//                        startActivity(i);
//
//                    }
//
//
//
//                    @Override
//                    public void onLongItemClick(View view, int position) { // 삭제버튼, 체크 박스 보이기.
//                        long_click=1;
//                        Log.d("롱","클릭"+position);
//
////                        Button a = findViewById(R.id.all_delete_button);
////                        a.setVisibility(View.VISIBLE);
////
////                        Button b = findViewById(R.id.delete_button);
////                        b.setVisibility(View.VISIBLE);
//
//                        //롱 클릭 --> 체크박스 모두 보이게
////                        for(int i=0; i< body_ArrayList.size();i++){
////                            position=i;
////                            body_ArrayList.get(position);
////                            CheckBox check = findViewById(R.id.checkBox);
////                            check.setVisibility(View.VISIBLE);
////                        }
//
//                    }
//                }));}

    }



    @Override
    public void onBackPressed() {
        //롱클릭 상황에서 취소
        if(long_click==1) {
//            Button a = findViewById(R.id.all_delete_button);
//            a.setVisibility(View.INVISIBLE);
//
//            Button b = findViewById(R.id.delete_button);
//            b.setVisibility(View.INVISIBLE);

            long_click=0;
        }else{ //아닐 때는 정상적으로 종료
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        }
    }






    //옵션 메뉴 생성 할때
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_activity, menu);

        //워너비 이미지 바꾸기
        mimageView = findViewById(R.id.profile_photo);
        TextView wanna = findViewById(R.id.wannabe);


        // 워너비 텍스트 클릭!
        wanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialog();
            }
        });
        mimageView = findViewById(R.id.profile_photo);
        SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
        mimageView.setImageURI(Uri.parse(pref1.getString("photo",String.valueOf(photoUri)))); //사진 세팅

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_settings1) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //네비게이션 버튼들
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { // 네비게이션 안에 버튼 들
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) { // 몸 사진
            Intent i = new Intent(Navigation_activity.this,Body_Img.class);
            startActivity(i);
        }
//        else if (id == R.id.nav_gallery) { //모아보기 ==> 그리드 뷰
//
//            mRecyclerView = findViewById(R.id.recycler_view1); //리사이클러 id 받음.
//            mRecyclerView.setHasFixedSize(true);
//
//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//            mLayoutManager.setReverseLayout(true);  //역순으로 저장
//            mLayoutManager.setStackFromEnd(true);
//            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
//
//        }
        else if (id == R.id.eat) { // 식단 기록
            Intent i = new Intent( Navigation_activity.this,Eat.class);
            startActivity(i);
        } else if (id == R.id.nav_profile_edit) { // 식단 이미지,
            Intent i = new Intent(Navigation_activity.this,Img.class);
            startActivity(i);
        } else if (id == R.id.nav_ex) { // 운동 메모
            Intent i = new Intent(Navigation_activity.this,Test_Main.class);
            startActivity(i);
        } else if (id == R.id.alarm) { // 알람 설정
            Intent i = new Intent(this,Alarm_main.class);
            startActivity(i);
        }
        else if(id == R.id.test){ // 몸 자랑 게시판
            Intent i = new Intent(this,board_main.class);
            startActivity(i);
        }
        else if (id == R.id.test2) { // 영양 성분 도출하기
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        //ㅡㅡㅡㅡㅡㅡ외부
//        else if (id == R.id.map) { // 우리동네 운동 정보
//            Intent i = new Intent(Navigation_activity.this,Map.class);
//            startActivity(i);
//        }
        else if (id == R.id.news) { //건강 정보
            Intent news = new Intent(Navigation_activity.this,News.class);
            startActivity(news);

        } else if (id == R.id.wannabe_photo) { // 워너비 사진 고르기
            Intent Wannabe = new Intent(Navigation_activity.this,Wannabe.class);
            startActivity(Wannabe);

        } else if (id == R.id.mini_game) { // 미니 게임
            Intent i = new Intent(Navigation_activity.this,Rollet.class);
            startActivity(i);

        } else if (id ==R.id.feedback_send){ //피드백 보내기
            Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address ={"1013cm@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL,address);
                email.putExtra(Intent.EXTRA_SUBJECT,"[바디 다이어리] 피드백 입니다.");
                email.putExtra(Intent.EXTRA_TEXT," 피드백 감사합니다.\n\n\n사용중인 휴대폰 기종: \n\n" +
                        "문의 종류: \n" +
                        "ex) 문제점, 고치면 좋을 점 등..\n\n" +
                        "상세 내용: ");
                startActivity(email);

        } else if (id == R.id.nav_logout) { // 로그아웃
            DialogSimple();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

// 로그아웃 다이얼로그
    public void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Navigation_activity.this);
        alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        Intent a = new Intent(Navigation_activity.this,login_body.class);
                        startActivity(a);

                        //자동 로그인 해제 저장!
                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
                        SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다

                        editor.putBoolean("auto_login",false);
                        editor.apply();

                        Toast.makeText(Navigation_activity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish(); //프래그먼트 종료.
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("로그아웃");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.body_logo);
        alert.show();

    }


    //사진 골라라
    public void PhotoDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Navigation_activity.this);
        alt_bld.setMessage("자신의 워너비 사진을 선택합니다.").setCancelable(
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
        alert.setTitle("워너비 사진");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.body_logo);
        alert.show();
    }
    //퍼미션 필요없는거 같네.
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

    //사진 찍어서 올리기
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(Navigation_activity.this, "이미지 처리 오류!", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Navigation_activity.this,
                    "com.example.a1013c.body_sns.provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "nostest_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    //갤러리에서 고르기
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

    //결과 가져오는거(?)
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
            MediaScannerConnection.scanFile(Navigation_activity.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            mimageView.setImageURI(null);
            //프로필.. 사진
            //이미지 받아서 프리퍼런스에 저장
            SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref1.edit();
            editor.putString("photo", String.valueOf(photoUri));

            mimageView.setImageURI(Uri.parse(pref1.getString("photo",String.valueOf(photoUri))));
            editor.commit();

        }
    }

    //Android N crop image 이미지 크롭하는 거
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
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            File croppedFileName = null;
            try {
                croppedFileName = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File folder = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(Navigation_activity.this,
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
