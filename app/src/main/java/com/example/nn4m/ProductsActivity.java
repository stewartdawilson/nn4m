package com.example.nn4m;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProductsActivity";


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    RecyclerView recyclerView;
    List<Product> products;

    private static String JSON_URL = "https://static-ri.ristack-3.nn4maws.net/v1/plp/en_gb/2506/products.json";
    ProductsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textView = findViewById(R.id.txtTitle);
        textView.setText(R.string.products);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        recyclerView = findViewById(R.id.productList);

        products = new ArrayList<>();

        extractProducts();
        actionBarDrawerToggle.syncState();

    }

    private void extractProducts() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, response.length()+"");
                try {
                    JSONArray jsonArray = response.getJSONArray("Products");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject productObject = jsonArray.getJSONObject(i);

                            Product product = new Product();
                            product.setName(productObject.getString("name"));
                            product.setPrice("£"+productObject.getString("cost"));
                            product.setImage(productObject.getJSONArray("allImages").getString(0));
                            products.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductsActivity.this,2,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    adapter = new ProductsAdapter(ProductsActivity.this,products);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Log.e(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: startActivity(new Intent(this, MainActivity.class));
            case R.id.products: drawerLayout.closeDrawers();
        }
        return true;
    }
}
