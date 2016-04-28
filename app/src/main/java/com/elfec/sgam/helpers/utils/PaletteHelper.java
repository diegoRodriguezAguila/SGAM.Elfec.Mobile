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
        getPaletteBackgroundColor(drawable, view::setBackgroundColor);
    }

    /**
     * Gets asynchronously the adequate background color for a
     * drawable
     * @param drawable drawable
     * @param callback callback
     */
    public static void getPaletteBackgroundColor(Drawable drawable, OnGetColorCallback callback){
        Palette.from(DrawableHelper.drawableToBitmap(drawable))
                .generate(palette -> {
                    int bgColor = palette.getDarkVibrantColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getDarkMutedColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getVibrantColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getMutedColor(DEFAULT_COLOR);
                    if(ColorTools.colorsAreClose(bgColor, PRIMARY_COLOR))
                        bgColor = ColorTools.lighter(bgColor);
                    if(callback!=null)
                        callback.gotColor(bgColor);
                });
    }

    /**
     * Interface for got color callback
     */
    public interface OnGetColorCallback {
        void gotColor(@ColorInt int color);
    }
}
