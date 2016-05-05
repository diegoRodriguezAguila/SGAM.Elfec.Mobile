package com.elfec.sgam.helpers.utils.collections;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Observable collection that notifies its observers
 */
public class ObservableCollection<T> extends ArrayList<T> {

    private List<CollectionChangesListener<T>> mListeners;

    public void addListener(CollectionChangesListener<T> listener) {
        if (mListeners == null)
            mListeners = new ArrayList<>();

        mListeners.add(listener);
    }

    public void removeListener(CollectionChangesListener<T> listener) {
        if (mListeners != null)
            mListeners.remove(listener);
    }

    @Override
    public boolean add(T item) {
        boolean success = super.add(item);
        if (success && mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.added(item);
            }
        }
        return success;
    }

    @Override
    public void add(int index, T object) {
        super.add(index, object);
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.added(index, object);
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean success = super.addAll(collection);
        if (success && mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.addedAll(collection);
            }
        }
        return success;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        boolean success = super.addAll(index, collection);
        if (success && mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.addedAll(index, collection);
            }
        }
        return success;
    }

    @Override
    public T set(int index, T object) {
        T item = super.set(index, object);
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.set(index, object);
            }
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object item) {
        boolean success = super.remove(item);
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.removed((T)item);
            }
        }
        return success;
    }

    @Override
    public T remove(int index) {
        T item = super.remove(index);
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.removed(index);
            }
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NonNull Collection<?> collection){
        boolean success = super.removeAll(collection);
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.removedAll((Collection<? extends T>) collection);
            }
        }
        return success;
    }

    @Override
    public void clear() {
        super.clear();
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.cleared();
            }
        }
    }

    /**
     * Notifies all the colection listeners
     * that the item at the specified index has
     * been modified
     * @param index index
     */
    public void notifyItemUpdated(int index){
        if (mListeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: mListeners) {
                listener.itemUpdated(index);
            }
        }
    }

    /**
     * Listener of collection changes
     * @param <T>
     */
    public interface CollectionChangesListener<T>{
        void added(T item);
        void added(int index, T item);
        void addedAll(Collection<? extends T> collection);
        void addedAll(int index, Collection<? extends T> collection);
        void set(int index, T object);
        void itemUpdated(int index);
        void removed(T item);
        void removed(int index);
        void removedAll(Collection<? extends T> collection);
        void cleared();
    }
}
