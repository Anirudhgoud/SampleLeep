package com.goleep.driverapp.interfaces;

/**
 * Created by vishalm on 05/03/18.
 */

public interface ParentListItem {
    int TYPE_HEADER = 1000;
    int TYPE_DO_ITEM = 10;
    void setType(Integer itemType);
}
