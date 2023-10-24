package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    private int listID;

    private List<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> listItemsAdapter;
    private DataBase dataBase;

    private List<Integer> itemsIds;

    @Override
    protected void onRestart() {
        super.onRestart();
        updateItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        Intent intent = getIntent();

        TextView listName = findViewById(R.id.list_name);
        Button newItem = findViewById(R.id.add_item);
        Button search = findViewById(R.id.search);
        Button deleteList = findViewById(R.id.delete_list);
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
            newIntent.putExtra("listID", listID);
            startActivity(newIntent);
        });

        items.setOnItemClickListener((parent, view, position, id) -> {
            Intent newIntent = new Intent(ListItemsActivity.this, ShowItemActivity.class);
            startActivity(newIntent);
        });

        deleteList.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Apagar lista?")
                    .setNegativeButton("Cancelar", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Ok", (dialog, i) -> {
                        dataBase.deleteList(this.listID);
                        finish();
                    })
                    .show();
        });

        items.setOnItemClickListener((parent, view, position, id) -> {

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            input.setText(""+dataBase.getItemQty(this.itemsIds.get(position)));
            new AlertDialog.Builder(this)
                    .setTitle("Adicionar/remover item")
                    .setView(input)
                    .setNegativeButton("Cancelar", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Ok", (dialog, i) -> {
                        dataBase.setItemQtyOnList(this.itemsIds.get(position), Integer.parseInt(input.getText().toString()));
                        updateItems();
                    })
                    .show();
        });

        updateItems();
    }

    private void updateItems() {
        String itemName;

        this.itemsIds = dataBase.getListItemsIds(this.listID);

        this.listItems.clear();

        Log.d("Items:", ""+itemsIds);

        if(this.itemsIds == null) {
            this.listItemsAdapter.notifyDataSetChanged();
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