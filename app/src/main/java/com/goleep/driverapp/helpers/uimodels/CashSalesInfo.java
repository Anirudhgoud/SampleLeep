package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by vishalm on 09/03/18.
 */

public class CashSalesInfo extends BaseListItem {
    private int totalProducts;
    private int totalValue;

    public CashSalesInfo(int totalProducts, int totalValue) {
        this.totalProducts = totalProducts;
        this.totalValue = totalValue;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }
}
