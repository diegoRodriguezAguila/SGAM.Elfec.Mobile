package com.elfec.sgam.helpers.ui;


import android.graphics.Color;

/**
 * Some color utils
 */
public class ColorTools {
    private static final double DEFAULT_THRESHOLD = 50;
    private static final double STD_FACTOR = 0.1;
    /**
     * Checks if two colors are close
     * @param color1 color 1
     * @param color2 color 2
     * @return true if colors are close
     */
    public static boolean colorsAreClose(int color1, int color2){
        return colorsAreClose(color1,color2,DEFAULT_THRESHOLD);
    }

    /**
     * Checks if two colors are close
     * @param color1 color 1
     * @param color2 color 2
     * @param threshold difference threshold
     * @return true if colors are close
     */
    public static boolean colorsAreClose(int color1, int color2, double threshold)
    {
        int r = Color.red(color1) - Color.red(color2),
            g = Color.green(color1) - Color.green(color2),
            b = Color.blue(color1) - Color.blue(color2);
        return (r*r + g*g + b*b) <= threshold*threshold;
    }

    /**
     * Lightens a color by the defaul lighter factor
     * @param color the color to lighten
     * @return lighter version of the specified color.
     */
    public static int lighter(int color){
        return lighter(color, STD_FACTOR);
    }

    /**
     * Lightens a color by a given factor.
     *
     * @param color
     *            The color to lighten
     * @param factor
     *            The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *            color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, double factor) {
        if(factor>1 || factor<0)
            throw new IllegalArgumentException("factor must be between 0 and 1");
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    /**
     * Darkens a color  by the default darker factor
     * @param color  The color to darken
     * @return darker version of the specified color.
     */
    public static int darker(int color) {return darker(color, STD_FACTOR);}

    /**
     * Darkens a color by a given factor.
     *
     * @param color
     *            The color to darken
     * @param factor
     *            The factor to darken the color. 0 will make the color unchanged. 1 will make the
     *            color black.
     * @return darker version of the specified color.
     */
    public static int darker(int color, double factor) {
        if(factor>1 || factor<0)
            throw new IllegalArgumentException("factor must be between 0 and 1");
        int red = Color.red(color);
        red -= (int)(red*factor);
        int green = Color.green(color);
        green -= (int)(green*factor);
        int blue = Color.blue(color);
        blue -= (int)(blue*factor);
        return Color.argb(Color.alpha(color), red, green, blue);
    }
}
