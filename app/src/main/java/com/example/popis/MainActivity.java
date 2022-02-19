package com.example.popis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent worker_intent, company_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        worker_intent = new Intent(this, data_workers.class);
        company_intent = new Intent(this, data_companies.class);

    }

    public void show_workers(View view) {
        startActivity(worker_intent);
    }

    public void show_companies(View view) { startActivity(company_intent); }
}