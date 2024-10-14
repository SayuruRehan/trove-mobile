package com.example.trove_mobile.cart;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Cart {

    @SerializedName("cartId")
    private String cartId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("productIds")
    private List<String> productIds;

    // Getters and Setters
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<String> getProductIds() { return productIds; }
    public void setProductIds(List<String> productIds) { this.productIds = productIds; }
}
