package com.tracotech.tracoman.services.room.entities;

/**
 * Created by vishalm on 19/03/18.
 */

public class ReportsEntity {

    private int totalPaymentCollected;
    private int cash;
    private int kmsTravelled;
    private int sales;
    private int unitsDelivered;
    private int returns;
    private int locations;

    public int getTotalPaymentCollected() {
        return totalPaymentCollected;
    }

    public void setTotalPaymentCollected(int totalPaymentCollected) {
        this.totalPaymentCollected = totalPaymentCollected;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getKmsTravelled() {
        return kmsTravelled;
    }

    public void setKmsTravelled(int kmsTravelled) {
        this.kmsTravelled = kmsTravelled;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getUnitsDelivered() {
        return unitsDelivered;
    }

    public void setUnitsDelivered(int unitsDelivered) {
        this.unitsDelivered = unitsDelivered;
    }

    public int getReturns() {
        return returns;
    }

    public void setReturns(int returns) {
        this.returns = returns;
    }

    public int getLocations() {
        return locations;
    }

    public void setLocations(int locations) {
        this.locations = locations;
    }


}
