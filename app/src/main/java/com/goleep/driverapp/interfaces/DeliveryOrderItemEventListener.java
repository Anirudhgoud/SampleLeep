package com.goleep.driverapp.interfaces;

/**
 * Created by anurag on 05/03/18.
 */

public interface DeliveryOrderItemEventListener {
    void onUnitsTap(int itemId, int currentUnits);
    void onCheckboxTap(int itemId, boolean isChecked);
}
