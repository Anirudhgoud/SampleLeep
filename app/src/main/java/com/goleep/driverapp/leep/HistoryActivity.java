package com.goleep.driverapp.leep;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.HistoryListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.FontUtils;
import com.goleep.driverapp.viewmodels.DeliveryOrderViewModel;
import com.goleep.driverapp.viewmodels.HistoryViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends ParentAppCompatActivity implements Observer<List<DeliveryOrderEntity>>{

    @BindView(R.id.rb_today)
    RadioButton rbToday;
    @BindView(R.id.rb_this_week)
    RadioButton rbThisWeek;
    @BindView(R.id.rb_this_month)
    RadioButton rbThisMonth;
    @BindView(R.id.rg_sort)
    RadioGroup rgDuration;
    @BindView(R.id.rg_orders_type)
    RadioGroup rgOrdersType;
    @BindView(R.id.rb_delivery_order)
    RadioButton rbDeliveredOrders;
    @BindView(R.id.rb_returned_order)
    RadioButton rbReturnedOrders;
    @BindView(R.id.history_recyclerview)
    RecyclerView doRecyclerView;
    private HistoryListAdapter adapter;
    private HistoryViewModel deliveryOrderViewModel;

    final String ORDER_TYPE_DO = "delivered";
    final String ORDER_TYPE_RO = "returned";

    private UILevelNetworkCallback ordersHistoryCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            if(uiModels != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateList((List<DeliveryOrderEntity>) uiModels);
                    }
                });
            }
        }
    };

    private View.OnClickListener detailsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String doId = (String) view.getTag();
            startDetailActivity(doId);
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(HistoryActivity.this);
        deliveryOrderViewModel = ViewModelProviders.of(HistoryActivity.this).get(HistoryViewModel.class);
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
        Typeface typeface = new FontUtils().getTypeface(HistoryActivity.this, "NotoSans-Regular");
        rbToday.setTypeface(typeface);
        rbThisWeek.setTypeface(typeface);
        rbThisMonth.setTypeface(typeface);
        rbDeliveredOrders.setTypeface(typeface);
        rbReturnedOrders.setTypeface(typeface);
        initialiseRadioButtons();
        initRecyclerView();
    }

    private void initialiseRadioButtons(){
        rgOrdersType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                onOrdersTypeChange(checkedId);
            }
        });
        rgDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioSelectionChange(checkedId);
            }
        });
    }

    private void initRecyclerView() {
        doRecyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        doRecyclerView.addItemDecoration(new DividerItemDecoration(HistoryActivity.this,
                DividerItemDecoration.VERTICAL));
        LiveData<List<DeliveryOrderEntity>> deliveryOrdersLiveData = deliveryOrderViewModel.getDeliveryOrders(
                DeliveryOrderViewModel.TYPE_CUSTOMER, DeliveryOrderViewModel.STATUS_DELIVERED);
        deliveryOrdersLiveData.observe(HistoryActivity.this, HistoryActivity.this);
        adapter = new HistoryListAdapter(new ArrayList<>());
        doRecyclerView.setAdapter(adapter);
        adapter.setDetailsClickListener(detailsOnClickListener);
        getInitialData();
    }

    private void getInitialData() {
        showProgressDialog();
        Date today = new Date();
        String todayDateString = DateTimeUtils.convertedDate(today, "dd/MM/yyyy");
        deliveryOrderViewModel.fetchDeliveryOrders(todayDateString, todayDateString,
                ordersHistoryCallback);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }

    private void onOrdersTypeChange(int checkedId){
        showProgressDialog();
        switch (checkedId){
            case R.id.rb_delivery_order:
                deliveryOrderViewModel.fetchDeliveryOrders(ordersHistoryCallback);
                break;
            case R.id.rb_returned_order:
                deliveryOrderViewModel.fetchReturnedOrders(ordersHistoryCallback);
                break;
        }
    }

    private void onRadioSelectionChange(int checkedId){
        showProgressDialog();
        Date today = new Date();
        String todayDateString = DateTimeUtils.convertedDate(today, "dd/MM/yyyy");
        int ordersTYpe = rgOrdersType.getCheckedRadioButtonId();
        switch (checkedId){
            case R.id.rb_today:
                if(ordersTYpe == R.id.rb_delivery_order) {
                    deliveryOrderViewModel.fetchDeliveryOrders(todayDateString, todayDateString,
                            ordersHistoryCallback);
                }
                else{
                    deliveryOrderViewModel.fetchReturnedOrders(todayDateString, todayDateString, ordersHistoryCallback);
                }
                break;

            case R.id.rb_this_week:
                Calendar calender = Calendar.getInstance();
                calender.add(Calendar.DAY_OF_YEAR, -7);
                Date weekAgo = calender.getTime();
                String weekAgoString = DateTimeUtils.convertedDate(weekAgo, "dd/MM/yyyy");
                if(ordersTYpe == R.id.rb_delivery_order) {
                    deliveryOrderViewModel.fetchDeliveryOrders(weekAgoString, todayDateString,
                            ordersHistoryCallback);
                }
                else{
                    deliveryOrderViewModel.fetchReturnedOrders(weekAgoString, todayDateString, ordersHistoryCallback);
                }
                break;

            case R.id.rb_this_month:
                Calendar month = Calendar.getInstance();
                month.add(Calendar.DAY_OF_YEAR, -30);
                Date monthAgo = month.getTime();
                String monthAgoString = DateTimeUtils.convertedDate(monthAgo, "dd/MM/yyyy");
                if(ordersTYpe == R.id.rb_delivery_order) {
                    deliveryOrderViewModel.fetchDeliveryOrders(monthAgoString, todayDateString,
                            ordersHistoryCallback);
                }
                else{
                    deliveryOrderViewModel.fetchReturnedOrders(monthAgoString, todayDateString, ordersHistoryCallback);
                }
                break;
        }
    }

    @Override
    public void onChanged(@Nullable List<DeliveryOrderEntity> deliveryOrderEntities) {
        if(deliveryOrderEntities.size() > 0) {
            adapter.updateList(deliveryOrderEntities);
        }
    }

    private void startDetailActivity(String doId){
        Intent intent = new Intent(HistoryActivity.this, HistoryDetailsActivity.class);
        intent.putExtra(IntentConstants.ORDER_ID, doId);
        startActivity(intent);
    }
}
