package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.elfec.sgam.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main extends AppCompatActivity implements DesktopFragment
        .OnDesktopInteractionListener, ApplicationsFragment.OnApplicationsInteractionListener {

    private Fragment mCurrentFragment;
    private DesktopFragment mDesktopFragment;
    private ApplicationsFragment mApplicationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDesktopFragment = DesktopFragment.newInstance();
        mApplicationsFragment = ApplicationsFragment.newInstance();
        mCurrentFragment = mDesktopFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_content, mDesktopFragment).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onShowApps() {
        mCurrentFragment = mApplicationsFragment;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.nothing)
                .add(R.id.main_content, mApplicationsFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentFragment != mDesktopFragment) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
                    .remove(mCurrentFragment).commit();
            mCurrentFragment = mDesktopFragment;
        }
    }

}
