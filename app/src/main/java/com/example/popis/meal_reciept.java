package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.popis.orders.TABLE_ORDERS;
import static com.example.popis.orders.WORKER_ID;

public class meal_reciept extends AppCompatActivity {

    TextView compTitle, food, datest;
    Intent orderdata;

    SQLiteDatabase db;
    helperDB hlp;
    Cursor crsr;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_reciept);

        compTitle = (TextView) findViewById(R.id.comptitle);
        food = (TextView) findViewById(R.id.food);
        datest = (TextView) findViewById(R.id.datest);

        orderdata = getIntent();
        id = orderdata.getStringExtra("id");

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        System.out.println(id);

        db = hlp.getWritableDatabase();
        crsr = db.query(TABLE_ORDERS, null, WORKER_ID+"=?", new String[] {id}, null, null, null, "1");
        crsr.moveToFirst();

        compTitle.setText("RECEIPT:\n\tworker:" + crsr.getString(1) + "\n\tsupplier:" + crsr.getString(2));
        food.setText("ORDER:\n\t" + crsr.getString(4));
        datest.setText("AT: " + crsr.getString(3));

        db.close();
    }

    public void back_to_db(View view) {
        finish();
    }
}