package com.goleep.driverapp.fragments;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.leep.NewCustomerActivity;
import com.goleep.driverapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesNewCustomerFragment extends Fragment implements View.OnClickListener {
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

    public CashSalesNewCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_new_customer, container, false);
        ButterKnife.bind(this, view);
        relativeLayoutButtonAddCustomer.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        List<String> listBussinessType = new ArrayList<>();
        listBussinessType.add("Retailer");
        listBussinessType.add("Warehouse");
        listBussinessType.add("Distributor");
        ArrayAdapter<String> adapterBusinessType = new ArrayAdapter(getContext(), R.layout.custom_spinner_layout, listBussinessType);
        adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBusinessType.setAdapter(adapterBusinessType);
        return view;
    }

    private boolean checkInputsValidation() {
        String strBusinesName = etBusinessName.getText().toString();
        String strBusinessType = "Retailer";//to be changed in future
        String strContactName = etContactName.getText().toString();
        String strContactNumber = etContactNumber.getText().toString();
        String strEmailId = etEmailId.getText().toString();
        if (strBusinesName.length() == 0) {
            etBusinessName.setError("Business Name could not be empty");
            return false;
        } else if (strContactName.length() == 0) {
            etContactName.setError("Contact Name could not be empty");
            return false;
        } else if (strContactNumber.length() != 10) {
            etContactNumber.setError("Contact Number should be 10 digits");
            return false;
        } else if (!AppUtils.isValidEmail(strEmailId)) {
            etEmailId.setError("please Enter valid email id");
        } else return true;
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
               /* if (checkInputsValidation()) {*/
                    startActivity(new Intent(getContext(), NewCustomerActivity.class));
                    ((Activity) getContext()).finish();
                /*}*/
                break;
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

}

