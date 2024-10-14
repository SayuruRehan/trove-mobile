package com.example.trove_mobile.payment;

import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("paymentId")
    private String paymentId;

    @SerializedName("paymentReference")
    private String paymentReference;

    @SerializedName("amount")
    private double amount;

    @SerializedName("userId")
    private String userId;

    public Payment(String paymentId, String paymentReference, double amount, String userId) {
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.amount = amount;
        this.userId = userId;
    }

    // Getters and Setters

    public String getPaymentId() {
        return paymentId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public double getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }
}
