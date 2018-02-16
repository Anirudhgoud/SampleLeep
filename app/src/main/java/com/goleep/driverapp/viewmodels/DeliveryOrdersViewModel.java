package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.UserMeta;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 14/02/18.
 */

public class DeliveryOrdersViewModel extends AndroidViewModel {

    private Context context;
    public LiveData<List<DeliveryOrder>> deliveryOrders;
    private AppDatabase leepDatabase;

    public DeliveryOrdersViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        leepDatabase = RoomDBService.sharedInstance().getDatabase(this.getApplication());
        deliveryOrders = leepDatabase.deliveryOrderDao().getAllDeliveryOrders();
    }

    public void fetchAllDeliveryOrders(final UILevelNetworkCallback doCallBack){

        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.DELIVERY_ORDERS_URL, true, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type){
                    case NetworkConstants.SUCCESS:

                    List<DeliveryOrder> deliveryOrdersList = new ArrayList<>();
                    try{
                        Type listType = new TypeToken<List<DeliveryOrder>>() {}.getType();
                        JSONObject obj = (JSONObject) response.get(0);
                        JsonElement element = (JsonElement) obj.get("data");
                        deliveryOrdersList = new Gson().fromJson(element, listType);
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
