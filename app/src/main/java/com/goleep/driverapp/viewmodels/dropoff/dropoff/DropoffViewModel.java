package com.goleep.driverapp.viewmodels.dropoff.dropoff;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.viewmodels.WarehouseDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 30/03/18.
 */

public class DropoffViewModel extends WarehouseDetailsViewModel {

    private List<Integer> selectedReturnableIds = new ArrayList<>();
    private List<Integer> selectedSellableIds = new ArrayList<>();

    public DropoffViewModel(@NonNull Application application) {
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
}
