package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Location;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.LocationParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashSalesInvoiceViewModel extends AndroidViewModel {

    private Customer consumerLocation;
    private List<Product> scannedProducts;
    private double outstandingBalance;

    private int selectedProductCount = 0;
    private int returnedProductCount = 0;

    public CashSalesInvoiceViewModel(@NonNull Application application) {
        super(application);
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

    public double totalReturnsValue() {
        double totalReturns = 0;
        for (Product product : scannedProducts) {
            if (product == null) continue;
            totalReturns += product.getTotalReturnsPrice();
        }
        return totalReturns;
    }

    public double totalCurrentSales() {
        double totalSales = 0;
        for (Product product : scannedProducts) {
            if (product == null) continue;
            totalSales += product.getTotalPrice();
        }
        return totalSales;
    }

    public double grandTotal(double returns, double currentSales, double outstandingBalance) {
        return currentSales + outstandingBalance - returns;
    }



    //Getters and setters
    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public List<Product> getScannedProducts() {
        return scannedProducts == null ? Collections.emptyList() : scannedProducts;
    }

    public void setScannedProducts(ArrayList<Product> scannedProducts) {
        this.scannedProducts = scannedProducts;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
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
