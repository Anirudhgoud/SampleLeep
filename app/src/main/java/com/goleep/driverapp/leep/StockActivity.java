package com.goleep.driverapp.leep;


import android.os.Bundle;

import com.goleep.driverapp.R;

public class StockActivity extends ParentAppCompatActivity {


    @Override
    public void doInitialSetup() {
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_stock);
    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.stock), R.drawable.ic_stock_title_icon);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
