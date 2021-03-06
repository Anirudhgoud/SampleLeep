package com.tracotech.tracoman.leep.pickup.returns;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;


import com.tracotech.tracoman.R;
import com.tracotech.tracoman.fragments.CashSalesExistingCustomerFragment;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;

public class ReturnsCustomerSelectActivity extends ParentAppCompatActivity {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_customer_select);
    }

    @Override
    public void doInitialSetup() {
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
        addFragment();
    }

    private void addFragment() {
        CashSalesExistingCustomerFragment fragment = new CashSalesExistingCustomerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button :
                finish();
                break;
        }
    }
}
