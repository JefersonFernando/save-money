package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayAdapter<String> listsAdapter;
    String url ="https://savemoney.anticitizen1.com/produtos/busca?q=nescau";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        RequestQueue queue = Volley.newRequestQueue(SearchItemActivity.this);
        TextView searchContent = findViewById(R.id.searchContent);
        ListView lists = findViewById(R.id.items_list);
        listsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itens);

        Button searchItem = findViewById(R.id.search_item);
        searchItem.setOnClickListener(v -> {
//            String url = baseUrl + searchContent.toString();
//            jsonObjectRequest.
            url = "https://savemoney.anticitizen1.com/produtos/busca?q=" + searchContent.getText().toString();

            Log.d("URL:", url);

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
//                    Toast.makeText(SearchItemActivity.this, "Resposta executada.", Toast.LENGTH_SHORT).show();
                        itens.clear();

                        for(int i = 0 ; i < response.length(); i++){
                            JSONObject jsonobject = response.getJSONObject(i);
                            itens.add(jsonobject.getString("nome"));
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

            queue.add(jsonObjectRequest);
        });

        lists.setAdapter(listsAdapter);

    }
}