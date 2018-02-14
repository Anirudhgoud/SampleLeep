package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.uihelpers.EditTextHelper;
import com.goleep.driverapp.interfaces.EditTextListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.viewmodels.ForgotPasswordViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.constants.UrlConstants.FORGOT_PASSWORD_URL;

public class ForgotPasswordActivity extends ParentAppCompatActivity implements EditTextListener {
    @BindView(R.id.email_edit_text)
    CustomEditText emailEditText;
    @BindView(R.id.submit_button)
    CustomButton submitButton;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private UILevelNetworkCallback submitEmailCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
            if(errorMessage == null){
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
        switch (resourceId){
            case R.id.submit_button:
                performSubmitEmail();
                break;
        }
    }

    private void performSubmitEmail() {
        if(isValidEmail()){
            submitEmail();
        }
    }

    private void submitEmail() {
        forgotPasswordViewModel.submitEmail(emailEditText.getText().toString(), submitEmailCallback);
    }

    private boolean isValidEmail() {
        final String PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(emailEditText.getText().toString().matches(PATTERN))
            return true;
        return false;
    }

    @Override
    public void onTextChanged(int textLength) {
        if(isValidEmail()){
            emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0);
        } else {
            if(textLength == 0)
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            else
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }
}
