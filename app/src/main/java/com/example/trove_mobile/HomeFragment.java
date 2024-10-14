package com.example.trove_mobile;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.trove_mobile.Home.HomeAdapter;
import com.example.trove_mobile.Home.ImageSliderAdapter;
import com.example.trove_mobile.R;
import com.example.trove_mobile.product.Product;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView viewAllText;
    private ViewPager2 viewPager;
    private GridView homeProductsGrid;
    private List<String> imageUrls;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        viewPager = view.findViewById(R.id.viewPager);
        homeProductsGrid = view.findViewById(R.id.newProductsGrid);
        viewAllText = view.findViewById(R.id.viewAllText);

        setupImageSlider();
        loadProducts();

        // Handle "View All" click
        viewAllText.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "View All Clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // Setup auto-swiping image slider
    private void setupImageSlider() {
        imageUrls = new ArrayList<>();
        imageUrls.add("https://img.freepik.com/premium-vector/black-friday-sale-banner-template-vector_7087-196.jpg");
        imageUrls.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkNxQGIynkL5eTId6ZWf4slfqmNUHAg5S6CGthOHK_yCt_8xXFPUuCSCds147r-r5jgcg&usqp=CAU");
        imageUrls.add("https://img.freepik.com/premium-vector/buy-one-get-one-free-sale-banner-special-banner-with-text-effect_535749-1568.jpg");
        imageUrls.add("https://img.freepik.com/premium-vector/special-shopping-day-sale-banner-template-end-year-sale_32996-861.jpg");

        ImageSliderAdapter adapter = new ImageSliderAdapter(getActivity(), imageUrls);
        viewPager.setAdapter(adapter);

        // Auto-swipe with a delay
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); // Slide every 3 seconds
            }
        });
    }

    // Runnable to automatically swipe
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int nextSlide = (viewPager.getCurrentItem() + 1) % imageUrls.size();
            viewPager.setCurrentItem(nextSlide);
        }
    };

    // Load products by calling GET api/products
    private void loadProducts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Product>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    if (productList != null && !productList.isEmpty()) {
                        HomeAdapter adapter = new HomeAdapter(getActivity(), productList);
                        homeProductsGrid.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "No products found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
}
