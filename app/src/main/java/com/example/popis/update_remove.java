package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class update_remove extends AppCompatActivity {

    Intent send_data;
    EditText first,last,company,worker_id,phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_remove);

        first = (EditText) findViewById(R.id.first_input);
        last = (EditText) findViewById(R.id.last_input);
        company = (EditText) findViewById(R.id.company_input);
        worker_id = (EditText) findViewById(R.id.id_input);
        phone_number = (EditText) findViewById(R.id.phone_input);
    }

    public void back_to_db(View view) {
        finish();
    }

    public void save(View view) {
        if (first.getText().toString().matches("") || last.getText().toString().matches("") ||
                company.getText().toString().matches("") || worker_id.getText().toString().matches("") ||
                phone_number.getText().toString().matches("")){

            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
        }
        else if (worker_id.getText().toString().length() != 9){
            Toast.makeText(this, "Enter a valid ID.", Toast.LENGTH_SHORT).show();
        }
        else if (phone_number.getText().toString().length() != 9 || phone_number.getText().toString().length() != 10){
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            send_data.putExtra("first", first.getText().toString());
            send_data.putExtra("last", last.getText().toString());
            send_data.putExtra("company", company.getText().toString());
            send_data.putExtra("worker_id", worker_id.getText().toString());
            send_data.putExtra("phone_number", phone_number.getText().toString());
            setResult(RESULT_OK, send_data);
            finish();
        }

    }


}