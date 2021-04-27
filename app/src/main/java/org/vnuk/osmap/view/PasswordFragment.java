package org.vnuk.osmap.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.vnuk.osmap.R;
import org.vnuk.osmap.util.Helper;
import org.vnuk.osmap.viewmodel.PasswordViewModel;

public class PasswordFragment extends Fragment {
    private static final String TAG = PasswordFragment.class.getSimpleName();

    public static final String PASSWORD_VALUE = "password_value";

    private EditText etPassword;
    private Button btnSubmit;
    private PasswordViewModel passwordViewModel;
    private Helper helper;

    public PasswordFragment() {
        super(R.layout.password_fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate.");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.v(TAG, "OnViewCreated.");
        helper = new Helper();
        setupViewModel();
        etPassword = view.findViewById(R.id.et_password);
        btnSubmit = view.findViewById(R.id.btn_submit_pass);
        btnSubmit.setOnClickListener(v -> onBtnSubmitClick());
    }

    private void setupViewModel() {
        Log.v(TAG, "Setting up ViewModel.");
        passwordViewModel = new ViewModelProvider(this).get(PasswordViewModel.class);
        passwordViewModel.getMldPassword().observe(getViewLifecycleOwner(), password -> {
            if (!TextUtils.isEmpty(password)) {
                startMapActivity(password);
            } else {
                Log.v(TAG, "User given password does not fulfill all requirements.");
                helper.alerter(getContext(), R.string.password_error_title, R.string.password_error_msg);
            }
        });
    }

    private void onBtnSubmitClick() {
        // Assuming App is only operational when device is connected to internet,
        // user is informed to connect device.
        Log.v(TAG, "Checking if device is connected to internet.");
        if (!helper.isDeviceConnected(getActivity())) {
            Log.e(TAG, "Device is not connected to internet.");
            helper.alerter(getActivity(), R.string.no_internet_title, R.string.no_internet_msg);
            // In case MapActivity is started without internet, there would be no locations on map
            // and only cached tiles will be shown.
            return;
        }
        String passValue = etPassword.getText().toString();
        passwordViewModel.submitPassword(passValue);
    }

    private void startMapActivity(String passValue) {
        Log.i(TAG, "Valid password was given, starting MapActivity.");
        Intent mapActivityIntent = new Intent(getContext(), MapActivity.class);
        mapActivityIntent.putExtra(PASSWORD_VALUE, passValue);
        startActivity(mapActivityIntent);
    }
}