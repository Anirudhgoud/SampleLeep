package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;

import java.util.Comparator;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDoByTotalValue implements Comparator<BaseListItem> {

    @Override
    public int compare(BaseListItem do1, BaseListItem do2) {
        Float amount1 = ((DeliveryOrderEntity)do1).getTotalValue();
        Float amount2 = ((DeliveryOrderEntity)do2).getTotalValue();
        return amount2.compareTo(amount1);
    }
}
