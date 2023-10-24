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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchItemActivity extends AppCompatActivity {

    private List<String> itens = new ArrayList<>();
    private List<String> codBarras = new ArrayList<>();
    private ArrayAdapter<String> listsAdapter;
    private int listID;
    private DataBase dataBase = DataBase.getInstance();
    String url ="https://savemoney.anticitizen1.com/produtos/busca?q=nescau";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        RequestQueue queue = Volley.newRequestQueue(SearchItemActivity.this);
        TextView searchContent = findViewById(R.id.searchContent);
        ListView lists = findViewById(R.id.items_list);
        Button returnButton = findViewById(R.id.return_button);
        listsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itens);

        Intent intent = getIntent();
        this.listID = intent.getIntExtra("listID", -1);
        if (this.listID == -1) {
            Log.d("Error:", "List not got.");
            finish();
        }

        Button searchItem = findViewById(R.id.search_item);
        searchItem.setOnClickListener(v -> {
            url = "https://savemoney.anticitizen1.com/produtos/busca?q=" + searchContent.getText().toString();

            Log.d("URL:", url);

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        itens.clear();
                        codBarras.clear();

                        for(int i = 0 ; i < response.length(); i++){
                            JSONObject jsonobject = response.getJSONObject(i);
                            itens.add(jsonobject.getString("nome"));
                            codBarras.add(jsonobject.getString("codBarra"));
                        }

                        listsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Resposta:", error.toString());
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsonObjectRequest);
        });

        lists.setAdapter(listsAdapter);

        lists.setOnItemClickListener((parent, view, position, id) -> {
//            Intent newIntent = new Intent(SearchItemActivity.this, ShowItemActivity.class);
//
//            Log.d("Selected item:", "Name:" + this.itens.get(position) + " Index:" + position + " CodBarras:" + this.codBarras.get(position));
//
//            newIntent.putExtra("listID", listID);
//            newIntent.putExtra("codBarras", this.codBarras.get(position));
//            startActivity(newIntent);

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            new AlertDialog.Builder(this)
                    .setTitle("Adicionar/remover item")
                    .setView(input)
                    .setNegativeButton("Cancelar", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Ok", (dialog, i) -> {
                        dataBase.setItemOnList(codBarras.get(position), itens.get(position), listID, Integer.parseInt(input.getText().toString()));
                    })
                    .show();
        });

        returnButton.setOnClickListener(v -> {
            finish();
        });
    }
}