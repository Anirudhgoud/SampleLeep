package com.goleep.driverapp.leep;

import android.os.Bundle;

import com.goleep.driverapp.R;

public class ReturnsSelectProductsActivity extends ParentAppCompatActivity {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_select_products);
    }

    @Override
    public void doInitialSetup() {
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
    }

    @Override
    public void onClickWithId(int resourceId) {

    }
}
