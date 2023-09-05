package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        Intent intent = getIntent();

        TextView listName = findViewById(R.id.list_name);
        Button newItem = findViewById(R.id.add_item);
        Button search = findViewById(R.id.search);
        ListView items = findViewById(R.id.items_list);

        listName.setText(intent.getStringExtra("listName"));

        newItem.setOnClickListener(v -> {
            Intent newIntent = new Intent(ListItemsActivity.this, SearchItemActivity.class);
            startActivity(newIntent);
        });

        search.setOnClickListener(v -> {
            Intent newIntent = new Intent(ListItemsActivity.this, ListStoresActivity.class);
            startActivity(newIntent);
        });

        items.setOnItemClickListener((parent, view, position, id) -> {
            Intent newIntent = new Intent(ListItemsActivity.this, ShowItemActivity.class);
            startActivity(newIntent);
        });
    }
}