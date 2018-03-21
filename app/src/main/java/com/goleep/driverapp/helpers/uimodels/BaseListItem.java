package com.goleep.driverapp.helpers.uimodels;

import android.arch.persistence.room.Ignore;

import java.util.List;

/**
 * Created by vishalm on 28/02/18.
 */

public class BaseListItem {

    @Ignore
    protected int selectedCount = 0;

    @Ignore
    private Integer itemType = 0;

    @Ignore
    private String ordersHeader;

    @Ignore
    private boolean isItemChecked = false;

    public int getSelectedCount() {
        return (selectedCount < 1) ? 0 : selectedCount;
    }

    public void setOrdersHeader(String ordersHeader) {
        this.ordersHeader = ordersHeader;
    }

    public void addSelection(int selection){
        selectedCount = selectedCount + selection;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getOrdersHeader() {
        return ordersHeader;
    }

    public int getItemType() {
        return itemType;
    }

    public boolean isItemChecked(){
        return isItemChecked;
    }

    public void checkItem(boolean itemChecked){
        this.isItemChecked = itemChecked;
    }

    public static List<BaseListItem> setItemType(List<BaseListItem> baseListItems, int itemType){
        for(BaseListItem baseListItem:baseListItems)
            baseListItem.setItemType(itemType);
        return baseListItems;
    }
}
