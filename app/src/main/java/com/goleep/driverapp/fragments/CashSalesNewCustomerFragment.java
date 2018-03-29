package com.goleep.driverapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;

public class CashSalesNewCustomerFragment extends Fragment {

    public CashSalesNewCustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_sales_new_customer, container, false);

        RelativeLayout relativeLayoutAddCustomer = view.findViewById(R.id.rl_add_customer);
        ScrollView scrollView = view.findViewById(R.id.sv_add_customer);
        CustomTextView textViewAddCustomer = view.findViewById(R.id.tv_add_customer);
        textViewAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutAddCustomer.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });
        return view;
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
