package com.example.popis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.popis.helperDB.*;

import java.util.ArrayList;

import static com.example.popis.workers.COMPANY;
import static com.example.popis.workers.FIRST_NAME;
import static com.example.popis.workers.LAST_NAME;
import static com.example.popis.workers.TABLE_WORKERS;
import static com.example.popis.workers.WORKER_ID;

public class data extends AppCompatActivity {

    Intent input_intent;
    String first_back,last_back,company_back,worker_id_back,phone_number_back;
    SQLiteDatabase db;

    helperDB hlp;
    Cursor crsr;

    ListView data_display;
    ArrayAdapter adp;
    ArrayList<String> tbl = new ArrayList<>();

    AlertDialog.Builder sortby;
    String[] sort_options = {"First Name", "Last Name", "Id", "Company"};
    String[] sort_helpers = {workers.FIRST_NAME, workers.LAST_NAME, workers.WORKER_ID, workers.COMPANY};
    String sort_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        input_intent = new Intent(this,add_new.class);
        data_display = (ListView) findViewById(R.id.data_display);


        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        sort_value = workers.WORKER_ID;

        sortby = new AlertDialog.Builder(this);
        sortby.setTitle("Sort workers By");
        sortby.setItems(sort_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sort_value = sort_helpers[which];
                update_data(sort_value);
            }
        });


        update_data(sort_value);
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

            update_data(sort_value);
        }
    }

    public void update_data(String sort){
        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();

        crsr = db.query(TABLE_WORKERS, null, null, null, null, null, null);
        int first_name_col = crsr.getColumnIndex(workers.FIRST_NAME);
        int last_name_col = crsr.getColumnIndex(workers.LAST_NAME);
        int id_col = crsr.getColumnIndex(workers.WORKER_ID);
        int company_col = crsr.getColumnIndex(workers.COMPANY);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String first = crsr.getString(first_name_col);
            String last = crsr.getString(last_name_col);
            String id = crsr.getString(id_col);
            String company = crsr.getString(company_col);
            String tmp = "" + first + " - " + last + " - " + id + " - " + company;
            tbl.add(tmp);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        data_display.setAdapter(adp);

    }

    public void sort_choose(View view) {
        AlertDialog sort_now = sortby.create();
        sort_now.show();

    }
}