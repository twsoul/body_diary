package com.example.a1013c.body_sns;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;


public class Alarm_TimePicker extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int Hour = c.get(Calendar.HOUR_OF_DAY); //시간
        int Minute = c.get(Calendar.MINUTE); //분

        // 시간, 분, 시간 나타내는 형식
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(),
                Hour, Minute, android.text.format.DateFormat.is24HourFormat(getContext()));

        return mTimePickerDialog;
    }

    //타임피커에서 지정한 시간 --> 보내주는 것 같음.
    //text뷰 찾아서 리싸이클러 묶어주기


}
