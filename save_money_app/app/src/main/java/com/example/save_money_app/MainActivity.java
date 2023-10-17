package com.example.save_money_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> savedLists = new ArrayList<>();
    private ArrayAdapter<String> listsAdapter;

    Context ctx = this;

    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = DataBase.getInstance(ctx);

        listsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedLists);


        ListView lists = findViewById(R.id.items_list);
        Button addList = findViewById(R.id.add_list);

        addList.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Nova Lista")
                    .setView(input)
                    .setNegativeButton("Cancelar", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Ok", (dialog, i) -> {
//                        savedLists.add(input.getText().toString());
//                        listsAdapter.notifyDataSetChanged();
                        dataBase.insertList(input.getText().toString());
                        updateLists();
                    })
                    .show();
        });

        lists.setAdapter(listsAdapter);

        lists.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
            intent.putExtra("listName", savedLists.get(position));
            startActivity(intent);
        });

        updateLists();

    }

    private void updateLists() {
        List<String> lists = dataBase.getLists();

        if(lists == null){
            Log.d("UpdateLists:", "NULL Value");
            return;
        }

        savedLists.clear();

        for(int i = 0; i < lists.size(); i++){
            savedLists.add(lists.get(i));
        }

        listsAdapter.notifyDataSetChanged();
    }
}