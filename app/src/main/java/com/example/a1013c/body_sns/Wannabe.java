package com.example.a1013c.body_sns;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Wannabe extends AppCompatActivity {


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Wanna_Info> mArrayList;
    public  String i[] = new String[50];

    Document doc = null;
    Elements contents;
    String img;
    int count = 0;

    String title;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wannabe_main);


        mRecyclerView = findViewById(R.id.wannabe_rc);
        mRecyclerView.setHasFixedSize(true);

//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mArrayList = new ArrayList<>();


        textView =findViewById(R.id.textView23);
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) { // 백그라운드 개념
                try {
                    //구글 검색--> 남자 운동 자극 사진
                    doc = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=image&query=%EB%82%A8%EC%9E%90+%EC%9A%B4%EB%8F%99+" +
                            "%EC%9E%90%EA%B7%B9+%EC%82%AC%EC%A7%84&oquery=%EC%9A%B4%EB%8F%99+%EC%9E%90%EA%B7%B9+%EC%82%AC%EC%A7%84&tqi=TH06AwpySERsstHy13CssssstWh-210876").get();
                    Log.d("네이버", String.valueOf(doc));

                    contents = doc.select("div.photo_grid._box img._img"); //이미지 뭉치

                    Log.d("네이버 이미지 뭉치", String.valueOf(contents));


                } catch (IOException e) {
                    e.printStackTrace();
                }

                title ="운동 자극 사진";
                for(Element element: contents) {
                    img = element.attr("data-source"); //이미지

                    Log.d("몸 이미지", String.valueOf(img));


                    i[count] = img;
                    mArrayList.add(new Wanna_Info(i[count]));
                    if(i[count]==null){
                        break;
                    }
                    count++;
                }


                return null;
            }
            @Override
            protected void onPostExecute(Object o) { // 결과 표출
                super.onPostExecute(o);
                textView.setText(title);

            }
        }.execute();



        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {
                        Log.d("원","클릭"+position);

                        AlertDialog.Builder alt_bld = new AlertDialog.Builder(Wannabe.this);
                        alt_bld.setMessage((position+1) +" 번째 사진을 다운도르 하시겠습니까?").setCancelable(
                                false).setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'Yes' Button
                                        Toast.makeText(getApplicationContext(),(position+1) + " 번째 사진 다운로드 시작." ,Toast.LENGTH_SHORT).show();
                                        String down_img = i[position];
                                        new ImageDownload().execute(down_img);
                                    }
                                }).setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'Yes' Button
                                       dialog.cancel();

                                    }
                                });
                        AlertDialog alert = alt_bld.create();
                        // Title for AlertDialog
                        alert.setTitle("외부 이미지 다운로드");
                        // Icon for AlertDialog
                        alert.setIcon(R.drawable.body_logo);
                        alert.show();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                }));


        Wannabe_Adapter myAdapter = new Wannabe_Adapter(this,mArrayList);
        mRecyclerView.setAdapter(myAdapter);
    }


    private class ImageDownload extends AsyncTask<String, Void, Void> {



        /**

         * 파일명

         */

        private String fileName;



        /**

         * 저장할 폴더

         */

        private final String SAVE_FOLDER = "/Wannabe";



        @Override

        protected Void doInBackground(String... params) {

            //다운로드 경로를 지정

            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;



            File dir = new File(savePath);

            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {

                dir.mkdirs();

            }



            //파일 이름 :날짜_시간

            Date day = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);

            fileName = String.valueOf(sdf.format(day));



            //웹 서버 쪽 파일이 있는 경로

            String fileUrl = params[0];



            //다운로드 폴더에 동일한 파일명이 존재하는지 확인

            if (new File(savePath + "/" + fileName).exists() == false) {

            } else {

            }



            String localPath = savePath + "/" + fileName + ".jpg";



            try {

                URL imgUrl = new URL(fileUrl);

                //서버와 접속하는 클라이언트 객체 생성

                HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();

                int len = conn.getContentLength();

                byte[] tmpByte = new byte[len];

                //입력 스트림을 구한다

                InputStream is = conn.getInputStream();

                File file = new File(localPath);

                //파일 저장 스트림 생성

                FileOutputStream fos = new FileOutputStream(file);

                int read;

                //입력 스트림을 파일로 저장

                for (;;) {

                    read = is.read(tmpByte);

                    if (read <= 0) {

                        break;

                    }

                    fos.write(tmpByte, 0, read); //file 생성

                }

                is.close();

                fos.close();

                conn.disconnect();

            } catch (Exception e) {

                e.printStackTrace();

            }


            return null;

        }



        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);



//            저장한 이미지 열기

            Intent i = new Intent(Intent.ACTION_VIEW);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;

            File file = new File(targetDir + "/" + fileName + ".jpg");

            //이미지 스캔해서 갤러리 업데이트

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            Toast.makeText(getApplicationContext(), " 다운로드 완료" ,Toast.LENGTH_SHORT).show();

        }

    }







//    public class img_crawling extends AsyncTask<Integer, Integer, Void> {
//
//        Document doc = null;
//        Elements contents;
//        String img;
//        int count = 0;
//
//        @Override
//        protected Void doInBackground(Integer... Void) {
//            try {
//                //구글 검색--> 남자 운동 자극 사진
//                doc = Jsoup.connect("https://search.naver.com/search.naver?sm=tab_hty.top&where=image&query=%EB%82%A8%EC%9E%90+%EC%9A%B4%EB%8F%99+" +
//                        "%EC%9E%90%EA%B7%B9+%EC%82%AC%EC%A7%84&oquery=%EC%9A%B4%EB%8F%99+%EC%9E%90%EA%B7%B9+%EC%82%AC%EC%A7%84&tqi=TH06AwpySERsstHy13CssssstWh-210876").get();
//                Log.d("네이버", String.valueOf(doc));
//
//                contents = doc.select("div.photo_grid._box img._img"); //이미지 뭉치
//
//                Log.d("네이버 이미지 뭉치", String.valueOf(contents));
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            for(Element element: contents) {
//                img = element.attr("data-source"); //이미지
//                String a =element.absUrl("src");
//                Log.d("몸 이미지", String.valueOf(img));
//
//
//                i[count] = img;
//                mArrayList.add(new Wanna_Info(i[count]));
//                    if(i[count]==null){
//                    break;
//                }
//                count++;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//    }

}
