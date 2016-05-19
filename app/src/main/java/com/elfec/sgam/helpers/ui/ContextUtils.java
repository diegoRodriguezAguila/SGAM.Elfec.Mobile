package com.elfec.sgam.helpers.ui;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;

import com.elfec.sgam.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Utils for context manipulation
 */
public class ContextUtils {
    /**
     * Gets the wrapped context, wrapping with the main theme
     * and with calligraphy wrapper for custom fonts
     * @param context context to wrap
     * @return wrapped context
     */
    public static Context wrapContext(Context context) {

        return CalligraphyContextWrapper
                .wrap(new ContextThemeWrapper(context,
                        R.style.AppCustomTheme));
    }
}
