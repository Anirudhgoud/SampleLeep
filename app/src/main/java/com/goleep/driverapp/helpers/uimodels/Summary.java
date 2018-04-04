package com.goleep.driverapp.helpers.uimodels;


import static com.goleep.driverapp.utils.StringUtils.formatToOneDecimal;

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

    public String getPickUpFromWarehouse() {
        return formatToOneDecimal(pickUpFromWarehouse);
    }

    public void setPickUpFromWarehouse(int pickUpFromWarehouse) {
        this.pickUpFromWarehouse = pickUpFromWarehouse;
    }

    public String getReturnsFromCustomers() {
        return formatToOneDecimal(returnsFromCustomers);
    }

    public void setReturnsFromCustomers(int returnsFromCustomers) {
        this.returnsFromCustomers = returnsFromCustomers;
    }

    public String getDropOffDeliveryOrdersCount() {
        return formatToOneDecimal(dropOffDeliveryOrdersCount);
    }

    public void setDropOffDeliveryOrdersCount(int dropOffDeliveryOrdersCount) {
        this.dropOffDeliveryOrdersCount = dropOffDeliveryOrdersCount;
    }

    public String getDropOffCashSales() {
        return formatToOneDecimal(dropOffCashSales);
    }

    public void setDropOffCashSales(int dropOffCashSales) {
        this.dropOffCashSales = dropOffCashSales;
    }

    public String getDropOffToWarehouse() {
        return formatToOneDecimal(dropOffToWarehouse);
    }

    public void setDropOffToWarehouse(int dropOffToWarehouse) {
        this.dropOffToWarehouse = dropOffToWarehouse;
    }

    public String getInformationStocks() {
        return formatToOneDecimal(informationStocks);
    }

    public void setInformationStocks(int informationStocks) {
        this.informationStocks = informationStocks;
    }

    public String getInformationOnHistory() {
        return formatToOneDecimal(informationOnHistory);
    }

    public void setInformationOnHistory(int informationOnHistory) {
        this.informationOnHistory = informationOnHistory;
    }

    public String getInformationOnReports() {
        return formatToOneDecimal(informationOnReports);
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



