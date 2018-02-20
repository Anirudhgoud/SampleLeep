package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.services.room.entities.DeliveryOrder;

import java.util.Comparator;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDoByTotalValue implements Comparator<DeliveryOrder> {

    @Override
    public int compare(DeliveryOrder do1, DeliveryOrder do2) {
        Float amount1 = do1.getTotalValue() == null ? 0 : do1.getTotalValue();
        Float amount2 = do2.getTotalValue() == null ? 0 : do2.getTotalValue();
        return amount2.compareTo(amount1);
    }
}
