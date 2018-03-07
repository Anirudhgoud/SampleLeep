package com.goleep.driverapp.services.room.entities.comparators;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDoByPreferredDeliveryDate implements Comparator<BaseListItem> {

    @Override
    public int compare(BaseListItem do1, BaseListItem do2) {
        Date date1 = DateTimeUtils.dateFrom(((DeliveryOrderEntity)do1).getPreferredDeliveryDate(), "yyyy-MM-dd");
        if(date1 == null){
            date1 = new Date();
        }
        Date date2 = DateTimeUtils.dateFrom(((DeliveryOrderEntity)do2).getPreferredDeliveryDate(), "yyyy-MM-dd");
        if(date2 == null){
            date2 = new Date();
        }
        return date2.compareTo(date1);
    }
}
