package com.goleep.driverapp.interfaces;

/**
 * Created by anurag on 05/03/18.
 */

public interface DeliveryOrderItemEventListener {
    void onUnitsTap(Integer itemId, Integer currentUnits);
    void onCheckboxTap(Integer itemId, boolean isChecked);
}
