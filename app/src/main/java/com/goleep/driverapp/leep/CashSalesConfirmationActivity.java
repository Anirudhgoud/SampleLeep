package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;

import butterknife.ButterKnife;

public class CashSalesConfirmationActivity extends ParentAppCompatActivity {

    private CashSalesConfirmationViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_confirmation);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(CashSalesConfirmationViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setClickListeners();
    }

    private void extractIntentData() {
        viewModel.setConsumerLocation(getIntent().getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setScannedProducts(getIntent().getParcelableArrayListExtra(IntentConstants.PRODUCT_LIST));
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.cash_sales), R.drawable.ic_cash_sales);
    }

    private void setClickListeners() {

    }

    @Override
    public void onClickWithId(int resourceId) {

    }
}
