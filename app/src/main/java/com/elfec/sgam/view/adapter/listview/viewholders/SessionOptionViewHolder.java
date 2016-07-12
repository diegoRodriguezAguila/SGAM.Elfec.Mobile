package com.elfec.sgam.view.adapter.listview.viewholders;

import android.view.View;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.utils.DrawableHelper;
import com.elfec.sgam.view.launcher.session.SessionOption;

import butterknife.BindView;

/**
 * ViewHolder for session options
 */
public class SessionOptionViewHolder {
    @BindView(R.id.lbl_session_option)
    protected TextView mLblOption;

    public SessionOptionViewHolder(View root) {
        mLblOption = (TextView) root;
    }

    /**
     * BindViews session option
     * @param sessionOption session option
     */
    public void bindSessionOption(SessionOption sessionOption) {
        mLblOption.setText(sessionOption.getLblId());
        DrawableHelper.setDrawableLeft(mLblOption, sessionOption.getIcon());
    }
}
