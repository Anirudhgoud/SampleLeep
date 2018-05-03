package com.tracotech.tracoman.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.ReturnReasonListAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.helpers.uimodels.ReturnReason;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.StringUtils;
import com.tracotech.tracoman.viewmodels.pickup.returns.ReturnReasonsViewModel;

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
        initToolbar();
        populateProductInfo();
        initListeners();
    }

    private void initToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        if(getIntent().getIntExtra(IntentConstants.FLOW, -1) == AppConstants.RETURNS_FLOW)
            setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
        else if(getIntent().getIntExtra(IntentConstants.FLOW, -1) == AppConstants.CASH_SALES_FLOW)
            setTitleIconAndText(getString(R.string.cash_sales), R.drawable.ic_cash_sales);
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
            tvValue.setText(StringUtils.amountToDisplay((float) product.getTotalReturnsPrice(), this));
        }
    }

    private void initRecyclerView(){
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        if(returnReasonsViewModel.getReturnReasons() != null) {
            adapter = new ReturnReasonListAdapter(returnReasonsViewModel.generateReturnReasonsList());
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
                intent.putExtra(IntentConstants.RETURN_REASON, (ReturnReason)adapter.getSelectedReason());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
