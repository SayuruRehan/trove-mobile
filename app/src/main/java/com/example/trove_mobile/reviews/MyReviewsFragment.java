package com.example.trove_mobile.reviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.trove_mobile.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.retrofit.UpdateOperation;
import com.example.trove_mobile.user.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MyReviewsFragment extends Fragment implements ReviewAdapter.OnReviewClickListener {

    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_reviews, container, false);

        // Initialize RecyclerView
        reviewsRecyclerView = view.findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewsList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewsList, this);
        reviewsRecyclerView.setAdapter(reviewAdapter);

        // Fetch userId from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        // Fetch user reviews
        fetchUserReviews(userId);

        return view;
    }

    private void fetchUserReviews(String userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<RatingResponse>> call = apiService.getUserRatings(userId);
        call.enqueue(new Callback<List<RatingResponse>>() {
            @Override
            public void onResponse(Call<List<RatingResponse>> call, Response<List<RatingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<RatingResponse> ratings = response.body();
                    for (RatingResponse rating : ratings) {
                        // Fetch vendor name for each rating
                        fetchVendorName(rating);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RatingResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchVendorName(RatingResponse rating) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<User> call = apiService.getVendorById(rating.getVendorId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String vendorName = response.body().getUsername();
                    Review review = new Review(vendorName, rating.getRatingValue(), rating.getComment(), rating.getDatePosted(), rating.getRatingId());
                    reviewsList.add(review);
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch vendor name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle the review click to open a dialog for editing
    @Override
    public void onReviewClick(Review review) {
        showEditReviewDialog(review);
    }

    private void showEditReviewDialog(Review review) {
        // Create a dialog with EditText for comment and SeekBar for rating
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_review, null);
        builder.setView(dialogView);

        EditText commentEditText = dialogView.findViewById(R.id.editComment);
        SeekBar ratingSeekBar = dialogView.findViewById(R.id.editRatingSeekBar);
        commentEditText.setText(review.getComment());
        ratingSeekBar.setProgress(review.getRatingValue());

        builder.setPositiveButton("Submit", (dialog, which) -> {
            int newRating = ratingSeekBar.getProgress();
            String newComment = commentEditText.getText().toString().trim();

            // Call the PATCH API to update the review
            updateReview(review.getRatingId(), newRating, newComment);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void updateReview(String ratingId, int newRatingValue, String newComment) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        List<UpdateOperation> operations = new ArrayList<>();
        operations.add(new UpdateOperation("replace", "/RatingValue", String.valueOf(newRatingValue)));
        operations.add(new UpdateOperation("replace", "/Comment", newComment));

        Call<Void> call = apiService.updateRating(ratingId, operations);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Update the review in the list and notify the adapter
                    for (Review review : reviewsList) {
                        if (review.getRatingId().equals(ratingId)) {
                            review.setRatingValue(newRatingValue);
                            review.setComment(newComment);
                            reviewAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    Toast.makeText(getContext(), "Rating updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update rating", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
