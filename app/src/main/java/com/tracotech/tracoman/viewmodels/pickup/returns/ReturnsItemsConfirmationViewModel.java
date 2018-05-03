package com.tracotech.tracoman.viewmodels.pickup.returns;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;

import java.util.ArrayList;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsItemsConfirmationViewModel extends AndroidViewModel{

    private Customer customer;
    private ArrayList<Product> products;

    public ReturnsItemsConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


}
