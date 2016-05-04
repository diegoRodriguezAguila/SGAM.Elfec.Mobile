package com.elfec.sgam.view;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.ButtonClicksHelper;
import com.elfec.sgam.helpers.ui.ColorTools;
import com.elfec.sgam.helpers.utils.PaletteHelper;
import com.elfec.sgam.settings.AppPreferences;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Desktop extends AppCompatActivity {
    @Bind(R.id.date)
    protected TextView mTxtDate;
    @Bind(R.id.desktop_bar)
    protected View mToolBar;
    @Bind(R.id.desktop_bar_border)
    protected View mToolBarBorder;

    private Drawable mWallpaperDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desktop);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume(){
        super.onResume();
        mTxtDate.setText(DateTime.now().toString("EEEE, d 'de' MMMM"));
        initializeWallpaperAndToolbar();
        if(AppPreferences.instance().getLoggedUsername()==null){
            Intent i = new Intent(Desktop.this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            overridePendingTransition(R.anim.nothing, R.anim.nothing);
        }
    }

    /**
     * Initializes the wallpaper and the toolbar colors depending on it
     */
    private void initializeWallpaperAndToolbar() {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        if(wallpaperDrawable!=mWallpaperDrawable) {
            mWallpaperDrawable = wallpaperDrawable;
            findViewById(R.id.desktop_background).setBackground(mWallpaperDrawable);
            PaletteHelper.getDrawableBackgroundColor(mWallpaperDrawable, color -> {
                int bg = ColorUtils.setAlphaComponent(color, 0xCA);
                mToolBar.setBackgroundColor(bg);
                int bgBorder = ColorUtils.setAlphaComponent(ColorTools.darker(color, 0.25), 0xEA);
                mToolBarBorder.setBackgroundColor(bgBorder);
            });
        }
    }

    /**
     * Show apps button click event
     * @param v view
     */
    public void btnShowApps(View v){
        if (ButtonClicksHelper.canClickButton()) {
            Intent i = new Intent(Desktop.this, Applications.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in,0);
        }
    }

}
