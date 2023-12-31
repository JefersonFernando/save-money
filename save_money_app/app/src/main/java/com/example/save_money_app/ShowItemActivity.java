package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ShowItemActivity extends AppCompatActivity {

    private int listID;
    private String codBarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        TextView quantity = findViewById(R.id.ProductCounter);
        Button increase = findViewById(R.id.button_increase);
        Button decrease = findViewById(R.id.button_decrease);
        Button returnButton = findViewById(R.id.return_button);
        TextView description = findViewById(R.id.textView2);
        TextView GTIN = findViewById(R.id.textView4);

        Intent intent = getIntent();
        listID = intent.getIntExtra("listID", -1);
        codBarras = intent.getStringExtra("codBarras");
        if(listID == -1) {
            finish();
        }

        GTIN.setText(codBarras);

        returnButton.setOnClickListener(v -> {
            finish();
        });

        increase.setOnClickListener(v -> {
            int value = Integer.parseInt(quantity.getText().toString());
            value++;

            if(value > 99) {
                value = 99;
            }

            quantity.setText(""+value);
        });

        decrease.setOnClickListener(v -> {
            int value = Integer.parseInt(quantity.getText().toString());

            value--;

            if(value < 0) {
                value = 0;
            }

            quantity.setText(""+value);
        });


    }
}