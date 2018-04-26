package com.goleep.driverapp.viewmodels.information;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class StocksViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;
    private List<Integer> selectedIds = new ArrayList<>();
    private StockProductEntity selectedStock;

    public StocksViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public List<StockProductEntity> getStockList(int listType){
        List<StockProductEntity> stockProductEntities;
        switch (listType){
            case ProductListAdapter.TYPE_SELLABLE :
                stockProductEntities = leepDatabase.stockProductDao().getSellableStocks();
                break;
            case ProductListAdapter.TYPE_RETURNED :
                stockProductEntities = leepDatabase.stockProductDao().getReturnedStocks();
                break;
            case ProductListAdapter.TYPE_DELIVERABLE :
                default:
                stockProductEntities = leepDatabase.stockProductDao().getDeliverableStocks();
                break;
        }
        updateSelectedIds(stockProductEntities);
        return stockProductEntities;
    }

    private void updateSelectedIds(List<StockProductEntity> stockProductEntities) {
        selectedIds.clear();
        for(StockProductEntity stockProductEntity : stockProductEntities){
            selectedIds.add(stockProductEntity.getId());
        }
    }

    public void updateOrderItemSelectionStatus(int itemId, boolean isChecked) {
        if(isChecked && !selectedIds.contains(itemId)){
            selectedIds.add(itemId);
        } else if(!isChecked && selectedIds.contains(itemId)){
            selectedIds.removeAll(Collections.singletonList(itemId));
        }
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }

    public StockProductEntity getStockProduct(int itemId) {
        return leepDatabase.stockProductDao().getStock(itemId);
    }

    public void setSelectedOrderItem(StockProductEntity selectedStock) {
        this.selectedStock = selectedStock;
    }

    public StockProductEntity getSelectedOrderItem() {
        return selectedStock;
    }


    public void updateOrderItemQuantity(int id, int updatedQuantity, int type) {
        if(type == ProductListAdapter.TYPE_SELLABLE)
            leepDatabase.stockProductDao().updateSellableQuantity(id, updatedQuantity);
        else if(type == ProductListAdapter.TYPE_RETURNED)
            leepDatabase.stockProductDao().updateReturnableQuantity(id, updatedQuantity);
    }
}
