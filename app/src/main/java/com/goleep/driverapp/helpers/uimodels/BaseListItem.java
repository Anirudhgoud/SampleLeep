package com.goleep.driverapp.helpers.uimodels;

import android.arch.persistence.room.Ignore;

import com.goleep.driverapp.adapters.ExpandableRecyclerAdapter;

import java.util.List;

/**
 * Created by vishalm on 28/02/18.
 */

public class BaseListItem extends ExpandableRecyclerAdapter.ListItem{
    public static int TYPE_HEADER = 1000;
    public static int TYPE_DO_ITEM = 10;
    public static final int TYPE_ORDERS_HEADER = 12;
    public static final int TYPE_CASH_SALES_ITEM = 13;
    @Ignore
    protected int selectedCount = 0;

    public void setOrdersHeader(String ordersHeader) {
        this.ordersHeader = ordersHeader;
    }

    @Ignore
    private String ordersHeader;
    public BaseListItem(int itemType) {
        super(itemType);
    }

    public BaseListItem(){
        super(0);
    }


    public void addSelection(int selection){
        selectedCount = selectedCount + selection;
    }

    public static List<BaseListItem> setItemType(List<BaseListItem> baseListItems, int itemType){
        for(BaseListItem baseListItem:baseListItems)
            baseListItem.setItemType(itemType);
        return baseListItems;
    }

    public String getString() {
        return ordersHeader;
    }
}
