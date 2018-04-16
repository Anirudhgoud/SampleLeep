package com.goleep.driverapp.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.pickup.returns.ReturnsItemsConfirmationViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnItemsConfirmActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    CustomTextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    CustomTextView tvStoreAddress;
    @BindView(R.id.ll_do_number)
    LinearLayout llDoNumber;
    @BindView(R.id.tv_time)
    CustomTextView tvTime;
    @BindView(R.id.rv_return_items)
    RecyclerView returnList;
    @BindView(R.id.confirm_button)
    CustomButton btConfirm;
    private ReturnsItemsConfirmationViewModel viewModel;
    private ProductListAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_return_confirm);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        initViewModel();
        initView();
        processIntent();
    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ReturnsItemsConfirmationViewModel.class);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
        initListeners();
    }

    private void initListeners() {
        btConfirm.setOnClickListener(this);
    }

    private void processIntent(){
        Intent intent = getIntent();
        Customer customer = intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION);
        viewModel.setCustomer(customer);
        ArrayList<Product> products = intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST);
        viewModel.setProducts(products);
        populateInfo();
        initRecyclerView();
    }

    private void initRecyclerView() {
        returnList.setLayoutManager(new LinearLayoutManager(this));
        returnList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new ProductListAdapter(viewModel.getProducts());
        returnList.setAdapter(adapter);
    }

    private void populateInfo() {
        llDoNumber.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);
        tvCustomerName.setText(viewModel.getCustomer().getName());
        tvStoreAddress.setText(StringUtils.toString(viewModel.getCustomer().getArea(), ""));
    }

    private void goToNextScreen(){
        Intent intent = new Intent(this, ReturnsPaymentActivity.class);
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, viewModel.getProducts());
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getCustomer());
        startActivity(intent);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.confirm_button:
                goToNextScreen();
                break;
        }
    }

}
