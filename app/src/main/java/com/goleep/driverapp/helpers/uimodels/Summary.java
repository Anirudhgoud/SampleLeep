package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by vishalm on 15/02/18.
 */

public class Summary {

    private int pickUpFromWarehouse = 0;
    private int returnsFromCustomers = 0;
    private int dropOffDeliveryOrdersCount = 0;
    private int dropOffCashSales = 0;
    private int dropOffToWarehouse = 0;
    private int informationStocks = 0;
    private int informationOnHistory = 0;
    private int informationOnReports = 0;

    public int getPickUpFromWarehouse() {
        return pickUpFromWarehouse;
    }

    public void setPickUpFromWarehouse(int pickUpFromWarehouse) {
        this.pickUpFromWarehouse = pickUpFromWarehouse;
    }

    public int getReturnsFromCustomers() {
        return returnsFromCustomers;
    }

    public void setReturnsFromCustomers(int returnsFromCustomers) {
        this.returnsFromCustomers = returnsFromCustomers;
    }

    public int getDropOffDeliveryOrdersCount() {
        return dropOffDeliveryOrdersCount;
    }

    public void setDropOffDeliveryOrdersCount(int dropOffDeliveryOrdersCount) {
        this.dropOffDeliveryOrdersCount = dropOffDeliveryOrdersCount;
    }

    public int getDropOffCashSales() {
        return dropOffCashSales;
    }

    public void setDropOffCashSales(int dropOffCashSales) {
        this.dropOffCashSales = dropOffCashSales;
    }

    public int getDropOffToWarehouse() {
        return dropOffToWarehouse;
    }

    public void setDropOffToWarehouse(int dropOffToWarehouse) {
        this.dropOffToWarehouse = dropOffToWarehouse;
    }

    public int getInformationStocks() {
        return informationStocks;
    }

    public void setInformationStocks(int informationStocks) {
        this.informationStocks = informationStocks;
    }

    public int getInformationOnHistory() {
        return informationOnHistory;
    }

    public void setInformationOnHistory(int informationOnHistory) {
        this.informationOnHistory = informationOnHistory;
    }

    public int getInformationOnReports() {
        return informationOnReports;
    }

    public void setInformationOnReports(int informationOnReports) {
        this.informationOnReports = informationOnReports;
    }
}



