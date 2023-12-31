package com.example.save_money_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListStoresActivity extends AppCompatActivity {

    private int listID;
    private List<String> stores = new ArrayList<>();
    private ArrayAdapter<String> storesAdapter;
    private DataBase dataBase;
    private List<String> barCodes;

    //Classe do mercado
    private List<String> storeName = new ArrayList<>();
    ;
    private List<String> storePrice = new ArrayList<>();
    private List<String> storeAddress = new ArrayList<>();
    private ArrayAdapter<String> storeAdapter;

    private LocationListener locationListener;

    private LocationManager locationManager;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stores);
        Button searchItem = findViewById(R.id.return_button);
        ListView storesList = findViewById(R.id.items_list);
        TextView listName = findViewById(R.id.list_name);

        Intent intent = getIntent();
        this.listID = intent.getIntExtra("listID", -1);
        if (this.listID == -1) {
            finish();
            return;
        }

        storeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, storeName);
        storesList.setAdapter(storeAdapter);

        dataBase = DataBase.getInstance(this);
        queue = Volley.newRequestQueue(ListStoresActivity.this);

        String listNameString = dataBase.getListName(listID);
        if (listNameString == null) {
            finish();
            return;
        }
        listName.setText(listNameString);

        this.barCodes = dataBase.getListBarCodes(this.listID);
        if (this.barCodes == null || this.barCodes.size() == 0) {
            Toast.makeText(this, "Lista vazia.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        this.locationManager = (LocationManager)
                getSystemService(this.LOCATION_SERVICE);

        this.locationListener = LocationListener.getInstance();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(this, "Localização não autorizada!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        searchItem.setOnClickListener(v -> {
            finish();
        });

        storesList.setOnItemClickListener((parent, view, position, id) -> {

            new AlertDialog.Builder(this)
                    .setTitle(storeName.get(position))
                    .setMessage(storeAddress.get(position))
                    .setPositiveButton("Ok", (dialog, i) -> {
                    })
                    .show();
        });

        Toast.makeText(this, "Obtendo localização...", Toast.LENGTH_SHORT).show();


        if (locationListener.getLongitude() == 0.0 || locationListener.getLatitude() == 0.0) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    listStores();
                }
            }, 3000);

        }else{
            listStores();
        }
    }

    private void listStores() {
        String url = "https://savemoney.anticitizen1.com/lista/?codBarras=";

        /* Adiciona  */
        url += String.join(",", this.barCodes);

        url += "&latitude=" + locationListener.getLatitude() + "&longitude=" + locationListener.getLongitude() + "&raio=5";

        Log.d("URL:", url);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonobject = response.getJSONObject(i);

                        storePrice.add(jsonobject.getString("precoTotal"));

                        jsonobject = jsonobject.getJSONObject("loja");
                        storeName.add(jsonobject.getString("nome") + " R$:" + storePrice.get(i));
                        storeAddress.add(jsonobject.getString("endereco"));
                    }

                    storeAdapter.notifyDataSetChanged();
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
    }
}