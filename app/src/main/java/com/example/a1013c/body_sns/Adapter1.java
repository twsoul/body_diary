package com.example.a1013c.body_sns;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

//몸 사진 + 메모
public class Adapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView Picture;

        TextView tvMeMo;
        TextView date;
        CheckBox del;

        MyViewHolder(View view){
            super(view);
            Picture = view.findViewById(R.id.iv_picture);

            tvMeMo = view.findViewById(R.id.tv_price);
            date = view.findViewById(R.id.tv_day);
            del = view.findViewById(R.id.checkBox);
        }
    }

    private ArrayList<rc_content_1> body_ArrayList;

    Adapter1(ArrayList<rc_content_1> foodInfoArrayList){
        this.body_ArrayList = foodInfoArrayList;
    }



    // 뷰 홀더 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 한줄을 v에 담아서
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle1_row, parent, false);

        // v 한줄 생성
        return new MyViewHolder(v);
    }

    //뷰 홀더와 뷰들을 연결
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int pos = position;

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.Picture.setImageURI(Uri.parse(body_ArrayList.get(position).body_img)); // 이미지 뷰홀더 uri로 받아서 넘겨라

        myViewHolder.tvMeMo.setText(body_ArrayList.get(position).memo);
        myViewHolder.date.setText(body_ArrayList.get(position).day);

//       myViewHolder.del.setChecked(body_ArrayList.get(position).isSelected);
        myViewHolder.del.setChecked(body_ArrayList.get(position).isSelected());
        myViewHolder.del.setTag(body_ArrayList.get(position));

        // 체크박스에 체크 될때 마다 포지션 전달.
        myViewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                rc_content_1 contact = (rc_content_1) cb.getTag();

                contact.setSelected(cb.isChecked());
                body_ArrayList.get(pos).setSelected(cb.isChecked());

            }
        });
    }

    @Override
    public int getItemCount() { //리스트 길이 만큼 생성 될수 있도록.
        return body_ArrayList.size();
    }





}