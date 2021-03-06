package com.tracotech.tracoman.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.BusinessCategoryAdapter;
import com.tracotech.tracoman.adapters.BusinessesListAdapter;
import com.tracotech.tracoman.adapters.CountryCodeAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.uihelpers.CountryCodeHelper;
import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.helpers.uimodels.CustomerInfo;
import com.tracotech.tracoman.helpers.uimodels.Business;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.dropoff.cashsales.NewCustomerActivity;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.CashSalesNewCustomerViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesNewCustomerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, TextWatcher {
    @BindView(R.id.rl_btn_add_customer)
    RelativeLayout rlBtnAddCustomer;
    @BindView(R.id.rl_add_customer)
    RelativeLayout rlAddCustomer;
    @BindView(R.id.sp_business_type)
    Spinner spBusinessType;
    @BindView(R.id.country_code_spinner)
    Spinner countryCodeSpinner;
    @BindView(R.id.et_contact_name)
    EditText etContactName;
    @BindView(R.id.et_contact_number)
    EditText etContactNumber;
    @BindView(R.id.et_email_id)
    EditText etEmailId;
    @BindView(R.id.et_designation)
    EditText etDesignation;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.sv_add_customer)
    ScrollView scrollView;
    @BindView(R.id.ac_tv_business_name)
    AutoCompleteTextView acTvBusinessName;

    private BusinessCategoryAdapter businessTypeAdapter;
    private CashSalesNewCustomerViewModel viewModel;
    private BusinessesListAdapter businessListAdapter;
    private CountryCodeAdapter countryCodeAdapter;

    private AdapterView.OnItemSelectedListener countryCodeSelectionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            viewModel.setSelectedCountry((Country) countryCodeSpinner.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public CashSalesNewCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_new_customer, container, false);
        ButterKnife.bind(this, view);
        doInitSetup();
        return view;
    }

    private void doInitSetup() {
        viewModel = ViewModelProviders.of(getActivity()).get(CashSalesNewCustomerViewModel.class);
        addListeners();
        initAdapters();
        networkCallsForBusinessData();
    }

    private void initAdapters() {
        businessTypeAdapter = new BusinessCategoryAdapter(getContext(), R.layout.custom_spinner_layout);
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBusinessType.setAdapter(businessTypeAdapter);
        businessListAdapter = new BusinessesListAdapter(getContext(), R.layout.fragment_cash_sales_new_customer, android.R.id.text1, new ArrayList<>());
        acTvBusinessName.setThreshold(1);
        acTvBusinessName.setAdapter(businessListAdapter);
        initCountryCodeAdapter();
    }

    private void initCountryCodeAdapter() {
        viewModel.setCountryList(new CountryCodeHelper().getCountries(getContext()));
        countryCodeSpinner.setOnItemSelectedListener(countryCodeSelectionListener);
        countryCodeAdapter = new CountryCodeAdapter(getContext(), R.layout.layout_country_code_item,
                getContext().getResources().getColor(R.color.text_black));
        countryCodeAdapter.addAll(viewModel.getCountryList());
        countryCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(countryCodeAdapter);
        countryCodeSpinner.setSelection(0);
        viewModel.setSelectedCountry((Country) countryCodeSpinner.getSelectedItem());
    }

    private void networkCallsForBusinessData() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ((ParentAppCompatActivity) activity).showProgressDialog();
        viewModel.getBusinessTypes(busissnesTypeCallBack);
        viewModel.getBusinessData(getBusinessesDataCallBack);
    }

    private void addListeners() {
        rlBtnAddCustomer.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        spBusinessType.setOnItemSelectedListener(this);
        acTvBusinessName.setOnItemClickListener(this);
        acTvBusinessName.addTextChangedListener(this);
    }

    private boolean checkInputsValidation() {
        String strBusinesName = acTvBusinessName.getText().toString();
        String strContactName = etContactName.getText().toString();
        String strContactNumber = etContactNumber.getText().toString();
        String strDesignation = etDesignation.getText().toString();
        String strEmailId = etEmailId.getText().toString();
        boolean returnValue = true;
        if (strBusinesName.isEmpty()) {
            acTvBusinessName.setError(getResources().getString(R.string.business_name_field_empty));
            returnValue = false;
        }
        if (viewModel.getBusinessTypeId() == 0) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return false;
            Toast.makeText(activity, getResources().getString(R.string.business_type_dropdown_error), Toast.LENGTH_LONG).show();
            returnValue = false;
        }
        if (strDesignation.isEmpty()) {
            etDesignation.setError(getResources().getString(R.string.designation_field_empty));
            returnValue = false;
        }
        if (strContactName.isEmpty()) {
            etContactName.setError(getResources().getString(R.string.invalid_name_error));
            returnValue = false;
        }
        if(viewModel.getSelectedCountry() != null && viewModel.getSelectedCountry().getDialCode().isEmpty()){
            etContactNumber.setError(getResources().getString(R.string.invalid_country_code));
            returnValue = false;
        }
        if (strContactNumber.length() < AppConstants.PHONE_MIN_LENGTH) {
            etContactNumber.setError(getResources().getString(R.string.invalid_contact_number));
            returnValue = false;
        }
        if (!AppUtils.isValidEmail(strEmailId)) {
            etEmailId.setError(getResources().getString(R.string.email_field_empty));
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_btn_add_customer:
                rlAddCustomer.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_confirm:
                if (checkInputsValidation())
                    startNewActivity();
                break;
        }
    }

    private void startNewActivity() {
        Intent intentCreateNewCustomer = new Intent(getContext(), NewCustomerActivity.class);
        intentCreateNewCustomer.putExtra(IntentConstants.CUSTOMER_INFO, getCustomerInfoParcelable());
        startActivityForResult(intentCreateNewCustomer, AppConstants.ACTIVITY_CLEAR_FORM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == AppConstants.ACTIVITY_CLEAR_FORM) {
            rlAddCustomer.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            clearCustomerFormData();
        }
    }


    private void clearCustomerFormData() {
        acTvBusinessName.setText("");
        etDesignation.setText("");
        etContactNumber.setText("");
        etContactName.setText("");
        etEmailId.setText("");
    }


    private CustomerInfo getCustomerInfoParcelable() {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setBusinessName(acTvBusinessName.getText().toString());
        customerInfo.setBusinessType(viewModel.getStrBusinesstype());
        customerInfo.setBusinessTypeId(viewModel.getBusinessTypeId());
        customerInfo.setContactName(etContactName.getText().toString());
        customerInfo.setContactNumber(etContactNumber.getText().toString());
        customerInfo.setCountryDialCode(viewModel.getSelectedCountry().getDialCode());
        customerInfo.setEmail(etEmailId.getText().toString());
        customerInfo.setBusinessId(viewModel.getSelectedBusinessId());
        customerInfo.setDesignation(etDesignation.getText().toString());
        return customerInfo;
    }

    private UILevelNetworkCallback busissnesTypeCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        activity.runOnUiThread(() -> busissnesTypeCallBack(uiModels, isDialogToBeShown, errorMessage, toLogout));
    };

    private void busissnesTypeCallBack(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
        parentActivity.dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                parentActivity.logoutUser();
            }
        } else if (uiModels.size() > 0) {
            @SuppressWarnings("unchecked") final List<Business> data = (List<Business>) uiModels;
            businessTypeAdapter.addAll(data);
        }
    }

    private UILevelNetworkCallback getBusinessesDataCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        activity.runOnUiThread(() -> handleResponsegetBusinessesDataCallBack(uiModels, isDialogToBeShown, errorMessage, toLogout));
    };

    private void handleResponsegetBusinessesDataCallBack(List<?> uiModels, boolean isDialogToBeShown,
                                                         String errorMessage, boolean toLogout) {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
        parentActivity.dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                parentActivity.logoutUser();
            }
        } else if (uiModels.size() > 0) {
            @SuppressWarnings("unchecked") final List<Business> data = (List<Business>) uiModels;
            viewModel.setListGetBusinessesData(data);
            businessListAdapter.updateData(viewModel.getListGetBusinessesData());
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Business business = businessTypeAdapter.getItem(position);
        if (business != null) {
            viewModel.setStrBusinesstype(business.getName());
            viewModel.setBusinessTypeId(business.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        viewModel.setSelectedBusinessId(0);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Business getBusinessesData = (Business) parent.getAdapter().getItem(position);
        viewModel.setSelectedBusinessId(getBusinessesData.getId());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        viewModel.setSelectedBusinessId(0);
    }
}