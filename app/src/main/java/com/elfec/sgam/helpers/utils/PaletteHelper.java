package com.elfec.sgam.helpers.utils;

import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;

/**
 * Created by drodriguez on 28/04/2016.
 * Helper class for pallete methods
 */
public class PaletteHelper {

    private static final int DEFAULT_COLOR = 0xff02296a;

    /**
     * Sets the background of a view based on the drawable parameter
     * @param view view
     * @param drawable image
     */
    public static void setPaletteBackground(View view, Drawable drawable){
        Palette.from(DrawableHelper.drawableToBitmap(drawable))
                .generate(palette -> {
                    int bgColor = palette.getDarkVibrantColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getDarkMutedColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getVibrantColor(DEFAULT_COLOR);
                    if(bgColor==DEFAULT_COLOR)
                        bgColor = palette.getMutedColor(DEFAULT_COLOR);
                    view.setBackgroundColor(bgColor);
                });
    }
}
