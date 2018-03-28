package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA;
import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by anurag on 27/02/18.
 */

public class DropOffDeliveryOrderDetailsViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;
    private int deliveryOrderId;
    private DeliveryOrderEntity deliveryOrder;
    private OrderItemEntity selectedOrderItem;
    private int selectedItemCount = 0;
    private String businessAddress;
    private double outstandingBalance;

    public DropOffDeliveryOrderDetailsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public void deliveryOrder(int deliveryOrderId) {
        deliveryOrder = leepDatabase.deliveryOrderDao().deliveryOrder(deliveryOrderId);
    }

    public LiveData<List<OrderItemEntity>> deliveryOrderItems(int deliveryOrderId){
        return leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(deliveryOrderId);
    }

    public OrderItemEntity orderItem(int orderItemId){
        return leepDatabase.deliveryOrderItemDao().getOrderItem(orderItemId);
    }

    public void updateOrderItemQuantity(int orderItemId, int updatedQuantity){
        leepDatabase.deliveryOrderItemDao().updateOrderItemQuantity(orderItemId, updatedQuantity);
    }

    public void updateOrderItemSelectionStatus(int orderItemId, boolean checked) {
        leepDatabase.deliveryOrderItemDao().updateOrderItemSelectionStatus(orderItemId, checked);
    }

    public double currentSales() {
        double total = 0;
        for (OrderItemEntity orderItem : leepDatabase.deliveryOrderItemDao().getSelectedOrderItems(deliveryOrderId)) {
            total += orderItem.getQuantity() * orderItem.getPrice();
        }
        return total;
    }

    public String dateToDisplay(String dateString){
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString,
                DateTimeUtils.ORDER_SERVER_DATE_FORMAT, ORDER_DISPLAY_DATE_FORMAT_COMMA);

    }

    public String timeToDisplay(String timeString){
        if (timeString != null){
            String[] times = timeString.split(" - ");
            if(times.length == 2){
                String startTime = DateTimeUtils.convertdDate(times[0].trim(),
                        TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                String endTime = DateTimeUtils.convertdDate(times[1].trim(),
                        TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

    public String getAddress(String line1, String line2){
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

    public String getAddress(Location location) {
        String address = "";
        if (location == null) {
            return address;
        }
        address = location.getAddressLine1() + ",\n" + location.getAddressLine2() + ",\n" + location.getCity() + ", " + location.getState() + " " + location.getPincode();
        return address;
    }

    //API calls
    public void fetchDeliveryOrderItems(final int doId, final UILevelNetworkCallback orderItemNetworkCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId,
                                    orderItemParser.orderItemsByParsingJsonResponse(response, doId));
                            int businessId = orderItemParser.getDestinationBusinessIdParsingDoDetailsJson(response);
                            orderItemNetworkCallBack.onResponseReceived(new ArrayList<>(Arrays.asList(businessId)), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            orderItemNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            orderItemNetworkCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;
                    }
                });
    }

    public void fetchBusinessLocation(int businessId, int destinationLocationId, final UILevelNetworkCallback locationCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.BUSINESS_LOCATIONS_URL + "/" + businessId + "/locations/" + destinationLocationId, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            LocationParser locationParser = new LocationParser();
                            Location location = locationParser.getBusinessLocation(response);
                            locationCallBack.onResponseReceived(new ArrayList<>(Arrays.asList(location)), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            locationCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            locationCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;
                    }
                });
    }


    //Getters and setters
    public int getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public DeliveryOrderEntity getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrderEntity deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public OrderItemEntity getSelectedOrderItem() {
        return selectedOrderItem;
    }

    public void setSelectedOrderItem(OrderItemEntity selectedOrderItem) {
        this.selectedOrderItem = selectedOrderItem;
    }

    public int getSelectedItemCount() {
        return selectedItemCount;
    }

    public void setSelectedItemCount(int selectedItemCount) {
        this.selectedItemCount = selectedItemCount;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
}
