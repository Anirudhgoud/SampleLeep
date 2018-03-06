package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;

import java.util.Comparator;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDOByDeliveryTime implements Comparator<BaseListItem> {

    @Override
    public int compare(BaseListItem do1, BaseListItem do2) {
        Integer duration1 = ((DeliveryOrderEntity)do1).getDurationFromCurrentLocation();
        Integer duration2 = ((DeliveryOrderEntity)do2).getDurationFromCurrentLocation();
        return duration1.compareTo(duration2);
    }
}
