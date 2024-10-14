package com.example.trove_mobile.payment;

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

import com.example.mobile_app_client.R;

/**
 * Fragment for adding or changing payment methods.
 */
public class PaymentMethodsFragment extends Fragment {

    private EditText referencesInput;
    private Button savePaymentButton;
    private ImageView backButtonPayment;

    public PaymentMethodsFragment() {
        // Required empty public constructor
    }

    public static PaymentMethodsFragment newInstance() {
        return new PaymentMethodsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_payment_methods, container, false);

        referencesInput = view.findViewById(R.id.referencesInput);
        savePaymentButton = view.findViewById(R.id.savePaymentButton);
        backButtonPayment = view.findViewById(R.id.backButtonPayment);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userSession", Context.MODE_PRIVATE);

        // Load existing payment reference if any
        referencesInput.setText(sharedPreferences.getString("paymentReference", ""));

        savePaymentButton.setOnClickListener(v -> {
            if (validatePaymentDetails()) {
                // Save to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("paymentReference", referencesInput.getText().toString());
                editor.apply();

                // Show a toast message
                Toast.makeText(getActivity(), "Payment reference saved successfully.", Toast.LENGTH_SHORT).show();

                // Navigate back to OrderFragment
                getFragmentManager().popBackStack();
            }
        });


        backButtonPayment.setOnClickListener(v -> {
            // Navigate back
            getFragmentManager().popBackStack();
        });

        return view;
    }

    private boolean validatePaymentDetails() {
        if (TextUtils.isEmpty(referencesInput.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter payment reference", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}