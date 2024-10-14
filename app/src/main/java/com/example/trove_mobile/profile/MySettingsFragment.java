package com.example.trove_mobile.profile;

import android.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.trove_mobile.R;
import com.example.trove_mobile.auth.LoginActivity;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySettingsFragment extends Fragment {
    private Button btnAccDelete;
    private RelativeLayout changePasswordOption;
    private RelativeLayout changeEmailOption;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_settings, container, false);

        btnAccDelete = view.findViewById(R.id.btnAccDelete);
        changePasswordOption = view.findViewById(R.id.changePasswordOption);
        changeEmailOption = view.findViewById(R.id.changeEmailOption);

        // Set onClick listener for account deletion
        btnAccDelete.setOnClickListener(v -> showDeleteConfirmationDialog());

        changePasswordOption.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ChangePasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        changeEmailOption.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ChangeEmailFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });


        return view;
    }

    private void showDeleteConfirmationDialog() {
        // Show a confirmation dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteAccount() {
        // Fetch the userId from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Make DELETE request to delete the user account
        Call<Void> call = apiService.deleteUserById(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Account deleted successfully, log out the user
                    logout();
                } else {
                    Toast.makeText(getContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        // Clear session data
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        // Show a message that the user has logged out
        Toast.makeText(getActivity(), "Account deleted successfully, logging out...", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Finish the current activity to prevent back navigation to it
        getActivity().finish();
    }

}