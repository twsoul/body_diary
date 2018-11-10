package com.example.a1013c.body_sns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class edit_profile extends AppCompatActivity {
//    private static final int PICK_FROM_CAMERA=1;
//    private static final int PICK_FROM_ALBUM=2;
//    private static final int CROP_FROM_CAMERA=3;

//    Uri ImageUri;
    private  ImageView mimageView;
//    private  ImageView onsaveimg;


//    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//    private static final int MULTIPLE_PERMISSIONS = 101; //권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수


private int a=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        checkPermissions();
        getPreferences();


        //프리퍼런스 --> 프로필 사진 저장
        mimageView = findViewById(R.id.profile_change);
        SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
        mimageView.setImageURI(Uri.parse(pref1.getString("photo",String.valueOf(photoUri))));
        //회원가입 --> 닉네임,메인 운동,직업 불러오기

        //사진 바꾸기
        Button change_im = findViewById(R.id.change_image1);
        change_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoDialog();
            }
        });


        //취소 버튼
        Button cancel = findViewById(R.id.cancel_profile);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //저장 버튼
        Button save = findViewById(R.id.save_profile);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save_edit_exit();
                finish(); // 저장 버튼 구현.
            }
        });

    }

    public void PhotoDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(edit_profile.this);
        alt_bld.setMessage("프로필 사진을 선택합니다.").setCancelable(
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
        alert.setTitle("프로필 사진");
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
            Toast.makeText(edit_profile.this, "이미지 처리 오류!", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(edit_profile.this,
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
            MediaScannerConnection.scanFile(edit_profile.this,
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } else if (requestCode == CROP_FROM_CAMERA) {
            mimageView.setImageURI(null);
            SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref1.edit();
            editor.putString("photo", String.valueOf(photoUri));
            editor.commit();
            mimageView.setImageURI(Uri.parse(pref1.getString("photo",String.valueOf(photoUri))));


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

            photoUri = FileProvider.getUriForFile(edit_profile.this,
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

    private void getPreferences(){ //원래 데이터 불러오기
        EditText nick = findViewById(R.id.edit_nick);
        EditText main = findViewById(R.id.edit_ex);
        EditText career = findViewById(R.id.edit_career);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        nick.setText(pref.getString("nick", ""));
        main.setText(pref.getString("main", ""));
        career.setText(pref.getString("career", ""));
    }

    private void save_edit(){ //변하는 값 저장하기
        EditText nick = findViewById(R.id.edit_nick);
        EditText main = findViewById(R.id.edit_ex);
        EditText career = findViewById(R.id.edit_career);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("new_nick", nick.getText().toString());
        editor.putString("new_main", main.getText().toString());
        editor.putString("new_career", career.getText().toString());

        editor.commit();
    }

    private void save_edit_exit(){ //변하는 값 저장하기
        EditText nick = findViewById(R.id.edit_nick);
        EditText main = findViewById(R.id.edit_ex);
        EditText career = findViewById(R.id.edit_career);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        nick.setText(pref.getString("new_nick", nick.getText().toString()));
        main.setText(pref.getString("new_main", main.getText().toString()));
        career.setText(pref.getString("new_main", main.getText().toString()));
//        editor.putString("new_nick", nick.getText().toString());
//        editor.putString("new_main", main.getText().toString());
//        editor.putString("new_career", career.getText().toString());

        editor.commit();
    }


    @Override
    protected void onPause() { // 쓰던 것 저장.
        super.onPause();
        Log.d("프로필수정","Pause");

        //onPause 지나면 플래그 등록
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("edit_flag", "3");
        editor.commit();

        save_edit(); //쓰던 것 저장!!

    }

    @Override
    protected void onResume() { // 전에 쓰던 것 불러 올껀지 물어 보기. 예 / 아니오
        super.onResume();
        Log.d("프로필수정", "Resume");
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if(pref.getString("edit_flag", "").equals("3")){
               saveDialog(); }
               else{
 }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("프로필수정","Destroy");
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("edit_flag", "0");
        editor.commit();

    }

    public void saveDialog(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(edit_profile.this);
        alt_bld.setMessage("쓰던 편집을 불러 오시겠습니까?").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // <save_nick> 저장했던 값 불러오기
                        save_edit();
                    }
                }).setNeutralButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // <nick> 전에 저장했던 값 불러오기
                        getPreferences();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("편집");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.body_logo);
        alert.show();
    }



    @Override
    protected void onStart() {
        Log.d("프로필수정","Start");
        super.onStart();
        if(a>0) { //저장하면 이미지 받아와서 출력.
            SharedPreferences pref1 = getSharedPreferences("pref1", MODE_PRIVATE);
            mimageView.setImageURI(Uri.parse(pref1.getString("photo", String.valueOf(photoUri))));
//            editor.commit();
        }
    }

    @Override
    protected void onStop() { //이미지 넘기기.
        super.onStop();
        Log.d("프로필수정","Stop");

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("프로필수정","Restart");
    }

}


