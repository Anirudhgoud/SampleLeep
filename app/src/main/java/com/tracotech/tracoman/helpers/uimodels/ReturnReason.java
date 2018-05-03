package com.tracotech.tracoman.helpers.uimodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.tracotech.tracoman.constants.ReasonCategory;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReason extends Reason implements Parcelable {

    private int id;
    private String reason;
    private String reasonCategory;


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

    public String getReasonCategory() {
        return reasonCategory;
    }

    public void setReasonCategory(String reasonCategory) {
        switch (reasonCategory){
            case "resellable": this.reasonCategory = ReasonCategory.RESELLABLE; break;
            case "non_resellable": this.reasonCategory = ReasonCategory.NON_RESELLABLE; break;
            default: this.reasonCategory = ReasonCategory.RESELLABLE;
        }
    }

    //Parcel
    protected ReturnReason(Parcel in) {
        id = in.readInt();
        reason = in.readString();
        reasonCategory = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(reason);
        dest.writeString(reasonCategory);
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
