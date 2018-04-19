package com.goleep.driverapp.leep.dropoff.dropoff;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.customviews.LeepSuccessDialog;
import com.goleep.driverapp.interfaces.SuccessDialogEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.HomeActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.viewmodels.dropoff.dropoff.DropoffConfirmationViewModel;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropoffToWarehouseConfirmationActivity extends ParentAppCompatActivity {

    private DropoffConfirmationViewModel dropoffConfirmationViewModel;
    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    @BindView(R.id.map_button)
    LinearLayout mapButton;
    @BindView(R.id.from_text_view)
    CustomTextView fromTextView;
    @BindView(R.id.confirm_button)
    CustomButton confirmButton;
    @BindView(R.id.rv_dropoff_list)
    LinearLayout dropoffItemsList;

    private UILevelNetworkCallback dropoffConfirmCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        logoutUser();
                    } else if (isDialogToBeShown) {
                        showNetworkRelatedDialogs(errorMessage);
                    }
                } else {
                    sendSuccessBroadcast();
                    showSuccessDialog();
                    LogUtils.debug(this.getClass().getSimpleName(), "Drop off to warehouse successful");
                }
            });
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_warehouse_dropoff_confirmation);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        dropoffConfirmationViewModel = ViewModelProviders.of(this).get(DropoffConfirmationViewModel.class);
        processIntent();
        initView();
    }

    private void processIntent() {
        Intent intent = getIntent();
        int locationId = intent.getIntExtra(IntentConstants.WAREHOUSE_ID, -1);
        if (locationId != -1)
            dropoffConfirmationViewModel.setWarehouse(locationId);
        dropoffConfirmationViewModel.setSelectedReturnableIds(intent.getIntegerArrayListExtra(IntentConstants.RETURNABLE));
        dropoffConfirmationViewModel.setSelectedSellableIds(intent.getIntegerArrayListExtra(IntentConstants.SELLABLE));
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.dropoff_stock), R.drawable.ic_drop_off_title);
        setWareHouseDetails();
        initItemsList();
        confirmButton.setOnClickListener(this);
    }

    private void initItemsList() {
        List<Integer> selectedSellableItems = dropoffConfirmationViewModel.getSelectedSellableIds();
        List<Integer> selectedReturnableIds = dropoffConfirmationViewModel.getSelectedReturnableIds();
        if (selectedReturnableIds != null && selectedReturnableIds.size() > 0) {
            dropoffItemsList.addView(orderHeaderView(getString(R.string.returned)));
            dropoffItemsList.addView(listHeaderView());
            dropoffItemsList.addView(dividerView());
            for (Integer id : selectedReturnableIds) {
                View view = stockProductView(dropoffConfirmationViewModel.getStockProduct(id),
                        ProductListAdapter.TYPE_RETURNED);
                dropoffItemsList.addView(view);
                dropoffItemsList.addView(dividerView());
            }
        }
        if (selectedSellableItems != null && selectedSellableItems.size() > 0) {
            dropoffItemsList.addView(orderHeaderView(getString(R.string.sellable)));
            dropoffItemsList.addView(listHeaderView());
            dropoffItemsList.addView(dividerView());
            for (Integer id : selectedSellableItems) {
                View view = stockProductView(dropoffConfirmationViewModel.getStockProduct(id),
                        ProductListAdapter.TYPE_SELLABLE);
                dropoffItemsList.addView(view);
                dropoffItemsList.addView(dividerView());
            }
        }
    }

    private View orderHeaderView(String header) {
        View view = LayoutInflater.from(this).inflate(R.layout.orders_header_layout, dropoffItemsList, false);
        CustomTextView textView = view.findViewById(R.id.orders_header_text_view);
        textView.setText(header);
        return view;
    }

    private View listHeaderView() {
        return LayoutInflater.from(this).inflate(R.layout.item_list_header_layout, dropoffItemsList, false);
    }

    private View dividerView() {
        View dividerView = LayoutInflater.from(this).inflate(R.layout.divider_view, dropoffItemsList, false);
        dividerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        return dividerView;
    }

    private View stockProductView(StockProductEntity stockProductEntity, int type) {
        View orderItemView = LayoutInflater.from(this).inflate(R.layout.do_details_list_item,
                dropoffItemsList, false);
        CustomTextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        CustomTextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        CustomTextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        CustomTextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        productCheckbox.setVisibility(View.GONE);
        tvProductName.setText(stockProductEntity.getProductName() == null ? "" :
                stockProductEntity.getProductName());
        tvProductQuantity.setText(getString(R.string.weight_with_units, stockProductEntity.getWeight(),
                stockProductEntity.getWeightUnit()));
        tvUnits.setText(String.valueOf(stockProductEntity.getQuantity(type)));

        double value = stockProductEntity.getQuantity(type) * stockProductEntity.getDefaultPrice();
        tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this),
                String.format(Locale.getDefault(), "%.02f", value)));
        return orderItemView;
    }

    private void setWareHouseDetails() {
        mapButton.setVisibility(View.GONE);
        fromTextView.setText(getString(R.string.to));
        wareHouseInfoTextView.setText(dropoffConfirmationViewModel.getWareHouseNameAddress());
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.confirm_button:
                dropoffConfirmationViewModel.confirmDropoff(dropoffConfirmCallback);
                break;
        }
    }

    private void showSuccessDialog() {
        LeepSuccessDialog successDialog = new LeepSuccessDialog(this, getString(R.string.delivery_successful));
        successDialog.show();
        successDialog.setSuccessDialogEventListener(new SuccessDialogEventListener() {
            @Override
            public void onOkButtonTap() {
                goBackToHomeScreen();
            }

            @Override
            public void onCloseButtonTap() {
                goBackToHomeScreen();
            }

            @Override
            public void onPrintButtonTap() {
                LogUtils.debug(this.getClass().getSimpleName(), "Print tapped");
            }
        });
    }

    private void sendSuccessBroadcast(){
        Intent intent = new Intent(IntentConstants.TASK_SUCCESSFUL);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void goBackToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
