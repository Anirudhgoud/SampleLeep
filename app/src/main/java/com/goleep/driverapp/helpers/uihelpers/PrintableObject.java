package com.goleep.driverapp.helpers.uihelpers;

 /**
 * Created by vishalm on 02/05/18.
 */
public class PrintableObject {

    private String customerName;
    private String doNumber;
    private String roNumber;
    private String address;
    private String dateTime;
    private String saleItems;
    private String returnsItem;
    private double saleTotal;
    private double returnsTotal;
    private double grandTotal;
    private double paymentCollected;
    private int objectType;

    public static int TYPE_DELIVERY_ORDER = 1;
    public static int TYPE_DELIVERY_ORDER_HISTORY = 2;
    public static int TYPE_CASH_SALES = 3;
    public static int TYPE_RETURNS = 4;
    public static int TYPE_RETURN_ORDER_HISTORY = 5;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public void setRoNumber(String roNumber) {
        this.roNumber = roNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setSaleItems(String saleItems) {
        this.saleItems = saleItems;
    }

    public void setReturnsItem(String returnsItem) {
        this.returnsItem = returnsItem;
    }

    public void setSaleTotal(double saleTotal) {
        this.saleTotal = saleTotal;
    }

    public void setReturnsTotal(double returnsTotal) {
        this.returnsTotal = returnsTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public void setPaymentCollected(double paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

     public String getCustomerName() {
         return customerName;
     }

     public String getDoNumber() {
         return doNumber;
     }

     public String getRoNumber() {
         return roNumber;
     }

     public String getAddress() {
         return address;
     }

     public String getDateTime() {
         return dateTime;
     }

     public String getSaleItems() {
         return saleItems;
     }

     public String getReturnsItem() {
         return returnsItem;
     }

     public double getSaleTotal() {
         return saleTotal;
     }

     public double getReturnsTotal() {
         return returnsTotal;
     }

     public double getGrandTotal() {
         return grandTotal;
     }

     public double getPaymentCollected() {
         return paymentCollected;
     }

     public int getObjectType() {
         return objectType;
     }

     public void setObjectType(int objectType) {
         this.objectType = objectType;
     }
 }
