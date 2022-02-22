package com.example.popis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.popis.orders.*;

public class MainActivity extends AppCompatActivity {

    Intent worker_intent, company_intent, orders_intent, add_intent;
    String card_id_back,company_id_back,meal_details;

    SQLiteDatabase db;
    helperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        worker_intent = new Intent(this, data_workers.class);
        company_intent = new Intent(this, data_companies.class);
        orders_intent = new Intent(this, data_orders.class);
        add_intent = new Intent(this, add_new_meal.class);

        hlp = new helperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
    }

    public void show_workers(View view) {
        startActivity(worker_intent);
    }

    public void show_companies(View view) { startActivity(company_intent); }

    public void show_orders(View view) { startActivity(orders_intent); }

    public void add_order(View view) {  startActivityForResult(add_intent, 0); }

    @Override
    protected void onActivityResult(int source, int good, @Nullable Intent data_back) {
        super.onActivityResult(source, good, data_back);
        System.out.println(data_back);
        if (data_back != null){
            card_id_back = data_back.getStringExtra("card_id");
            company_id_back = data_back.getStringExtra("company_id");
            meal_details = data_back.getStringExtra("mealDetails");
            System.out.println(meal_details);

            ContentValues cv = new ContentValues();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            cv.put(orders.WORKER_CARD_ID, card_id_back);
            cv.put(orders.COMPANY_ID, company_id_back);
            cv.put(orders.TIME, formatter.toString());
            cv.put(orders.MEAL_DETAILS, meal_details);

            db = hlp.getWritableDatabase();



            db.insert(TABLE_ORDERS, null, cv);

            db.close();
        }
    }

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
            Intent home = new Intent(this,MainActivity.class);
            startActivity(home);
        }

        return true;
    }
}