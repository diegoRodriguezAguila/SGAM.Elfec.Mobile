package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.elfec.sgam.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Login extends AppCompatActivity implements LoginFragment.OnLoginInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_content, LoginFragment.newInstance()).commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onUserAuthenticated() {
        finish();
        Toast.makeText(this, getString(R.string.msg_account_registered), Toast.LENGTH_LONG).show();
    }
}
