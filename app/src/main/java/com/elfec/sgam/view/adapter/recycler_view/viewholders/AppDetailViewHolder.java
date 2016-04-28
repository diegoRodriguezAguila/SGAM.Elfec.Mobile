package com.elfec.sgam.view.adapter.recycler_view.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.model.AppDetail;
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
        mImgAppIcon.setImageDrawable(application.getIcon());
        mTxtAppName.setText(application.getLabel());
    }
}
