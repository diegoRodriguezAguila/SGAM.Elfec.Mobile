package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.presenter.LoginPresenter;
import com.elfec.sgam.presenter.views.ILoginView;
import com.elfec.sgam.view.launcher.LauncherApps;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements ILoginView {

    private OnLoginInteractionListener mListener;

    /**
     * Presenter
     */
    private LoginPresenter presenter;

    private Handler mHandler;

    private RxPermissions mRxPermissions;

    @BindView(R.id.txt_username)
    protected EditText mTxtUsername;
    @BindView(R.id.txt_password)
    protected EditText mTxtPassword;
    @BindView(R.id.layout_login_form)
    protected LinearLayout mLayoutLoginForm;
    @BindView(R.id.snackbar_position)
    protected CoordinatorLayout mSnackbarPosition;
    @BindView(R.id.layout_loading)
    protected LinearLayout mLayoutLoading;
    @BindView(R.id.txt_waiting_message)
    protected TextView mTxtWaitingMessage;
    @BindView(R.id.layout_errors)
    protected LinearLayout mLayoutErrors;
    @BindView(R.id.txt_error_message)
    protected TextView mTxtErrorMessage;
    private Animation slideLeftAnim;

    public LoginFragment() {
        // Required empty public constructor
        mHandler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        slideLeftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_in);
        mTxtPassword.setTransformationMethod(MetroPasswordTransformationMethod.getInstance());
        presenter = new LoginPresenter(this);
        mTxtUsername.setText("drodriguez");
        mTxtPassword.setText("Rasta$#\"!");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginInteractionListener) {
            mListener = (OnLoginInteractionListener) context;
            mRxPermissions = new RxPermissions(getActivity());
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mRxPermissions = null;
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
        Snackbar.make(mSnackbarPosition,
                R.string.msg_fields_no_empty, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginErrors(final Exception... validationErrors) {
        mHandler.post(() -> {
            mLayoutLoading.clearAnimation();
            mLayoutLoading.setVisibility(View.GONE);
            mTxtErrorMessage.setText(MessageListFormatter
                    .formatHTMLFromErrors(validationErrors));
            mLayoutErrors.setVisibility(View.VISIBLE);
            mLayoutErrors.startAnimation(slideLeftAnim);
        });
    }

    @Override
    public void userLoggedInSuccessfully(List<AppDetail> apps) {
        LauncherApps.instance().setAppsCache(apps);
        if (mListener != null)
            mListener.onUserAuthenticated();
    }

    @Override
    public void showWaiting() {
        mHandler.post(() -> {
            mLayoutLoginForm.setVisibility(View.GONE);
            mLayoutLoading.setVisibility(View.VISIBLE);
            mLayoutLoading.startAnimation(slideLeftAnim);
            mTxtWaitingMessage.setText(R.string.msg_login_user);
        });
    }

    @Override
    public void updateWaiting(@StringRes final int strId) {
        mHandler.post(() -> mTxtWaitingMessage.setText(strId));
    }

    @Override
    public void hideWaiting() {
        mHandler.post(() -> {
            mLayoutLoading.clearAnimation();
            mLayoutLoading.setVisibility(View.GONE);
        });
    }
    //endregion

    /**
     * Click for logIn button
     *
     * @param v vista
     */
    @OnClick(R.id.btn_login)
    public void btnLoginClick(View v) {
        if (!ButtonClicksHelper.canClickButton()) {
            return;
        }
        KeyboardHelper.hideKeyboard(getView());
        mRxPermissions.request(GET_ACCOUNTS, READ_PHONE_STATE,
                CAMERA, WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        presenter.logIn();
                    } else {
                        showPermissionsRequired();
                    }
                });
    }

    /**
     * Click de limpiar errores
     *
     * @param v vista
     */
    @OnClick(R.id.btn_clear_errors)
    public void btnClearErrorClick(View v) {
        mLayoutErrors.setVisibility(View.GONE);
        mTxtPassword.setText("");
        mLayoutLoginForm.setVisibility(View.VISIBLE);
        mLayoutLoginForm.startAnimation(slideLeftAnim);
    }

    public void showPermissionsRequired(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_permissions_are_mandatory)
                .setMessage(R.string.msg_permissions_are_mandatory)
                .setPositiveButton(R.string.btn_ok, null).show();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLoginInteractionListener {
        /**
         * Se ejecuta cuando el usuario se autenticó exitosamente
         */
        void onUserAuthenticated();
    }
}
