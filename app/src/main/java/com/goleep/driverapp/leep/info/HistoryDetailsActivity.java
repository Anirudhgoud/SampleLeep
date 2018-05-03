package com.goleep.driverapp.leep.info;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.uihelpers.PrintableLine;
import com.goleep.driverapp.helpers.uihelpers.PrinterHelper;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.dropoff.deliveryorders.DropOffPaymentConfirmationActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.printer.PrinterService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.information.HistoryDetailsViewModel;
import com.ngx.BluetoothPrinter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.RAILS_TIMESTAMP_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;

public class HistoryDetailsActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    TextView customerNameTv;
    @BindView(R.id.tv_store_address)
    TextView storeAddressTv;
    @BindView(R.id.tv_do_number)
    TextView doNumberTv;
    @BindView(R.id.tv_do_id_label)
    TextView tvDoLabel;
    @BindView(R.id.tv_date)
    TextView dateTv;
    @BindView(R.id.tv_schedule)
    TextView timeTv;
    @BindView(R.id.tv_items_value)
    TextView itemsTv;
    @BindView(R.id.tv_do_amount)
    TextView doAmountTv;
    @BindView(R.id.bt_print)
    Button btPrint;
    @BindView(R.id.order_items_recycler_view)
    RecyclerView productsList;

    private HistoryDetailsViewModel historyDetailsViewModel;
    private ProductListAdapter adapter;

    private UILevelNetworkCallback orderItemsCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        logoutUser();
                    } else if (isDialogToBeShown){
                        showNetworkRelatedDialogs(errorMessage);
                    }
                }else if(uiModels.size() > 0){
                    if(uiModels.get(0) instanceof OrderItemEntity)
                        adapter.updateList((List<OrderItemEntity>)uiModels);
                    else if(uiModels.get(0) instanceof ReturnOrderEntity)
                        adapter.updateReturnOrdersList((List<ReturnOrderItem>)uiModels);
                }
            });
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(HistoryDetailsActivity.this);
        historyDetailsViewModel = ViewModelProviders.of(HistoryDetailsActivity.this).get(HistoryDetailsViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_orders_history_details);
    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.history), R.drawable.ic_history_title_icon);
        initRecyclerView();
        processIntent();
        initListeners();
    }

    private void initListeners() {
        btPrint.setOnClickListener(this);
    }

    private void initRecyclerView() {
        productsList.setLayoutManager(new LinearLayoutManager(HistoryDetailsActivity.this));
        productsList.addItemDecoration(new DividerItemDecoration(HistoryDetailsActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new ProductListAdapter(new ArrayList<>());
        productsList.setAdapter(adapter);
    }

    private void processIntent(){
        String orderIdStr = getIntent().getStringExtra(IntentConstants.ORDER_ID);
        if(orderIdStr != null){
            String[] orderIdInfo = orderIdStr.split("#");
            try {
                int orderId = Integer.parseInt(orderIdInfo[1]);
                String orderType = orderIdInfo[0];
                if (orderType.equals("type_do")) {
                    historyDetailsViewModel.setOrderType(AppConstants.TYPE_DELIVERY);
                    populateDoInfo(historyDetailsViewModel.getDeliveryOrderEntity(orderId));
                    historyDetailsViewModel.fetchDoItems(orderId, orderItemsCallBack);
                } else {
                    historyDetailsViewModel.setOrderType(AppConstants.TYPE_RETURN);
                    populateRoInfo(historyDetailsViewModel.getReturnOrderEntity(orderId));
                    historyDetailsViewModel.fetchRoItems(orderId, orderItemsCallBack);
                }
            }catch (NumberFormatException e){
                long orderId = Long.parseLong(orderIdInfo[1]);
                historyDetailsViewModel.setOrderType(AppConstants.TYPE_RETURN);
                populateRoInfo(historyDetailsViewModel.getReturnOrderEntity(orderId));
                historyDetailsViewModel.fetchRoItems(orderId, orderItemsCallBack);
            }

        }
    }

    private void populateDoInfo(DeliveryOrderEntity deliveryOrderEntity) {
        if(deliveryOrderEntity != null){
            customerNameTv.setText(deliveryOrderEntity.getCustomerName());
            storeAddressTv.setText(StringUtils.getAddress(deliveryOrderEntity.getDestinationAddressLine1(),
                    deliveryOrderEntity.getDestinationAddressLine2()));
            doNumberTv.setText(deliveryOrderEntity.getDoNumber());
            dateTv.setText(DateTimeUtils.convertdDate(deliveryOrderEntity.getActualDeliveryDate(),
                    RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT));
            timeTv.setText(DateTimeUtils.convertdDate(deliveryOrderEntity.getActualDeliveryDate(),
                    RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT));
            itemsTv.setText(String.valueOf(deliveryOrderEntity.getDeliveryOrderItemsCount()));
            doAmountTv.setText(StringUtils.amountToDisplay(deliveryOrderEntity.getTotalValue(), this));
        }
    }

    private void populateRoInfo(ReturnOrderEntity returnOrderEntity){
        if(returnOrderEntity == null) return;

        String type = returnOrderEntity.getType();
        String locationName = "";
        String address = "";
        String dateTime = null;
        switch (type){
            case "driver":
                locationName = returnOrderEntity.getDestinationLocationName();
                dateTime = returnOrderEntity.getActualReturnAt();
                address = StringUtils.getAddress(returnOrderEntity.getDestinationAddressLine1(), returnOrderEntity.getDestinationAddressLine2());
                break;
            case "customer":
                locationName = returnOrderEntity.getSourceLocationName();
                dateTime = returnOrderEntity.getActualAcceptedAt();
                address = StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(), returnOrderEntity.getSourceAddressLine2());
                break;
        }
        customerNameTv.setText(locationName);
        if (address.isEmpty() || address.trim().equals(",")) storeAddressTv.setVisibility(View.GONE);
        else storeAddressTv.setText(address);
        doNumberTv.setText(String.valueOf(returnOrderEntity.getRoNumber()));
        tvDoLabel.setText(R.string.ro_number);
        dateTv.setText(DateTimeUtils.convertdDate(dateTime,
                RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT));
        timeTv.setText(DateTimeUtils.convertdDate(dateTime,
                RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT));
        itemsTv.setText(String.valueOf(returnOrderEntity.getReturnOrderItemsCount()));
        doAmountTv.setText(StringUtils.amountToDisplay((float) returnOrderEntity.getTotalValue(), this));
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
            case R.id.bt_print:
                printInvoice();
                break;
        }
    }

    private void printInvoice() {
        PrinterHelper printerHelper = new PrinterHelper(this);
        if(printerHelper.getPrinter().getState() == BluetoothPrinter.STATE_CONNECTED) {
            if(historyDetailsViewModel.getOrderType() == AppConstants.TYPE_DELIVERY) {

                List<PrintableLine> printableLines = printerHelper.generateDeliveryOrderPrintableLines(
                        historyDetailsViewModel.getDeliveryOrderEntity(),
                        historyDetailsViewModel.getDoItems(),
                        AppUtils.userCurrencySymbol(HistoryDetailsActivity.this),
                        0,  true);
                printerHelper.print(printableLines);
            }
            else {
                List<PrintableLine> printableLines = printerHelper.generateReturnOrderPrintableLines(
                        historyDetailsViewModel.getReturnOrderEntity(),
                        historyDetailsViewModel.getRoItems(), AppUtils.userCurrencySymbol(this));
                printerHelper.print(printableLines);
            }
        } else {
            printerHelper.getPrinter().showDeviceList(this);
        }
    }
}
