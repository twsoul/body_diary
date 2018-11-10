package com.example.a1013c.body_sns;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Img extends AppCompatActivity {
    private static String TAG = "recyclerview_example";

    private ArrayList<Img_eat_Dictionary> mArrayList;
    private Img_eat_adapter mAdapter;
    private RecyclerView mRecyclerView_1;

    public ImageView mimageView;
    private View header;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_eat);
        checkPermissions(); //퍼미션 체크
        //리사이클러

        load_data();

        //인플레터
        header =  getLayoutInflater().inflate(R.layout.img_edit, null, false);

        //이미지 뷰
        mimageView = header.findViewById(R.id.img);


        mRecyclerView_1 = (RecyclerView) findViewById(R.id.recycleview_main_list_1);
        mRecyclerView_1.setLayoutManager(new LinearLayoutManager(this));
//        mArrayList = new ArrayList<>();  //데이터 로딩때문에 지워준 것.

//        mimageView = findViewById(R.id.img);

        mAdapter = new Img_eat_adapter(this, mArrayList);
        mRecyclerView_1.setAdapter(mAdapter);


        // 이미지 공유 받기
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) { // 타입중에 이미지를 받아 왔을 때
                // 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(Img.this);
                View view = LayoutInflater.from(Img.this)
                        .inflate(R.layout.img_edit, null, false);

                // 이미지, 메모 , 버튼 이벤트 처리
                Button ButtonSubmit =  view.findViewById(R.id.img_save);
                ImageView img_uri =  view.findViewById(R.id.img);
                final TextView editmemo1 =  view.findViewById(R.id.edit_img_memo);

                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();

                // 이미지 뷰 --> 공유 받은 uri 넣기
                final Uri imageUri =  intent.getParcelableExtra(Intent.EXTRA_STREAM);
                String share = getRealPathFromURI(imageUri);
                img_uri.setImageURI(Uri.parse(share));

                Toast.makeText(this, "음식 갤러리 입니다.", Toast.LENGTH_SHORT).show();

                //저장
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {
                        String strID = imageUri.toString(); // 이미지 uri 넣기
                        String strEnglish = editmemo1.getText().toString();


                        //날짜 입력
                        long now = System.currentTimeMillis();
                        Date day = new Date(now);
                        SimpleDateFormat a = new SimpleDateFormat("yy년-MM월-dd일");
                        String getTime = a.format(day);

                        //이미지 uri 받아서 넣기.

                        Img_eat_Dictionary dict = new Img_eat_Dictionary(strID,strEnglish, getTime);

                        mArrayList.add(0, dict); //첫 줄에 삽입
                        //mArrayList.add(dict); //마지막 줄에 삽입

                        mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영

                        dialog.dismiss();
                    }
                });

            }
        }



        //추가 버튼
        Button buttonInsert = (Button)findViewById(R.id.eat_add_img);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Img.this);
                View view = LayoutInflater.from(Img.this)
                        .inflate(R.layout.img_edit, null, false);
                builder.setView(view);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.img_save);


                final ImageView img_uri = (ImageView) view.findViewById(R.id.img);
                final TextView editmemo =  (TextView)view.findViewById(R.id.edit_img_memo);
                //인플레터 먹임.
                mimageView = (ImageView) view.findViewById(R.id.img);



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

                        // 사진 안넣고 저장 하기 했을 때 돌려 보냄
                        if(photoUri==null){
                            return;
                        }
                        //
                        String strID = photoUri.toString();
                        String strEnglish = editmemo.getText().toString();
                        BitmapFactory.decodeFile(strID);

                        //날짜 입력
                        long now = System.currentTimeMillis();
                        Date day = new Date(now);
                        SimpleDateFormat a = new SimpleDateFormat("yy년-MM월-dd일");
                        String getTime = a.format(day);

                        //이미지 uri 받아서 넣기.

                        Img_eat_Dictionary dict = new Img_eat_Dictionary(strID,strEnglish, getTime);

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
        save_data();
    }

    private void save_data(){
            //json 저장
            SharedPreferences pref = getSharedPreferences("Img",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mArrayList);
            editor.putString("eat_img_list",json);
            editor.apply();
    }

    private void load_data() {
        SharedPreferences pref = getSharedPreferences("Img", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("eat_img_list", null);
        Type type = new TypeToken<ArrayList<Img_eat_Dictionary>>() {
        }.getType();
        mArrayList = gson.fromJson(json, type);

        if (mArrayList == null) {
            mArrayList = new ArrayList<>();
        }
    }





    //카메라
    public void PhotoDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Img.this);
        alt_bld.setMessage("음식 사진을 선택합니다.").setCancelable(
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
        alert.setTitle("음식 사진");
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
            Toast.makeText(Img.this, "이미지 처리 오류!", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(Img.this,
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
            MediaScannerConnection.scanFile(Img.this,
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

            File folder = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");
            File tempFile = new File(folder.toString(), croppedFileName.getName());

            photoUri = FileProvider.getUriForFile(Img.this,
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

    // 이미지 절대 경로 얻기
    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));

        Log.d(TAG, "getRealPathFromURI(), path : " + uri.toString());

        cursor.close();
        return path;
    }

}

