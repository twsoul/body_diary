package com.example.a1013c.body_sns;

import android.net.Uri;

public class Body_Img_Dictionary {

    private String img_img; //이미지 Uri
    private String img_memo;
    private String date;

    boolean selected;

    public boolean isSelected1() {
        return selected;
    }
    public void setSelected1(boolean selected) {
        this.selected = selected;
    }

    // 1
    public String getImg_img() {
        return img_img;
    }
    public void setImg_img(Uri id) {
        this.img_img = String.valueOf(id);
    }

    // 2
    public String getImg_memo() {
        return img_memo;
    }

    public void setImg_memo(String english) {
        img_memo = english;
    }

    // 4
    public String getDate() {
        return date;
    }

    public void setDate(String korean) {
        date = korean;
    }

    public Body_Img_Dictionary(String id, String english, String date) {
        this.date = date;
        this.img_img = id;
        img_memo = english;

    }
}

