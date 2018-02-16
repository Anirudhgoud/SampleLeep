package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by vishalm on 15/02/18.
 */

public class InnerDashboardUiModel {
    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(String topNumber) {
        this.topNumber = topNumber;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    private String mainText;
    private String subText;
    private String topText;
    private String topNumber;
    private int iconResId;
    private int topColorId;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

    public int getTopColorId() {
        return topColorId;
    }

    public void setTopColorId(int topColorId) {
        this.topColorId = topColorId;
    }

    public InnerDashboardUiModel(String mainText, String subText, String topText, String topNumber,
                                 int iconResId, int topColorId, String tag) {
        this.mainText = mainText;
        this.subText = subText;
        this.topText = topText;
        this.topNumber = topNumber;
        this.iconResId = iconResId;
        this.topColorId = topColorId;
        this.tag = tag;

    }

    public static final String TAG_PICKUP = "1";
    public static final String TAG_RETURNS = "2";
    public static final String TAG_DELIVERY_ORDERS = "3";
    public static final String TAG_CASH_SALES = "4";
    public static final String TAG_DROP_OFF = "5";
    public static final String TAG_STOCKS = "6";
    public static final String TAG_HISTORY = "7";
    public static final String TAG_REPORTS = "8";
}
