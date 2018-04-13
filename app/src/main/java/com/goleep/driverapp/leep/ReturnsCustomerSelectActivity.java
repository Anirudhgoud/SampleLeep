package com.goleep.driverapp.leep;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;


import com.goleep.driverapp.R;
import com.goleep.driverapp.fragments.CashSalesExistingCustomerFragment;
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);
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
