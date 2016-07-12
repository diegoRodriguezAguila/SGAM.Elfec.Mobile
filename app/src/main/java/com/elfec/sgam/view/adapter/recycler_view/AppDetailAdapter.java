package com.elfec.sgam.view.adapter.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.VibrationHelper;
import com.elfec.sgam.helpers.utils.collections.ObservableCollection;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.view.adapter.recycler_view.viewholders.AppDetailViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;

import java.util.Collection;
import java.util.List;

/**
 * Adapter for applications
 */
public class AppDetailAdapter extends RecyclerView.Adapter<AppDetailViewHolder>
        implements DraggableItemAdapter<AppDetailViewHolder> {

    private List<AppDetail> mApplications;

    public AppDetailAdapter(List<AppDetail> applications) {
        mApplications = applications;
        if (applications instanceof ObservableCollection) {
            ((ObservableCollection<AppDetail>)applications).addListener(new AppDetailListener());
        }
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
        holder.bindApplication(mApplications.get(pos));
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition)
            return;
        final AppDetail app = mApplications.remove(fromPosition);
        mApplications.add(toPosition, app);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public boolean onCheckCanStartDrag(AppDetailViewHolder holder, int position, int x, int y) {
        VibrationHelper.shortVibrate();
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(AppDetailViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    private class AppDetailListener implements ObservableCollection.CollectionChangesListener<AppDetail> {

        @Override
        public void itemUpdated(int index) {
            notifyItemChanged(index);
        }

        //region unused methods
        @Override
        public void added(AppDetail item) {

        }

        @Override
        public void added(int index, AppDetail item) {

        }

        @Override
        public void addedAll(Collection<? extends AppDetail> collection) {

        }

        @Override
        public void addedAll(int index, Collection<? extends AppDetail> collection) {

        }

        @Override
        public void set(int index, AppDetail object) {

        }

        @Override
        public void removed(AppDetail item) {

        }

        @Override
        public void removed(int index) {

        }

        @Override
        public void removedAll(Collection<? extends AppDetail> collection) {

        }

        @Override
        public void cleared() {

        }

        //endregion
    }
}
