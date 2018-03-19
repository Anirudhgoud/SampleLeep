package com.goleep.driverapp.leep;


import android.os.Bundle;

import com.goleep.driverapp.R;

public class HistoryActivity extends ParentAppCompatActivity {


    @Override
    public void doInitialSetup() {
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_history);
    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.history), R.drawable.ic_history_title_icon);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
