package com.example.a1013c.body_sns.Board;

public class Upload {
    private  String mSubject;
    private  String mImageUri;
    private  String mNickname;

    public Upload(){
        //이거 필요함
    }

    public Upload(String subject,String imageUri,String nickname){
        if(subject.trim().equals("")){
            subject ="제목 없음";
        }
        mSubject = subject;
        mImageUri = imageUri;
        mNickname = nickname;
    }

    //게터세터
    public String getSub(){
        return mSubject;
    } // 여기임..
    public void setSub(String subject){
        mSubject = subject;
    }

    public String getImageUri(){
        return mImageUri;
    }
    public void setImageUri(String imageUri){
        mImageUri = imageUri;
    }

    public String getNickname(){
        return mNickname;
    }
    public void setNickname(String nickname){
        mNickname = nickname;
    }
}
