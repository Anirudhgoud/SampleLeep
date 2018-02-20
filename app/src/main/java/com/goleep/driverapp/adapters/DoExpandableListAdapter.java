package com.goleep.driverapp.adapters;

/**
 * Created by vishalm on 20/02/18.
 */

public class DoExpandableListAdapter {
    final int LIST_DO_ITEM = 10;
    final int LIST_DO_HEADER = 11;


    private class DoListItem extends ExpandableRecyclerAdapter.ListItem{
        String itemName;
        public DoListItem(int itemType) {
            super(itemType);
        }
        public DoListItem(String itemName){
            super(LIST_DO_ITEM);
            this.itemName = itemName;
        }
    }
}
