package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.viewmodels.CashSalesSelectProductsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesSelectProductsActivity extends ParentAppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private CashSalesSelectProductsViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_select_products);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(CashSalesSelectProductsViewModel.class);
        ButterKnife.bind(this);
        initialiseToolbar();
        initialiseTabBar();
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void initialiseTabBar() {
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(getString(R.string.scan), getResources().getDrawable(R.drawable.ic_scan_barcode))));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(getString(R.string.search), getResources().getDrawable(R.drawable.ic_search))));
    }

    private View getTabView(String title, Drawable iconDrawable) {
        View listTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = listTab.findViewById(R.id.title_text);
        ImageView icon = listTab.findViewById(R.id.icon);
        listTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(title);
        icon.setImageDrawable(iconDrawable);
        return listTab;
    }
}
