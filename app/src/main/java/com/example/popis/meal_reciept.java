package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.popis.orders.TABLE_ORDERS;

public class meal_reciept extends AppCompatActivity {

    TextView compTitle, food, datest;
    Intent orderdata;

    SQLiteDatabase db;
    helperDB hlp;
    Cursor crsr;

    String id, card, name, serial, comp_name, order, at;

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


        db = hlp.getWritableDatabase();

        crsr = db.query(TABLE_ORDERS, null, orders.KEY_ID+"=?", new String[] {id}, null, null, null, "1");
        crsr.moveToFirst();

        card = crsr.getString(1);
        serial = crsr.getString(2);
        order = crsr.getString(4);
        at = crsr.getString(3);

        crsr = db.query(workers.TABLE_WORKERS, null, workers.KEY_ID+"=?", new String[] {card}, null, null, null, "1");
        crsr.moveToFirst();

        name = crsr.getString(1) + " " + crsr.getString(2);

        crsr = db.query(companies.TABLE_COMPANIES, null, companies.SERIAL_ID+"=?", new String[] {serial}, null, null, null, "1");
        crsr.moveToFirst();

        comp_name = crsr.getString(1);

        compTitle.setText("RECEIPT:\n\tworker card:" + card + "\n\tworker name:" + name
                + "\n\tsupplier id:" + serial + "\n\tsupplier name:" + comp_name);
        food.setText("ORDER:\n" + order);
        datest.setText("AT: " + at);

        db.close();
    }

    public void back_to_db(View view) {
        finish();
    }
}