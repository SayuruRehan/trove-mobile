package com.example.trove_mobile.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trove_mobile.R;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviewList;
    private OnReviewClickListener onReviewClickListener;

    public ReviewAdapter(List<Review> reviews, OnReviewClickListener listener) {
        this.reviewList = reviews;
        this.onReviewClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.vendorName.setText(review.getVendorName());
        holder.ratingBar.setRating(review.getRatingValue());
        holder.comment.setText(review.getComment());
        holder.datePosted.setText(review.getDatePosted());

        // Set click listener for each review item
        holder.itemView.setOnClickListener(v -> onReviewClickListener.onReviewClick(review));
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vendorName, comment, datePosted;
        RatingBar ratingBar;  // Add the RatingBar

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorName = itemView.findViewById(R.id.vendorName);
            ratingBar = itemView.findViewById(R.id.ratingBar);  // Initialize RatingBar
            comment = itemView.findViewById(R.id.comment);
            datePosted = itemView.findViewById(R.id.datePosted);
        }
    }


    // Interface for click listener
    public interface OnReviewClickListener {
        void onReviewClick(Review review);
    }
}

