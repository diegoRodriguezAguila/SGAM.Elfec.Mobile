package com.elfec.sgam.view.adapter.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.elfec.sgam.R;
import com.elfec.sgam.view.adapter.listview.viewholders.SessionOptionViewHolder;
import com.elfec.sgam.view.launcher.session.SessionOption;
import com.elfec.sgam.view.launcher.session.options.CloseSessionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drodriguez on 18/05/2016.
 * Adapter for the session Options
 */
public class SessionOptionAdapter extends ListAdapterImpl<SessionOption, SessionOptionViewHolder> {

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
                ContextCompat.getDrawable(context, R.drawable.close_session), new
                CloseSessionHandler()));
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

}
