package com.example.trove_mobile.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trove_mobile.HomeFragment;
import com.example.trove_mobile.MainActivity;
import com.example.trove_mobile.R;

/**
 * Fragment to display after a successful order.
 */
public class SuccessFragment extends Fragment {

    private Button buttonContinueShopping;

    public SuccessFragment() {
        // Required empty public constructor
    }

    public static SuccessFragment newInstance() {
        return new SuccessFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        buttonContinueShopping = view.findViewById(R.id.buttonContinueShopping);

        buttonContinueShopping.setOnClickListener(v -> {
            // Navigate to HomeFragment
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new HomeFragment());

            // Commit the transaction
            transaction.commit();

            // Update the Bottom Navigation selection to "Home"
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateBottomNavigation(R.id.home);
            }
        });

        return view;
    }
}
