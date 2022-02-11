package com.example.popis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.popis.workers.*;

public class helperDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbexam.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    public helperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        strCreate="CREATE TABLE "+TABLE_WORKERS;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+FIRST_NAME+" TEXT,";
        strCreate+=" "+LAST_NAME+" TEXT,";
        strCreate+=" "+COMPANY+" TEXT,";
        strCreate+=" "+WORKER_ID+" TEXT,";
        strCreate+=" "+PHONE_NUMBER+" TEXT";
        strCreate+=");";
        System.out.println(strCreate);
        db.execSQL(strCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        strDelete="DROP TABLE IF EXISTS "+TABLE_WORKERS;
        db.execSQL(strDelete);

        onCreate(db);
    }

}
