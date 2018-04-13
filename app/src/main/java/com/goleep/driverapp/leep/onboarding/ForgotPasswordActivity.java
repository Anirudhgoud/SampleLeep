package com.goleep.driverapp.leep.onboarding;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.ForgotPasswordViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends ParentAppCompatActivity implements EditTextListener {
    @BindView(R.id.email_edit_text)
    CustomEditText emailEditText;
    @BindView(R.id.submit_button)
    CustomButton submitButton;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private UILevelNetworkCallback submitEmailCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if (toLogout)
                logoutUser();
            else if (errorMessage == null) {
                finish();
            }
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        forgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
        initView();
    }

    private void initView() {
        new EditTextHelper(this).attachTextChangedListener(emailEditText);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_forgot_password);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.submit_button:
                performSubmitEmail();
                break;
        }
    }

    private void performSubmitEmail() {
        if (AppUtils.isValidEmail(emailEditText.getText().toString())) {
            submitEmail();
        }
    }

    private void submitEmail() {
        forgotPasswordViewModel.submitEmail(emailEditText.getText().toString(), submitEmailCallback);
    }

    @Override
    public void onTextChanged(int textLength) {
        if (AppUtils.isValidEmail(emailEditText.getText().toString())) {
            emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
        } else {
            if (textLength == 0)
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }
}
