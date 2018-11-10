package com.example.a1013c.body_sns;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

//식단
public class Eat_adapter extends RecyclerView.Adapter<Eat_adapter.CustomViewHolder>  {

    private ArrayList<Eat_Dictionary> mList;
    private Context mContext;



    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 1. 리스너 추가

        protected TextView mId;
        protected TextView mEnglish;
        protected TextView mKorean;
        protected TextView mDate;


        public CustomViewHolder(View view) {
            super(view);

            this.mId = (TextView) view.findViewById(R.id.textview_recyclerview_id);
            this.mEnglish = (TextView) view.findViewById(R.id.textview_recyclerview_english);
            this.mKorean = (TextView) view.findViewById(R.id.textview_recyclerview_korean);
            this.mDate = view.findViewById(R.id.today_date);

            view.setOnCreateContextMenuListener(this); //2. 리스너 등록
        }




        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 메뉴 추가U

            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        // 4. 캔텍스트 메뉴 클릭시 동작을 설정
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                // 수정
                switch (item.getItemId()) {
                    case 1001:

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View view = LayoutInflater.from(mContext)
                                .inflate(R.layout.eat_edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                        // 아침,점심,저녁
                        final EditText editTextID = (EditText) view.findViewById(R.id.edittext_dialog_id);
                        final EditText editTextEnglish = (EditText) view.findViewById(R.id.edittext_dialog_endlish);
                        final EditText editTextKorean = (EditText) view.findViewById(R.id.edittext_dialog_korean);
                        final EditText editDate = view.findViewById(R.id.edittext_date); //날짜

                        editDate.setVisibility(View.VISIBLE);

                        editTextID.setText(mList.get(getAdapterPosition()).getBreakfast());
                        editTextEnglish.setText(mList.get(getAdapterPosition()).getLunch());
                        editTextKorean.setText(mList.get(getAdapterPosition()).getDinner());
                        editDate.setText(mList.get(getAdapterPosition()).getDate());

                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String strID = editTextID.getText().toString();
                                String strEnglish = editTextEnglish.getText().toString();
                                String strKorean = editTextKorean.getText().toString();
                                String strDate = editDate.getText().toString();

                                Eat_Dictionary dict = new Eat_Dictionary(strDate, strID, strEnglish, strKorean );

                                mList.set(getAdapterPosition(), dict);
                                notifyItemChanged(getAdapterPosition());

                                dialog.dismiss();
                            }
                        });

                        dialog.show();


                        break;

                    case 1002:

                        mList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), mList.size());

                        break;

                }
                return true;
            }
        };


    }



    public Eat_adapter(Context context, ArrayList<Eat_Dictionary> list) {
        mList = list;
        mContext = context;

    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eat_item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.mId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewholder.mEnglish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        viewholder.mKorean.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        viewholder.mId.setGravity(Gravity.CENTER);
        viewholder.mEnglish.setGravity(Gravity.CENTER);
        viewholder.mKorean.setGravity(Gravity.CENTER);

        viewholder.mId.setText(mList.get(position).getBreakfast());
        viewholder.mEnglish.setText(mList.get(position).getLunch());
        viewholder.mKorean.setText(mList.get(position).getDinner());
        viewholder.mDate.setText(mList.get(position).getDate());

    }


    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }



}
