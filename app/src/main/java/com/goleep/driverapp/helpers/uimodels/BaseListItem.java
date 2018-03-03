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

    public static List<BaseListItem> setItemType(List<BaseListItem> baseListItems, int itemType){
        for(BaseListItem baseListItem:baseListItems)
            baseListItem.setItemType(itemType);
        return baseListItems;
    }
}
