package com.elfec.sgam.model;

import android.graphics.drawable.Drawable;

/**
 * App details to show in panel
 */
public class AppDetail {
    private CharSequence appName;
    private String packageName;
    private Drawable icon;
    private int bgColor;

    //region Getters Setters
    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    //endregion
}
