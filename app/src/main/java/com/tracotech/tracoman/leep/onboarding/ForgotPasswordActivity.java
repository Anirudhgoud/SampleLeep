package com.tracotech.tracoman.leep.onboarding;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.helpers.uihelpers.EditTextHelper;
import com.tracotech.tracoman.interfaces.EditTextListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.viewmodels.onboarding.ForgotPasswordViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends ParentAppCompatActivity implements EditTextListener {

    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.submit_button)
    Button submitButton;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private UILevelNetworkCallback submitEmailCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        dismissProgressDialog();
        if (uiModels != null) finish();
        else if (isDialogToBeShown){
            showNetworkRelatedDialogs(errorMessage);
        }
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
        if (AppUtils.isValidEmailOrContactNumber(emailEditText.getText().toString())) {
            submitEmail();
        }
    }

    private void submitEmail() {
        showProgressDialog();
        forgotPasswordViewModel.submitEmail(emailEditText.getText().toString(), submitEmailCallback);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int rightDrawableRes = AppUtils.isValidEmailOrContactNumber(emailEditText.getText().toString()) ? R.drawable.ic_valid : editable.length() == 0 ? 0 : R.drawable.ic_invalid;
        emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightDrawableRes, 0);
    }
}
