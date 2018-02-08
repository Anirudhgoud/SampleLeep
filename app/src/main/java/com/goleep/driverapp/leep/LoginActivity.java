package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.viewmodels.LoginViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends ParentAppCompatActivity implements EditTextListener {
    LoginViewModel loginViewModel;
    @BindView(R.id.phone_editText)CustomEditText phoneEditText;
    @BindView(R.id.password_editText) CustomEditText passwordEditText;
    @BindView(R.id.login_button)
    CustomButton loginButton;
    @BindView(R.id.forgot_password_textView)
    CustomTextView forgotPasswordTextView;

    private UILevelNetworkCallback loginCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
            handleLoginResponse(isDialogToBeShown, errorMessage);
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
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.login_button: performLoginOperation();
            break;
        }

    }

    private void performLoginOperation() {
        loginViewModel.login(phoneEditText.getText().toString(), passwordEditText.getText().toString(),
                "91", loginCallBack);
    }

    private void attachEditTextListeners() {
        new EditTextHelper(this).attachTextChangedListener(phoneEditText);
    }

    private void handleLoginResponse(boolean isDialogToBeShown, String errorMessage) {

    }

    @Override
    public void onTextChanged(int textLength) {
        if(textLength == AppConstants.PHONE_NUMBER_LENGTH){
            phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
        } else {
            phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }
}
