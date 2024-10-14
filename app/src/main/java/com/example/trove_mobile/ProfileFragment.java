package com.example.trove_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trove_mobile.R;
import com.example.trove_mobile.auth.LoginActivity;
import com.example.trove_mobile.orderDetails.MyOrdersFragment;
import com.example.trove_mobile.profile.MySettingsFragment;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.reviews.MyReviewsFragment;
import com.example.trove_mobile.user.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private Button btnLogout;
    private RelativeLayout myOrdersOption;
    private RelativeLayout mySettingsOption;
    private RelativeLayout myReviewsOption;

    // private RelativeLayout myReviewsOption;

    private TextView userName;
    private TextView userEmail;
    private ImageView profilePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // You can initialize other things here if necessary
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize shared preferences
        sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);

        // Initialize views
        btnLogout = view.findViewById(R.id.btnLogout);
        myOrdersOption = view.findViewById(R.id.myOrdersOption);
        mySettingsOption = view.findViewById(R.id.mySettingsOption);
        myReviewsOption = view.findViewById(R.id.myReviewsOption);
        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        profilePicture = view.findViewById(R.id.profilePicture);

        // Set user information
        String name = sharedPreferences.getString("username", "Guest User");
        userName.setText(name);

        // Fetch the userId from SharedPreferences
        String userId = sharedPreferences.getString("userId", "");

        // Fetch and display user details including email
        getUserDetails(userId);

        // Optionally load profile picture
        // You can use libraries like Glide or Picasso to load images
        // For example:
        // String profilePicUrl = sharedPreferences.getString("profilePictureUrl", null);
        // if (profilePicUrl != null) {
        //     Glide.with(this).load(profilePicUrl).into(profilePicture);
        // }

        // Handle logout button click
        btnLogout.setOnClickListener(v -> {
            // Clear session data
            sharedPreferences.edit().clear().apply();

            // Show a message that the user has logged out
            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // Finish the current activity to prevent back navigation to it
            getActivity().finish();
        });

        // Set click listener for My Orders
        myOrdersOption.setOnClickListener(v -> {
            // Navigate to MyOrdersFragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new MyOrdersFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set click listener for My Settings
        mySettingsOption.setOnClickListener(v -> {
            // Navigate to MyOrdersFragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new MySettingsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set click listener for My Reviews
        myReviewsOption.setOnClickListener(v -> {
            // Navigate to MyReviewsFragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new MyReviewsFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    private void getUserDetails(String userId) {
        // Create the ApiService instance
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Make the GET request
        Call<User> call = apiService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Step 2: Update the userEmail TextView with the fetched email
                    userEmail.setText(response.body().getEmail());
                } else {
                    Toast.makeText(getContext(), "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
