package com.elfec.sgam.view;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.elfec.sgam.R;
import com.elfec.sgam.view.adapter.recycler_view.AppDetailAdapter;
import com.elfec.sgam.view.launcher.LauncherApps;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Applications extends AppCompatActivity {

    @Bind(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        ButterKnife.bind(this);
        initializeDragDropManager();
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setItemAnimator(animator);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        loadLauncherApps();
        // additional decorations
        if (!supportsViewElevation()) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable)
                    ContextCompat.getDrawable(this, R.drawable.material_shadow_z1)));
        }
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        findViewById(R.id.apps_background).setBackground(wallpaperDrawable);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Initializes the drag drop manager
     */
    private void initializeDragDropManager() {
        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));
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
}
