package com.elfec.sgam.view.adapter.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.utils.PaletteHelper;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.view.adapter.recycler_view.viewholders.AppDetailViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import java.util.List;

/**
 * Adapter for applications
 */
public class AppDetailAdapter extends RecyclerView.Adapter<AppDetailViewHolder>
        implements DraggableItemAdapter<AppDetailViewHolder> {
    private static final String TAG = "MyDraggableItemAdapter";

    private List<AppDetail> mApplications;
    private int[] mBgColors;

    public AppDetailAdapter(List<AppDetail> applications) {
        mApplications = applications;
        mBgColors = new int[applications.size()];
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        //package name is unique and we get its hash code as it has to be an integer
        return mApplications.get(position).getPackageName().hashCode();
    }

    @Override
    public AppDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.application_item, parent, false);
        return new AppDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppDetailViewHolder holder, int pos) {
        final AppDetail app = mApplications.get(pos);
        if(mBgColors[pos]==0)
            PaletteHelper.getPaletteBackgroundColor(app.getIcon(),
                    color->{
                        if(mBgColors[pos]==0) {
                            mBgColors[pos] = color;
                            holder.mBackground.setBackgroundColor(color);
                        }
                    });
        else holder.mBackground.setBackgroundColor(mBgColors[pos]);
        holder.bindApplication(app);
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");
        if (fromPosition == toPosition) {
            return;
        }
        final AppDetail app = mApplications.remove(fromPosition);
        mApplications.add(toPosition, app);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(AppDetailViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(AppDetailViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }
}
