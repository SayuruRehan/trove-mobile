package com.example.trove_mobile.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.cart.Cart;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.user.User;
import com.example.trove_mobile.vendor.VendorStoreActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productPriceTextView, productDescriptionTextView, productVendorTextView, productViewPageTitle;
    ImageButton viewShopBtn;
    private Button addToCartButton;
    private String productId;
    private String categoryName;
    private Product selectedProduct;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        // Initialize views
        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productVendorTextView = findViewById(R.id.productVendorTextView);
        productViewPageTitle = findViewById(R.id.productViewPageTitle);
        addToCartButton = findViewById(R.id.addToCartButton);
        viewShopBtn = findViewById(R.id.viewShopBtn);
        sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE);


        productId = getIntent().getStringExtra("selectedProductId");
        categoryName = getIntent().getStringExtra("selectedCategoryName");

        getProductDetails(productId);

        // Back button handler
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Add to Cart button handler
        addToCartButton.setOnClickListener(v -> addToCart());

        // Handle View Shop button click
        viewShopBtn.setOnClickListener(v -> {
            if (selectedProduct != null) {
                Intent intent = new Intent(ProductViewActivity.this, VendorStoreActivity.class);
                intent.putExtra("vendorId", selectedProduct.getVendorId());
                startActivity(intent);
            } else {
                Toast.makeText(ProductViewActivity.this, "Vendor information is unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch product details based on the productId from backend
    private void getProductDetails(String productId) {
        ApiService productService = RetrofitClient.getClient().create(ApiService.class);
        Call<Product> call = productService.getProductById(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    selectedProduct = response.body();
                    displayProductDetails();
                } else {
                    Toast.makeText(ProductViewActivity.this, "Failed to load product details", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductViewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Display the product details on the screen
    private void displayProductDetails() {
        if (selectedProduct != null) {
            productViewPageTitle.setText(getIntent().getStringExtra("selectedCategoryName"));
            productNameTextView.setText(selectedProduct.getProductName());
            productPriceTextView.setText(String.format("LKR %.2f", selectedProduct.getPrice()));
            productDescriptionTextView.setText(selectedProduct.getProductDescription());

            fetchVendorDetails(selectedProduct.getVendorId());

            // Decode and display the product image
            String base64Image = selectedProduct.getImage();
            if (base64Image != null && !base64Image.isEmpty()) {
                if (base64Image.contains(",")) {
                    base64Image = base64Image.substring(base64Image.indexOf(",") + 1);
                }
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                productImageView.setImageBitmap(decodedByte);
            } else {
                productImageView.setImageResource(R.drawable.placeholder_image);
            }
        }
    }

    // Fetch vendor details using vendorId
    private void fetchVendorDetails(String vendorId) {
        ApiService userService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = userService.getUserById(vendorId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User vendor = response.body();
                    if (vendor != null) {
                        productVendorTextView.setText(vendor.getUsername());
                    }
                } else {
                    productVendorTextView.setText("Unknown Vendor");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                productVendorTextView.setText("Vendor Not Found");
            }
        });
    }

    // Add selected product to the cart
    private void addToCart() {
        if (selectedProduct != null) {
            String userId = sharedPreferences.getString("userId", null);

            if (userId == null) {
                Toast.makeText(this, "Please log in to add products to your cart", Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            // Step 1: Check if a cart already exists for the user
            Call<List<Cart>> cartCall = apiService.getCartByUserId(userId);

            cartCall.enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                    if (response.isSuccessful()) {
                        List<Cart> existingCarts = response.body();

                        if (existingCarts != null && !existingCarts.isEmpty()) {
                            // Step 2: A cart already exists, so update it by adding the product
                            Cart existingCart = existingCarts.get(0);  // Assuming one cart per user
                            List<String> productIds = existingCart.getProductIds();

                            // Add the product if it's not already in the cart
                            if (!productIds.contains(selectedProduct.getProductId())) {
                                productIds.add(selectedProduct.getProductId());
                            } else {
                                Toast.makeText(ProductViewActivity.this, "Product is already in the cart", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Update the cart with the new product list
                            existingCart.setProductIds(productIds);
                            updateCart(existingCart);
                        } else {
                            // Step 3: No cart exists, so create a new cart
                            Cart newCart = new Cart();
                            newCart.setUserId(userId);
                            newCart.setProductIds(List.of(selectedProduct.getProductId()));

                            createNewCart(newCart);
                        }
                    } else if (response.code() == 404) {
                        // Handle "No carts found" error with message
                        try {
                            String errorBody = response.errorBody().string();
                            if (errorBody.contains("No carts found for this user.")) {
                                // Step 3: No cart exists, so create a new cart
                                Cart newCart = new Cart();
                                newCart.setUserId(userId);
                                newCart.setProductIds(List.of(selectedProduct.getProductId()));

                                createNewCart(newCart);
                            } else {
                                Toast.makeText(ProductViewActivity.this, "Failed to check cart", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ProductViewActivity.this, "Failed to check cart", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Cart>> call, Throwable t) {
                    Toast.makeText(ProductViewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show();
        }
    }



    // Update an existing cart
    private void updateCart(Cart cart) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> updateCall = apiService.updateAddToCart(cart.getCartId(), cart);

        updateCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductViewActivity.this, "Product added to existing cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductViewActivity.this, "Failed to update cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductViewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Create a new cart
    private void createNewCart(Cart cart) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> createCall = apiService.addToCart(cart);  // POST request to create a new cart

        createCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductViewActivity.this, "Product added to a new cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductViewActivity.this, "Failed to create cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductViewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
