package com.example.trove_mobile.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobile_app_client.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.reviews.RatingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button submitReviewButton;
    private String vendorId, userId;  // Assuming these are passed from the intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar);
        reviewEditText = findViewById(R.id.reviewEditText);
        submitReviewButton = findViewById(R.id.submitReviewButton);

        // Retrieve vendorId and userId from intent
        Intent intent = getIntent();
        vendorId = intent.getStringExtra("vendorId");
        userId = intent.getStringExtra("userId");

        // Handle back button click
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Handle submit review button click
        submitReviewButton.setOnClickListener(v -> submitReview());
    }

    private void submitReview() {
        int ratingValue = (int) ratingBar.getRating();
        String comment = reviewEditText.getText().toString();

        if (ratingValue == 0) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        RatingResponse rating = new RatingResponse();
        rating.setVendorId(vendorId);
        rating.setUserId(userId);
        rating.setRatingValue(ratingValue);
        rating.setComment(comment);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.addRating(rating);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSuccessDialog(); // Show success popup after submission
    //                Toast.makeText(SubmitReviewActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SubmitReviewActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SubmitReviewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to show success dialog
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        // Custom view for the dialog
        builder.setView(R.layout.dialog_feedback_success);

        builder.setPositiveButton("Done", (dialog, which) -> {
            dialog.dismiss();
            finish();  // Close the activity after dismissing the dialog
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Automatically dismiss the dialog after 3 seconds (3000 milliseconds)
        new android.os.Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                finish();  // Close the activity after dismissing the dialog
            }
        }, 3000); // 2000 milliseconds = 2 seconds
    }
}
