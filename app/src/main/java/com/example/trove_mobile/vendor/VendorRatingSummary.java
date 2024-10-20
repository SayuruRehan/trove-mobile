package com.example.trove_mobile.vendor;

import java.util.Map;

public class VendorRatingSummary {
    private double averageRating;
    private int totalReviews;
    private Map<Integer, Integer> starDistribution;

    // Getters
    public double getAverageRating() {
        return averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public Map<Integer, Integer> getStarDistribution() {
        return starDistribution;
    }

    // Setters
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public void setStarDistribution(Map<Integer, Integer> starDistribution) {
        this.starDistribution = starDistribution;
    }
}
