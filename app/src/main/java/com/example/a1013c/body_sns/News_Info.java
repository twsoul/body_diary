package com.example.a1013c.body_sns;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class News_Info {
    public String drawabled;
    public String subject;
    public String time;
    public String news_name;

    public News_Info(String drawabled, String subject, String time, String news_name){
        this.drawabled = drawabled;
        this.subject = subject;
        this.time = time;
        this.news_name = news_name;
    }
}
