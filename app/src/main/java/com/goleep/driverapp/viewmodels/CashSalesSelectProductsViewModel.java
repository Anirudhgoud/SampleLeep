package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import java.util.ArrayList;
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

    public StockProductEntity sellebleProductHavingBarcode(String barcode){
        return leepDatabase.stockProductDao().sellableProductHavingBarcode(barcode);
    }

    public List<StockProductEntity> sellebleProductsWithName(String searchText){
        return leepDatabase.stockProductDao().sellebleProductsWithName(searchText);
    }

    public void addToScannedProduct(StockProductEntity scannedProduct) {
        scannedProducts.add(scannedProduct);
    }

    public int getSourceLocationId(){
        DriverEntity driverEntity = leepDatabase.driverDao().getDriver();
        return driverEntity != null ?  driverEntity.getLocationId() : 0;
    }

    public int getDestinationLocationId(){
        return 0;
    }

    public void addToProductMaxQuantities(int id, int quantity){
        productMaxQuantities.put(id, quantity);
    }

    public int getMaxQuantityofProduct(int id){
        return productMaxQuantities.get(id);
    }

    public StockProductEntity getProductFromScannedProducts(int id){
        for (StockProductEntity entity: scannedProducts) {
            if (entity.getId() == id) return entity;
        }
        return null;
    }

    public boolean isProductInScannedList(int id){
        for (StockProductEntity entity: scannedProducts) {
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
