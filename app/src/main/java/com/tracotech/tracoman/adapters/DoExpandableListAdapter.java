package com.tracotech.tracoman.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.helpers.uimodels.CashSalesInfo;
import com.tracotech.tracoman.interfaces.ItemCheckListener;
import com.tracotech.tracoman.leep.pickup.pickup.PickupActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.DateTimeUtils;
import com.tracotech.tracoman.utils.LogUtils;
import com.tracotech.tracoman.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tracotech.tracoman.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT;
import static com.tracotech.tracoman.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;

/**
 * Created by vishalm on 20/02/18.
 */

public class DoExpandableListAdapter extends ExpandableRecyclerAdapter<BaseListItem>{
    private View.OnClickListener headerClickListener;
    private List<BaseListItem> recyclerViewListData = new ArrayList<>();
    private Context context;
    public void setItemCheckListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }

    private ItemCheckListener itemCheckListener;
    public DoExpandableListAdapter(Context context, List<BaseListItem> doList) {
        super(context);
        this.context = context;
        if(doList.size() > 0) {
            this.recyclerViewListData = doList;
            setItems(doList);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AppConstants.TYPE_HEADER:
                View headerView = inflate(R.layout.item_header, parent);
                return new HeaderViewHolder(headerView);
            case AppConstants.TYPE_ORDERS_HEADER:
                View ordersHeader = inflate(R.layout.orders_header_layout, parent);
                return new OrdersHeaderViewHolder(ordersHeader);
            case AppConstants.TYPE_ITEMS_HEADER:
                View itemsHeader = inflate(R.layout.item_header_layout, parent);
                return new OrdersHeaderViewHolder(itemsHeader);
            case AppConstants.TYPE_CASH_SALES_ITEM :
                View cashSalesItem = inflate(R.layout.confirm_cash_sales_do_item, parent);
                return new ItemViewHolder(cashSalesItem);
            case AppConstants.TYPE_SALES_INFO:
                View cashSalesInfo = inflate(R.layout.sales_info_layout, parent);
                return new CashSalesInfoViewHolder(cashSalesInfo);
            case AppConstants.TYPE_DO_ITEM:
            default:
                View contentView = inflate(R.layout.do_details_list_item, parent);
                return new ItemViewHolder(contentView);
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AppConstants.TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case AppConstants.TYPE_ORDERS_HEADER:
                ((OrdersHeaderViewHolder)holder).bind(position);
                break;
            case AppConstants.TYPE_SALES_INFO:
                ((CashSalesInfoViewHolder) holder).bind(position);
                break;
            case AppConstants.TYPE_CASH_SALES_ITEM:
            case AppConstants.TYPE_DO_ITEM:
                ((ItemViewHolder) holder).bind(position);
                break;
        }
    }

    public void upDateList(List<BaseListItem> deliveryOrders) {
        if(deliveryOrders.size() >0) {
            recyclerViewListData.clear();
            recyclerViewListData.addAll(deliveryOrders);
            setItems(recyclerViewListData);
        }
    }

    public void addItemsList(List<BaseListItem> baseListItems, int doId) {
        int listSize = recyclerViewListData.size();
        int position = doPositionMapAllItems.get(doId);
        for (int j = position + 1; j < listSize; j++) {
            if (!(recyclerViewListData.get(position + 1) instanceof DeliveryOrderEntity)) {
                recyclerViewListData.remove(position + 1);
            } else break;
        }
        BaseListItem itemsHeader = new BaseListItem();
        itemsHeader.setItemType(AppConstants.TYPE_ITEMS_HEADER);
        recyclerViewListData.add(++position, itemsHeader);
        position += 1;                          //Insert after Header and ItemsHeader
        for (int i = 0; i < baseListItems.size(); i++) {
            BaseListItem baseListItem = baseListItems.get(i);
            baseListItem.setItemType(AppConstants.TYPE_DO_ITEM);
            recyclerViewListData.add(position + i, baseListItem);
        }
        setItems(recyclerViewListData);
    }

    public BaseListItem getItemAt(int pos) {
        return visibleItems.get(pos);
    }

    public void setHeaderClickListener(View.OnClickListener headerClickListener) {
        this.headerClickListener = headerClickListener;
    }

    public void addCombinedListItems(List<BaseListItem> baseListItems) {
        if (baseListItems.size() > 0) {
            recyclerViewListData.clear();
            recyclerViewListData.addAll(baseListItems);
            setItems(recyclerViewListData);
        }
    }

    public boolean isPartialDoSelected() {
        for(BaseListItem baseListItem:visibleItems){
            if(baseListItem instanceof DeliveryOrderEntity){
                int a = baseListItem.getSelectedCount();
                int b = ((DeliveryOrderEntity) baseListItem).getDeliveryOrderItemsCount();
                if(baseListItem.getSelectedCount() != 0 &&
                        baseListItem.getSelectedCount() != ((DeliveryOrderEntity) baseListItem).getDeliveryOrderItemsCount())
                    return true;
            }
        }
        return false;
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder{
        TextView tvCustomerName;
        TextView tvStoreAddress;
        TextView tvDate;
        TextView tvSchedule;
        TextView tvAmount;
        ImageView selectionIcon;
        LinearLayout dateLayout;
        LinearLayout timeLayout;
        LinearLayout doNumberLayout;

        public HeaderViewHolder(View itemView) {
            super(itemView, itemView.findViewById(R.id.img_arrow), headerClickListener);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvStoreAddress = itemView.findViewById(R.id.tv_store_address);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvSchedule = itemView.findViewById(R.id.tv_schedule);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            selectionIcon = itemView.findViewById(R.id.selection_icon);
            dateLayout = itemView.findViewById(R.id.date_layout);
            timeLayout = itemView.findViewById(R.id.time_layout);
            doNumberLayout = itemView.findViewById(R.id.do_number_layout);
        }

        public void bind(int position) {
            super.bind(position);
            DeliveryOrderEntity deliveryOrder = (DeliveryOrderEntity) visibleItems.get(position);
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(),
                    deliveryOrder.getDestinationAddressLine2()));
            tvAmount.setText(StringUtils.amountToDisplay(deliveryOrder.getTotalValue(), itemView.getContext()));
            doNumberLayout.setVisibility(View.GONE);
            if (((Activity) context).getClass().getSimpleName().equals(PickupActivity.class.getSimpleName())) {
                tvDate.setText(DateTimeUtils.convertdDate(deliveryOrder.getPreferredDeliveryDate(),
                        ORDER_SERVER_DATE_FORMAT, ORDER_DISPLAY_DATE_FORMAT));
                tvSchedule.setText(StringUtils.timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
                dateLayout.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.VISIBLE);
                selectionIcon.setVisibility(View.VISIBLE);
                DeliveryOrderEntity deliveryOrderEntity =  ((DeliveryOrderEntity) visibleItems.get(position));
                if (deliveryOrderEntity.getDeliveryOrderItemsCount() ==
                        visibleItems.get(position).getSelectedCount()) {
                    selectionIcon.setImageResource(R.drawable.ic_do_selected);
                    itemCheckListener.itemChecked(deliveryOrder, true);
                } else {
                    selectionIcon.setImageResource(R.drawable.ic_do_unselected);
                    itemCheckListener.itemChecked(deliveryOrder, false);
                }
            } else {
                dateLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.GONE);
                selectionIcon.setVisibility(View.GONE);
            }
        }
    }

    public class OrdersHeaderViewHolder extends ExpandableRecyclerAdapter.ViewHolder{
        TextView ordersHeaderTextView;
        public OrdersHeaderViewHolder(View view) {
            super(view);
            ordersHeaderTextView = view.findViewById(R.id.orders_header_text_view);
        }

        public void bind(int position) {
            if(position < visibleItems.size())
                ordersHeaderTextView.setText(visibleItems.get(position).getOrdersHeader());
        }
    }

    private class CashSalesInfoViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        private TextView totalProductsTv;
        private TextView totalValueTv;

        public CashSalesInfoViewHolder(View view) {
            super(view);
            totalProductsTv = view.findViewById(R.id.total_products_tv);
            totalValueTv = view.findViewById(R.id.total_value_tv);
        }

        public void bind(int position) {
            CashSalesInfo cashSalesInfo = (CashSalesInfo) visibleItems.get(position);
            if (cashSalesInfo != null) {
                totalProductsTv.setText(String.format(context.getResources().getString(R.string.total_product_label),
                        cashSalesInfo.getTotalProducts()));
                totalValueTv.setText(String.format(context.getResources().getString(R.string.total_value_label),
                        StringUtils.amountToDisplay((float) cashSalesInfo.getTotalValue(), itemView.getContext())));
            }
        }
    }

    public class ItemViewHolder extends ExpandableRecyclerAdapter.ViewHolder{
        private TextView productNameTv, productQuantityTv, amountTv, unitsTv;
        private CheckBox productCheckbox;
        private CompoundButton.OnCheckedChangeListener checkListener = (compoundButton, isChecked) -> {
            int doId = (int) compoundButton.getTag();
            visibleItems.get(getLayoutPosition()).checkItem(isChecked);
            if (isChecked)
                allItems.get(doPositionMapAllItems.get(doId)).addSelection(1);
            else allItems.get(doPositionMapAllItems.get(doId)).addSelection(-1);
            LogUtils.debug("Selection", allItems.get(doPositionMapAllItems.get(doId)).getSelectedCount()+"");
            notifyDataSetChanged();
        };
        public ItemViewHolder(View view) {
            super(view);
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
            productNameTv = view.findViewById(R.id.product_name_text_view);
            productQuantityTv = view.findViewById(R.id.quantity_text_view);
            amountTv = view.findViewById(R.id.amount_text_view);
            unitsTv = view.findViewById(R.id.units_text_view);
            productCheckbox = view.findViewById(R.id.product_checkbox);
        }

        public void bind(int position) {
            OrderItemEntity doDetails = (OrderItemEntity) visibleItems.get(position);
            productNameTv.setText(doDetails.getProduct().getName());
            double value = doDetails.getQuantity() * doDetails.getPrice();
            productQuantityTv.setText(doDetails.getProduct().getWeight()+" "+
                    doDetails.getProduct().getWeightUnit());
            unitsTv.setText(String.valueOf(doDetails.getQuantity()));
            amountTv.setText(StringUtils.amountToDisplay((float) value, itemView.getContext()));
            if (context instanceof PickupActivity) {
                {
                    if(productCheckbox != null) {
                        productCheckbox.setVisibility(View.VISIBLE);
                        productCheckbox.setTag(doDetails.getOrderId());
                        productCheckbox.setOnCheckedChangeListener(null);
                        LogUtils.debug("Test", doDetails.getId()+" "+visibleItems.get(position).isItemChecked());
                        productCheckbox.setChecked(visibleItems.get(position).isItemChecked());
                        productCheckbox.setOnCheckedChangeListener(checkListener);
                    }
                }
            } else{
                if(productCheckbox != null)
                    productCheckbox.setVisibility(View.GONE);
            }
        }
    }
}
