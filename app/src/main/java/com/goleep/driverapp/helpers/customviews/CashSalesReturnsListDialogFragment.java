package com.goleep.driverapp.helpers.customviews;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.pickup.CashSalesReturnsDialogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesReturnsListDialogFragment extends DialogFragment {

    @BindView(R.id.tv_item_count)
    CustomTextView tvItemCount;
    @BindView(R.id.tv_returned_item_count)
    TextView tvReturnedItemCount;
    @BindView(R.id.ll_returned_label)
    LinearLayout llReturnedLabel;
    @BindView(R.id.ll_item_list_layout)
    LinearLayout llItemListLayout;
    @BindView(R.id.bt_close)
    Button btClose;
    @BindView(R.id.iv_expandable_indicator)
    ImageView ivExpandableIndicator;


    private CashSalesReturnsDialogViewModel viewModel;

    public static CashSalesReturnsListDialogFragment newInstance(List<Product> selectedProductList) {
        CashSalesReturnsListDialogFragment fragment = new CashSalesReturnsListDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IntentConstants.SELECTED_PRODUCT_LIST, (ArrayList<Product>) selectedProductList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CashSalesReturnsDialogViewModel.class);
        viewModel.setSelectedProducts(getArguments().getParcelableArrayList(IntentConstants.SELECTED_PRODUCT_LIST));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.cash_sales_returns_dialog_layout, null);
        ButterKnife.bind(this, rootView);
        setCLickListeners();
        displayProductList();
        updateItemSummaryUI();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void setCLickListeners() {
        btClose.setOnClickListener(v -> getDialog().cancel());
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void updateItemSummaryUI() {
        int selectedProductCount = viewModel.getSelectedProductCount();
        if (selectedProductCount > 0) {
            tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, selectedProductCount, selectedProductCount)));
            tvItemCount.setVisibility(View.VISIBLE);
        } else {
            tvItemCount.setVisibility(View.GONE);
        }

        int returnedProductCount = viewModel.getReturnedProductCount();
        if (returnedProductCount > 0) {
            tvReturnedItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, returnedProductCount, returnedProductCount)));
            tvReturnedItemCount.setVisibility(View.VISIBLE);
            llReturnedLabel.setVisibility(View.VISIBLE);
        } else {
            tvReturnedItemCount.setVisibility(View.GONE);
            llReturnedLabel.setVisibility(View.GONE);
        }
        ivExpandableIndicator.setVisibility(View.GONE);
    }

    private void displayProductList() {
        List<Product> scannedProducts = viewModel.getSelectedProducts();

        llItemListLayout.addView(listHeaderView());
        llItemListLayout.addView(dividerView());

        for (Product product : scannedProducts) {
            llItemListLayout.addView(productListItemView(product));
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

    private View productListItemView(Product product) {
        View orderItemView = LayoutInflater.from(getContext()).inflate(R.layout.cash_sales_returns_list_item, llItemListLayout, false);
        if (product == null) return orderItemView;

        TextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        TextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        TextView tvAmount = orderItemView.findViewById(R.id.tv_amount);
        TextView tvUnits = orderItemView.findViewById(R.id.tv_units);
        LinearLayout llReturnedLabel = orderItemView.findViewById(R.id.ll_returned_label);
        TextView tvReturnedAmount = orderItemView.findViewById(R.id.tv_returned_amount);
        TextView tvReturnedUnits = orderItemView.findViewById(R.id.tv_return_units);
        TextView tvReturnreason = orderItemView.findViewById(R.id.tv_return_reason);

        tvProductName.setText(StringUtils.toString(product.getProductName(), ""));
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));

        if (product.getQuantity() > 0) {
            tvUnits.setText(String.valueOf(product.getQuantity()));
            tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.format(Locale.getDefault(), "%.02f", product.getTotalPrice())));
            viewModel.incrementSelectedProductCount();
        } else {
            tvUnits.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
        }

        if (product.getReturnQuantity() > 0) {
            tvReturnedUnits.setText(String.valueOf(product.getReturnQuantity()));
            tvReturnedAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.format(Locale.getDefault(), "%.02f", product.getTotalReturnsPrice())));
            llReturnedLabel.setVisibility(View.VISIBLE);
            tvReturnedAmount.setVisibility(View.VISIBLE);
            tvReturnreason.setVisibility(View.VISIBLE);
            ReturnReason returnReason = product.getReturnReason();
            if (returnReason != null && returnReason.getReason() != null)
                tvReturnreason.setText(returnReason.getReason());
            viewModel.incrementReturnedProductCount();
        } else {
            llReturnedLabel.setVisibility(View.GONE);
            tvReturnedAmount.setVisibility(View.GONE);
            tvReturnreason.setVisibility(View.GONE);
        }
        return orderItemView;
    }
}
