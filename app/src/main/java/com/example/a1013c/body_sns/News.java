package com.example.a1013c.body_sns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class News extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;


    Context mContext;


    TextView textView; //결과를 띄어줄 TextView
    TextView reload; //reload버튼
    Elements contents;


    public String subject;
    public String img;
    public String news_name;
    public String times;

    public String web_uri;
    public String uri[] = new String[10];
    int count = 0;// uri 값 받기

    Document doc = null;
   String Top10;//본문 내용

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);


        //인터넷 연결 안됐을 때 처리
        NetworkInfo mNetworkState = getNetworkInfo();
        if(mNetworkState==null){
            Toast.makeText(this, "인터넷 연결이 없습니다. 확인 후 다시 시도 해주세요", Toast.LENGTH_SHORT).show();
            return;
        }


        mRecyclerView = findViewById(R.id.news_recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final ArrayList<News_Info> News_ArrayList = new ArrayList<>();


        // [건강정보] 텍스트
        textView =findViewById(R.id.textView);

        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) { // 백그라운드 개념

                try {
                    doc = Jsoup.connect("https://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=103&sid2=241").get(); //naver페이지를 불러옴

                    contents = doc.select("ul.type06_headline li"); //한개의 컬럼.

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Top10 = "건강 정보";

                for(Element element: contents) {
                    subject = element.select("dt a").text(); //제목

                    img = element.select("dt.photo img").attr("src"); //이미지 uri
                    if(img==""){ // 이미지 에러 발생 시키기
                        img="1";
                    }
                    news_name = element.select("dd span.writing").text(); //뉴스 이름
                    times = element.select("dd span.date.is_new").text(); //시간 여기가 비었으면 다른 시간을 넣으라고 만들어 줘야함 .select("dd span.date.is_outdated")

                    if(times ==""){ // 다른 시간!! 입력되게 하기
                        times = element.select("dd span.date.is_outdated").text();
                    }

                    web_uri = element.select("dt a").attr("href");


                    uri[count] = web_uri; // uri 배열에 웹uri 하나씩 넣기

                    count++;


                    News_ArrayList.add(new News_Info(img, subject, times, news_name));
                    Log.i("엘리먼트",""+ element);
                    Log.d("이미지",""+ img);
                    Log.i("뉴스 이름",""+ news_name);
                    Log.i("시간",""+ times);
                    Log.d("URI",""+ uri);

                }

                return null;
            }
            @Override
            protected void onPostExecute(Object o) { // 결과 표출
                super.onPostExecute(o);
                Log.i("TAG",""+Top10);
                textView.setText(Top10);

            }
        }.execute();




        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


        //클릭 이벤트 처리  --> 뉴스 기사 uri로 연결
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("원","클릭"+position);

                        //뉴스 uri로 넘기기
                        if(position==0){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[0]));
                            startActivity(i);
                        }else if(position==1){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[1]));
                            startActivity(i);
                        }else if(position==2){
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[2]));
                            startActivity(i);
                        }else if(position==3) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[3]));
                            startActivity(i);
                        }else if(position==4) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[4]));
                            startActivity(i);
                        }else if(position==5) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[5]));
                            startActivity(i);
                        }else if(position==6) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[6]));
                            startActivity(i);
                        }else if(position==7) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[7]));
                            startActivity(i);
                        }else if(position==8) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[8]));
                            startActivity(i);
                        }else if(position==9) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri[9]));
                            startActivity(i);
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                }));



        News_Adapter myAdapter = new News_Adapter(this,News_ArrayList);
        mRecyclerView.setAdapter(myAdapter);

    }

    // 네트워크 상태 받아오기
   private NetworkInfo getNetworkInfo(){
       ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo  = connectivityManager.getActiveNetworkInfo();
       return networkInfo;
   }


}

