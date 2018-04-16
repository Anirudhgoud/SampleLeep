package com.goleep.driverapp.leep.onboarding;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;


import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.HomeActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.printer.PrinterService;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;


import com.goleep.driverapp.utils.PrinterUtils;
import com.goleep.driverapp.viewmodels.onboarding.LoginViewModel;
import com.ngx.BluetoothPrinter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends ParentAppCompatActivity implements EditTextListener {

    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final int PASSWORD_MAX_LENGTH = 15;
    public static final int PASSWORD_MIN_LENGTH = 8;


    private LoginViewModel loginViewModel;
    @BindView(R.id.phone_editText)
    CustomEditText phoneEditText;
    @BindView(R.id.password_editText)
    CustomEditText passwordEditText;
    @BindView(R.id.login_button)
    CustomButton loginButton;
    @BindView(R.id.forgot_password_textView)
    CustomTextView forgotPasswordTextView;

    private UILevelNetworkCallback loginCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            handleLoginResponse(uiModels, isDialogToBeShown, errorMessage);
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initView();
    }

    private void initView() {
        loginButton.setOnClickListener(this);
        attachEditTextListeners();
        forgotPasswordTextView.setOnClickListener(this);
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
        }
    }

    private void startForgotPasswordActivity() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void performLoginOperation() {
        if (isValidUsernamePassword()) {
            loginViewModel.login(phoneEditText.getText().toString(), passwordEditText.getText().toString(),
                    "+91", loginCallBack);
            showProgressDialog();
        }
    }

    private boolean isValidUsernamePassword() {
        final String PATTERN = ".*[A-Za-z0-9]+.*";
        if (phoneEditText.getText().toString().length() == PHONE_NUMBER_LENGTH &&
                !passwordEditText.getText().toString().isEmpty() &&
                passwordEditText.getText().toString().length() >= PASSWORD_MIN_LENGTH &&
                passwordEditText.getText().toString().length() <= PASSWORD_MAX_LENGTH &&
                passwordEditText.getText().toString().matches(PATTERN))
            return true;
        return false;
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
}
