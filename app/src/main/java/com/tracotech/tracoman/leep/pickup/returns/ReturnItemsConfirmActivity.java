package com.tracotech.tracoman.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.ProductListAdapter;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.StringUtils;
import com.tracotech.tracoman.viewmodels.pickup.returns.ReturnsItemsConfirmationViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnItemsConfirmActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.ll_do_number)
    LinearLayout llDoNumber;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rv_return_items)
    RecyclerView returnList;

    @BindView(R.id.bt_skip_payment)
    Button btSkipPayment;
    @BindView(R.id.bt_collect_payment)
    Button btCollectPayment;

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
        btCollectPayment.setOnClickListener(this);
        btSkipPayment.setOnClickListener(this);
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

    private void onSkipPaymentTap(){
        Double paymentCollected = 0.0;
        Intent intent = new Intent(this, ReturnsFinalConfirmationActivity.class);
        intent.putExtra(IntentConstants.PAYMENT_COLLECTED, paymentCollected);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getCustomer());
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST,
                (ArrayList<Product>) viewModel.getProducts());
        startActivity(intent);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.bt_collect_payment:
                goToNextScreen();
                break;

            case R.id.bt_skip_payment:
                onSkipPaymentTap();
                break;
        }
    }

}
