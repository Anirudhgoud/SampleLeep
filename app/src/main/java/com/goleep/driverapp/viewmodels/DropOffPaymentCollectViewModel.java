package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by anurag on 09/03/18.
 */

public class DropOffPaymentCollectViewModel extends AndroidViewModel {

    protected AppDatabase leepDatabase;

    private DeliveryOrderEntity deliveryOrder;
    private int deliveryOrderId;
    private List<OrderItemEntity> orderItems;
    private Location businessLocation;
    private double outstandingBalance;

    public DropOffPaymentCollectViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public DeliveryOrderEntity deliveryOrder(int deliveryOrderId) {
        return leepDatabase.deliveryOrderDao().deliveryOrder(deliveryOrderId);
    }

    public List<OrderItemEntity> getSelectedOrderItems() {
        return leepDatabase.deliveryOrderItemDao().getSelectedOrderItems(deliveryOrderId);
    }

    public double currrentSales() {
        double total = 0;
        for (OrderItemEntity orderItem : orderItems) {
            total += orderItem.getQuantity() * orderItem.getPrice();
        }
        return total;
    }

    public String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString, "yyyy-MM-dd", "dd MMM, yyyy");

    }

    public String currentTimeToDisplay() {
        Date now = new Date();
        return DateTimeUtils.convertedDate(now, "hh:mma");
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
    public void fetchDestinationBusinessId(int sourceLocationId, int destinationLocationId, final UILevelNetworkCallback businessIdCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.CONSUMER_LOCATIONS_URL + "/" + sourceLocationId + "/consumer_locations", true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            LocationParser locationParser = new LocationParser();
                            int businessId = locationParser.getDestinationBusinessId(response, destinationLocationId);
                            businessIdCallBack.onResponseReceived(new ArrayList<>(Arrays.asList(businessId)), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            businessIdCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            businessIdCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;
                    }
                });
    }

    public void fetchBusinessLocation(int businessId, int destinationLocationId, final UILevelNetworkCallback collectPaymentNetworkCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.BUSINESS_LOCATIONS_URL + "/" + businessId + "/locations/" + destinationLocationId, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            LocationParser locationParser = new LocationParser();
                            Location location = locationParser.getBusinessLocation(response);
                            collectPaymentNetworkCallBack.onResponseReceived(new ArrayList<>(Arrays.asList(location)), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            collectPaymentNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            collectPaymentNetworkCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;
                    }
                });
    }


    //Getters and Setters
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

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public Location getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(Location businessLocation) {
        this.businessLocation = businessLocation;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }
}
