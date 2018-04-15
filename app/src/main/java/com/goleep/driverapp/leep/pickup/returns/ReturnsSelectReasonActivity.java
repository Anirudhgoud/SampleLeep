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

    private UILevelNetworkCallback returnReasonsCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            if(uiModels != null && uiModels.size() > 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateList((List<ReturnReason>) uiModels);
                    }
                });
            }
        }
    };

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
        fetchReturnReasons();
    }

    private void initViewModel() {
        returnReasonsViewModel = ViewModelProviders.of(this).get(ReturnReasonsViewModel.class);
    }

    private void processIntent() {
        returnReasonsViewModel.setProduct(getIntent().getParcelableExtra(IntentConstants.PRODUCT));
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

    private void fetchReturnReasons() {
        returnReasonsViewModel.fetchReturnReasons(returnReasonsCallback);
    }

    private void initRecyclerView(){
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        adapter = new ReturnReasonListAdapter(new ArrayList<>());
        stocksRecyclerView.setAdapter(adapter);
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
        Intent intent = new Intent();
        if(adapter.getSelectedReason() != null)
            intent.putExtra(IntentConstants.RETURN_REASON, adapter.getSelectedReason());
        setResult(RESULT_OK, intent);
        finish();
    }
}
