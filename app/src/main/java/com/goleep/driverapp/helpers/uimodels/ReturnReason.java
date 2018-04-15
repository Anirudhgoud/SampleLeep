package com.goleep.driverapp.helpers.uimodels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReason implements Parcelable {

    private int id;
    private String reason;
    private boolean isSelected = false;

    public ReturnReason(){}

    //Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    //Parcel
    protected ReturnReason(Parcel in) {
        id = in.readInt();
        reason = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(reason);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReturnReason> CREATOR = new Parcelable.Creator<ReturnReason>() {
        @Override
        public ReturnReason createFromParcel(Parcel in) {
            return new ReturnReason(in);
        }

        @Override
        public ReturnReason[] newArray(int size) {
            return new ReturnReason[size];
        }
    };
}
