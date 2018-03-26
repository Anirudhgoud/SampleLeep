package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;

import java.util.Comparator;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDOByDeliveryTime implements Comparator<BaseListItem> {

    @Override
    public int compare(BaseListItem do1, BaseListItem do2) {
        Distance distance1 = ((DeliveryOrderEntity) do1).getDistanceFromCurrentLocation();
        Distance distance2 = ((DeliveryOrderEntity) do2).getDistanceFromCurrentLocation();
        Integer duration1 = distance1 == null ? 0 : distance1.getDurationValue();
        Integer duration2 = distance2 == null ? 0 : distance2.getDurationValue();
        return duration1.compareTo(duration2);
    }
}
