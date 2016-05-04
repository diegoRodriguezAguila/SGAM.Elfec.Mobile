package com.elfec.sgam.view;

import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elfec.sgam.R;
import com.elfec.sgam.view.adapter.recycler_view.AppDetailAdapter;
import com.elfec.sgam.view.launcher.LauncherApps;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnApplicationsInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationsFragment extends Fragment {

    private OnApplicationsInteractionListener mListener;

    @Bind(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    public ApplicationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ApplicationsFragment.
     */
    public static ApplicationsFragment newInstance() {
        ApplicationsFragment fragment = new ApplicationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_applications, container, false);
        ButterKnife.bind(this, view);
        initializeDragDropManager();
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setItemAnimator(animator);
        mLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadLauncherApps();
        // additional decorations
        if (!supportsViewElevation()) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable)
                    ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance){
        super.onViewCreated(view, savedInstance);
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_in);
        anim.setDuration(200);
        mRecyclerView.startAnimation(anim);
    }


    /**
     * Initializes the drag drop manager
     */
    private void initializeDragDropManager() {
        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    /**
     * Loads the apps
     */
    private void loadLauncherApps(){
        mAdapter = new AppDetailAdapter(LauncherApps.instance().getAppsCache());
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnApplicationsInteractionListener) {
            mListener = (OnApplicationsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnApplicationsInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.partial_shrink);
        anim.setFillAfter(true);
        mRecyclerView.startAnimation(anim);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnApplicationsInteractionListener {

    }
}
