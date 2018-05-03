package com.tracotech.tracoman.services.room.entities.comparators;

import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;

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
