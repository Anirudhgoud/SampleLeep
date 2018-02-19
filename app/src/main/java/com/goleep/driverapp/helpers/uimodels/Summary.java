package com.goleep.driverapp.helpers.uimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vishalm on 15/02/18.
 */

public class Summary {

    @SerializedName("pick_up_from_warehouse")
    @Expose
    private Integer pickUpFromWarehouse;
    @SerializedName("returns_from_customers")
    @Expose
    private Integer returnsFromCustomers;
    @SerializedName("drop_off_delivery_orders_count")
    @Expose
    private Integer dropOffDeliveryOrdersCount;
    @SerializedName("drop_off_cash_sales")
    @Expose
    private Integer dropOffCashSales;
    @SerializedName("drop_off_to_warehouse")
    @Expose
    private Integer dropOffToWarehouse;
    @SerializedName("information_stocks")
    @Expose
    private Integer informationStocks;
    @SerializedName("information_on_history")
    @Expose
    private Integer informationOnHistory;
    @SerializedName("information_on_reports")
    @Expose
    private Integer informationOnReports;

    public Summary(){
        this.dropOffCashSales = 0;
        this.dropOffDeliveryOrdersCount = 0;
        this.dropOffToWarehouse = 0;
        this.pickUpFromWarehouse = 0;
        this.returnsFromCustomers = 0;
        this.informationOnHistory = 0;
        this.informationOnReports = 0;
        this.informationStocks = 0;
    }

    public Integer getPickUpFromWarehouse() {
        return pickUpFromWarehouse;
    }

    public void setPickUpFromWarehouse(Integer pickUpFromWarehouse) {
        this.pickUpFromWarehouse = pickUpFromWarehouse;
    }

    public Integer getReturnsFromCustomers() {
        return returnsFromCustomers;
    }

    public void setReturnsFromCustomers(Integer returnsFromCustomers) {
        this.returnsFromCustomers = returnsFromCustomers;
    }

    public Integer getDropOffDeliveryOrdersCount() {
        return dropOffDeliveryOrdersCount;
    }

    public void setDropOffDeliveryOrdersCount(Integer dropOffDeliveryOrdersCount) {
        this.dropOffDeliveryOrdersCount = dropOffDeliveryOrdersCount;
    }

    public Integer getDropOffCashSales() {
        return dropOffCashSales;
    }

    public void setDropOffCashSales(Integer dropOffCashSales) {
        this.dropOffCashSales = dropOffCashSales;
    }

    public Integer getDropOffToWarehouse() {
        return dropOffToWarehouse;
    }

    public void setDropOffToWarehouse(Integer dropOffToWarehouse) {
        this.dropOffToWarehouse = dropOffToWarehouse;
    }

    public Integer getInformationStocks() {
        return informationStocks;
    }

    public void setInformationStocks(Integer informationStocks) {
        this.informationStocks = informationStocks;
    }

    public Integer getInformationOnHistory() {
        return informationOnHistory;
    }

    public void setInformationOnHistory(Integer informationOnHistory) {
        this.informationOnHistory = informationOnHistory;
    }

    public Integer getInformationOnReports() {
        return informationOnReports;
    }

    public void setInformationOnReports(Integer informationOnReports) {
        this.informationOnReports = informationOnReports;
    }

}



