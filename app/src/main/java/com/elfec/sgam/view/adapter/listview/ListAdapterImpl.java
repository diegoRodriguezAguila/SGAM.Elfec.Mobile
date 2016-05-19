package com.elfec.sgam.view.adapter.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic list adapter
 *
 * @param <T> Model type
 * @param <H> ViewHolder type
 */
public abstract class ListAdapterImpl<T, H> extends BaseAdapter {
    private final LayoutInflater mInflater;

    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;
    private Context mContext;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file to use when
     *                 instantiating views.
     */
    public ListAdapterImpl(Context context, @LayoutRes int resource) {
        this(context, resource, new ArrayList<>());
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ListAdapterImpl(Context context, @LayoutRes int resource,
                           @NonNull List<T> objects) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mObjects = objects;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
            viewHolder = onCreateViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (H) convertView.getTag();
        }
        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    /**
     * Gets an instance of ViewHolder. This method is only called
     * for viewholder creation
     * @param rootView root view
     * @return instance of ViewHolder
     */
    public abstract @NonNull H onCreateViewHolder(View rootView);

    /**
     * Binds the viewholder
     * @param holder viewholder
     * @param pos position to bind
     */
    public abstract void onBindViewHolder(@NonNull H holder, int pos);

    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }
}
