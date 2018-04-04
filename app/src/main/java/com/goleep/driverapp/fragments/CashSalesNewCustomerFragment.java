package com.goleep.driverapp.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.BusinessCategoryAttribute;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.CashSalesActivity;
import com.goleep.driverapp.leep.NewCustomerActivity;
import com.goleep.driverapp.leep.ReportsActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.viewmodels.CashSalesNewCustomerViewModel;
import com.goleep.driverapp.viewmodels.ReportsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesNewCustomerFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    @BindView(R.id.rl_btn_add_customer)
    RelativeLayout relativeLayoutButtonAddCustomer;
    @BindView(R.id.rl_add_customer)
    RelativeLayout relativeLayoutAddCustomer;
    @BindView(R.id.et_business_name)
    CustomEditText etBusinessName;
    @BindView(R.id.sp_business_type)
    Spinner spinnerBusinessType;
    @BindView(R.id.et_contact_name)
    CustomEditText etContactName;
    @BindView(R.id.et_contact_number)
    CustomEditText etContactNumber;
    @BindView(R.id.et_email_id)
    CustomEditText etEmailId;
    @BindView(R.id.tv_confirm)
    CustomTextView tvConfirm;
    @BindView(R.id.sv_add_customer)
    ScrollView scrollView;
    private CashSalesActivity cashSalesActivity;
    private CashSalesNewCustomerViewModel cashSalesNewCustomerViewModel;
    private ArrayAdapter<String> adapterBusinessType;
    private List<BusinessCategoryAttribute> listBussinessCategoryAttribute;
    private String strBusinesstype = null;
    private int businessId;
    Bundle bundle;

    public CashSalesNewCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_new_customer, container, false);
        ButterKnife.bind(this, view);
        cashSalesActivity = ((CashSalesActivity) getActivity());
        doInitSetup();
        return view;
    }

    private void doInitSetup() {
        cashSalesNewCustomerViewModel = ViewModelProviders.of(cashSalesActivity).get(CashSalesNewCustomerViewModel.class);
        relativeLayoutButtonAddCustomer.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        spinnerBusinessType.setOnItemSelectedListener(this);
        adapterBusinessType = new ArrayAdapter(getContext(), R.layout.custom_spinner_layout, new ArrayList<String>());
        adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusinessType.setAdapter(adapterBusinessType);
        cashSalesNewCustomerViewModel.getBusinessTypes(busissnesTypeCallBack);
    }


    private boolean checkInputsValidation() {
        String strBusinesName = etBusinessName.getText().toString();
        String strContactName = etContactName.getText().toString();
        String strContactNumber = etContactNumber.getText().toString();
        String strEmailId = etEmailId.getText().toString();
        if (strBusinesName.length() == 0) {
            etBusinessName.setError("Business Name could not be empty");
            return false;
        } else if (strBusinesName == null) {
            Toast.makeText(cashSalesActivity, "please select Business type", Toast.LENGTH_LONG).show();
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
        bundle = new Bundle();
        bundle.putString("BUSINESS_NAME", strBusinesName);
        bundle.putString("BUSINESS_TYPE", strBusinesstype);
        bundle.putInt("BUSINESS_ID", businessId);
        bundle.putString("CONTACT_NAME", strContactName);
        bundle.putString("CONTACT_NUMBER", strContactNumber);
        bundle.putString("EMAIL_ID", strEmailId);
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
               if (checkInputsValidation()) {
                startActivity(new Intent(getContext(), NewCustomerActivity.class).putExtras(bundle));
                ((Activity) getContext()).finish();
                }
                break;
        }
    }

    private UILevelNetworkCallback busissnesTypeCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            cashSalesActivity.hideLoading();
            cashSalesActivity.runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        ((CashSalesActivity) getActivity()).hideLoading();
        if (uiModels == null) {
            if (toLogout) {
                cashSalesActivity.logout();
            } else if (isDialogToBeShown) {
                cashSalesActivity.showErrorDialog(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            cashSalesActivity.runOnUiThread(() -> {
                listBussinessCategoryAttribute = (List<BusinessCategoryAttribute>) uiModels;
                for (int i = 0; i < listBussinessCategoryAttribute.size(); i++)
                    adapterBusinessType.add(listBussinessCategoryAttribute.get(i).getBusinessName());

            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (listBussinessCategoryAttribute != null) {
            BusinessCategoryAttribute businessCategoryAttribute = listBussinessCategoryAttribute.get(position);
            strBusinesstype = businessCategoryAttribute.getBusinessName();
            businessId = businessCategoryAttribute.getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

