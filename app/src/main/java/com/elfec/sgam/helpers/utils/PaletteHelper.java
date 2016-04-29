package com.elfec.sgam.helpers.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.elfec.sgam.helpers.ui.ColorTools;

/**
 * Created by drodriguez on 28/04/2016.
 * Helper class for pallete methods
 */
public class PaletteHelper {

    private static final int DEFAULT_COLOR = 0xff02296a;
    private static final int PRIMARY_COLOR = 0xff18468d;

    /**
     * Sets the background of a view based on the drawable parameter
     * @param view view
     * @param drawable image
     */
    public static void setPaletteBackground(final View view, Drawable drawable){
        getDrawableBackgroundColor(drawable, view::setBackgroundColor);
    }

    /**
     * Gets asynchronously the adequate background color for a
     * drawable
     * @param drawable drawable
     * @param callback callback
     */
    public static void getDrawableBackgroundColor(Drawable drawable, OnGetColorCallback callback){
        Palette.from(DrawableHelper.drawableToBitmap(drawable))
                .generate(palette -> {
                    int bgColor = getBackgroundColor(palette);
                    if(callback!=null)
                        callback.gotColor(bgColor);
                });
    }

    /**
     * Gets synchronously the adequate background color for a
     * drawable
     * @param drawable drawable
     * @return proper background color
     */
    public static int getDrawableBackgroundColor(Drawable drawable){
        return getBackgroundColor(Palette.from(DrawableHelper.drawableToBitmap(drawable))
                .generate());
    }

    /**
     * Gets an appropiate background color given the palette
     * @param palette palette
     * @return bg color
     */
    private static int getBackgroundColor(Palette palette) {
        int bgColor = palette.getDarkVibrantColor(DEFAULT_COLOR);
        if(bgColor==DEFAULT_COLOR)
            bgColor = palette.getDarkMutedColor(DEFAULT_COLOR);
        if(bgColor==DEFAULT_COLOR)
            bgColor = palette.getVibrantColor(DEFAULT_COLOR);
        if(bgColor==DEFAULT_COLOR)
            bgColor = palette.getMutedColor(DEFAULT_COLOR);
        if(ColorTools.colorsAreClose(bgColor, PRIMARY_COLOR))
            bgColor = ColorTools.lighter(bgColor);
        return bgColor;
    }

    /**
     * Interface for got color callback
     */
    public interface OnGetColorCallback {
        void gotColor(@ColorInt int color);
    }
}
