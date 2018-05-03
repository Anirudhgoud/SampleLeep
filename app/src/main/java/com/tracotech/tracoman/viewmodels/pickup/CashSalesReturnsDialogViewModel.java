package com.tracotech.tracoman.viewmodels.pickup;

import android.arch.lifecycle.ViewModel;

import com.tracotech.tracoman.helpers.uimodels.Product;

import java.util.List;

public class CashSalesReturnsDialogViewModel extends ViewModel {

    private List<Product> selectedProducts;
    private int selectedProductCount = 0;
    private int returnedProductCount = 0;

    public List<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public int getSelectedProductCount() {
        return selectedProductCount;
    }

    public void incrementSelectedProductCount() {
        this.selectedProductCount++;
    }

    public int getReturnedProductCount() {
        return returnedProductCount;
    }

    public void incrementReturnedProductCount() {
        this.returnedProductCount++;
    }
}
