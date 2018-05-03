package com.tracotech.tracoman.helpers.uimodels;


/**
 * Created by vishalm on 15/02/18.
 */

public class Summary {

    private int pickUpFromWarehouse = -1;
    private int returnsFromCustomers = -1;
    private int dropOffDeliveryOrdersCount = -1;
    private int dropOffCashSales = -1;
    private int dropOffToWarehouse = -1;
    private int informationStocks = -1;
    private int informationOnHistory = -1;
    private int informationOnReports = -1;

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

    public int getPickUpCount() {
        return pickUpFromWarehouse + returnsFromCustomers;
    }

    public int getDropoffCount() {
        return dropOffDeliveryOrdersCount + dropOffCashSales + dropOffToWarehouse;
    }

    public int getInformationCount() {
        return informationStocks + informationOnHistory + informationOnReports;
    }
}



