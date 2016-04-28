package com.elfec.sgam.model;

import android.graphics.drawable.Drawable;

/**
 * App details to show in panel
 */
public class AppDetail {
    private CharSequence label;
    private CharSequence packageName;
    private Drawable icon;
    private int bgColor;

    //region Getters Setters
    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getPackageName() {
        return packageName;
    }

    public void setPackageName(CharSequence packageName) {
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
