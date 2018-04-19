package com.goleep.driverapp.viewmodels.dropoff.cashsales;

import com.goleep.driverapp.helpers.uimodels.Product;

import java.util.List;

public class CashSalesReturnsConfirmationViewModel extends CashSalesConfirmationViewModel {

    private List<Product> returnedProducts;

    //Getters and setters
    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    public void setReturnedProducts(List<Product> returnedProducts) {
        this.returnedProducts = returnedProducts;
    }
}
