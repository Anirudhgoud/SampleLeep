package com.goleep.driverapp.leep;


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
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.viewmodels.DeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends ParentAppCompatActivity implements Observer<List<DeliveryOrderEntity>> {

    @BindView(R.id.rb_today)
    RadioButton rbToday;
    @BindView(R.id.rb_this_week)
    RadioButton rbThisWeek;
    @BindView(R.id.rb_this_month)
    RadioButton rbThisMonth;
    @BindView(R.id.rg_sort)
    RadioGroup rgDuration;
    @BindView(R.id.history_recyclerview)
    RecyclerView doRecyclerView;
    private HistoryListAdapter adapter;
    private DeliveryOrderViewModel deliveryOrderViewModel;
    private UILevelNetworkCallback ordersHistoryCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            if (uiModels != null) {
                if (uiModels.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateList((List<DeliveryOrderEntity>) uiModels);
                        }
                    });
                }
            }
        }
    };

    private View.OnClickListener detailsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int doId = (int) view.getTag();
            startDetailActivity(doId);
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(HistoryActivity.this);
        deliveryOrderViewModel = ViewModelProviders.of(HistoryActivity.this).get(DeliveryOrderViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_history);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.history), R.drawable.ic_history_title_icon);
        rbToday.setTypeface(AppUtils.getTypeface(HistoryActivity.this, "NotoSans-Regular"));
        rbThisWeek.setTypeface(AppUtils.getTypeface(HistoryActivity.this, "NotoSans-Regular"));
        rbThisMonth.setTypeface(AppUtils.getTypeface(HistoryActivity.this, "NotoSans-Regular"));
        initialiseRadioButtons();
        initRecyclerView();
    }

    private void initialiseRadioButtons() {
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
        deliveryOrderViewModel.fetchAllDeliveryOrders(ordersHistoryCallback, "delivered", null, null);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    private void onRadioSelectionChange(int checkedId) {
        showProgressDialog();
        Date today = new Date();
        String todayDateString = DateTimeUtils.convertedDate(today, "dd/mm/yyyy");
        switch (checkedId) {
            case R.id.rb_today:
                deliveryOrderViewModel.fetchAllDeliveryOrders(ordersHistoryCallback,
                        "delivered", todayDateString, todayDateString);
                break;

            case R.id.rb_this_week:
                Calendar calender = Calendar.getInstance();
                calender.add(Calendar.DAY_OF_YEAR, -7);
                Date weekAgo = calender.getTime();
                String weekAgoString = DateTimeUtils.convertedDate(weekAgo, "dd/mm/yyyy");
                deliveryOrderViewModel.fetchAllDeliveryOrders(ordersHistoryCallback,
                        "delivered", weekAgoString, todayDateString);
                break;

            case R.id.rb_this_month:
                Calendar month = Calendar.getInstance();
                month.add(Calendar.DAY_OF_YEAR, -7);
                Date monthAgo = month.getTime();
                String monthAgoString = DateTimeUtils.convertedDate(monthAgo, "dd/mm/yyyy");
                deliveryOrderViewModel.fetchAllDeliveryOrders(ordersHistoryCallback,
                        "delivered", monthAgoString, todayDateString);
                break;
        }
    }

    @Override
    public void onChanged(@Nullable List<DeliveryOrderEntity> deliveryOrderEntities) {
        if (deliveryOrderEntities.size() > 0) {
            adapter.updateList(deliveryOrderEntities);
        }
    }

    private void startDetailActivity(int doId) {
        Intent intent = new Intent(HistoryActivity.this, OrdersHistoryDetails.class);
        intent.putExtra(IntentConstants.DO_ID, doId);
        startActivity(intent);
    }
}
