package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.goleep.driverapp.services.room.entities.DeliveryOrderItem.getDeliveryOrderItemList;
import static com.goleep.driverapp.services.room.entities.Product.getProductsList;

/**
 * Created by vishalm on 22/02/18.
 */

public class CashSalesViewModel extends AndroidViewModel {
    private LiveData<DeliveryOrder> driverDO;
    private LiveData<List<DeliveryOrderItem>> driverDoDetails;
    private Context context;
    private  AppDatabase leepDatabase;

    public CashSalesViewModel(@NonNull Application application) {
        super(application);
        context = application;
        leepDatabase = RoomDBService.sharedInstance().getDatabase(context);
    }

    public LiveData<DeliveryOrder> getDriverDo(){
        driverDO = leepDatabase.deliveryOrderDao().getDriverDo();
        return driverDO;
    }

    public LiveData<List<DeliveryOrderItem>> getDriverDoDetails(Integer id) {
        driverDoDetails = leepDatabase.deliveryOrderItemDao().getDriverDoItems(id);
        return driverDoDetails;
    }

    public void fetchDriverDo(final UILevelNetworkCallback driverDOCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "?type=driver&assignees=1&statuses=assigned", true,
                new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                DeliveryOrder driverDO;
                                try{
                                    Type listType = new TypeToken<DeliveryOrder>() {}.getType();
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

    public void fetchDriverDoDetails(final String doId, final UILevelNetworkCallback doDetailsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true, new NetworkAPICallback() {
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
                                    leepDatabase.deliveryOrderItemDao().insertDeliveryOrderItems(
                                            getDeliveryOrderItemList(doDetailResponse));
                                    leepDatabase.productDao().insertAndDeleteInTransaction(doDetailResponse.getId(),
                                            getProductsList(doDetailResponse));
                                }catch (JSONException ex){
                                    ex.printStackTrace();
                                } catch (SQLiteConstraintException e){
                                    e.printStackTrace();
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

    public List<Product> getProducts(Integer doId) {
        List<Product> products = leepDatabase.productDao().getAllProducts(doId);
        return products;
    }
}
