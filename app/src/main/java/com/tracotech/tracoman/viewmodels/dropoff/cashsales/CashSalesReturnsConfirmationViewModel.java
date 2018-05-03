package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import com.tracotech.tracoman.helpers.uimodels.Product;

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
