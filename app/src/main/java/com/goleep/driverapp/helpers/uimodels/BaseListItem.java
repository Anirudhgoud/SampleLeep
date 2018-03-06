package com.goleep.driverapp.helpers.uimodels;

import android.arch.persistence.room.Ignore;

import java.util.List;

/**
 * Created by vishalm on 28/02/18.
 */

public class BaseListItem {
    public int getSelectedCount() {
        return selectedCount;
    }

    @Ignore
    protected int selectedCount = 0;

    @Ignore
    private Integer itemType = 0;

    public void setOrdersHeader(String ordersHeader) {
        this.ordersHeader = ordersHeader;
    }

    @Ignore
    private String ordersHeader;

    public void addSelection(int selection){
        selectedCount = selectedCount + selection;
    }

    public static List<BaseListItem> setItemType(List<BaseListItem> baseListItems, int itemType){
        for(BaseListItem baseListItem:baseListItems)
            baseListItem.setItemType(itemType);
        return baseListItems;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getString() {
        return ordersHeader;
    }

    public int getItemType() {
        return itemType;
    }
}
