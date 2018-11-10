package com.example.a1013c.body_sns;

import android.net.Uri;
import android.provider.BaseColumns;

public class Test_Contract {

    private  Test_Contract(){}

    public static final class TestEntry implements BaseColumns{
        public static final String TABLE_NAME = "eatList";
        public static final String COLUMN_TITLE = "breakfast";
        public static final String COLUMN_CONTENTS = "lunch";
        public static final String COLUMN_DATE = "dinner";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}

