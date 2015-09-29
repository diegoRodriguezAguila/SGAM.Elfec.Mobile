package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.text.MessageListFormatter;
import com.elfec.sgam.helpers.text.method.MetroPasswordTransformationMethod;
import com.elfec.sgam.helpers.ui.ButtonClicksHelper;
import com.elfec.sgam.helpers.ui.KeyboardHelper;
import com.elfec.sgam.presenter.LoginPresenter;
import com.elfec.sgam.presenter.views.ILoginView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Login extends AppCompatActivity implements ILoginView {

    /**
     * Presenter
     */
    private LoginPresenter presenter;

    @Bind(R.id.txt_username)
    protected EditText mTxtUsername;
    @Bind(R.id.txt_password)
    protected EditText mTxtPassword;
    @Bind(R.id.layout_login_form)
    protected LinearLayout mLayoutLoginForm;
    @Bind(R.id.snackbar_position)
    protected CoordinatorLayout mSnackbarPosition;
    @Bind(R.id.layout_loading)
    protected LinearLayout mLayoutLoading;
    @Bind(R.id.txt_waiting_message)
    protected TextView mTxtWaitingMessage;
    @Bind(R.id.layout_errors)
    protected LinearLayout mLayoutErros;
    @Bind(R.id.txt_error_message)
    protected TextView mTxtErrorMessage;
    private Animation slideLeftAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        slideLeftAnim = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        mTxtPassword.setTransformationMethod(MetroPasswordTransformationMethod.getInstance());
        presenter = new LoginPresenter(this);
        mTxtUsername.setText("drodriguezd");
        mTxtPassword.setText("elfec2015");
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
     * Click for logIn button
     *
     * @param v vista
     */
    public void btnLoginClick(View v) {
        if (ButtonClicksHelper.canClickButton()) {
            KeyboardHelper.hideKeyboard(getWindow().getDecorView().getRootView());
            presenter.logIn();
        }
    }

    /**
     * Click de limpiar errores
     *
     * @param v vista
     */
    public void btnClearErrorClick(View v) {
        mLayoutErros.setVisibility(View.GONE);
        mTxtPassword.setText("");
        mLayoutLoginForm.setVisibility(View.VISIBLE);
        mLayoutLoginForm.startAnimation(slideLeftAnim);
    }


    //region Interface Methods

    @Override
    public String getUsername() {
        return mTxtUsername.getText().toString().trim().toLowerCase();
    }

    @Override
    public String getPassword() {
        return mTxtPassword.getText().toString().trim();
    }

    @Override
    public void notifyErrorsInFields() {
        Snackbar.make(findViewById(R.id.snackbar_position),
                R.string.msg_fields_no_empty, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginErrors(final List<Exception> validationErrors) {
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 mLayoutLoading.clearAnimation();
                 mLayoutLoading.setVisibility(View.GONE);
                 mTxtErrorMessage.setText( MessageListFormatter
                         .formatHTMLFromErrors(validationErrors));
                 mLayoutErros.setVisibility(View.VISIBLE);
                 mLayoutErros.startAnimation(slideLeftAnim);
             }
         });
    }

    @Override
    public void showWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLayoutLoginForm.setVisibility(View.GONE);
                mLayoutLoading.setVisibility(View.VISIBLE);
                mLayoutLoading.startAnimation(slideLeftAnim);
            }
        });
    }

    @Override
    public void updateWaiting(@StringRes final int strId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtWaitingMessage.setText(strId);
            }
        });
    }

    @Override
    public void hideWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLayoutLoading.clearAnimation();
                mLayoutLoading.setVisibility(View.GONE);
            }
        });
    }


    //endregion
}
