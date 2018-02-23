package com.goleep.driverapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 20/02/18.
 */

public class DoExpandableListAdapter extends ExpandableRecyclerAdapter<DoExpandableListAdapter.DoListItem>{
    final int TYPE_DO_ITEM = 10;
    final int LIST_DO_HEADER = 11;
    private List<DeliveryOrder> doList;
    private Map<String, DoDetails> doDetailsMap = new HashMap<>();
    View.OnClickListener headerClickListener;
    private List<DoListItem> recyclerViewListData = new ArrayList<>();
    private List<DoListItem> headerList = new ArrayList<>();

    public DoExpandableListAdapter(Context context, ArrayList<DeliveryOrder> doList,
                                   View.OnClickListener headerClickListener) {
        super(context);
        this.doList = doList;
        setItems(getList(doList));
        this.headerClickListener = headerClickListener;
    }

    private List<DoListItem> getList(ArrayList<DeliveryOrder> doList) {
        recyclerViewListData.clear();
        headerList.clear();
        List<DoListItem> list = new ArrayList<>();
        for (DeliveryOrder deliveryOrder : doList) {
            list.add(new DoListItem(deliveryOrder));
        }
        headerList.addAll(list);
        recyclerViewListData.addAll(list);
        return recyclerViewListData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = inflate(R.layout.item_header, parent);
                return new HeaderViewHolder(headerView);
            case TYPE_DO_ITEM:
            default:
                View contentView = inflate(R.layout.item_content, parent);
                return new ItemViewHolder(contentView);
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_DO_ITEM:
            default:
                ((ItemViewHolder) holder).bind(position);
                break;
        }
    }

    public void upDateList(List<DeliveryOrder> deliveryOrders) {
        this.doList.clear();
        this.doList.addAll(deliveryOrders);
        setItems(getList((ArrayList<DeliveryOrder>) deliveryOrders));
        notifyDataSetChanged();
    }

    public void updateItems(DoDetails doDetails, int headerPosition) {
        int index = recyclerViewListData.indexOf(headerList.get(headerPosition));
        if(recyclerViewListData.size() < 2)
            recyclerViewListData.add(index+1, new DoListItem(doDetails.getDeliveryOrderItems().get(0)));
        setItems(recyclerViewListData);
        notifyDataSetChanged();
    }

    public DeliveryOrder getItemAt(int pos) {
        return doList.get(pos);
    }

    public void updateMap(DoDetails doDetails) {
        doDetailsMap.put(String.valueOf(doDetails.getId()), doDetails);
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder{
        CustomTextView tvCustomerName;
        CustomTextView tvStoreAddress;
        CustomTextView tvDoNumber;
        CustomTextView tvDate;
        CustomTextView tvSchedule;
        CustomTextView tvAmount;
        public HeaderViewHolder(View itemView) {
            super(itemView, (ImageView) itemView.findViewById(R.id.img_arrow), headerClickListener);
            tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvStoreAddress = itemView.findViewById(R.id.tv_store_address);
            tvDoNumber = itemView.findViewById(R.id.tv_do_number);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvSchedule = itemView.findViewById(R.id.tv_schedule);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }

        public void bind(int position) {
            super.bind(position);
            DeliveryOrder deliveryOrder = doList.get(position);
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
            tvDoNumber.setText(deliveryOrder.getDoNumber() ==  null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvSchedule.setText(timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
            tvAmount.setText(amountToDisplay(deliveryOrder.getTotalValue()));
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

    public class ItemViewHolder extends ExpandableRecyclerAdapter.ViewHolder{
        CustomTextView itemNameTextView;
        public ItemViewHolder(View view) {
            super(view);
            itemNameTextView = view.findViewById(R.id.item_text_view);
        }

        public void bind(int position) {
            String name = recyclerViewListData.get(position).doDetails.getProduct().getName();
            itemNameTextView.setText(name);
        }
    }


    public class DoListItem extends ExpandableRecyclerAdapter.ListItem{
        DeliveryOrder deliveryOrder;
        DoDetails.DeliveryOrderItem doDetails;
        int itemType;
        public DoListItem(int itemType) {
            super(itemType);
        }
        public DoListItem(DeliveryOrder deliveryOrder){
            super(TYPE_HEADER);
            this.deliveryOrder = deliveryOrder;
        }
        public DoListItem(DoDetails.DeliveryOrderItem doItem){
            super(TYPE_DO_ITEM);
            this.doDetails = doItem;
        }

        public DeliveryOrder getDo(){
            return deliveryOrder;
        }

        public DoDetails.DeliveryOrderItem getDoDetails(){
            return doDetails;
        }
    }

}
