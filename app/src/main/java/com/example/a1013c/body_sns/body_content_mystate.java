package com.example.a1013c.body_sns;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class body_content_mystate extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.body_my_state, container, false);
//        getPreferences();

        Button button = rootView.findViewById(R.id.log_out); //로그 아웃 버튼
        Button button1 = rootView.findViewById(R.id.change_image); //사진 올리기 버튼
        Button button2 = rootView.findViewById(R.id.do_edit);

//        Intent i = getActivity().getIntent();
//        byte[] arr = getActivity().getIntent().getByteArrayExtra("image");
//        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
//        ImageView BigImage = (ImageView)rootView.findViewById(R.id.profile_image);
//        BigImage.setImageBitmap(image);



        //로그 아웃 버튼 누르기
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    DialogSimple(); //로그아웃 한번 더 물어보기 다이얼로그.. 메소드 밑에 있음
            }
        });

        //사진 변경
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                Intent i = new Intent(getActivity(),MainActivity_camera.class);
//                startActivity(i);
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    PackageManager pm = getActivity().getPackageManager();

                    final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));

                    startActivity(intent);

                } catch (Exception e){ Log.i("TAG", "Unable to launch camera: " + e); }

            }
        });
        //편집 하기
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               Intent edit = new Intent(getActivity(),edit_profile.class);
               startActivityForResult(edit,858);
            }
        });


        return rootView;
    }


    public void DialogSimple(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        alt_bld.setMessage("로그아웃 하시겠습니까?").setCancelable(
                false).setPositiveButton("네",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        Intent a = new Intent(getActivity(),login_body.class);
                        startActivity(a);
                        Activity root = getActivity();
                        Toast.makeText(root, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().finish(); //프래그먼트 종료.
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
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




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(this.getClass().getSimpleName(), "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e(this.getClass().getSimpleName(), "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(this.getClass().getSimpleName(), "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("상태창", "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("상태창", "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("상태창", "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("상태창", "onDestroyView()");


    }

    @Override
    public void onDestroy() {
        Log.e("상태창", "onDestroy()");
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        Log.e("상태창", "onDetach()");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.e("상태창", "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }


}
