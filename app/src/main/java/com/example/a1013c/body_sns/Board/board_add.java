package com.example.a1013c.body_sns.Board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.a1013c.body_sns.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class board_add extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUplaod;
    private Button cancel; //취소 버튼 누르면 나감
    private EditText mEditSub; //
    private ImageView mImage;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUplaodTask;
    String idByANDROID_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_add);


//        idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        mButtonChooseImage = findViewById(R.id.choose_img);
        mButtonUplaod = findViewById(R.id.upload);
        cancel = findViewById(R.id.upload_cancel);

        mEditSub = findViewById(R.id.choose_sub);
        mImage = findViewById(R.id.upload_img);
        mProgressBar = findViewById(R.id.progressBar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        //이미지 고르기
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        // 이미지 올리기
        mButtonUplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUplaodTask != null && mUplaodTask.isInProgress()){ // 업로드 중에 중복 업로딩하는 거 막기
                    Toast.makeText(board_add.this, "업로드 진행중..", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();

                }
            }
        });
        //취소 --> 목록으로 돌아가기
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //메인 게시판 시작

            }
        });
    }

    private void openFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUplaodTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            },0); //프로그레스 없앨까.생각좀해봐야함
                        //리스트 제목/이미지/ 닉네임 순서임..
                            // 지금 방편으로 디바이스 고유넘버 줌.
                            final SharedPreferences pref = getSharedPreferences("gellery", Activity.MODE_PRIVATE);
                            String Nick_Name = pref.getString("gell_nick",null);

                        Upload upload = new Upload(mEditSub.getText().toString().trim(),taskSnapshot.getMetadata().getDownloadUrl().toString(), Nick_Name); //고유 넘버

                        String uploadId = mDatabaseRef.push().getKey();
                            assert uploadId != null;
                            mDatabaseRef.child(uploadId).setValue(upload);
//                        finish(); //업로드 끝나고 끄기
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(board_add.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                            if(progress==100){ // 업로드 성공하면서 나가기
                                finish();
                                Toast.makeText(board_add.this,"업로드 성공",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Toast.makeText(this, "파일이 선택 되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
