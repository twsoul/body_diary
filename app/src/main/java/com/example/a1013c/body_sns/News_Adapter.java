package com.example.a1013c.body_sns;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class News_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

public static class MyViewHolder_4 extends RecyclerView.ViewHolder {
    ImageView ivPicture; //이미지
    TextView subject; //본문
    TextView times; //시간
    TextView news_name; //뉴스이름

    public MyViewHolder_4(View view){
        super(view);
        ivPicture = view.findViewById(R.id.news_img);
        subject = view.findViewById(R.id.subject);
        times = view.findViewById(R.id.times);
        news_name =view.findViewById(R.id.news_name);
    }
}

    private ArrayList<News_Info> news_ArrayList;
    private Context mContext;

    News_Adapter(Context context, ArrayList<News_Info> foodInfoArrayList){
        this.news_ArrayList = foodInfoArrayList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.news_row, parent, false);


        return new MyViewHolder_4(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder_4 myViewHolder = (MyViewHolder_4) holder;

//        myViewHolder.ivPicture.setImageURI(Uri.parse(news_ArrayList.get(position).drawabled));
        Picasso.with(mContext).load(news_ArrayList.get(position).drawabled).error(R.drawable.body_logo).into(myViewHolder.ivPicture);


        myViewHolder.subject.setText(news_ArrayList.get(position).subject);
        myViewHolder.times.setText(news_ArrayList.get(position).time);
        myViewHolder.news_name.setText(news_ArrayList.get(position).news_name);


        //이거 뭐지?
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewHolder.times.setText(String.valueOf(news_ArrayList.get(position).time));
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return news_ArrayList.size();
    }

}

