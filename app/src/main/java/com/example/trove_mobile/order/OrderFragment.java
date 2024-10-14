package com.example.trove_mobile.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.payment.Payment;
import com.example.trove_mobile.payment.PaymentMethodsFragment;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for placing an order.
 */
public class OrderFragment extends Fragment {

    private TextView recipientName, addressLine, orderAmount, deliveryFee, totalAmount;
    private Button submitOrderButton;
    private RadioGroup deliveryMethodGroup;
    private TextView changeAddress, changePayment;

    private String userId;
    private ApiService apiService;

    private double orderAmountValue = 0.0;
    private double deliveryFeeValue = 0.0;
    private double totalAmountValue = 0.0;

    private String deliveryMethod = "postal"; // Default delivery method
    private String paymentReference = null;
    private String deliveryAddress = "";
    private String phoneNumber = "";

    private List<String> productIds = new ArrayList<>();

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Optionally navigate to LoginActivity
        }

        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Get data from arguments (e.g., order amount, product IDs)
        if (getArguments() != null) {
            orderAmountValue = getArguments().getDouble("orderAmount", 0.0);
            productIds = getArguments().getStringArrayList("productIds");
        } else {
            Toast.makeText(getActivity(), "No order data available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recipientName = view.findViewById(R.id.recipientName);
        addressLine = view.findViewById(R.id.addressLine);
        orderAmount = view.findViewById(R.id.orderAmount);
        deliveryFee = view.findViewById(R.id.deliveryFee);
        totalAmount = view.findViewById(R.id.totalAmount);
        submitOrderButton = view.findViewById(R.id.submitOrderButton);
        deliveryMethodGroup = view.findViewById(R.id.deliveryMethodGroup);
        changeAddress = view.findViewById(R.id.changeAddress);
        changePayment = view.findViewById(R.id.changePayment);

        // Set default values
        orderAmount.setText(String.format("Rs %.2f", orderAmountValue));
        deliveryFee.setText("Rs 0.00");
        totalAmount.setText(String.format("Rs %.2f", orderAmountValue));
        totalAmountValue = orderAmountValue;

//        fetchUserDetails();

        loadAddressFromSharedPreferences();

        // Set listeners
        deliveryMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.deliveryOption1) {
                deliveryMethod = "postal";
                deliveryFeeValue = 100.0;
            } else if (checkedId == R.id.deliveryOption2) {
                deliveryMethod = "express postal";
                deliveryFeeValue = 150.0;
            } else if (checkedId == R.id.deliveryOption3) {
                deliveryMethod = "courier";
                deliveryFeeValue = 200.0;
            }

            deliveryFee.setText(String.format("Rs %.2f", deliveryFeeValue));
            totalAmountValue = orderAmountValue + deliveryFeeValue;
            totalAmount.setText(String.format("Rs %.2f", totalAmountValue));
        });


        changeAddress.setOnClickListener(v -> {
            // Navigate to SubmitAddressFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new SubmitAddressFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        changePayment.setOnClickListener(v -> {
            // Navigate to PaymentMethodsFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new PaymentMethodsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        submitOrderButton.setOnClickListener(v -> {
            if (validateOrderDetails()) {
                createPayment();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload address data when fragment resumes
        loadAddressFromSharedPreferences();
    }

    private void loadAddressFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String savedRecipientName = sharedPreferences.getString("recipientName", null);
        String savedDeliveryAddress = sharedPreferences.getString("deliveryAddress", null);
        String savedPhoneNumber = sharedPreferences.getString("phoneNumber", null);

        if (!TextUtils.isEmpty(savedRecipientName) && !TextUtils.isEmpty(savedDeliveryAddress) && !TextUtils.isEmpty(savedPhoneNumber)) {
            recipientName.setText(savedRecipientName);
            addressLine.setText(savedDeliveryAddress);
            phoneNumber = savedPhoneNumber;
        } else {
            // If SharedPreferences do not have address, fetch from user details
            fetchUserDetails();
        }
    }

    private boolean validateOrderDetails() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        deliveryAddress = sharedPreferences.getString("deliveryAddress", "");
        phoneNumber = sharedPreferences.getString("phoneNumber", "");
        recipientName.setText(sharedPreferences.getString("recipientName", "Recipient Name"));
        addressLine.setText(deliveryAddress);

        paymentReference = sharedPreferences.getString("paymentReference", null);

        if (TextUtils.isEmpty(deliveryAddress) || TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getActivity(), "Please provide delivery address and phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(paymentReference)) {
            Toast.makeText(getActivity(), "Please provide payment reference", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    private void fetchUserDetails() {
        // Check if SharedPreferences already have address data
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String savedDeliveryAddress = sharedPreferences.getString("deliveryAddress", null);

        if (TextUtils.isEmpty(savedDeliveryAddress)) {
            // Only fetch from server if no address is saved
            Call<User> call = apiService.getUserById(userId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        if (user != null) {
                            // Set default values from the user object
                            recipientName.setText(user.getUsername());
                            addressLine.setText(user.getAddress());
                            phoneNumber = user.getPhoneNumber();

                            // Save to SharedPreferences for consistency
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("recipientName", user.getUsername());
                            editor.putString("deliveryAddress", user.getAddress());
                            editor.putString("phoneNumber", user.getPhoneNumber());
                            editor.apply();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void createPayment() {
        Payment payment = new Payment("66f120f30e1276fdba698258", paymentReference, totalAmountValue, userId);
        Call<Payment> call = apiService.createPayment(payment);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful()) {
                    Payment createdPayment = response.body();

                    // Show a toast message indicating payment creation success
                    Toast.makeText(getActivity(), "Payment created successfully.", Toast.LENGTH_SHORT).show();

                    createOrder(createdPayment.getPaymentId());
                } else {
                    Toast.makeText(getActivity(), "Failed to create payment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createOrder(String paymentId) {
        Order order = new Order("dummyId", userId, "Order Description", totalAmountValue,
                deliveryMethod, "processing", productIds, paymentId="66f120f30e1276fdba698258", phoneNumber, deliveryAddress);

        Log.d("OrderFragment", "Creating order with details: " + order.toString());

        Call<Order> call = apiService.createOrder(order);
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    // Clear cart and update product stock
                    clearCart();
                    updateProductStock();
                    // Navigate to SuccessFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, new SuccessFragment());
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity(), "Failed to create order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCart() {
        Call<Void> call = apiService.clearCartByUserId(userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cart cleared successfully
                    Log.d("OrderFragment", "Cart cleared successfully.");
                } else {
                    // Handle unsuccessful response
                    Log.d("OrderFragment", "Failed to clear cart. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("OrderFragment", "Error clearing cart: " + t.getMessage());
            }
        });
    }

    private void updateProductStock() {
        for (String productId : productIds) {
            // Fetch the product to get current stock
            Call<Product> call = apiService.getProductById(productId);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        if (product != null) {
                            int newStock = product.getStock() - 1; // Decrease stock by 1
                            if (newStock < 0) newStock = 0; // Ensure stock doesn't go negative
                            product.setStock(newStock);
                            // Update the product stock
                            apiService.updateProduct(productId, product).enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("OrderFragment", "Stock updated for productId: " + productId);
                                    } else {
                                        Log.d("OrderFragment", "Failed to update stock for productId: " + productId);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Log.e("OrderFragment", "Error updating stock for productId: " + productId + ", Error: " + t.getMessage());
                                }
                            });
                        }
                    } else {
                        Log.d("OrderFragment", "Failed to get product for productId: " + productId);
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e("OrderFragment", "Error fetching product for productId: " + productId + ", Error: " + t.getMessage());
                }
            });
        }
    }

}
