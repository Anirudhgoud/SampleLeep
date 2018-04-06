package com.goleep.driverapp.fragments;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.BusinessesListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.CustomerInfo;
import com.goleep.driverapp.helpers.uimodels.Business;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.CashSalesActivity;
import com.goleep.driverapp.leep.NewCustomerActivity;
import com.goleep.driverapp.leep.ParentAppCompatActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.CashSalesNewCustomerViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesNewCustomerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, TextWatcher {
    @BindView(R.id.rl_btn_add_customer)
    RelativeLayout relativeLayoutButtonAddCustomer;
    @BindView(R.id.rl_add_customer)
    RelativeLayout relativeLayoutAddCustomer;
    @BindView(R.id.sp_business_type)
    Spinner spinnerBusinessType;
    @BindView(R.id.et_contact_name)
    CustomEditText etContactName;
    @BindView(R.id.et_contact_number)
    CustomEditText etContactNumber;
    @BindView(R.id.et_email_id)
    CustomEditText etEmailId;
    @BindView(R.id.et_designation)
    CustomEditText etDesignation;
    @BindView(R.id.tv_confirm)
    CustomTextView tvConfirm;
    @BindView(R.id.sv_add_customer)
    ScrollView scrollView;
    @BindView(R.id.ac_tv_business_name)
    AutoCompleteTextView autoCompleteTextViewBusinessName;

    private ArrayAdapter<String> adapterBusinessType;
    private CashSalesNewCustomerViewModel cashSalesNewCustomerViewModel;
    BusinessesListAdapter businessesListAdapter;

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
        cashSalesNewCustomerViewModel = ViewModelProviders.of(getActivity()).get(CashSalesNewCustomerViewModel.class);
        addListeners();
        initAdapters();
        networkCalls();
    }

    private void initAdapters() {
        adapterBusinessType = new ArrayAdapter(getContext(), R.layout.custom_spinner_layout, new ArrayList<String>());
        adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusinessType.setAdapter(adapterBusinessType);
        businessesListAdapter = new BusinessesListAdapter(getContext(), R.layout.fragment_cash_sales_new_customer, android.R.id.text1, new ArrayList<Business>());
        autoCompleteTextViewBusinessName.setThreshold(1);
        autoCompleteTextViewBusinessName.setAdapter(businessesListAdapter);
    }

    private void networkCalls() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
            ((ParentAppCompatActivity) activity).showProgressDialog();
        cashSalesNewCustomerViewModel.getBusinessTypes(busissnesTypeCallBack);
        cashSalesNewCustomerViewModel.getBusinessesData(getBusinessesDataCallBack);
    }

    private void addListeners() {
        relativeLayoutButtonAddCustomer.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        spinnerBusinessType.setOnItemSelectedListener(this);
        autoCompleteTextViewBusinessName.setOnItemClickListener(this);
        autoCompleteTextViewBusinessName.addTextChangedListener(this);
    }


    private boolean checkInputsValidation() {
        String strBusinesName = autoCompleteTextViewBusinessName.getText().toString();
        String strContactName = etContactName.getText().toString();
        String strContactNumber = etContactNumber.getText().toString();
        String strDesignation = etDesignation.getText().toString();
        String strEmailId = etEmailId.getText().toString();
        if (strBusinesName.length() == 0) {
            autoCompleteTextViewBusinessName.setError("Business Name could not be empty");
            return false;
        } else if (strDesignation.length() == 0) {
            etDesignation.setError("Designation could not be empty");
            return false;
        } else if (strContactName.length() == 0) {
            etContactName.setError("Contact Name could not be empty");
            return false;
        } else if (strContactNumber.length() != 10) {
            etContactNumber.setError("Contact Number should be 10 digits");
            return false;
        } else if (!AppUtils.isValidEmail(strEmailId)) {
            etEmailId.setError("please Enter valid email id");
        }
        return true;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_btn_add_customer:
                relativeLayoutAddCustomer.setVisibility(View.GONE);
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
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setBusinessName(autoCompleteTextViewBusinessName.getText().toString());
        customerInfo.setBusinessType(cashSalesNewCustomerViewModel.getStrBusinesstype());
        customerInfo.setBusinessTypeId(cashSalesNewCustomerViewModel.getBusinessTypeId());
        customerInfo.setContactName(etContactName.getText().toString());
        customerInfo.setContactNumber(etContactNumber.getText().toString());
        customerInfo.setEmail(etEmailId.getText().toString());
        customerInfo.setBusinessId(cashSalesNewCustomerViewModel.getSelectedBusinessId());
        customerInfo.setDesignation(etDesignation.getText().toString());
        intentCreateNewCustomer.putExtra(IntentConstants.CUSTOMER_INFO, customerInfo);
        startActivity(new Intent(intentCreateNewCustomer));
        ((Activity) getContext()).finish();
    }

    private UILevelNetworkCallback busissnesTypeCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            if (!(getActivity()).isFinishing())

            getActivity().runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;

        ((ParentAppCompatActivity) activity).dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                ((ParentAppCompatActivity) activity).logoutUser();
            } else if (isDialogToBeShown) {
                ((ParentAppCompatActivity) activity).showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            getActivity().runOnUiThread(() -> {
                cashSalesNewCustomerViewModel.setListBusinessData((List<Business>) uiModels);
                for (int i = 0; i < cashSalesNewCustomerViewModel.getListBusinessData().size(); i++)
                    adapterBusinessType.add(cashSalesNewCustomerViewModel.getListBusinessData().get(i).getName());

            });
        }
    }

    private UILevelNetworkCallback getBusinessesDataCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return;
            ((ParentAppCompatActivity) activity).dismissProgressDialog();
            getActivity().runOnUiThread(() -> handleResponsegetBusinessesDataCallBack(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

    private void handleResponsegetBusinessesDataCallBack(List<?> uiModels, boolean isDialogToBeShown,
                                                         String errorMessage, boolean toLogout) {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ((ParentAppCompatActivity) activity).dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                ((ParentAppCompatActivity) activity).logoutUser();
            } else if (isDialogToBeShown) {
                ((ParentAppCompatActivity) activity).showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            getActivity().runOnUiThread(() -> {
                cashSalesNewCustomerViewModel.setListGetBusinessesData((List<Business>) uiModels);
                businessesListAdapter.updateData(cashSalesNewCustomerViewModel.getListGetBusinessesData());
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (cashSalesNewCustomerViewModel.getListBusinessData() != null) {
            Business business = cashSalesNewCustomerViewModel.getListBusinessData().get(position);
            cashSalesNewCustomerViewModel.setStrBusinesstype(business.getName());
            cashSalesNewCustomerViewModel.setBusinessTypeId(business.getId());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        cashSalesNewCustomerViewModel.setSelectedBusinessId(0);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Business getBusinessesData = (Business) parent.getAdapter().getItem(position);
        cashSalesNewCustomerViewModel.setSelectedBusinessId(getBusinessesData.getId());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        cashSalesNewCustomerViewModel.setSelectedBusinessId(0);
    }

    @Override
    public void afterTextChanged(Editable s) {
        cashSalesNewCustomerViewModel.setSelectedBusinessId(0);
    }
}