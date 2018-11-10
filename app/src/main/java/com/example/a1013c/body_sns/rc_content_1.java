package com.example.a1013c.body_sns;

import android.net.Uri;
import android.widget.ImageView;

public class rc_content_1 {
    public String body_img;

    public String memo;
    public String day;
    public boolean isSelected;

    public rc_content_1(String body_img, String memo, String day, boolean isSelected){
        this.body_img =body_img;

        this.memo = memo;
        this.day = day;
        this.isSelected =isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getBody_img() {
        return body_img;
    }
    public String getmemo() {
        return memo;
    }
    public String getday() {
        return day;
    }
}
