package com.example.trove_mobile.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobile_app_client.R;
import com.example.trove_mobile.auth.LoginActivity;
import com.example.trove_mobile.auth.LoginRequest;
import com.example.trove_mobile.auth.LoginResponse;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.retrofit.UpdateOperation;
import com.example.trove_mobile.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        EditText oldPasswordEditText = view.findViewById(R.id.oldPassword);
        EditText newPasswordEditText = view.findViewById(R.id.newPassword);
        Button changePasswordButton = view.findViewById(R.id.changePassConfrimBtn);

        // SharedPreferences to get the username and userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Guest User");
        String userId = sharedPreferences.getString("userId", "");

        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                // Step 1: Verify the old password by calling the login API
                verifyOldPassword(username, oldPassword, newPassword, userId);
            } else {
                Toast.makeText(getContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void verifyOldPassword(String username, String oldPassword, String newPassword, String userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        LoginRequest loginRequest = new LoginRequest(username, oldPassword);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Step 2: If old password is verified, call the updatePassword method
                    updatePassword(userId, newPassword);
                } else {
                    Toast.makeText(getContext(), "Old password verification failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "API request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword(String userId, String newPassword) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Create the PATCH request body
        List<UpdateOperation> operations = new ArrayList<>();
        operations.add(new UpdateOperation("replace", "/Password", newPassword));

        Call<User> call = apiService.updateUserPassword(userId, operations);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Step 3: Log out the user after successful password update
                    logout();
                } else {
                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "API request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Toast.makeText(getActivity(), "Password updated, logging out...", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }



}
