package com.example.popis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.popis.workers.*;
import static com.example.popis.companies.*;
import static com.example.popis.orders.*;

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
        strCreate+=" ("+workers.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+workers.FIRST_NAME+" TEXT,";
        strCreate+=" "+workers.LAST_NAME+" TEXT,";
        strCreate+=" "+workers.COMPANY+" TEXT,";
        strCreate+=" "+workers.WORKER_ID+" TEXT,";
        strCreate+=" "+workers.PHONE_NUMBER+" TEXT,";
        strCreate+=" "+workers.ACTIVE+" INTEGER"; // 0 - active, 1 - inactive
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_COMPANIES;
        strCreate+=" ("+companies.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+companies.COMAPNY_NAME+" TEXT,";
        strCreate+=" "+companies.SERIAL_ID+" TEXT,";
        strCreate+=" "+companies.PHONE_NUMBER+" TEXT,";
        strCreate+=" "+companies.SECOND_PHONE_NUMBER+" TEXT,";
        strCreate+=" "+companies.ACTIVE+" INTEGER"; // 0 - active, 1 - inactive
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+TABLE_ORDERS;
        strCreate+=" ("+orders.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+orders.WORKER_CARD_ID+" TEXT,";
        strCreate+=" "+orders.COMPANY_ID+" TEXT,";
        strCreate+=" "+orders.TIME+" TEXT,";
        strCreate+=" "+orders.MEAL_DETAILS+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        strDelete="DROP TABLE IF EXISTS "+TABLE_WORKERS;
        db.execSQL(strDelete);

        strDelete="DROP TABLE IF EXISTS "+TABLE_COMPANIES;
        db.execSQL(strDelete);

        strDelete="DROP TABLE IF EXISTS "+TABLE_ORDERS;
        db.execSQL(strDelete);

        onCreate(db);
    }

}
