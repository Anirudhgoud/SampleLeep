package com.goleep.driverapp.leep.onboarding;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.CountryCodeAdapter;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.uihelpers.CountryCodeHelper;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.helpers.uimodels.Country;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.HomeActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;


import com.goleep.driverapp.viewmodels.onboarding.LoginViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends ParentAppCompatActivity implements EditTextListener, AdapterView.OnItemSelectedListener {

    private final int PHONE_NUMBER_LENGTH = 10;
    private final int PASSWORD_MAX_LENGTH = 15;
    private final int PASSWORD_MIN_LENGTH = 8;


    private LoginViewModel loginViewModel;
    @BindView(R.id.phone_editText)
    EditText phoneEditText;
    @BindView(R.id.password_editText)
    EditText passwordEditText;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.forgot_password_textView)
    TextView forgotPasswordTextView;
    @BindView(R.id.country_code_spinner)
    Spinner countryCodeSpinner;

    private CountryCodeAdapter adapter;

    private UILevelNetworkCallback loginCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        dismissProgressDialog();
        handleLoginResponse(uiModels, isDialogToBeShown, errorMessage);
    });

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initView();
    }

    private void initView() {
        initCountryCodeDropdown();
        loginButton.setOnClickListener(this);
        attachEditTextListeners();
        forgotPasswordTextView.setOnClickListener(this);
    }

    private void initCountryCodeDropdown() {
        loginViewModel.setCountries(new CountryCodeHelper().getCountries(this));
        countryCodeSpinner.setOnItemSelectedListener(this);
        adapter = new CountryCodeAdapter(this, R.layout.custom_spinner_layout, getResources().getColor(R.color.white));
        adapter.addAll(loginViewModel.getCountries());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);
        countryCodeSpinner.setSelection(0);
        loginViewModel.setSelectedCountry((Country) countryCodeSpinner.getSelectedItem());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_login);
        if (isLoggedIn()) {
            startHomeActivity();
        }
    }

    private boolean isLoggedIn() {
        if (!LocalStorageService.sharedInstance().getLocalFileStore().getString(
                LoginActivity.this, SharedPreferenceKeys.AUTH_TOKEN).isEmpty()) {
            LogUtils.error("AuthToken", LocalStorageService.sharedInstance().getLocalFileStore().getString(
                    LoginActivity.this, SharedPreferenceKeys.AUTH_TOKEN));
            return true;
        }
        return false;
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.login_button:
                performLoginOperation();
                break;
            case R.id.forgot_password_textView:
                startForgotPasswordActivity();
                break;
            case R.id.country_code_spinner:

        }
    }

    private void startForgotPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void performLoginOperation() {
        if (isValidUsernamePassword() && loginViewModel.getSelectedCountry() != null) {
            loginViewModel.login(phoneEditText.getText().toString(), passwordEditText.getText().toString(),
                    loginViewModel.getSelectedCountry().getDialCode(), loginCallBack);
            showProgressDialog();
        }
    }

    private boolean isValidUsernamePassword() {
        final String PATTERN = ".*[A-Za-z0-9]+.*";
        return phoneEditText.getText().toString().length() == PHONE_NUMBER_LENGTH &&
                !passwordEditText.getText().toString().isEmpty() &&
                passwordEditText.getText().toString().length() >= PASSWORD_MIN_LENGTH &&
                passwordEditText.getText().toString().length() <= PASSWORD_MAX_LENGTH &&
                passwordEditText.getText().toString().matches(PATTERN);
    }

    private void attachEditTextListeners() {
        phoneEditText.clearFocus();
        passwordEditText.clearFocus();
        new EditTextHelper(this).attachTextChangedListener(phoneEditText);
    }

    private void handleLoginResponse(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
        if (errorMessage != null)
            showNetworkRelatedDialogs(errorMessage);
        else
            startHomeActivity();

    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTextChanged(int textLength) {
        if (textLength == PHONE_NUMBER_LENGTH) {
            phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
        } else {
            if (textLength == 0)
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        loginViewModel.setSelectedCountry((Country) adapterView.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
