package com.example.trove_mobile.orderDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trove_mobile.notification.Notification;
import com.example.mobile_app_client.R;
import com.example.trove_mobile.order.Order;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsFragment extends Fragment {

    private static final String ARG_ORDER_ID = "orderId";

    private String orderId;
    private Order order;

    private TextView orderNumberDetails, orderDateDetails, orderStatusDetails,
            orderItemCountDetails, shippingAddressDetails, paymentMethodText,
            deliveryMethodDetails, totalAmountDetails;

    private RecyclerView recyclerViewOrderedItems;
    private OrderedProductAdapter orderedProductAdapter;
    private List<Product> orderedProductList;

    private ProgressBar progressBar;
    private ImageView backButtonOrderDetails;
    private Button buttonCancelOrder;

    private ApiService apiService;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment newInstance(String orderId) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            orderId = getArguments().getString(ARG_ORDER_ID);
        }

        // Initialize ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        orderedProductList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        // Initialize views
        initializeViews(view);

        // Set up RecyclerView
        recyclerViewOrderedItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderedProductAdapter = new OrderedProductAdapter(orderedProductList, getActivity());
        recyclerViewOrderedItems.setAdapter(orderedProductAdapter);

        // Fetch order details
        fetchOrderDetails(orderId);

        // Handle back button click
        backButtonOrderDetails.setOnClickListener(v -> {
            // Navigate back to previous fragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Handle "Cancel Order" button click
        buttonCancelOrder.setOnClickListener(v -> {
            createCancelOrderNotification();
        });

        return view;
    }

    private void initializeViews(View view) {
        backButtonOrderDetails = view.findViewById(R.id.backButtonOrderDetails);
        orderNumberDetails = view.findViewById(R.id.orderNumberDetails);
        orderDateDetails = view.findViewById(R.id.orderDateDetails);
        orderStatusDetails = view.findViewById(R.id.orderStatusDetails);
        orderItemCountDetails = view.findViewById(R.id.orderItemCountDetails);
        recyclerViewOrderedItems = view.findViewById(R.id.recyclerViewOrderedItems);
        shippingAddressDetails = view.findViewById(R.id.shippingAddressDetails);
        paymentMethodText = view.findViewById(R.id.paymentMethodText);
        deliveryMethodDetails = view.findViewById(R.id.deliveryMethodDetails);
        totalAmountDetails = view.findViewById(R.id.totalAmountDetails);
        buttonCancelOrder = view.findViewById(R.id.buttonCancelOrder);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void fetchOrderDetails(String orderId) {
        progressBar.setVisibility(View.VISIBLE);
        Call<Order> call = apiService.getOrderById(orderId);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response){
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    order = response.body();
                    if (order != null) {
                        populateOrderDetails();
                        fetchOrderedProducts(order.getProductIds());
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load order details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void populateOrderDetails() {
        // Set order details
        orderNumberDetails.setText("Order: " + order.getOrderId());

        // Format date
        String formattedDate = order.getOrderDate();
        if (order.getOrderDate() != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date date = inputFormat.parse(order.getOrderDate());
                formattedDate = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        orderDateDetails.setText(formattedDate);

        orderStatusDetails.setText(order.getStatus());
        if (order.getStatus().equalsIgnoreCase("delivered")) {
            orderStatusDetails.setTextColor(getResources().getColor(R.color.colorGreen));
        } else if (order.getStatus().equalsIgnoreCase("processing")) {
            orderStatusDetails.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (order.getStatus().equalsIgnoreCase("cancelled")) {
            orderStatusDetails.setTextColor(getResources().getColor(R.color.red));
        }

        orderItemCountDetails.setText(order.getProductIds().size() + " items");

        shippingAddressDetails.setText(order.getDeliveryAddress());

        // Payment method (Adjust based on your data)
        paymentMethodText.setText("Payment Reference: " + order.getPaymentId());

        // Delivery method
        String deliveryMethod = "";
        switch (order.getDeliveryMethod().toLowerCase()) {
            case "postal":
                deliveryMethod = "Postal";
                break;
            case "express postal":
                deliveryMethod = "Express Postal";
                break;
            case "courier service":
                deliveryMethod = "Courier Service";
                break;
            default:
                deliveryMethod = order.getDeliveryMethod();
                break;
        }
        deliveryMethodDetails.setText(deliveryMethod);

        totalAmountDetails.setText("Rs " + order.getAmount());

        // Hide "Cancel Order" button if necessary
        if (order.getStatus().equalsIgnoreCase("delivered") || order.getStatus().equalsIgnoreCase("cancelled")) {
            buttonCancelOrder.setVisibility(View.GONE);
        }
    }

    private void fetchOrderedProducts(List<String> productIds) {
        orderedProductList.clear();

        for (String productId : productIds) {
            Call<Product> call = apiService.getProductById(productId);

            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response){
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        if (product != null) {
                            orderedProductList.add(product);
                            orderedProductAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to load product details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t){
                    Toast.makeText(getActivity(), "An error occurred: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        }
    }

    private void createCancelOrderNotification() {
        // Create the notification object
        Notification notification = new Notification();
        notification.setTitle("Cancel Order");
        notification.setMessage("Order no " + order.getOrderId() + " needs to be Cancelled.");
        notification.setRole("Admin");

        // Get the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        notification.setDateTime(currentDateTime);

        // Get userId from SharedPreferences (if required)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userId = "";
        notification.setUserId(userId);

        // Log the notification details for debugging
        Log.d("NotificationRequest", "Notification: " + new com.google.gson.Gson().toJson(notification));

        // Call the API to send the notification
        Call<Notification> call = apiService.createNotification(notification);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response){
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Notification sent to Admin", Toast.LENGTH_SHORT).show();
                    // Optionally disable the button
                    buttonCancelOrder.setEnabled(false);
                    buttonCancelOrder.setText("Cancellation Requested");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("NotificationError", "Notification failed: " + errorBody);
                        Toast.makeText(getActivity(), "Failed to send notification: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("NotificationError", "Failed to parse error body", e);
                        Toast.makeText(getActivity(), "Notification failed", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t){
                Log.e("NotificationError", "Error: " + t.getMessage(), t);
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
