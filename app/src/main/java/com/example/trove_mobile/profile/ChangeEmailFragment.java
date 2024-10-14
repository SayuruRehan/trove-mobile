package com.example.trove_mobile.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trove_mobile.ProfileFragment;
import com.example.trove_mobile.R;
import com.example.trove_mobile.retrofit.ApiService;
import com.example.trove_mobile.retrofit.RetrofitClient;
import com.example.trove_mobile.retrofit.UpdateOperation;
import com.example.trove_mobile.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangeEmailFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_email, container, false);

        EditText newEmailEditText = view.findViewById(R.id.newEmail);
        Button changeEmailButton = view.findViewById(R.id.changeEmailConfrimBtn);

        // SharedPreferences to get the userId
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

        changeEmailButton.setOnClickListener(v -> {
            String newEmail = newEmailEditText.getText().toString().trim();

            if (!newEmail.isEmpty()) {
                // Make the API request to update the email
                updateEmail(userId, newEmail);
            } else {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void updateEmail(String userId, String newEmail) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        // Create the PATCH request body
        List<UpdateOperation> operations = new ArrayList<>();
        operations.add(new UpdateOperation("replace", "/Email", newEmail));

        Call<User> call = apiService.updateUserEmail(userId, operations);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Navigate to ProfileFragment after successful update
                    navigateToProfileFragment();
                } else {
                    Toast.makeText(getContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "API request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToProfileFragment() {
        // Use the FragmentManager to navigate to ProfileFragment
        Fragment profileFragment = new ProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, profileFragment)
                .addToBackStack(null)
                .commit();
    }

}