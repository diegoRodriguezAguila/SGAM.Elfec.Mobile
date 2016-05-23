package com.elfec.sgam.view.launcher.session;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.elfec.sgam.view.launcher.session.options.IOptionHandler;

/**
 * Session Option
 */
public class SessionOption {
    @StringRes
    private int mLblId;
    private Drawable mIcon;
    private IOptionHandler mHandler;
    //region better collapse
    public SessionOption() {
    }

    public SessionOption(int lblId, Drawable icon, @Nullable IOptionHandler handler) {
        this.mLblId = lblId;
        this.mIcon = icon;
        this.mHandler = handler;
    }

    public int getLblId() {
        return mLblId;
    }

    public void setLblId(int lblId) {
        this.mLblId = lblId;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    @Nullable
    public IOptionHandler getHandler() {
        return mHandler;
    }

    public void setHandler(@Nullable IOptionHandler handler) {
        this.mHandler = handler;
    }
    //endregion
}
