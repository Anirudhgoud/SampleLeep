package com.tracotech.tracoman.helpers.uimodels;

/**
 * Created by shubham on 23/03/2018.
 */

public class Report {
  private double totalSales;
  private double cashCollected;
  private int units;
  private int returns;
  private int locations;

    public double getTotalSales() {
        return totalSales;
    }

    public double getCashCollected() {
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

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public void setCashCollected(double cashCollected) {
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
