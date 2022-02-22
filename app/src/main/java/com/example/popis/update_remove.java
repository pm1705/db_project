package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.popis.workers.*;

import static com.example.popis.companies.*;


public class update_remove extends AppCompatActivity {

    Intent come;
    EditText inp1,inp2,inp3,inp4,inp5;
    String key;

    int chosendb; // 0 - workers, 1 - company

    SQLiteDatabase db;
    helperDB hlp;
    Cursor crsr;

    Switch active_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_remove);

        come = getIntent();
        key = come.getStringExtra("key");

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        chosendb = come.getIntExtra("chosendb",0);

        inp1 = (EditText) findViewById(R.id.input0);
        inp2 = (EditText) findViewById(R.id.input2);
        inp3 = (EditText) findViewById(R.id.input4);
        inp4 = (EditText) findViewById(R.id.input5);
        inp5 = (EditText) findViewById(R.id.input6);

        active_switch = (Switch) findViewById(R.id.active_switch);
        active_switch.setChecked(true);

        db = hlp.getWritableDatabase();

        if (chosendb == 0) {
            inp1.setInputType(InputType.TYPE_CLASS_TEXT);
            inp1.setHint("First Name");

            inp2.setInputType(InputType.TYPE_CLASS_TEXT);
            inp2.setHint("Last Name");

            inp3.setInputType(InputType.TYPE_CLASS_TEXT);
            inp3.setHint("Company");

            inp4.setInputType(InputType.TYPE_CLASS_NUMBER);
            inp4.setHint("Worker ID");

            inp5.setInputType(InputType.TYPE_CLASS_NUMBER);
            inp5.setHint("Phone Number");

            crsr = db.query(TABLE_WORKERS, null, workers.KEY_ID+"=?", new String[] {key}, null, null, null, null);

            crsr.moveToFirst();
            inp1.setText(crsr.getString(1));
            inp2.setText(crsr.getString(2));
            inp3.setText(crsr.getString(3));
            inp4.setText(crsr.getString(4));
            inp5.setText(crsr.getString(5));
            db.close();
        }
        else if (chosendb == 1){
            inp1.setInputType(InputType.TYPE_CLASS_TEXT);
            inp1.setHint("Company Name");

            inp2.setInputType(InputType.TYPE_CLASS_TEXT);
            inp2.setHint("Serial ID");

            inp3.setInputType(InputType.TYPE_CLASS_NUMBER);
            inp3.setHint("First Phone");

            inp4.setInputType(InputType.TYPE_CLASS_NUMBER);
            inp4.setHint("Second Phone");

            inp5.setVisibility(View.INVISIBLE);

            crsr = db.query(TABLE_COMPANIES, null, companies.KEY_ID+"=?", new String[] {key}, null, null, null, null);
            crsr.moveToFirst();
            inp1.setText(crsr.getString(1));
            inp2.setText(crsr.getString(2));
            inp3.setText(crsr.getString(3));
            inp4.setText(crsr.getString(4));

        }


    }

    public static boolean is_valid_id(String str){
        int last_dig = Integer.parseInt(String.valueOf(str.charAt(8)));
        int sum = 0;
        for (int i=0;i<8;i+=2){
            sum = sum + Integer.parseInt(String.valueOf(str.charAt(i))) * 1;
            if (Integer.parseInt(String.valueOf(str.charAt(i+1))) * 2 < 10){
                sum = sum + Integer.parseInt(String.valueOf(str.charAt(i+1))) * 2;
            }
            else{
                sum = sum + 1;
                sum = sum + (Integer.parseInt(String.valueOf(str.charAt(i+1))) * 2)-10;
            }
        }
        if (((last_dig) + sum)%10 == 0){
            return true;
        }
        return false;
    }

    public void back_to_db(View view) {
        finish();
    }

    public void save(View view) {
        if (chosendb == 0) {
            save_worker(view);
        }
        else if(chosendb == 1){
            save_company(view);
        }
    }

    public void save_worker(View view) {
        if (inp1.getText().toString().matches("") || inp2.getText().toString().matches("") ||
                inp3.getText().toString().matches("") || inp4.getText().toString().matches("") ||
                inp5.getText().toString().matches("")){

            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
        else if (inp4.getText().toString().length() != 9){
            Toast.makeText(this, "Enter a valid ID.", Toast.LENGTH_SHORT).show();
        }
        else if (inp5.getText().toString().length() != 10 && inp5.getText().toString().length() != 9){
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else if (!is_valid_id(inp4.getText().toString())){
            Toast.makeText(this, "Enter a valid id number", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues cv = new ContentValues();

            cv.put(workers.FIRST_NAME, inp1.getText().toString());
            cv.put(workers.LAST_NAME, inp2.getText().toString());
            cv.put(workers.COMPANY, inp3.getText().toString());
            cv.put(workers.WORKER_ID, inp4.getText().toString());
            cv.put(workers.PHONE_NUMBER, inp5.getText().toString());
            if (active_switch.isChecked()){
                cv.put(workers.ACTIVE, 0);
            }
            else{
                cv.put(workers.ACTIVE, 1);
            }


            db = hlp.getWritableDatabase();
            db.update(TABLE_WORKERS, cv, workers.KEY_ID+"=?", new String[]{key});
            db.close();

            come.putExtra("add",0);
            setResult(RESULT_OK, come);

            finish();
        }

    }

    public void save_company(View view) {
        if (inp1.getText().toString().matches("") || inp2.getText().toString().matches("") ||
                inp3.getText().toString().matches("") || inp4.getText().toString().matches("")){
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
        else if (inp3.getText().toString().length() != 9 && inp4.getText().toString().length() != 9){
            Toast.makeText(this, "Enter valid phone Numbers", Toast.LENGTH_SHORT).show();
        }
        else {

            ContentValues cv = new ContentValues();

            cv.put(companies.COMAPNY_NAME, inp1.getText().toString());
            cv.put(companies.SERIAL_ID, inp2.getText().toString());
            cv.put(companies.PHONE_NUMBER, inp3.getText().toString());
            cv.put(companies.SECOND_PHONE_NUMBER, inp4.getText().toString());
            if (active_switch.isChecked()){
                cv.put(companies.ACTIVE, 0);
            }
            else{
                cv.put(companies.ACTIVE, 1);
            }

            db = hlp.getWritableDatabase();
            db.update(TABLE_COMPANIES, cv, companies.KEY_ID+"=?", new String[]{key});
            db.close();

            come.putExtra("add",0);
            setResult(RESULT_OK, come);
            finish();
        }
    }


}