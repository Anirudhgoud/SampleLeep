package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anurag on 30/03/18.
 */

public class CashSalesSelectProductsViewModel extends AndroidViewModel {

    private AppDatabase leepDatabase;
    private StockProductEntity selectedStockProductEntity;
    private List<StockProductEntity> scannedProducts = new ArrayList<>();
    private Map<Integer, Integer> productMaxQuantities = new HashMap<>();

    public CashSalesSelectProductsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public StockProductEntity sellebleProductHavingBarcode(String barcode) {
        return leepDatabase.stockProductDao().sellableProductHavingBarcode(barcode);
    }

    public List<StockProductEntity> sellebleProductsWithName(String searchText) {
        return leepDatabase.stockProductDao().sellebleProductsWithName(searchText);
    }

    public void getProductPricing(int sourceLocationId, int destinationLocationId, int productId, UILevelNetworkCallback networkCallback) {
        String url = UrlConstants.PRODUCT_PRICING_URL + "?source_location_id=" + sourceLocationId + "&destination_location_id=" + destinationLocationId + "&product_id=" + productId;

        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), url, true, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type) {
                    case NetworkConstants.SUCCESS:
                        double product_price = new OrderItemParser().getProductPriceByParsingJsonArray(response);
                        networkCallback.onResponseReceived(new ArrayList<>(Arrays.asList(product_price)), false,
                                null, false);
                        break;

                    case NetworkConstants.FAILURE:
                    case NetworkConstants.NETWORK_ERROR:
                        networkCallback.onResponseReceived(null, true, errorMessage, false);
                        break;

                    default:
                        networkCallback.onResponseReceived(null, false, null, false);
                        break;
                }
            }
        });
    }

    public void addToScannedProduct(StockProductEntity scannedProduct) {
        scannedProducts.add(scannedProduct);
    }

    public int getSourceLocationId() {
        DriverEntity driverEntity = leepDatabase.driverDao().getDriver();
        return driverEntity != null ? driverEntity.getLocationId() : 0;
    }

    public void addToProductMaxQuantities(int id, int quantity) {
        productMaxQuantities.put(id, quantity);
    }

    public int getMaxQuantityofProduct(int id) {
        return productMaxQuantities.get(id);
    }

    public StockProductEntity getProductFromScannedProducts(int id) {
        for (StockProductEntity entity : scannedProducts) {
            if (entity.getId() == id) return entity;
        }
        return null;
    }

    public boolean isProductInScannedList(int id) {
        for (StockProductEntity entity : scannedProducts) {
            if (entity.getId() == id) return true;
        }
        return false;
    }

    // getters and setters
    public StockProductEntity getSelectedStockProductEntity() {
        return selectedStockProductEntity;
    }

    public void setSelectedStockProductEntity(StockProductEntity selectedStockProductEntity) {
        this.selectedStockProductEntity = selectedStockProductEntity;
    }

    public List<StockProductEntity> getScannedProducts() {
        return scannedProducts;
    }
}
