package com.tracotech.tracoman.helpers.uimodels;

/**
 * Created by shubham on 05/04/2018.
 */

public class Country {
    private  int id;
    private String name;
    private String dialCode;
    private String currencySymbol;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String toString() {
        return dialCode + " " + name;
    }
}
