package com.elfec.sgam.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.elfec.sgam.R;
import com.elfec.sgam.security.SessionManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Main extends AppCompatActivity implements DesktopFragment
        .OnDesktopInteractionListener, ApplicationsFragment.OnApplicationsInteractionListener,
        LoginFragment.OnLoginInteractionListener {

    private Fragment mCurrentFragment;
    private DesktopFragment mDesktopFragment;
    private ApplicationsFragment mApplicationsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDesktopFragment = DesktopFragment.newInstance();
        mApplicationsFragment = ApplicationsFragment.newInstance();
        setCurrentFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        goToDesktop();
    }

    /**
     * Analizes the conditions to put the current fragment
     */
    private void setCurrentFragment() {
        mCurrentFragment = mDesktopFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, mDesktopFragment);

        if (!SessionManager.isSessionOpened()) {
            mCurrentFragment = LoginFragment.newInstance();
            transaction.add(R.id.main_content, mCurrentFragment);
        }
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        goToDesktop();
    }

    @Override
    public void onShowApps() {
        if (mCurrentFragment != mApplicationsFragment) {
            mCurrentFragment = mApplicationsFragment;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.nothing)
                    .add(R.id.main_content, mApplicationsFragment)
                    .commit();
        }
    }

    @Override
    public void onUserAuthenticated() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.slide_right_out)
                .remove(mCurrentFragment).commit();
        mCurrentFragment = mDesktopFragment;
    }


    /**
     * Goes to the desktop
     */
    private void goToDesktop() {
        if (!(mCurrentFragment instanceof LoginFragment)
                && mCurrentFragment != mDesktopFragment) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
                    .remove(mCurrentFragment).commit();
            mCurrentFragment = mDesktopFragment;
        }
    }
}
