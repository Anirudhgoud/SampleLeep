package com.goleep.driverapp.leep.info;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DeliveryOrderViewModel;
import com.goleep.driverapp.viewmodels.information.HistoryViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.utils.DateTimeUtils.REQUEST_DATE_FORMAT;

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

    private UILevelNetworkCallback ordersHistoryCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if(uiModels != null) adapter.updateList((List<DeliveryOrderEntity>) uiModels);
            });
        }
    };

    private View.OnClickListener detailsOnClickListener = view -> {
        String doId = (String) view.getTag();
        startDetailActivity(doId);
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
        initialiseRadioButtons();
        initRecyclerView();
    }

    private void initialiseRadioButtons(){
        rgOrdersType.setOnCheckedChangeListener((radioGroup, checkedId) -> onOrdersTypeChange(checkedId));
        rgDuration.setOnCheckedChangeListener((group, checkedId) -> onRadioSelectionChange(checkedId));
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
        String todayDateString = DateTimeUtils.convertedDate(today, REQUEST_DATE_FORMAT);
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
        String todayDateString = DateTimeUtils.convertedDate(today, REQUEST_DATE_FORMAT);
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
                String weekAgoString = getWeeksStartDate();
                if(ordersTYpe == R.id.rb_delivery_order) {
                    deliveryOrderViewModel.fetchDeliveryOrders(weekAgoString, todayDateString,
                            ordersHistoryCallback);
                }
                else{
                    deliveryOrderViewModel.fetchReturnedOrders(weekAgoString, todayDateString, ordersHistoryCallback);
                }
                break;

            case R.id.rb_this_month:
                String monthAgoString = getMonthStartDate();
                if(ordersTYpe == R.id.rb_delivery_order) {
                    deliveryOrderViewModel.fetchDeliveryOrders(monthAgoString, todayDateString,
                            ordersHistoryCallback);
                }
                else{
                    deliveryOrderViewModel.fetchReturnedOrders(monthAgoString, todayDateString,
                            ordersHistoryCallback);
                }
                break;
        }
    }

    @Override
    public void onChanged(@Nullable List<DeliveryOrderEntity> deliveryOrderEntities) {
        if(deliveryOrderEntities != null && deliveryOrderEntities.size() > 0) {
            adapter.updateList(deliveryOrderEntities);
        }
    }

    private void startDetailActivity(String doId){
        Intent intent = new Intent(this, HistoryDetailsActivity.class);
        intent.putExtra(IntentConstants.ORDER_ID, doId);
        startActivity(intent);
    }

    private String getWeeksStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date mondayDate = calendar.getTime();
        calendar = Calendar.getInstance();
        Date todaysDate = calendar.getTime();
        if (mondayDate.after(todaysDate)) {
            int daysToSubtract = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
            calendar.add(Calendar.DATE, -daysToSubtract - 6);
            Date start = calendar.getTime();
            return DateTimeUtils.REQUEST_DATE_FORMAT.format(start);
        } else {
            return DateTimeUtils.REQUEST_DATE_FORMAT.format(mondayDate);
        }
    }

    private String getMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return DateTimeUtils.REQUEST_DATE_FORMAT.format(calendar.getTime());
    }
}
