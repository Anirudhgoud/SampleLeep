package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportAttr {
   int total_sales;
   int cash_collected;
   int units;
   int returns;
   int locations;

    public int getTotal_sales() {
        return total_sales;
    }

    public int getCash_collected() {
        return cash_collected;
    }

    public int getUnits() {
        return units;
    }

    public int getReturns() {
        return returns;
    }

    public int getLocations() {
        return locations;
    }

    public void setTotal_sales(int total_sales) {
        this.total_sales = total_sales;
    }

    public void setCash_collected(int cash_collected) {
        this.cash_collected = cash_collected;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setReturns(int returns) {
        this.returns = returns;
    }

    public void setLocations(int locations) {
        this.locations = locations;
    }
}
