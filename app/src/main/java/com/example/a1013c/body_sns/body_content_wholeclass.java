package com.example.a1013c.body_sns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class body_content_wholeclass extends Fragment { //전체 클래스


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.body_wholeclass, container, false);

        ImageView button = rootView.findViewById(R.id.profile_image); //프로필 사진 버튼 누르기
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent a = new Intent(getActivity(),other_profile.class); //다른사람 프로필 열림
                startActivity(a);

            }
        });
        TextView button1 = rootView.findViewById(R.id.profile_name); //프로필 이름 버튼 누르기
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent a = new Intent(getActivity(),other_profile.class); //다른사람 프로필 열림
                startActivity(a);
//                onPause();
            }
        });



        return rootView;

    }


}
