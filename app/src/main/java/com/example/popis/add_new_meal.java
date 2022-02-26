package com.example.popis;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author paz malul
 *
 * when sent to this activity the user can enter data for a new order.
 */

public class add_new_meal extends AppCompatActivity {

    Intent send_data;

    EditText card_id,company_id,firstCourse,mainCourse,appetizer,dessert,drink;
    String mealDetails;

    SQLiteDatabase db;

    helperDB hlp;
    Cursor crsr;

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

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

    }

    /**
     * return to the database you came from
     * @param view
     */
    public void back_to_db(View view) {
        finish();
    }

    /**
     * check if the input matches up with the rules and send back to the database activity
     * @param view
     */
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
            db = hlp.getWritableDatabase();
            crsr = db.query(workers.TABLE_WORKERS, null, workers.KEY_ID+"=?", new String[] {card_id.getText().toString()}, null, null, null, "1");
            crsr.moveToFirst();

            if(crsr.getCount()==0){
                Toast.makeText(this, "food card doesn't exist", Toast.LENGTH_SHORT).show();
                db.close();
            }
            else{
                crsr = db.query(companies.TABLE_COMPANIES, null, companies.SERIAL_ID+"=?", new String[] {company_id.getText().toString()}, null, null, null, "1");
                crsr.moveToFirst();

                if(crsr.getCount()==0){
                    Toast.makeText(this, "company doesn't exist", Toast.LENGTH_SHORT).show();
                    db.close();
                }

                else {
                    db.close();
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
    }
}