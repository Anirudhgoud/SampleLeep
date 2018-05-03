package com.tracotech.tracoman.services.room.entities.comparators;

import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.utils.DateTimeUtils;

import java.util.Comparator;
import java.util.Date;

import static com.tracotech.tracoman.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;

/**
 * Created by anurag on 20/02/18.
 */

public class SortDoByPreferredDeliveryDate implements Comparator<BaseListItem> {

    @Override
    public int compare(BaseListItem do1, BaseListItem do2) {
        Date date1 = DateTimeUtils.dateFrom(((DeliveryOrderEntity)do1).getPreferredDeliveryDate(),
                ORDER_SERVER_DATE_FORMAT);
        if(date1 == null){
            date1 = new Date();
        }
        Date date2 = DateTimeUtils.dateFrom(((DeliveryOrderEntity)do2).getPreferredDeliveryDate(),
                ORDER_SERVER_DATE_FORMAT);
        if(date2 == null){
            date2 = new Date();
        }
        return date2.compareTo(date1);
    }
}
