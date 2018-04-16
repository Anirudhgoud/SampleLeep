package com.goleep.driverapp.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ReturnReasonListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.pickup.returns.ReturnReasonsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnsSelectReasonActivity extends ParentAppCompatActivity {
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_quantity)
    TextView tvProductQuantity;
    @BindView(R.id.tv_units_value)
    TextView tvUnits;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.confirm_button)
    AppCompatButton doneButton;
    @BindView(R.id.reasons_rv)
    RecyclerView stocksRecyclerView;

    private ReturnReasonsViewModel returnReasonsViewModel;

    private ReturnReasonListAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_select_reason);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        initViewModel();
        processIntent();
        initView();
        initRecyclerView();
    }

    private void initViewModel() {
        returnReasonsViewModel = ViewModelProviders.of(this).get(ReturnReasonsViewModel.class);
    }

    private void processIntent() {
        Intent intent = getIntent();
        returnReasonsViewModel.setProduct(intent.getParcelableExtra(IntentConstants.PRODUCT));
        returnReasonsViewModel.setReturnReasons(intent.getParcelableArrayListExtra(IntentConstants.RETURN_REASONS));
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
        populateProductInfo();
        initListeners();
    }

    private void initListeners() {
        doneButton.setOnClickListener(this);
    }

    private void populateProductInfo() {
        Product product = returnReasonsViewModel.getProduct();
        if(product != null) {
            tvProductName.setText(product.getProductName());
            tvProductQuantity.setText(product.getWeight()+product.getWeightUnit());
            tvUnits.setText(String.valueOf(product.getReturnQuantity()));
            tvValue.setText(StringUtils.amountToDisplay((float) product.getTotalReturnsPrice()));
        }
    }

    private void initRecyclerView(){
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        if(returnReasonsViewModel.getReturnReasons() != null) {
            adapter = new ReturnReasonListAdapter(returnReasonsViewModel.getReturnReasons());
            stocksRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.confirm_button:
                setResultAndFinish();
                break;
        }
    }

    private void setResultAndFinish() {
        if(adapter.getSelectedIndex() != -1) {
            Intent intent = new Intent();
            if (adapter.getSelectedReason() != null)
                intent.putExtra(IntentConstants.RETURN_REASON, adapter.getSelectedReason());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
