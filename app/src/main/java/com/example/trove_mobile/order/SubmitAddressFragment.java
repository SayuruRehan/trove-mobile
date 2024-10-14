package com.example.trove_mobile.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trove_mobile.R;

/**
 * Fragment for submitting or changing the shipping address.
 */
public class SubmitAddressFragment extends Fragment {

    private EditText editTextFullName, editTextAddress, editTextCity, editTextState, editTextZipCode, editTextMobileNumber;
    private Button buttonSaveAddress;
    private ImageView backButtonSubmitAddress;

    public SubmitAddressFragment() {
        // Required empty public constructor
    }

    public static SubmitAddressFragment newInstance() {
        return new SubmitAddressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_submit_address, container, false);

        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextState = view.findViewById(R.id.editTextState);
        editTextZipCode = view.findViewById(R.id.editTextZipCode);
        editTextMobileNumber = view.findViewById(R.id.editTextMobileNumber);
        buttonSaveAddress = view.findViewById(R.id.buttonSaveAddress);
        backButtonSubmitAddress = view.findViewById(R.id.backButtonSubmitAddress);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);

        // Load existing data if any
        editTextFullName.setText(sharedPreferences.getString("recipientName", ""));
        editTextAddress.setText(sharedPreferences.getString("deliveryAddress", ""));
        editTextMobileNumber.setText(sharedPreferences.getString("phoneNumber", ""));

        buttonSaveAddress.setOnClickListener(v -> {
            if (validateAddressDetails()) {
                // Save to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("recipientName", editTextFullName.getText().toString());
                editor.putString("deliveryAddress", editTextAddress.getText().toString() + ", " +
                        editTextCity.getText().toString() + ", " +
                        editTextState.getText().toString() + ", " +
                        editTextZipCode.getText().toString());
                editor.putString("phoneNumber", editTextMobileNumber.getText().toString());
                editor.apply();

                // Show a toast message
                Toast.makeText(getActivity(), "Address saved.", Toast.LENGTH_SHORT).show();

                // Navigate back to OrderFragment
                getFragmentManager().popBackStack();
            }
        });

        backButtonSubmitAddress.setOnClickListener(v -> {
            // Navigate back
            getFragmentManager().popBackStack();
        });

        return view;
    }

    private boolean validateAddressDetails() {
        if (TextUtils.isEmpty(editTextFullName.getText().toString()) ||
                TextUtils.isEmpty(editTextAddress.getText().toString()) ||
                TextUtils.isEmpty(editTextCity.getText().toString()) ||
                TextUtils.isEmpty(editTextState.getText().toString()) ||
                TextUtils.isEmpty(editTextZipCode.getText().toString()) ||
                TextUtils.isEmpty(editTextMobileNumber.getText().toString())) {
            Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}