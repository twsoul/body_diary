package com.example.a1013c.body_sns;

public class Eat_Dictionary {

    private String breakfast;
    private String lunch;
    private String dinner;
    private String date;

    boolean selected;

    public boolean isSelected1() {
        return selected;
    }
    public void setSelected1(boolean selected) {
        this.selected = selected;
    }
    // 1
    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String id) {
        this.breakfast = id;
    }

    // 2
    public String getLunch() {
        return lunch;
    }

    public void setLunch(String english) {
        lunch = english;
    }

    // 3
    public String getDinner() {
        return dinner;
    }

    public void setDinner(String korean) {
        dinner = korean;
    }

    // 4
    public String getDate() {
        return date;
    }

    public void setDate(String korean) {
        date = korean;
    }

    public Eat_Dictionary(String date,String id, String english, String korean) {
        this.date = date;
        this.breakfast = id;
        lunch = english;
        dinner = korean;
    }
}
