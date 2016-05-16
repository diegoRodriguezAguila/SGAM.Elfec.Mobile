package com.elfec.sgam.view;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.ColorUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.ButtonClicksHelper;
import com.elfec.sgam.helpers.ui.ColorTools;
import com.elfec.sgam.helpers.utils.PaletteHelper;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDesktopInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DesktopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DesktopFragment extends Fragment {


    private OnDesktopInteractionListener mListener;
    @Bind(R.id.desktop_background)
    protected View mBackground;
    @Bind(R.id.date)
    protected TextView mTxtDate;
    @Bind(R.id.desktop_bar)
    protected View mToolBar;
    @Bind(R.id.desktop_bar_border)
    protected View mToolBarBorder;

    private WallpaperManager mWallpaperManager;
    private Drawable mWallpaperDrawable;



    public DesktopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DesktopFragment.
     */
    public static DesktopFragment newInstance() {
        DesktopFragment fragment = new DesktopFragment();
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
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDesktopInteractionListener) {
            mListener = (OnDesktopInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDesktopInteractionListener");
        }
        mWallpaperManager = WallpaperManager.getInstance(getContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mWallpaperManager = null;
        mWallpaperDrawable = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        mTxtDate.setText(DateTime.now().toString(getString(R.string.date_format)));
        initializeWallpaperAndToolbar();
    }

    /**
     * Initializes the wallpaper and the toolbar colors depending on it
     */
    private void initializeWallpaperAndToolbar() {
        Drawable wallpaperDrawable = mWallpaperManager.getDrawable();
        if(wallpaperDrawable!=mWallpaperDrawable) {
            mWallpaperDrawable = wallpaperDrawable;
            mBackground.setBackground(mWallpaperDrawable);
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
    @OnClick(R.id.btn_show_apps)
    public void btnShowAppsClick(View v){
        if (mListener != null && ButtonClicksHelper.canClickButton() &&
                getActivity()!=null) {
            mListener.onShowApps();
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
    public interface OnDesktopInteractionListener {
        /**
         * Indicates that show apps was requested
         */
        void onShowApps();
    }
}
