package com.goleep.driverapp.viewmodels.pickup.returns;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;

import java.util.ArrayList;
import java.util.Collections;

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
