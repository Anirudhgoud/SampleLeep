package com.tracotech.tracoman.interfaces;

/**
 * Created by anurag on 05/03/18.
 */

public interface DeliveryOrderItemEventListener {
    void onUnitsTap(int itemId, int maxUnits);
    void onCheckboxTap(int itemId, boolean isChecked);
}
