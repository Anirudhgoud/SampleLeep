package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.services.room.entities.DeliveryOrder;

import java.util.Comparator;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDOByDeliveryTime implements Comparator<DeliveryOrder> {

    @Override
    public int compare(DeliveryOrder do1, DeliveryOrder do2) {
        Integer duration1 = do1.getDurationFromCurrentLocation();
        Integer duration2 = do2.getDurationFromCurrentLocation();
        return duration1.compareTo(duration2);
    }
}
