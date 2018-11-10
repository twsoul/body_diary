package com.example.a1013c.body_sns;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Wannabe_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPicture; //이미지

        public ViewHolder(View view){
            super(view);
            ivPicture = view.findViewById(R.id.wan_image);
        }
    }

    private ArrayList<Wanna_Info> mArrayList;
    private Context mContext;

    Wannabe_Adapter(Context context, ArrayList<Wanna_Info> wanna){
        this.mArrayList = wanna;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.wannabe_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder myViewHolder = (ViewHolder) holder;

        Picasso.with(mContext).load(mArrayList.get(position).drawabled).error(R.drawable.body_logo).into(myViewHolder.ivPicture);
//        Log.d("어뎁터에 이미지", mArrayList.get(position).drawabled);



//        //이거 뭐지?
//        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                myViewHolder.times.setText(String.valueOf(mArrayList.get(position).time));
////                notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

}