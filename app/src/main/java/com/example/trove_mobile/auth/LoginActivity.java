package com.example.trove_mobile.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trove_mobile.MainActivity;
import com.example.mobile_app_client.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private MaterialButton btnLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.contains("userId")) {
            navigateToHome();
        }

        btnLogin.setOnClickListener(v -> loginUser());

        TextView tvRegisterNow = findViewById(R.id.tvRegisterNow);
        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare login request
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Make the API call
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                int statusCode = response.code();  // Get the HTTP status code
                LoginResponse loginResponse = response.body();  // May be null for some status codes

                if (statusCode == 200 && loginResponse != null) {
                    // Save user details to SharedPreferences
                    sharedPreferences.edit()
                            .putString("username", loginResponse.getUsername())
                            .putString("role", loginResponse.getRole())
                            .putString("userId", loginResponse.getUserId())
                            .apply();

                    navigateToHome();
                } else if (statusCode == 400) {
                    // In case of a 400 error, handle the response body, which may contain the message
                    try {
                        // Convert the error body into a LoginResponse object using Retrofit's converter
                        LoginResponse errorResponse = response.errorBody() != null ?
                                new Gson().fromJson(response.errorBody().charStream(), LoginResponse.class) : null;

                        if (errorResponse != null && errorResponse.getMessage() != null) {
                            // Show the message to the user (e.g., "Login not allowed. Your account status is 'Pending'.")
                            Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            // Generic message if no specific error message is available
                            Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Error parsing response. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
