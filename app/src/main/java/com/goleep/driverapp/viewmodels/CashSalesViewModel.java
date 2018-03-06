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
import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */

public class CashSalesViewModel extends AndroidViewModel {
    private LiveData<DeliveryOrderEntity> driverDO;
    private LiveData<List<OrderItemEntity>> driverDoDetails;
    private Context context;
    private  AppDatabase leepDatabase;

    public CashSalesViewModel(@NonNull Application application) {
        super(application);
        context = application;
        leepDatabase = RoomDBService.sharedInstance().getDatabase(context);
    }

    public LiveData<DeliveryOrderEntity> getDriverDo(){
        driverDO = leepDatabase.deliveryOrderDao().getDriverDo();
        return driverDO;
    }

    public LiveData<List<OrderItemEntity>> getDriverDoDetails(Integer id) {
        driverDoDetails = leepDatabase.deliveryOrderItemDao().getDriverDoItems(id);
        return driverDoDetails;
    }

    public void fetchDriverDo(final UILevelNetworkCallback driverDOCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "?type=driver&assignees=1&statuses=assigned",
                true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                DeliveryOrderEntity driverDO;
                                try{
                                    Type listType = new TypeToken<DeliveryOrderEntity>() {}.getType();
                                    JSONObject obj = (JSONObject) response.get(0);
                                    driverDO = new Gson().fromJson(String.valueOf(
                                            obj.getJSONArray("data").get(0)), listType);
                                    leepDatabase.deliveryOrderDao().deleteDriverDo();
                                    leepDatabase.deliveryOrderDao().insertDeliveryOrder(driverDO);

                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                                    break;
                                case NetworkConstants.UNAUTHORIZED :
                                    driverDOCallback.onResponseReceived(null,
                                            false, errorMessage, true);
                                    break;
                        }
                    }
                });
    }

    public void fetchDriverDoDetails(final Integer doId, final UILevelNetworkCallback doDetailsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                try{
                                    JSONObject obj = (JSONObject) response.get(0);
                                    DoDetailResponseModel responseModel = new DoDetailResponseModel();
                                    responseModel.parseJSON(obj.optJSONArray("delivery_order_items"), doId);
                                    leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId,
                                            responseModel.getOrderItemEntities());
                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                }
                                break;
                            case NetworkConstants.UNAUTHORIZED :
                                doDetailsCallback.onResponseReceived(null,
                                        false, errorMessage, true);
                                break;
                        }
                    }
                });
    }
}
