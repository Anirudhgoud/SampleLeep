package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.viewmodels.LoginViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.constants.AppConstants.PHONE_NUMBER_LENGTH;

public class LoginActivity extends ParentAppCompatActivity implements EditTextListener {
    LoginViewModel loginViewModel;
    @BindView(R.id.phone_editText)CustomEditText phoneEditText;
    @BindView(R.id.password_editText) CustomEditText passwordEditText;
    @BindView(R.id.login_button) CustomButton loginButton;
    @BindView(R.id.forgot_password_textView) CustomTextView forgotPasswordTextView;

    private UILevelNetworkCallback loginCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
            handleLoginResponse(uiModels, isDialogToBeShown, errorMessage);
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginButton.setOnClickListener(this);
        attachEditTextListeners();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_login);
        if(isLoggedIn()){
            startHomeActivity();
        }
    }

    private boolean isLoggedIn() {
        if(!LocalStorageService.sharedInstance().getLocalFileStore().getString(
                LoginActivity.this, SharedPreferenceKeys.USER_META).isEmpty())
            return true;
        return false;
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.login_button: performLoginOperation();
            break;
        }
    }

    private void performLoginOperation() {
        if(phoneEditText.getText().toString().length() == PHONE_NUMBER_LENGTH &&
                !passwordEditText.getText().toString().isEmpty())
            loginViewModel.login(phoneEditText.getText().toString(), passwordEditText.getText().toString(),
                "+91", loginCallBack);
    }

    private void attachEditTextListeners() {
        phoneEditText.clearFocus();
        passwordEditText.clearFocus();
        new EditTextHelper(this).attachTextChangedListener(phoneEditText);
    }

    private void handleLoginResponse(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
        if(isDialogToBeShown)
            showNetworkRelatedDialogs(isDialogToBeShown, errorMessage);
        if(uiModels != null)
            startHomeActivity();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTextChanged(int textLength) {
        if(textLength == PHONE_NUMBER_LENGTH){
            phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
        } else {
            if(textLength == 0)
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }
}
