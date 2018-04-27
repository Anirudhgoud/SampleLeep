package com.goleep.driverapp.leep.onboarding;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.onboarding.ForgotPasswordViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends ParentAppCompatActivity implements EditTextListener {

    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.submit_button)
    Button submitButton;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private UILevelNetworkCallback submitEmailCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        if (toLogout) logoutUser();
        else if (errorMessage == null) finish();
    });

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
    public void afterTextChanged(Editable editable) {
        int rightDrawableRes = AppUtils.isValidEmail(emailEditText.getText().toString()) ? R.drawable.ic_valid : editable.length() == 0 ? 0 : R.drawable.ic_invalid;
        emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightDrawableRes, 0);
    }
}
