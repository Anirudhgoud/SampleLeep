package com.goleep.driverapp.constants;

/**
 * Created by vishalm on 08/02/18.
 */

public interface AppConstants {
    String CASH_DOITEM_KEY = "do_items_cash";
    String CASH_PRODUCT_KEY = "products_cash";

    int TYPE_HEADER = 1000;
    int TYPE_DO_ITEM = 10;
    int TYPE_ORDERS_HEADER = 12;
    int TYPE_CASH_SALES_ITEM = 13;
    int TYPE_ITEMS_HEADER = 14;
    int TYPE_SALES_INFO = 15;
    String DO_IDS_KEY = "do_selected";

    int ACTIVITY_SUCCESS_RESULT = 101;
    int ACTIVITY_CLEAR_FORM = 99;

    int TYPE_DELIVERABLE = 0;
    int TYPE_SELLABLE = 1;
    int TYPE_RETURNED = 2;

    //Permission codes
    int LOCATION_PERMISSION_REQUEST_CODE = 100;
}
