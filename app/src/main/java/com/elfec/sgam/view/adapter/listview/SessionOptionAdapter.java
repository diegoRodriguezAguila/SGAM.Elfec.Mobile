package com.elfec.sgam.view.adapter.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.elfec.sgam.R;
import com.elfec.sgam.view.adapter.listview.viewholders.SessionOptionViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drodriguez on 18/05/2016.
 * Adapter for the session Options
 */
public class SessionOptionAdapter extends ListAdapterImpl<SessionOptionAdapter
        .SessionOption, SessionOptionViewHolder> {

    /**
     * Constructor: Construye el adapter con las opciones por defecto
     *
     * @param context  context
     * @param resource res layout
     */
    public SessionOptionAdapter(Context context, @LayoutRes int resource) {
        super(context, resource, generateSessionOptions(context));
    }

    private static List<SessionOption> generateSessionOptions(Context context) {
        List<SessionOption> sessionOptions = new ArrayList<>();
        sessionOptions.add(new SessionOption(R.string.ses_opt_close_session,
                ContextCompat.getDrawable(context, R.drawable.close_session)));
        return sessionOptions;
    }

    @NonNull
    @Override
    public SessionOptionViewHolder onCreateViewHolder(View rootView) {
        return new SessionOptionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionOptionViewHolder holder, int pos) {
        holder.bindSessionOption(getItem(pos));
    }

    /**
     * Session Option
     */
    public static class SessionOption {
        private
        @StringRes
        int lblId;
        private Drawable icon;
        //region better collapse
        public SessionOption() {
        }

        public SessionOption(int lblId, Drawable icon) {
            this.lblId = lblId;
            this.icon = icon;
        }

        public int getLblId() {
            return lblId;
        }

        public void setLblId(int lblId) {
            this.lblId = lblId;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }
        //endregion
    }
}
