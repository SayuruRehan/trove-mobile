package com.example.trove_mobile.reviews;

public class Review {
    private String ratingId;
    private String vendorName;
    private int ratingValue;
    private String comment;
    private String datePosted;

    public Review(String vendorName, int ratingValue, String comment, String datePosted, String ratingId) {
        this.vendorName = vendorName;
        this.ratingValue = ratingValue;
        this.comment = comment;
        this.datePosted = datePosted;
        this.ratingId = ratingId;
    }

    // Getters and Setters
    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }
}
