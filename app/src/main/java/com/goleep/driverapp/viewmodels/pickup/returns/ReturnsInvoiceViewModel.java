package com.goleep.driverapp.viewmodels.pickup.returns;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;

import java.util.Collections;
import java.util.List;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsInvoiceViewModel extends AndroidViewModel {
    private Customer consumerLocation;
    private List<Product> scannedProducts;
    private double outstandingBalance;

    public ReturnsInvoiceViewModel(@NonNull Application application) {
        super(application);
    }

    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public List<Product> getScannedProducts() {
        return scannedProducts;
    }

    public void setScannedProducts(List<Product> scannedProducts) {
        this.scannedProducts = scannedProducts;
    }

    public void fetchBusinessLocation(int businessId, int locationId, final UILevelNetworkCallback locationCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.BUSINESS_LOCATIONS_URL + "/" + businessId + "/locations/" + locationId, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            LocationParser locationParser = new LocationParser();
                            Location location = locationParser.getBusinessLocation(response);
                            locationCallBack.onResponseReceived(Collections.singletonList(location), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            locationCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            locationCallBack.onResponseReceived(null, false, errorMessage, true);
                            break;
                    }
                });
    }

    public void updateAreaInConsumerLocation(String updatedAddress) {
        if (consumerLocation != null) consumerLocation.setArea(updatedAddress);
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public double totalReturnsValue() {
        double totalReturns = 0;
        for (Product product : scannedProducts) {
            if (product == null) continue;
            totalReturns += product.getTotalReturnsPrice();
        }
        return totalReturns;
    }

    public double grandTotal(double returns, double outstandingBalance) {
        return  outstandingBalance - returns;
    }

}
