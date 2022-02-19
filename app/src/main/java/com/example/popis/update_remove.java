package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

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
    String id;

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
        id = come.getStringExtra("id");

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        chosendb = come.getIntExtra("chosendb",0);

        inp1 = (EditText) findViewById(R.id.input1);
        inp2 = (EditText) findViewById(R.id.input2);
        inp3 = (EditText) findViewById(R.id.input3);
        inp4 = (EditText) findViewById(R.id.input4);
        inp5 = (EditText) findViewById(R.id.input5);

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

            crsr = db.query(TABLE_WORKERS, null, WORKER_ID+"=?", new String[] {id}, null, null, null, null);

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

            crsr = db.query(TABLE_COMPANIES, null, SERIAL_ID+"=?", new String[] {id}, null, null, null, null);
            crsr.moveToFirst();
            inp1.setText(crsr.getString(1));
            inp2.setText(crsr.getString(2));
            inp3.setText(crsr.getString(3));
            inp4.setText(crsr.getString(4));

        }


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
        else {
            db = hlp.getWritableDatabase();
            db.delete(TABLE_WORKERS, WORKER_ID+"=?", new String[]{id});
            db.close();
            come.putExtra("first", inp1.getText().toString());
            come.putExtra("last", inp2.getText().toString());
            come.putExtra("company", inp3.getText().toString());
            come.putExtra("worker_id", inp4.getText().toString());
            come.putExtra("phone_number", inp5.getText().toString());
            if (active_switch.isChecked()){
                come.putExtra("active", 0);
            }
            else{
                come.putExtra("active", 1);
            }
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
            db = hlp.getWritableDatabase();
            db.delete(TABLE_COMPANIES, SERIAL_ID+"=?", new String[]{id});
            db.close();
            come.putExtra("name", inp1.getText().toString());
            come.putExtra("serial", inp2.getText().toString());
            come.putExtra("phone1", inp3.getText().toString());
            come.putExtra("phone2", inp4.getText().toString());
            if (active_switch.isChecked()){
                come.putExtra("active", 0);
            }
            else{
                come.putExtra("active", 1);
            }
            setResult(RESULT_OK, come);
            finish();
        }
    }


}