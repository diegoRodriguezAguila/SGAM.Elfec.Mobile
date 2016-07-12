package com.elfec.sgam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.presenter.CloseSessionPresenter;
import com.elfec.sgam.presenter.views.ICloseSessionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSessionInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CloseSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloseSessionFragment extends Fragment implements ICloseSessionView {

    private OnSessionInteractionListener mListener;
    private CloseSessionPresenter mPresenter;

    @BindView(R.id.layout_loading)
    protected LinearLayout mLayoutLoading;
    @BindView(R.id.txt_waiting_message)
    protected TextView mTxtWaitingMessage;
    @BindView(R.id.layout_errors)
    protected LinearLayout mLayoutErrors;
    @BindView(R.id.txt_error_message)
    protected TextView mTxtErrorMessage;
    private Animation slideLeftAnim;

    public CloseSessionFragment() {
        // Required empty public constructor
    }

    public static CloseSessionFragment newInstance() {
        CloseSessionFragment fragment = new CloseSessionFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_close_session, container, false);
        ButterKnife.bind(this, view);
        slideLeftAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_in);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionInteractionListener) {
            mListener = (OnSessionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSessionInteractionListener");
        }
        mPresenter = new CloseSessionPresenter(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mPresenter = null;
    }

    /**
     * Tells the view to start the session closing process
     */
    public void startSessionClosing(){
        mPresenter.logOut();
    }

    @Override
    public void showError(Exception error) {
        mLayoutLoading.clearAnimation();
        mLayoutLoading.setVisibility(View.GONE);
        mTxtErrorMessage.setText(error.getMessage());
        mLayoutErrors.setVisibility(View.VISIBLE);
        mLayoutErrors.startAnimation(slideLeftAnim);
    }

    @Override
    public void onSessionClosed() {
        if (mListener != null) {
            mListener.onSessionClosed();
        }
    }

    @Override
    public void showWaiting() {
        mTxtWaitingMessage.setText(R.string.msg_closing_device_session);
    }

    @Override
    public void updateWaiting(@StringRes int strId) {
        getActivity().runOnUiThread(() -> mTxtWaitingMessage.setText(strId));
    }

    @Override
    public void hideWaiting() {
        mLayoutLoading.clearAnimation();
        mLayoutLoading.setVisibility(View.GONE);
    }

    /**
     * Click de limpiar errores
     *
     * @param v vista
     */
    @OnClick(R.id.btn_clear_errors)
    public void btnClearErrorClick(View v) {
        mLayoutErrors.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onSessionCloseFailed();
        }
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
    public interface OnSessionInteractionListener {
        /**
         * Called when the session was closed successfully
         */
        void onSessionClosed();

        /**
         * Called when the session couldn't be closed succesfully
         */
        void onSessionCloseFailed();
    }
}
