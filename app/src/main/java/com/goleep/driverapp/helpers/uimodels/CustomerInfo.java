package com.goleep.driverapp.helpers.uimodels;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shubham on 06/04/2018.
 */

public class CustomerInfo implements Parcelable {
    private String businessName;
    private String businessType;
    private int businessTypeId;
    private String contactName;
    private String contactNumber;
    private String email;
    private int businessId;
    private String designation;

    public CustomerInfo(){

    }

    protected CustomerInfo(Parcel in) {
        businessName = in.readString();
        businessType = in.readString();
        businessTypeId = in.readInt();
        contactName = in.readString();
        contactNumber = in.readString();
        email = in.readString();
        businessId = in.readInt();
        designation = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(businessName);
        dest.writeString(businessType);
        dest.writeInt(businessTypeId);
        dest.writeString(contactName);
        dest.writeString(contactNumber);
        dest.writeString(email);
        dest.writeInt(businessId);
        dest.writeString(designation);
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomerInfo> CREATOR = new Parcelable.Creator<CustomerInfo>() {
        @Override
        public CustomerInfo createFromParcel(Parcel in) {
            return new CustomerInfo(in);
        }

        @Override
        public CustomerInfo[] newArray(int size) {
            return new CustomerInfo[size];
        }
    };
}