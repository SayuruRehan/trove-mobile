package com.example.trove_mobile.product;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("productDescription")
    private String productDescription;

    @SerializedName("price")
    private double price;

    @SerializedName("stock")
    private int stock;

    @SerializedName("image")
    private String image; // Base64 encoded image

    @SerializedName("categoryId")
    private String categoryId;

    @SerializedName("vendorId")
    private String vendorId;

    // Getters and Setters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductDescription() { return productDescription; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImage() { return image; }
    public String getCategoryId() { return categoryId; }
    public String getVendorId() { return vendorId; }

    public void setStock(int stock) { this.stock = stock; }
}
