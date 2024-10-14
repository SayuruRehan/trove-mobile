package com.example.trove_mobile.vendor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.reviews.RatingResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VendorReviewAdapter extends RecyclerView.Adapter<VendorReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<RatingResponse> ratingsList;

    public VendorReviewAdapter(Context context, List<RatingResponse> ratingsList) {
        this.context = context;
        this.ratingsList = ratingsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vendor_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        RatingResponse ratingResponse = ratingsList.get(position);
        holder.commentTextView.setText(ratingResponse.getComment());
        // Convert the date string to the desired format
        String formattedDate = formatDate(ratingResponse.getDatePosted());
        holder.dateTextView.setText("Posted on: " + formattedDate);
        holder.ratingBar.setRating(ratingResponse.getRatingValue());
    }

    @Override
    public int getItemCount() {
        return ratingsList.size();
    }

    // Method to format the date
    private String formatDate(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView commentTextView, dateTextView;
        RatingBar ratingBar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
        }
    }
}
