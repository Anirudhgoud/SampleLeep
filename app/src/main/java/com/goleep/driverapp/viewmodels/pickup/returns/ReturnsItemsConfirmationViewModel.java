package com.goleep.driverapp.viewmodels.pickup.returns;

import android.arch.lifecycle.ViewModel;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;

import java.util.ArrayList;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsItemsConfirmationViewModel extends ViewModel{

    private Customer customer;
    private ArrayList<Product> products;

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
