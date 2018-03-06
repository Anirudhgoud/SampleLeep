package com.goleep.driverapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 20/02/18.
 */

public class DoExpandableListAdapter extends ExpandableRecyclerAdapter<BaseListItem>{
    private List<BaseListItem> doList = new ArrayList<>();
    private View.OnClickListener headerClickListener;
    private List<BaseListItem> recyclerViewListData = new ArrayList<>();
    private int headerSelectionCount = 0;

    public void setItemCheckListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }

    private ItemCheckListener itemCheckListener;
    public DoExpandableListAdapter(Context context, List<BaseListItem> doList) {
        super(context);
        if(doList.size() > 0) {
            this.doList = doList;
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
            case AppConstants.TYPE_CASH_SALES_ITEM :
                View cashSalesItem = inflate(R.layout.confirm_cash_sales_do_item, parent);
                return new ItemViewHolder(cashSalesItem);
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
            case AppConstants.TYPE_CASH_SALES_ITEM:
            case AppConstants.TYPE_DO_ITEM:
            default:
                ((ItemViewHolder) holder).bind(position);
                break;
        }
    }

    public void upDateList(List<BaseListItem> deliveryOrders) {
        if(deliveryOrders.size() >0) {
            this.doList.clear();
            this.doList.addAll(deliveryOrders);
            recyclerViewListData.addAll(deliveryOrders);
            setItems(deliveryOrders);
        }
    }

    public void addItemsList(List<BaseListItem> baseListItems, int position) {
        for(int i=0;i<baseListItems.size();i++){
            recyclerViewListData.add(position+i+1, baseListItems.get(i));
        }
        setItems(recyclerViewListData);
    }

    public BaseListItem getItemAt(int pos) {
        return doList.get(pos);
    }

    public void setHeaderClickListener(View.OnClickListener headerClickListener) {
        this.headerClickListener = headerClickListener;
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder{
        CustomTextView tvCustomerName;
        CustomTextView tvStoreAddress;
        CustomTextView tvDoNumber;
        CustomTextView tvDate;
        CustomTextView tvSchedule;
        CustomTextView tvAmount;
        ImageView selectionIcon;

        public HeaderViewHolder(View itemView) {
            super(itemView, (ImageView) itemView.findViewById(R.id.img_arrow), headerClickListener);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvStoreAddress = itemView.findViewById(R.id.tv_store_address);
            tvDoNumber = itemView.findViewById(R.id.tv_do_number);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvSchedule = itemView.findViewById(R.id.tv_schedule);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            selectionIcon = itemView.findViewById(R.id.selection_icon);
        }

        public void bind(int position) {
            super.bind(position);
            DeliveryOrderEntity deliveryOrder = (DeliveryOrderEntity)recyclerViewListData.get(position);
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(getAddress(deliveryOrder.getDestinationAddressLine1(),
                    deliveryOrder.getDestinationAddressLine2()));
            tvDoNumber.setText(deliveryOrder.getDoNumber() ==  null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvSchedule.setText(timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
            tvAmount.setText(amountToDisplay(deliveryOrder.getTotalValue()));
            if(((DeliveryOrderEntity)recyclerViewListData.get(position)).getDeliveryOrderItemsCount() ==
                    recyclerViewListData.get(position).getSelectedCount()) {
                selectionIcon.setImageResource(R.drawable.ic_do_selected);
                headerSelectionCount +=1;
                itemCheckListener.itemChecked(deliveryOrder, true);
            }
            else {
                headerSelectionCount = headerSelectionCount == 0? 0 : --headerSelectionCount;
                selectionIcon.setImageResource(R.drawable.ic_do_unselected);
                itemCheckListener.itemChecked(deliveryOrder, false);
            }
        }
        private String dateToDisplay(String dateString){
            return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString,
                    "yyyy-MM-dd", "dd MMM, yyyy");

        }

        private String timeToDisplay(String timeString){
            if (timeString != null){
                String[] times = timeString.split(" - ");
                if(times.length == 2){
                    String startTime = DateTimeUtils.convertdDate(times[0].trim(), "HH:mm", "hh:mma");
                    String endTime = DateTimeUtils.convertdDate(times[1].trim(), "HH:mm", "hh:mma");
                    return startTime + " - " + endTime;
                }
            }
            return "-";
        }

        private String amountToDisplay(Float amountString){
            String currencySymbol = AppUtils.userCurrencySymbol();
            if (amountString != null){
                return currencySymbol + " " + Math.round(amountString);
            }
            return currencySymbol + " 0";
        }

        private String getAddress(String line1, String line2){
            String address = "";
            if(line1 != null){
                address = line1;
            }
            if(line2 != null){
                if(line1 != null){
                    address += ", ";
                }
                address = address + line2;
            }
            return address;
        }
    }

    public class OrdersHeaderViewHolder extends ExpandableRecyclerAdapter.ViewHolder{
        CustomTextView ordersHeaderTextView;
        public OrdersHeaderViewHolder(View view) {
            super(view);
            ordersHeaderTextView = view.findViewById(R.id.orders_header_text_view);
        }

        public void bind(int position) {
            ordersHeaderTextView.setText(recyclerViewListData.get(position).getString());
        }
    }

    public class ItemViewHolder extends ExpandableRecyclerAdapter.ViewHolder{
        private CustomTextView productNameTv, productQuantityTv, amountTv, unitsTv;
        private CheckBox productCheckbox;
        public ItemViewHolder(View view) {
            super(view);
            productNameTv = view.findViewById(R.id.product_name_text_view);
            productQuantityTv = view.findViewById(R.id.quantity_text_view);
            amountTv = view.findViewById(R.id.amount_text_view);
            unitsTv = view.findViewById(R.id.units_text_view);
            productCheckbox = view.findViewById(R.id.product_checkbox);
        }

        public void bind(int position) {
            DeliveryOrderItem doDetails = (DeliveryOrderItem)recyclerViewListData.get(position);
            productNameTv.setText(doDetails.getProduct().getName());
            double value = doDetails.getQuantity() * doDetails.getPrice();
            productQuantityTv.setText(doDetails.getProduct().getWeight()+" "+
                    doDetails.getProduct().getWeightUnit());
            unitsTv.setText(String.valueOf(doDetails.getQuantity()));
            amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
            if(productCheckbox != null)
                productCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked)
                            recyclerViewListData.get(0).addSelection(1);
                        else recyclerViewListData.get(0).addSelection(-1);
                        notifyDataSetChanged();
                    }
                });
        }
    }
}
