package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.adapters.StockProductListAdapter;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class StocksViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;

    public StocksViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public List<StockProductEntity> getStockList(int listType) {
        List<StockProductEntity> stockProductEntities = new ArrayList<>();
        switch (listType) {

            case StockProductListAdapter.TYPE_SELLABLE:
                stockProductEntities = leepDatabase.stockProductDao().getSellableStocks();
                break;
            case StockProductListAdapter.TYPE_RETURNED:
                stockProductEntities = leepDatabase.stockProductDao().getReturnedStocks();
                break;
            case StockProductListAdapter.TYPE_DELIVERABLE:
            default:
                stockProductEntities = leepDatabase.stockProductDao().getDeliverableStocks();
                break;
        }
        return stockProductEntities;
    }
}
