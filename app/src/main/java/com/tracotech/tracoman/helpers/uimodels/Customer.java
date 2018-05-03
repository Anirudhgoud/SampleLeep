package com.tracotech.tracoman.helpers.uimodels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anurag on 20/03/18.
 */

public class Customer implements Parcelable {
    private int id;
    private String name;
    private int businessId;
    private String area;
    private String lastDeliveryDate;

    public Customer(){}

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(String lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    //Parcel
    protected Customer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        businessId = in.readInt();
        area = in.readString();
        lastDeliveryDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(businessId);
        dest.writeString(area);
        dest.writeString(lastDeliveryDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}
