package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportAttrribute {
  private int totalSales;
  private int cashCollected;
  private int units;
  private int returns;
  private int locations;

    public int getTotalSales() {
        return totalSales;
    }

    public int getCashCollected() {
        return cashCollected;
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

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public void setCashCollected(int cashCollected) {
        this.cashCollected = cashCollected;
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
