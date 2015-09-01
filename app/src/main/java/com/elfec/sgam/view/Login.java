package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.text.method.MetroPasswordTransformationMethod;
import com.elfec.sgam.helpers.ui.ButtonClicksHelper;
import com.elfec.sgam.helpers.ui.KeyboardHelper;
import com.elfec.sgam.presenter.LoginPresenter;
import com.elfec.sgam.presenter.views.ILoginView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Login extends AppCompatActivity implements ILoginView {

    /**
     * Presenter
     */
    private LoginPresenter presenter;

    private EditText mTxtUsername;
    private EditText mTxtPassword;
    private CoordinatorLayout mSnackbarPosition;
    private LinearLayout mLayoutLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTxtUsername = (EditText) findViewById(R.id.txt_username);
        mTxtPassword = (EditText) findViewById(R.id.txt_password);
        mSnackbarPosition = (CoordinatorLayout) findViewById(R.id.snackbar_position);
        mLayoutLoading = (LinearLayout) findViewById(R.id.layout_loading);

        mTxtPassword.setTransformationMethod(MetroPasswordTransformationMethod.getInstance());
        presenter = new LoginPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Click for login button
     *
     * @param v vista
     */
    public void btnLoginClick(View v) {
        if (ButtonClicksHelper.canClickButton()) {
            KeyboardHelper.hideKeyboard(getWindow().getDecorView().getRootView());
            presenter.login();
            mTxtPassword.setVisibility(View.GONE);
            mTxtUsername.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            mLayoutLoading.setVisibility(View.VISIBLE);
            mLayoutLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_in));
        }
    }


    //region Interface Methods

    @Override
    public String getUsername() {
        return mTxtUsername.getText().toString().trim().toUpperCase();
    }

    @Override
    public String getPassword() {
        return mTxtPassword.getText().toString().trim();
    }

    @Override
    public String getIMEI() {
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
    }

    @Override
    public void notifyErrorsInFields() {
        Snackbar.make(findViewById(R.id.snackbar_position),
                R.string.errors_in_fields, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    //endregion
}
