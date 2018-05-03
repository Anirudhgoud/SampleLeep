package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.arch.lifecycle.ViewModel;

import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashSalesConfirmationViewModel extends ViewModel {

    private Customer consumerLocation;
    private List<Product> selectedProducts;

    //Getters and setters
    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public List<Product> getSelectedProducts() {
        return selectedProducts == null ? Collections.emptyList() : selectedProducts;
    }

    public void setSelectedProducts(ArrayList<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }
}
