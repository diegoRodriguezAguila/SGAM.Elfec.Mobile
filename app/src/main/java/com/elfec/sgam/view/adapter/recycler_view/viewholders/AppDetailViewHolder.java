package com.elfec.sgam.view.adapter.recycler_view.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.animation.Animator;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.view.launcher.ApplicationTools;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Viewholder for applications
 */
public class AppDetailViewHolder extends AbstractDraggableItemViewHolder {
    @Bind(R.id.layout_background)
    protected LinearLayout mBackground;
    @Bind(R.id.img_app_icon)
    protected ImageView mImgAppIcon;
    @Bind(R.id.txt_app_name)
    protected TextView mTxtAppName;



    public AppDetailViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bindApplication(final AppDetail application){
        mBackground.setBackgroundColor(application.getBgColor());
        mImgAppIcon.setImageDrawable(application.getIcon()!=null?application.getIcon():
        ApplicationTools.getAppDefaultIcon());
        mTxtAppName.setText(application.getAppName());
        mBackground.setOnClickListener(v -> {
            Animator.click(mBackground);
            ApplicationTools.launchApplication(application.getPackageName());
        });
    }
}
