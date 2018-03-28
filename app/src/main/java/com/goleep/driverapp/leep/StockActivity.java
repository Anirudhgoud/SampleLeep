package com.goleep.driverapp.leep;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.helpers.uihelpers.FontProvider;
import com.goleep.driverapp.viewmodels.StocksViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockActivity extends ParentAppCompatActivity {

    @BindView(R.id.rb_deliverable)
    RadioButton rbDeliverable;
    @BindView(R.id.rb_sellable)
    RadioButton rbSellable;
    @BindView(R.id.rb_returned)
    RadioButton rbReturned;
    @BindView(R.id.stocks_recyclerview)
    RecyclerView stocksRecyclerView;
    @BindView(R.id.rg_sort)
    RadioGroup rgListType;

    private StocksViewModel stocksViewModel;
    private ProductListAdapter adapter;

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        stocksViewModel = ViewModelProviders.of(StockActivity.this).get(StocksViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_stock);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.stock), R.drawable.ic_stock_title_icon);
        Typeface typeface = FontProvider.getTypeface(FontProvider.REGULAR, this);
        rbDeliverable.setTypeface(typeface);
        rbSellable.setTypeface(typeface);
        rbReturned.setTypeface(typeface);
        initialiseRadioButtons();
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(StockActivity.this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(StockActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new ProductListAdapter(stocksViewModel.getStockList(
                ProductListAdapter.TYPE_DELIVERABLE));
        stocksRecyclerView.setAdapter(adapter);
        rgListType.check(R.id.rb_deliverable);
    }

    private void initialiseRadioButtons() {
        rgListType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioSelectionChange(checkedId);
            }
        });
    }

    private void onRadioSelectionChange(int checkedId) {
        switch (checkedId) {
            case R.id.rb_deliverable:
                adapter.updateList(stocksViewModel.getStockList(ProductListAdapter.TYPE_DELIVERABLE),
                        ProductListAdapter.TYPE_DELIVERABLE);
                break;

            case R.id.rb_sellable:
                adapter.updateList(stocksViewModel.getStockList(ProductListAdapter.TYPE_SELLABLE),
                        ProductListAdapter.TYPE_SELLABLE);
                break;

            case R.id.rb_returned:
                adapter.updateList(stocksViewModel.getStockList(ProductListAdapter.TYPE_RETURNED),
                        ProductListAdapter.TYPE_RETURNED);
                break;
        }
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }
}
