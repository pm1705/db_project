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

    EditText card_id,company_id,firstCourse,mainCourse,appetizer,dessert,drink;
    String mealDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        send_data = getIntent();

        mealDetails = "";

        card_id = (EditText) findViewById(R.id.input0);
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
        else if (company_id.getText().toString().length() != 6 || card_id.getText().toString().matches("")){
            Toast.makeText(this, "Enter valid ids", Toast.LENGTH_SHORT).show();
        }
        else {
            mealDetails += " First Course: " + firstCourse.getText().toString() + "\n";
            mealDetails += " Main Course: " + mainCourse.getText().toString() + "\n";
            mealDetails += " Appetizer: " + appetizer.getText().toString() + "\n";
            mealDetails += " Dessert: " + dessert.getText().toString() + "\n";
            mealDetails += " Drink: " + drink.getText().toString();

            send_data.putExtra("card_id", card_id.getText().toString());
            send_data.putExtra("company_id", company_id.getText().toString());
            send_data.putExtra("mealDetails", mealDetails);
            setResult(RESULT_OK, send_data);
            finish();
        }
    }
}