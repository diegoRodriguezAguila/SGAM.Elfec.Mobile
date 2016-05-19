package com.elfec.sgam.view.adapter.listview.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.view.adapter.listview.SessionOptionAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ViewHolder for session options
 */
public class SessionOptionViewHolder {
    @Bind(R.id.img_session_option)
    protected ImageView mImgIcon;
    @Bind(R.id.lbl_session_option)
    protected TextView mLblOption;

    public SessionOptionViewHolder(View root) {
        ButterKnife.bind(this, root);
    }

    /**
     * Binds session option
     * @param sessionOption session option
     */
    public void bindSessionOption(SessionOptionAdapter.SessionOption sessionOption) {
        mImgIcon.setImageDrawable(sessionOption.getIcon());
        mLblOption.setText(sessionOption.getLblId());
    }
}
