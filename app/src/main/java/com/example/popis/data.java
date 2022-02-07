package com.example.popis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.popis.helperDB.*;

import java.util.ArrayList;

import static com.example.popis.workers.TABLE_WORKERS;

public class data extends AppCompatActivity {

    Intent input_intent;
    String first_back,last_back,company_back,worker_id_back,phone_number_back;
    SQLiteDatabase db;

    helperDB hlp;
    Cursor crsr;

    ListView data_display;
    ArrayAdapter adp;
    ArrayList<String> tbl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        input_intent = new Intent(this,add_new.class);
        data_display = (ListView) findViewById(R.id.data_display);

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        update_data();

    }

    public void back_home(View view) {
        finish();
    }

    public void add_input(View view) {
        startActivityForResult(input_intent ,1);
    }

    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        if (data_back != null) {
            first_back = data_back.getStringExtra("first");
            last_back = data_back.getStringExtra("last");
            company_back = data_back.getStringExtra("company");
            worker_id_back = data_back.getStringExtra("worker_id");
            phone_number_back = data_back.getStringExtra("phone_number");
            System.out.println(first_back+last_back+company_back+worker_id_back+phone_number_back);

            ContentValues cv = new ContentValues();

            cv.put(workers.FIRST_NAME, first_back);
            cv.put(workers.LAST_NAME, last_back);
            cv.put(workers.COMPANY, company_back);
            cv.put(workers.WORKER_ID, worker_id_back);
            cv.put(workers.PHONE_NUMBER, phone_number_back);


            db = hlp.getWritableDatabase();

            db.insert(workers.TABLE_WORKERS, null, cv);

            db.close();

            update_data();
        }
    }

    public void update_data(){
        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();

        crsr = db.query(TABLE_WORKERS, null, null, null, null, null, null);
        int first_name_col = crsr.getColumnIndex(workers.FIRST_NAME);
        int last_name_col = crsr.getColumnIndex(workers.LAST_NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String first = crsr.getString(first_name_col);
            String last = crsr.getString(last_name_col);
            String tmp = "" + first + " - " + last;
            tbl.add(tmp);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        data_display.setAdapter(adp);

    }
}