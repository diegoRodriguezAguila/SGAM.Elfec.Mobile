package com.elfec.sgam.helpers.ui.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elfec.sgam.R;
import com.elfec.sgam.settings.AppPreferences;

/**
 * Helper class for animate views
 */
public class Animator {
    /**
     * Click animaiton
     */
    private static final Animation CLICK_ANIMATION = AnimationUtils.loadAnimation(
            AppPreferences.getApplicationContext()
            , R.anim.button_click);
    /**
     * Animates a view {@link #CLICK_ANIMATION}
     * @param view view
     */
    public static void click(View view){
        view.startAnimation(CLICK_ANIMATION);
    }
}
