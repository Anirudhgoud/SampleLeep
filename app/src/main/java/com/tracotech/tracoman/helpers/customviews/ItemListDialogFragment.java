package com.tracotech.tracoman.helpers.customviews;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.ProductEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.viewmodels.ItemListDialogFragmentViewModel;

import java.util.Locale;

/**
 * Created by anurag on 13/03/18.
 */

public class ItemListDialogFragment extends DialogFragment {

    private LinearLayout llItemListLayout;
    private TextView tvItemCount;
    private TextView tvGrandTotal;
    private Button btClose;

    private ItemListDialogFragmentViewModel viewModel;

    public static ItemListDialogFragment newInstance(int deliveryOrderId, double grandTotal) {
        ItemListDialogFragment fragment = new ItemListDialogFragment();
        Bundle args = new Bundle();
        args.putInt(IntentConstants.DELIVERY_ORDER_ID, deliveryOrderId);
        args.putDouble(IntentConstants.GRAND_TOTAL, grandTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ItemListDialogFragmentViewModel.class);
        viewModel.setDeliveryOrderId(getArguments().getInt(IntentConstants.DELIVERY_ORDER_ID));
        viewModel.setGrandTotal(getArguments().getDouble(IntentConstants.GRAND_TOTAL));
        viewModel.fetchOrderItems();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.item_list_dialog_layout, null);
        connectUIElements(rootView);
        setCLickListeners();
        addItemViewsToLayout();
        setData();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void connectUIElements(View view) {
        llItemListLayout = view.findViewById(R.id.ll_item_list_layout);
        tvItemCount = view.findViewById(R.id.tv_item_count);
        tvGrandTotal = view.findViewById(R.id.tv_grand_total);
        btClose = view.findViewById(R.id.bt_close);
    }

    private void setCLickListeners() {
        btClose.setOnClickListener(v -> getDialog().cancel());
    }

    private void setData() {
        tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, viewModel.getOrderItems().size(), viewModel.getOrderItems().size())));
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(getContext()), String.valueOf(viewModel.getGrandTotal())));
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void addItemViewsToLayout() {
        llItemListLayout.addView(listHeaderView());
        llItemListLayout.addView(dividerView());
        for (OrderItemEntity orderItem : viewModel.getOrderItems()) {
            llItemListLayout.addView(orderListItemView(orderItem));
            llItemListLayout.addView(dividerView());
        }
    }

    private View listHeaderView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.item_list_header_layout, llItemListLayout, false);
    }

    private View dividerView() {
        View dividerView = LayoutInflater.from(getContext()).inflate(R.layout.divider_view, llItemListLayout, false);
        dividerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        return dividerView;
    }

    private View orderListItemView(OrderItemEntity orderItem) {
        View orderItemView = LayoutInflater.from(getContext()).inflate(R.layout.do_details_list_item, llItemListLayout, false);
        TextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        TextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        TextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        TextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        productCheckbox.setVisibility(View.GONE);

        ProductEntity product = orderItem.getProduct();
        if (product != null) {
            tvProductName.setText(product.getName() == null ? "" : product.getName());
        }
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(orderItem.getQuantity()));

        double value = orderItem.getQuantity() * orderItem.getPrice();
        tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(getContext()), String.format(Locale.getDefault(), "%.02f", value)));
        return orderItemView;
    }
}
