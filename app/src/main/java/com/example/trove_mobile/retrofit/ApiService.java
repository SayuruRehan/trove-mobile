package com.example.trove_mobile.retrofit;

import com.example.trove_mobile.category.Category;
import com.example.trove_mobile.notification.Notification;
import com.example.trove_mobile.auth.LoginRequest;
import com.example.trove_mobile.auth.LoginResponse;
import com.example.trove_mobile.auth.RegisterRequest;
import com.example.trove_mobile.auth.RegisterResponse;
import com.example.trove_mobile.cart.Cart;
import com.example.trove_mobile.order.Order;
import com.example.trove_mobile.payment.Payment;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.reviews.RatingResponse;
import com.example.trove_mobile.user.User;
import com.example.trove_mobile.vendor.VendorRatingSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("Users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("Users")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);

    // User APIs
    @GET("Users/{userId}")
    Call<User> getUserById(@Path("userId") String userId);

    // Cart APIs
    @GET("Cart/user/{userId}")
    Call<List<Cart>> getCartByUserId(@Path("userId") String userId);

    @DELETE("Cart/user/{userId}")
    Call<Void> clearCartByUserId(@Path("userId") String userId);

    @POST("Cart")
    Call<Void> addToCart(@Body Cart cart);

    @PUT("Cart/{id}")
    Call<Void> updateAddToCart(@Path("id") String cartId, @Body Cart cart);

    @PUT("Cart/{cartId}")
    Call<Cart> updateCart(@Path("cartId") String cartId, @Body Cart cart);

    @GET("Orders/{orderId}")
    Call<Order> getOrderById(@Path("orderId") String orderId);

    // --- Product APIs ---
    @GET("Products/{productId}")
    Call<Product> getProductById(@Path("productId") String productId);

    @PUT("Products/{productId}")
    Call<Product> updateProduct(@Path("productId") String productId, @Body Product product);

    @GET("products/by-category/{categoryId}")
    Call<List<Product>> getProductsByCategory(@Path("categoryId") String categoryId);

    @GET("products")
    Call<List<Product>> getProducts();

    // --- Category APIs ---
    @GET("Category/active")
    Call<List<Category>> getCategories();

    @POST("Orders")
    Call<Order> createOrder(@Body Order order);

    @GET("Orders/user/{userId}")
    Call<List<Order>> getOrdersByUserId(@Path("userId") String userId);

    @POST("Payments")
    Call<Payment> createPayment(@Body Payment payment);

    @POST("Notification")
    Call<Notification> createNotification(@Body Notification notification);

    @PATCH("Users/{userId}")
    Call<User> updateUserPassword(@Path("userId") String userId, @Body List<UpdateOperation> operations);

    @PATCH("Users/{userId}")
    Call<User> updateUserEmail(@Path("userId") String userId, @Body List<UpdateOperation> operations);

    @DELETE("Users/{userId}")
    Call<Void> deleteUserById(@Path("userId") String userId);


    @GET("Rating/user/{userId}")
    Call<List<RatingResponse>> getUserRatings(@Path("userId") String userId);

    @GET("Users/{vendorId}")
    Call<User> getVendorById(@Path("vendorId") String vendorId);

    @PATCH("Rating/{ratingId}")
    Call<Void> updateRating(@Path("ratingId") String ratingId, @Body List<UpdateOperation> operations);

    // --- Vendor Ratings ---
    @GET("Rating/vendor/{vendorId}/summary")
    Call<VendorRatingSummary> getVendorRatingSummary(@Path("vendorId") String vendorId);

    @GET("Rating/vendor/{vendorId}")
    Call<List<RatingResponse>> getRatingsForVendor(@Path("vendorId") String vendorId);

    @POST("rating")
    Call<Void> addRating(@Body RatingResponse rating);

}

