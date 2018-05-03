package com.tracotech.tracoman.leep.info;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.ProductListAdapter;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.viewmodels.information.StocksViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StocksActivity extends ParentAppCompatActivity {

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
        stocksViewModel = ViewModelProviders.of(StocksActivity.this).get(StocksViewModel.class);
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
        initialiseRadioButtons();
        initRecyclerView();
        rgListType.check(R.id.rb_deliverable);
    }

    private void initRecyclerView() {
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(StocksActivity.this));
        stocksRecyclerView.addItemDecoration(new DividerItemDecoration(StocksActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new ProductListAdapter(stocksViewModel.getStockList(
                ProductListAdapter.TYPE_DELIVERABLE));
        stocksRecyclerView.setAdapter(adapter);
    }

    private void initialiseRadioButtons() {
        rgListType.setOnCheckedChangeListener((group, checkedId) -> onRadioSelectionChange(checkedId));
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
