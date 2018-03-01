package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */

public class DeliveryOrderViewModel extends AndroidViewModel {
    private Context context;
    protected AppDatabase leepDatabase;
    private LiveData<List<BaseListItem>> deliveryOrders;

    public static final String TYPE_CUSTOMER = "customer";
    public static final String TYPE_DRIVER = "driver";
    public static final String STATUS_IN_TRANSIT = "in_transit";
    public static final String STATUS_ASSIGNED = "assigned";

    public DeliveryOrderViewModel(@NonNull Application application) {
        super(application);
        context = application;
        leepDatabase = RoomDBService.sharedInstance().getDatabase(context);
    }
    public LiveData<List<BaseListItem>> getDeliveryOrders(String type, String status) {
        String doType;
        String doStatus;
        switch (type){
            case TYPE_CUSTOMER : doType = TYPE_CUSTOMER;
                break;
            case TYPE_DRIVER : doType = TYPE_DRIVER;
                break;
            default: doType = TYPE_CUSTOMER;
        }
        switch (status){
            case STATUS_ASSIGNED : doStatus = STATUS_ASSIGNED;
                break;
            case STATUS_IN_TRANSIT : doStatus = STATUS_IN_TRANSIT;
                break;
            default: doStatus = STATUS_ASSIGNED;
        }
        deliveryOrders = leepDatabase.deliveryOrderDao().getCustomerDeliveryOrders(doType, doStatus);
        return deliveryOrders;
    }

    public void fetchAllDeliveryOrders(final UILevelNetworkCallback doCallBack){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.DELIVERY_ORDERS_URL,
                true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                List<DeliveryOrder> deliveryOrdersList;
                                try{
                                    Type listType = new TypeToken<List<DeliveryOrder>>() {}.getType();
                                    JSONObject obj = (JSONObject) response.get(0);
                                    deliveryOrdersList = new Gson().fromJson(obj.getJSONArray("data").toString(), listType);
                                    leepDatabase.deliveryOrderDao().deleteAllDeliveryOrders();
                                    leepDatabase.deliveryOrderDao().insertDeliveryOrders(deliveryOrdersList);

                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                                break;
                        }
                    }
                });
    }
}
