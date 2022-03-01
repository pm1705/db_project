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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.popis.workers.TABLE_WORKERS;

/**
 * @author paz malul
 *
 * a hub for displaying, sorting, adding, and updating the databse of the workers.
 */

public class data_workers extends AppCompatActivity implements AdapterView.OnItemClickListener{

    Intent input_intent, update_intent;
    String first_back,last_back,company_back,worker_id_back,phone_number_back;
    int add_back;
    SQLiteDatabase db;

    helperDB hlp;
    Cursor crsr;

    ListView data_display;
    ArrayAdapter adp;
    ArrayList<String> tbl;
    ArrayList<String> keys;

    AlertDialog.Builder sortby;
    String[] sort_options = {"Card Id", "First", "Last", "Id"};
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

    /**
     * return to home screen
     * @param view
     */
    public void back_home(View view) {
        finish();
    }

    /**
     * launch add_new activity with 0 paramater meaning we want to add a worker
     * @param view
     */
    public void add_input(View view) {
        input_intent.putExtra("chosendb",0);
        startActivityForResult(input_intent ,1);
    }

    /**
     * this function will be called from two diffrent activities
     * if called from the add_new activity the information sent will be added to the db as a new line
     * if from the update_remove activity the information was already updated so nothing will happen
     * except the update data so all displayed data is up to date.
     * @param source i couldn't understand how this worked so i added an extra for what activity
     * @param good
     * @param data_back
     */
    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        if (data_back != null){
            add_back = data_back.getIntExtra("add", 0);
            if (add_back == 1){
                first_back = data_back.getStringExtra("first");
                last_back = data_back.getStringExtra("last");
                company_back = data_back.getStringExtra("company");
                worker_id_back = data_back.getStringExtra("worker_id");
                phone_number_back = data_back.getStringExtra("phone_number");

                ContentValues cv = new ContentValues();

                cv.put(workers.FIRST_NAME, first_back);
                cv.put(workers.LAST_NAME, last_back);
                cv.put(workers.COMPANY, company_back);
                cv.put(workers.WORKER_ID, worker_id_back);
                cv.put(workers.PHONE_NUMBER, phone_number_back);
                cv.put(workers.ACTIVE, 0);


                db = hlp.getWritableDatabase();

                db.insert(workers.TABLE_WORKERS, null, cv);

                db.close();
            }
            update_data(sort_value);
        }
    }

    /**
     * sort the database according to the sort list, show only the wanted fields and display them in table.
     * @param sort the sort list
     */
    public void update_data(int sort){

        tbl = new ArrayList<>();
        keys = new ArrayList<>();

        db = hlp.getWritableDatabase();
        crsr = db.query(TABLE_WORKERS, null, null, null, null, null, sort_helpers[sort] + sort_order);


        sort_button.setText(sort_options[sort]);


        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {

            String tmp = "[" + crsr.getString(0) + "] ";

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

            keys.add(crsr.getString(0));
            tbl.add(tmp);
            crsr.moveToNext();
        }

        crsr.close();
        db.close();

        adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        data_display.setAdapter(adp);

    }

    /**
     * bring up dialog box to choose sorting options
     * @param view
     */
    public void sort_choose(View view) {
        AlertDialog sort_now = sortby.create();
        sort_now.show();
    }

    /**
     * flips the current sorting order from a->z or z->a
     * @param view
     */
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

    /**
     * reset all show variables and launch dialog to choose them again
     * @param view
     */
    public void show_choose(View view) {
        show_list = new int[]{-1, -1, -1, -1, -1};
        AlertDialog show_now = showthis.create();
        show_now.show();
    }

    /**
     * when item is clicked send the paramaters to the update activity
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        update_intent.putExtra("key", keys.get(i));
        update_intent.putExtra("chosendb", 0);
        startActivityForResult(update_intent, 1);
    }

    /**
     * menu funcs
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.credits){
            Intent creds = new Intent(this,creditscreen.class);
            startActivity(creds);
        }

        else if (id == R.id.home){
            finish();
        }

        return true;
    }
}