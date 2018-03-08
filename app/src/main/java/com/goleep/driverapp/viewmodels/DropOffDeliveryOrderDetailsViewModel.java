package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by anurag on 27/02/18.
 */

public class DropOffDeliveryOrderDetailsViewModel extends AndroidViewModel {
    private Context context;
    private AppDatabase leepDatabase;

    public DropOffDeliveryOrderDetailsViewModel(@NonNull Application application) {
        super(application);
        context = application;
        leepDatabase = RoomDBService.sharedInstance().getDatabase(context);
    }

    public DeliveryOrderEntity deliveryOrder(int deliveryOrderId){
        return leepDatabase.deliveryOrderDao().deliveryOrder(deliveryOrderId);
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

    public void fetchDeliveryOrderItems(final int doId, final UILevelNetworkCallback orderItemNetworkCallBack){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                OrderItemParser orderItemParser = new OrderItemParser();
                                leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId,
                                        orderItemParser.orderItemsByParsingJsonResponse(response, doId));
                                break;

                            case NetworkConstants.FAILURE:
                            case NetworkConstants.NETWORK_ERROR:
                                orderItemNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                                break;

                            case NetworkConstants.UNAUTHORIZED :
                                orderItemNetworkCallBack.onResponseReceived(null,
                                        false, errorMessage, true);
                                break;
                        }
                    }
                });
    }

    public String dateToDisplay(String dateString){
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString, "yyyy-MM-dd", "dd MMM, yyyy");

    }

    public String timeToDisplay(String timeString){
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
}
