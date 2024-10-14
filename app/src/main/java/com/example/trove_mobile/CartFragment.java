package com.example.trove_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.trove_mobile.R;
import com.example.trove_mobile.cart.Cart;
import com.example.trove_mobile.cart.CartAdapter;
import com.example.trove_mobile.cart.CartItem;
import com.example.trove_mobile.cart.OnCartItemDeleteListener;
import com.example.trove_mobile.order.OrderFragment;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment representing the cart.
 */
public class CartFragment extends Fragment implements OnCartItemDeleteListener {

    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView tvTotalAmount;
    private Button btnCheckout;
    private double totalAmount = 0;
    private String userId;
    private Cart currentCart;

    private ApiService apiService;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get userId from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            // Handle the case when userId is not available
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            // Optionally, navigate to LoginActivity
        }

        apiService = RetrofitClient.getClient().create(ApiService.class);

        cartItemList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartItemList, getActivity(), this::updateTotalAmount,this);
    }

    @Override
    public void onCartItemDeleted(CartItem item) {
        if (currentCart != null) {
            // Remove the productId from the cart's productIds
            currentCart.getProductIds().remove(item.getProduct().getProductId());
            // Update the cart on the server
            updateCartInBackend();
        }
    }

    private void updateCartInBackend() {
        Call<Cart> call = apiService.updateCart(currentCart.getCartId(), currentCart);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    // Cart updated successfully
                    // You can add additional actions here if needed
                } else {
                    Toast.makeText(getActivity(), "Failed to update cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(getActivity(), "Error updating cart: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCartItems.setAdapter(cartAdapter);

        fetchCartData();

        btnCheckout.setOnClickListener(v -> {
            if (totalAmount > 0) {
                OrderFragment orderFragment = new OrderFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("orderAmount", totalAmount);
                ArrayList<String> productIds = new ArrayList<>();
                for (CartItem item : cartItemList) {
                    productIds.add(item.getProduct().getProductId());
                }
                bundle.putStringArrayList("productIds", productIds);
                orderFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, orderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                Toast.makeText(getActivity(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void fetchCartData() {
        Call<List<Cart>> call = apiService.getCartByUserId(userId);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    List<Cart> carts = response.body();
                    if (carts != null && !carts.isEmpty()) {
                        currentCart = carts.get(0); // Store the cart
                        if (currentCart.getProductIds() != null && !currentCart.getProductIds().isEmpty()) {
                            fetchProducts(currentCart.getProductIds());
                        } else {
                            Toast.makeText(getActivity(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Your cart is empty";
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


    private void fetchProducts(List<String> productIds) {
        cartItemList.clear();
        for (String productId : productIds) {
            Call<Product> call = apiService.getProductById(productId);
            call.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        if (product != null) {
                            CartItem cartItem = new CartItem(product, 1); // Assuming quantity is 1
                            cartItemList.add(cartItem);
                            cartAdapter.notifyDataSetChanged();
                            updateTotalAmount();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed to load product details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        }
    }

    private void updateTotalAmount() {
        totalAmount = 0;
        for (CartItem item : cartItemList) {
            totalAmount += item.getProduct().getPrice() * item.getQuantity();
        }
        tvTotalAmount.setText(String.format("Rs %.2f", totalAmount));
    }
}
