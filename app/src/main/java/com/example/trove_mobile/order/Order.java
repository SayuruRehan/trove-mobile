package com.example.trove_mobile.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("orderId")
    private String orderId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("orderDate")
    private String orderDate;

    @SerializedName("orderDescription")
    private String orderDescription;

    @SerializedName("amount")
    private double amount;

    @SerializedName("deliveryMethod")
    private String deliveryMethod;

    @SerializedName("status")
    private String status;

    @SerializedName("productIds")
    private List<String> productIds;

    @SerializedName("paymentId")
    private String paymentId;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("deliveryAddress")
    private String deliveryAddress;

    public Order(String orderId, String userId, String orderDescription, double amount,
                 String deliveryMethod, String status, List<String> productIds, String paymentId,
                 String phoneNumber, String deliveryAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDescription = orderDescription;
        this.amount = amount;
        this.deliveryMethod = deliveryMethod;
        this.status = status;
        this.productIds = productIds;
        this.paymentId = paymentId;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getOrderDescription() { return orderDescription; }
    public void setOrderDescription(String orderDescription) { this.orderDescription = orderDescription; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDeliveryMethod() { return deliveryMethod; }
    public void setDeliveryMethod(String deliveryMethod) { this.deliveryMethod = deliveryMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getProductIds() { return productIds; }
    public void setProductIds(List<String> productIds) { this.productIds = productIds; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", userId='" + userId + '\'' +
                ", orderDescription='" + orderDescription + '\'' +
                ", amount=" + amount +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", status='" + status + '\'' +
                ", productIds=" + productIds +
                ", paymentId='" + paymentId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                '}';
    }

}
