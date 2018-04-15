package com.goleep.driverapp.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import java.util.ArrayList;
import java.util.List;

public class SelectReturnsProductViewModel extends CashSalesSelectProductsViewModel {

    private ArrayList<Product> selectedProducts = new ArrayList<>();

    public SelectReturnsProductViewModel(@NonNull Application application) {
        super(application);
    }

    public List<StockProductEntity> allProductsContainingName(String searchText) {
        return leepDatabase.stockProductDao().allProductsWithName(searchText);
    }

    public StockProductEntity productsWithBarcode(String barcode) {
        return leepDatabase.stockProductDao().productHavingBarcode(barcode);
    }

    //Getters and setters
    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(ArrayList<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }
}
