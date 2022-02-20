package com.example.popis;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_new_meal extends AppCompatActivity {

    Intent send_data;

    int chosendb; // 0 - workers, 1 - company, 2 - orders

    EditText worker_id,company_id,firstCourse,mainCourse,appetizer,dessert,drink;
    String mealDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        send_data = getIntent();

        mealDetails = "";

        worker_id = (EditText) findViewById(R.id.input0);
        company_id = (EditText) findViewById(R.id.input1);
        firstCourse = (EditText) findViewById(R.id.input2);
        mainCourse = (EditText) findViewById(R.id.input3);
        appetizer  = (EditText) findViewById(R.id.input4);
        dessert = (EditText) findViewById(R.id.input5);
        drink = (EditText) findViewById(R.id.input6);

    }

    public void back_to_db(View view) {
        finish();
    }

    public void save(View view) {
        if (firstCourse.getText().toString().matches("") && mainCourse.getText().toString().matches("")
                && appetizer.getText().toString().matches("") && dessert.getText().toString().matches("")
                && drink.getText().toString().matches("")){
            Toast.makeText(this, "fill at least one meal detail", Toast.LENGTH_SHORT).show();
        }
        else if (worker_id.getText().toString().length() != 9 || company_id.getText().toString().length() != 6){
            Toast.makeText(this, "Enter valid ids", Toast.LENGTH_SHORT).show();
        }
        else {
            mealDetails += firstCourse.getText().toString() + "\n";
            mealDetails += mainCourse.getText().toString() + "\n";
            mealDetails += appetizer.getText().toString() + "\n";
            mealDetails += dessert.getText().toString() + "\n";
            mealDetails += drink.getText().toString();

            send_data.putExtra("worker_id", worker_id.getText().toString());
            send_data.putExtra("company_id", company_id.getText().toString());
            send_data.putExtra("mealDetails", mealDetails);
            setResult(RESULT_OK, send_data);
            finish();
        }
    }
}