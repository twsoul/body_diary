package com.example.a1013c.body_sns.Board;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a1013c.body_sns.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

//안녕
public class board_adapter extends RecyclerView.Adapter<board_adapter.board_ViewHolder> {

    private Context mContext;
    private List<Upload> mUpload;
    private  OnItemClickListener mListener;

    public board_adapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUpload = uploads;
    }

    public class board_ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,
    View.OnCreateContextMenuListener{
        public TextView Subject;
        public ImageView imgview;
        public TextView nickname;
        public ProgressBar progressBar;

        public board_ViewHolder(View itemView) {
            super(itemView);

            Subject = itemView.findViewById(R.id.row_subject);
            imgview = itemView.findViewById(R.id.row_image_upload);
            nickname = itemView.findViewById(R.id.nick_name);
            progressBar = itemView.findViewById(R.id.img_progress);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Slect Action");
//            MenuItem doWhatever =menu
        }
    }

    @Override
    public board_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_board_row, parent, false);
        return new board_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final board_ViewHolder holder, int position) {

        Upload uploadCurrent = mUpload.get(position);

        holder.Subject.setText(uploadCurrent.getSub()); //제목
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUri()) //이미지
                .placeholder(R.drawable.body_logo)
//                .fit()
//                .centerCrop()
                .into(holder.imgview, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.nickname.setText(uploadCurrent.getNickname()); // 닉네임 설정.
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
