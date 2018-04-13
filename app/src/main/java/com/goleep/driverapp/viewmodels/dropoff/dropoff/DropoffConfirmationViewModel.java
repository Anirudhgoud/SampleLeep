package com.goleep.driverapp.viewmodels.dropoff.dropoff;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.viewmodels.WarehouseDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 03/04/18.
 */

public class DropoffConfirmationViewModel extends WarehouseDetailsViewModel {

    private List<Integer> selectedReturnableIds = new ArrayList<>();
    private List<Integer> selectedSellableIds = new ArrayList<>();

    public DropoffConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Integer> getSelectedReturnableIds() {
        return selectedReturnableIds;
    }

    public void setSelectedReturnableIds(List<Integer> selectedReturnableIds) {
        this.selectedReturnableIds = selectedReturnableIds;
    }

    public List<Integer> getSelectedSellableIds() {
        return selectedSellableIds;
    }

    public void setSelectedSellableIds(List<Integer> selectedSellableIds) {
        this.selectedSellableIds = selectedSellableIds;
    }

    public List<StockProductEntity> generateAdapterItemList() {
        List<StockProductEntity>  baseListItems = new ArrayList<>();
        if(selectedReturnableIds != null && selectedReturnableIds.size() > 0){
            for(Integer id : selectedReturnableIds){
                baseListItems.add(leepDatabase.stockProductDao().getStock(id));
            }
        }
        if(selectedSellableIds != null && selectedSellableIds.size() > 0){
            for(Integer id : selectedSellableIds){
                baseListItems.add(leepDatabase.stockProductDao().getStock(id));
            }
        }

        return baseListItems;
    }

    public StockProductEntity getStockProduct(int id){
        return leepDatabase.stockProductDao().getStock(id);
    }
}
