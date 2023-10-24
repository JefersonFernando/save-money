package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    private int listID;

    private List<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listItemsAdapter;
    private DataBase dataBase;

    private List<Integer> itemsIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        Intent intent = getIntent();

        TextView listName = findViewById(R.id.list_name);
        Button newItem = findViewById(R.id.add_item);
        Button search = findViewById(R.id.search);
        ListView items = findViewById(R.id.items_list);

        Button returnButton = findViewById(R.id.return_button);


        listName.setText(intent.getStringExtra("listName"));
        listID = intent.getIntExtra("listID", -1);

        dataBase = DataBase.getInstance();

        listItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        items.setAdapter(listItemsAdapter);
        if(listID == -1) {
            finish();
        }

        returnButton.setOnClickListener(v -> {
            finish();
        });

        newItem.setOnClickListener(v -> {
            Intent newIntent = new Intent(ListItemsActivity.this, SearchItemActivity.class);
            newIntent.putExtra("listID", listID);
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

        updateItems();
    }

    private void updateItems() {
        String itemName;

        this.itemsIds = dataBase.getListItemsIds(this.listID);

        this.listItems.clear();

        Log.d("Items:", ""+itemsIds);

        if(this.itemsIds == null) {
            return;
        }

        for(int i = 0; i < this.itemsIds.size(); i++) {
            itemName = dataBase.getItemName(this.itemsIds.get(i));

            Log.d("Item name:", itemName);

            if(itemName == null){
                continue;
            }

            this.listItems.add(itemName);
        }

        this.listItemsAdapter.notifyDataSetChanged();
    }
}