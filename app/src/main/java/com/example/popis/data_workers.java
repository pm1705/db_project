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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.popis.workers.TABLE_WORKERS;

public class data_workers extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Intent input_intent, update_intent;
    String first_back,last_back,company_back,worker_id_back,phone_number_back;
    int active_back;
    SQLiteDatabase db;

    helperDB hlp;
    Cursor crsr;

    ListView data_display;
    ArrayAdapter adp;
    ArrayList<String> tbl;
    ArrayList<String> ids;

    AlertDialog.Builder sortby;
    String[] sort_options = {"Card Id", "First Name", "Last Name", "Id"};
    String[] sort_helpers = {workers.KEY_ID, workers.FIRST_NAME, workers.LAST_NAME, workers.WORKER_ID,};

    String[] show_options = {"First Name", "Last Name", "Company", "Id","Phone Number"};
    int sort_value, show_count;
    String sort_order;

    AlertDialog.Builder showthis;
    int[] show_list = {1,2,3,4,5};


    Button sort_button, show_button;
    ImageButton sort_order_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workers_data);

        input_intent = new Intent(this,add_new.class);
        update_intent = new Intent(this,update_remove.class);
        data_display = (ListView) findViewById(R.id.data_display);


        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        sort_value = 0;
        sort_order = "";
        sort_button = (Button) findViewById(R.id.sort_button);
        sort_order_button = (ImageButton) findViewById(R.id.sort_order_button);
        show_button = (Button) findViewById(R.id.show_button);

        data_display.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        data_display.setOnItemClickListener(this);


        sortby = new AlertDialog.Builder(this);
        sortby.setTitle("Sort workers By");
        sortby.setItems(sort_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sort_value = which;
                update_data(sort_value);
            }
        });

        showthis = new AlertDialog.Builder(this);
        showthis.setTitle("Show worker information:");
        showthis.setMultiChoiceItems(show_options ,null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b){
                    show_list[i] = i;
                }
                else{
                    show_list[i] = -1;
                }
            }
        });
        showthis.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int k) {
                update_data(sort_value);
                show_count = 0;
                for (int i=1;i<6;i++){
                    if (show_list[i-1] != -1){
                        show_count += 1;
                    }
                }
                if (show_count == 5){
                    show_button.setText("all fields");
                }
                else{
                    show_button.setText("" + show_count + " fields");
                }
                dialogInterface.cancel();
            }
        });

        update_data(sort_value);
    }

    public void back_home(View view) {
        finish();
    }

    public void add_input(View view) {
        input_intent.putExtra("chosendb",0);
        startActivityForResult(input_intent ,1);
    }

    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        if (data_back != null){
            first_back = data_back.getStringExtra("first");
            last_back = data_back.getStringExtra("last");
            company_back = data_back.getStringExtra("company");
            worker_id_back = data_back.getStringExtra("worker_id");
            phone_number_back = data_back.getStringExtra("phone_number");
            active_back = data_back.getIntExtra("active", 0);

            ContentValues cv = new ContentValues();

            cv.put(workers.FIRST_NAME, first_back);
            cv.put(workers.LAST_NAME, last_back);
            cv.put(workers.COMPANY, company_back);
            cv.put(workers.WORKER_ID, worker_id_back);
            cv.put(workers.PHONE_NUMBER, phone_number_back);
            cv.put(workers.ACTIVE, active_back);


            db = hlp.getWritableDatabase();

            db.insert(workers.TABLE_WORKERS, null, cv);

            db.close();

            update_data(sort_value);
        }
    }

    public void update_data(int sort){

        tbl = new ArrayList<>();
        ids = new ArrayList<>();

        db = hlp.getWritableDatabase();
        crsr = db.query(TABLE_WORKERS, null, null, null, null, null, sort_helpers[sort] + sort_order);


        sort_button.setText(sort_options[sort]);


        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {

            String tmp = crsr.getString(0) + ") ";

            for (int i=1;i<6;i++){
                if (show_list[i-1] != -1){
                    tmp += crsr.getString(i) + " ";
                }
            }

            if (crsr.getInt(6) == 0){
                tmp += "ACTIVE ";
            }
            else{
                tmp += "INACTIVE ";
            }

            ids.add(crsr.getString(4));
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

    public void change_order(View view) {
        if (sort_order == "") {
            sort_order = " DESC";
            sort_order_button.setImageResource(R.drawable.sort_down_dec);
        }
        else {
            sort_order = "";
            sort_order_button.setImageResource(R.drawable.sort_down_inc);
        }
        update_data(sort_value);
    }

    public void show_choose(View view) {
        show_list = new int[]{-1, -1, -1, -1, -1};
        AlertDialog show_now = showthis.create();
        show_now.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        update_intent.putExtra("id", ids.get(i));
        update_intent.putExtra("chosendb", 0);
        startActivityForResult(update_intent ,1);
    }
}