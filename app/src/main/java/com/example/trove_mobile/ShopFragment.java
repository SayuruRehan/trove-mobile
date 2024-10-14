package com.example.trove_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trove_mobile.R;
import com.example.trove_mobile.category.Category;
import com.example.trove_mobile.product.ProductListActivity;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends Fragment {

    private ListView categoryListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        categoryListView = view.findViewById(R.id.categoryListView);

        loadCategories();

        // Handle back button click
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getActivity().onBackPressed());

        // Handle search button click
        ImageButton searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            // Implement search functionality here
        });

        return view;
    }

    private void loadCategories() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<Category>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body();
                    setupListView(categories);
                } else {
                    Toast.makeText(getActivity(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListView(List<Category> categories) {
        String[] categoryNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getCategoryName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, categoryNames);
        categoryListView.setAdapter(adapter);

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);
            Intent intent = new Intent(getActivity(), ProductListActivity.class);
            intent.putExtra("categoryId", selectedCategory.getCategoryId());
            intent.putExtra("categoryName", selectedCategory.getCategoryName());
            startActivity(intent);
        });
    }
}
