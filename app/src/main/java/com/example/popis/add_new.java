package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class add_new extends AppCompatActivity {

    Intent send_data;

    int chosendb; // 0 - workers, 1 - company, 2 - orders

    EditText inp1,inp2,inp3,inp4,inp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        send_data = getIntent();

        chosendb = send_data.getIntExtra("chosendb",0);

        inp1 = (EditText) findViewById(R.id.input0);
        inp2 = (EditText) findViewById(R.id.input2);
        inp3 = (EditText) findViewById(R.id.input4);
        inp4 = (EditText) findViewById(R.id.input5);
        inp5 = (EditText) findViewById(R.id.input6);

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
            send_data.putExtra("first", inp1.getText().toString());
            send_data.putExtra("last", inp2.getText().toString());
            send_data.putExtra("company", inp3.getText().toString());
            send_data.putExtra("worker_id", inp4.getText().toString());
            send_data.putExtra("phone_number", inp5.getText().toString());
            send_data.putExtra("add",1);
            setResult(RESULT_OK, send_data);
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
            send_data.putExtra("name", inp1.getText().toString());
            send_data.putExtra("serial", inp2.getText().toString());
            send_data.putExtra("phone1", inp3.getText().toString());
            send_data.putExtra("phone2", inp4.getText().toString());
            send_data.putExtra("add",1);
            setResult(RESULT_OK, send_data);
            finish();
        }
    }
}