package com.example.trove_mobile.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private TextView productPageTitle;
    private String categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productPageTitle = findViewById(R.id.productPageTitle);

        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");

        productPageTitle.setText(categoryName);
        loadProductsByCategory(categoryId);

        // Handle back button click
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadProductsByCategory(String categoryId) {
        ApiService productService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Product>> call = productService.getProductsByCategory(categoryId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    setupProductRecyclerView(products);
                } else if(response.code() == 404){
                    // Navigate to NoProductsActivity
                    Intent intent = new Intent(ProductListActivity.this, NoProductsFoundActivity.class);
                    intent.putExtra("categoryName", getIntent().getStringExtra("categoryName"));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProductListActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupProductRecyclerView(List<Product> products) {
        productAdapter = new ProductAdapter(this, products, getIntent().getStringExtra("categoryName"));
        productRecyclerView.setAdapter(productAdapter);
    }
}