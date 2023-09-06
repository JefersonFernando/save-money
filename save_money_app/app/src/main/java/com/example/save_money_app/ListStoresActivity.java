package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ListStoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stores);

        Button searchItem = findViewById(R.id.return_button);

        searchItem.setOnClickListener(v -> {
            finish();
        });

    }
}