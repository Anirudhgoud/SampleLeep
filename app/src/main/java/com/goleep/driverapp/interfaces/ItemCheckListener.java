package com.goleep.driverapp.interfaces;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.Product;

/**
 * Created by vishalm on 26/02/18.
 */

public interface ItemCheckListener {
    void itemChecked(BaseListItem item, boolean checked, Product product);
}
