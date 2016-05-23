package com.elfec.sgam.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.elfec.sgam.ElfecApp;
import com.elfec.sgam.R;
import com.elfec.sgam.presenter.views.IMainView;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.view.launcher.LauncherMain;
import com.elfec.sgam.view.launcher.session.SessionNotifier;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.elfec.sgam.helpers.utils.ObservableUtils.applySchedulers;
import static com.elfec.sgam.view.ApplicationsFragment.OnApplicationsInteractionListener;
import static com.elfec.sgam.view.CloseSessionFragment.OnSessionInteractionListener;
import static com.elfec.sgam.view.DesktopFragment.OnDesktopInteractionListener;
import static com.elfec.sgam.view.LoginFragment.OnLoginInteractionListener;

public class Main extends AppCompatActivity implements IMainView, OnDesktopInteractionListener,
        OnApplicationsInteractionListener, OnLoginInteractionListener, OnSessionInteractionListener {

    private static final String CLOSE_SESSION_KEY = "close_session";
    private Fragment mCurrentFragment;
    private DesktopFragment mDesktopFragment;
    private ApplicationsFragment mApplicationsFragment;
    private boolean mIsClosingSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ElfecApp.setUiContext(this);
        LauncherMain.init(this);
        mDesktopFragment = DesktopFragment.newInstance();
        mApplicationsFragment = ApplicationsFragment.newInstance();
        setCurrentFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras().getBoolean(CLOSE_SESSION_KEY))
            goToCloseSession();
        else goToDesktop();
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
        } else {
            SessionManager.instance().getFullLoggedInUser()
                    .compose(applySchedulers())
                    .subscribe(SessionNotifier.create()::setCurrentUser);
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
        SessionManager.instance().getFullLoggedInUser()
                .compose(applySchedulers())
                .subscribe(SessionNotifier.create()::setCurrentUser);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.slide_right_out)
                .remove(mCurrentFragment).commit();
        mCurrentFragment = mDesktopFragment;
    }


    /**
     * Goes to the desktop
     */
    private void goToDesktop() {
        if (!(mCurrentFragment instanceof LoginFragment ||
                mCurrentFragment instanceof CloseSessionFragment)
                && mCurrentFragment != mDesktopFragment) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.nothing, R.anim.fade_out)
                    .remove(mCurrentFragment).commit();
            mCurrentFragment = mDesktopFragment;
        }
    }

    @Override
    public void closeSession() {
        //Bring launcher to foreground
        Intent i = getPackageManager().getLaunchIntentForPackage(getPackageName());
        i.putExtra(CLOSE_SESSION_KEY, true);
        getApplicationContext().startActivity(i);
    }

    private void goToCloseSession(){
        if (!mIsClosingSession && !(mCurrentFragment instanceof CloseSessionFragment)){
            mIsClosingSession = true;
            CloseSessionFragment fragment = CloseSessionFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, mDesktopFragment)
                    .setCustomAnimations(R.anim.fade_in, R.anim.nothing)
                    .add(R.id.main_content, fragment).commit();
            mCurrentFragment = fragment;
            //wait to finish adding of view
            getSupportFragmentManager().executePendingTransactions();
            fragment.startSessionClosing();
        }
    }

    @Override
    public boolean isClosingSession() {
        return mIsClosingSession;
    }

    @Override
    public void onSessionClosed() {
        SessionNotifier.create().clearCurrentUser();
        mIsClosingSession = false;
        Fragment loginFragment = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.slide_right_out)
                .remove(mCurrentFragment)
                .add(R.id.main_content, loginFragment)
                .commit();
        mCurrentFragment = loginFragment;
    }

    @Override
    public void onSessionCloseFailed() {
        mIsClosingSession = false;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.nothing, R.anim.slide_right_out)
                .remove(mCurrentFragment).commit();
        mCurrentFragment = mDesktopFragment;
    }
}
