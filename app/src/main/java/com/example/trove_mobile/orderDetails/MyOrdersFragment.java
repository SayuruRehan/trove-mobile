package com.example.trove_mobile.orderDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trove_mobile.R;
import com.example.trove_mobile.order.Order;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for displaying user's orders.
 */
public class MyOrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private List<Order> filteredOrderList = new ArrayList<>();
    private ProgressBar progressBar;
    private TabLayout orderStatusTabs;
    private ImageView backButtonMyOrders;

    private String userId;
    private ApiService apiService;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static MyOrdersFragment newInstance() {
        return new MyOrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Get userId from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Optionally, redirect to LoginActivity
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        // Initialize views
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        progressBar = view.findViewById(R.id.progressBar);
        orderStatusTabs = view.findViewById(R.id.orderStatusTabs);
        backButtonMyOrders = view.findViewById(R.id.backButtonMyOrders);

        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderAdapter = new OrderAdapter(filteredOrderList, getActivity());
        recyclerViewOrders.setAdapter(orderAdapter);

        // Fetch orders
        fetchOrders();

        // Set TabLayout listener
        orderStatusTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                filterOrdersByStatus(tab.getText().toString().toLowerCase());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){
                filterOrdersByStatus(tab.getText().toString().toLowerCase());
            }
        });

        // Handle back button click
        backButtonMyOrders.setOnClickListener(v -> {
            // Navigate back to ProfileFragment
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void fetchOrders() {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<Order>> call = apiService.getOrdersByUserId(userId);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response){
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    orderList = response.body();
                    // Initially display orders with status "delivered"
                    filterOrdersByStatus("delivered");
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrdersByStatus(String status) {
        filteredOrderList.clear();
        for (Order order : orderList) {
            if (order.getStatus().equalsIgnoreCase(status)) {
                filteredOrderList.add(order);
            }
        }
        orderAdapter.notifyDataSetChanged();
    }
}
