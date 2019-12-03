package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="userInfo.db";
    private static final int DB_VERSION=1;


    public static String CREATE_SQL="create table userInfoList (_membershipNumber integer primary key autoincrement," +
            "id text," +
            "password text," +
            "nicname text," +
            "name text," +
            "email text," +//5
            "matchHistory integer," +
            "win integer," +
            "draw integer," +
            "lose integer," +
            "rankPoint integer)";

    public static String DELETE_SQL="drop table if exists userInfoList";

    public dbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DELETE_SQL);
        onCreate(db);
    }
}
