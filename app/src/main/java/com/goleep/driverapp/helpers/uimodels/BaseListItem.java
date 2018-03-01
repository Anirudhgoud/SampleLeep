package com.goleep.driverapp.helpers.uimodels;

import android.arch.persistence.room.Ignore;

import com.goleep.driverapp.adapters.ExpandableRecyclerAdapter;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;

/**
 * Created by vishalm on 28/02/18.
 */

public class BaseListItem extends ExpandableRecyclerAdapter.ListItem{
    @Ignore
    protected int selectedCount = 0;
    public BaseListItem(int itemType) {
        super(itemType);
    }

    public BaseListItem(){
        super(0);
    }


    public void addSelection(int selection){
        selectedCount = selectedCount + selection;
    }
}
