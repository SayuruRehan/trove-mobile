package com.example.trove_mobile.vendor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.reviews.RatingResponse;
import com.example.trove_mobile.user.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
public class VendorStoreActivity extends AppCompatActivity {

    private FloatingActionButton writeReviewFab;
    private String vendorId;
    private TextView vendorNameTextView, vendorDescriptionTextView, ratingSummaryTextView, numberOfReviewsTextView;
    private RatingBar averageRatingBar;
    private RecyclerView reviewsRecyclerView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_store);

        // Initialize views
        vendorNameTextView = findViewById(R.id.vendorNameTextView);
        vendorDescriptionTextView = findViewById(R.id.vendorDescriptionTextView);
        ratingSummaryTextView = findViewById(R.id.ratingSummaryTextView);
        numberOfReviewsTextView = findViewById(R.id.numberOfReviewsTextView);
        averageRatingBar = findViewById(R.id.averageRatingBar);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        writeReviewFab = findViewById(R.id.writeReviewFab);

        // Get vendor ID from intent
        vendorId = getIntent().getStringExtra("vendorId");

        // Retrieve userId from SharedPreferences
        sharedPreferences = getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // Set click listener on the "Write a Review" button
        writeReviewFab.setOnClickListener(v -> {
            if (vendorId != null && !vendorId.isEmpty()) {
                Intent intent = new Intent(VendorStoreActivity.this, SubmitReviewActivity.class);
                intent.putExtra("vendorId", vendorId);
                intent.putExtra("userId", userId);
                startActivity(intent);
            } else {
                Toast.makeText(VendorStoreActivity.this, "Vendor information is unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        // Load vendor and ratings data
        getVendorDetails();
        getVendorRatingSummary();
        getVendorReviews();

        // Back button handler
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void getVendorDetails() {
        // Fetch vendor details from backend
        ApiService userService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = userService.getUserById(vendorId);  // Fetching vendor by user ID

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User vendor = response.body();
                    if (vendor != null && "Vendor".equals(vendor.getRole())) {
                        // Populate vendor details
                        vendorNameTextView.setText(vendor.getUsername());
                        vendorDescriptionTextView.setText(vendor.getAddress());
                    }
                } else {
                    Toast.makeText(VendorStoreActivity.this, "Failed to load vendor details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(VendorStoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVendorRatingSummary() {
        // Fetch vendor rating summary from backend
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<VendorRatingSummary> call = apiService.getVendorRatingSummary(vendorId);

        call.enqueue(new Callback<VendorRatingSummary>() {
            @Override
            public void onResponse(Call<VendorRatingSummary> call, Response<VendorRatingSummary> response) {
                if (response.isSuccessful()) {
                    VendorRatingSummary summary = response.body();
                    if (summary != null) {
                        averageRatingBar.setRating((float) summary.getAverageRating());
                        ratingSummaryTextView.setText(String.format("%.1f out of 5", summary.getAverageRating()));
                        numberOfReviewsTextView.setText(String.format("%d reviews", summary.getTotalReviews()));
                    }
                } else {
                    Toast.makeText(VendorStoreActivity.this, "Failed to load rating summary", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VendorRatingSummary> call, Throwable t) {
                Toast.makeText(VendorStoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVendorReviews() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<RatingResponse>> call = apiService.getRatingsForVendor(vendorId);

        call.enqueue(new Callback<List<RatingResponse>>() {
            @Override
            public void onResponse(Call<List<RatingResponse>> call, Response<List<RatingResponse>> response) {
                if (response.isSuccessful()) {
                    List<RatingResponse> ratingsList = response.body();
                    if (ratingsList != null) {
                        setupReviewsRecyclerView(ratingsList);
                    }
                } else {
                    Toast.makeText(VendorStoreActivity.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RatingResponse>> call, Throwable t) {
                Toast.makeText(VendorStoreActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupReviewsRecyclerView(List<RatingResponse> ratingsList) {
        VendorReviewAdapter adapter = new VendorReviewAdapter(this, ratingsList);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(adapter);
    }
}
