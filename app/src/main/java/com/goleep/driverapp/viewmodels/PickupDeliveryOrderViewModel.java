package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.services.room.entities.Driver;
import com.goleep.driverapp.services.room.entities.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.goleep.driverapp.services.room.entities.DeliveryOrderItem.getDeliveryOrderItemList;
import static com.goleep.driverapp.services.room.entities.Product.getProductsList;

/**
 * Created by vishalm on 27/02/18.
 */

public class PickupDeliveryOrderViewModel extends DropOffDeliveryOrdersViewModel {
    private Context context;
    private LiveData<List<DeliveryOrderItem>> doDetailsLiveData;

    public PickupDeliveryOrderViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public void fetchDoItems(final String doId){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                DoDetailResponseModel doDetailResponse;
                                try{
                                    Type doDetailsType = new TypeToken<DoDetailResponseModel>() {}.getType();
                                    JSONObject obj = (JSONObject) response.get(0);
                                    doDetailResponse = new Gson().fromJson(String.valueOf(obj), doDetailsType);
                                    leepDatabase.deliveryOrderItemDao().deleteDeliveryItems(doDetailResponse.getId());
                                    leepDatabase.productDao().insertAllProducts(getProductsList(doDetailResponse));
                                    leepDatabase.deliveryOrderItemDao().insertDeliveryOrderItems(
                                            getDeliveryOrderItemList(doDetailResponse));
                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                                break;
                        }
                    }
                });
    }



    public LiveData<List<DeliveryOrderItem>> getDoDetails(Integer id) {
        doDetailsLiveData = leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(id);
        return doDetailsLiveData;
    }

    public DoDetails getDoDetailsObj(Integer id) {
        return null;
    }

    public String getWareHouseNameAddress(){
        Driver driver = RoomDBService.sharedInstance().getDatabase(context).driverDao().getDriver();
        return driver.getAddressLine1()+", "+driver.getAddressLine2();
    }




}
