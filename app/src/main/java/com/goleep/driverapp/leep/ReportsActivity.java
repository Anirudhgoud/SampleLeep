package com.goleep.driverapp.leep;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RadioButton;

import com.goleep.driverapp.R;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.FontUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsActivity extends ParentAppCompatActivity {

    @BindView(R.id.rb_today)
    RadioButton rbToday;
    @BindView(R.id.rb_this_week)
    RadioButton rbThisWeek;
    @BindView(R.id.rb_this_month)
    RadioButton rbThisMonth;


    @Override
    public void doInitialSetup() {
        ButterKnife.bind(ReportsActivity.this);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_reports);
    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.reports), R.drawable.ic_reports_title);
        Typeface typeface = new FontUtils().getTypeface(ReportsActivity.this, "NotoSans-Regular");
        rbToday.setTypeface(typeface);
        rbThisWeek.setTypeface(typeface);
        rbThisMonth.setTypeface(typeface);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
