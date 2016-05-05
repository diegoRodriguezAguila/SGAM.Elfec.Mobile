package com.elfec.sgam.helpers.utils.collections;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Observable collection that notifies its observers
 */
public class ObservableCollection<T> extends ArrayList<T> {

    List<CollectionChangesListener<T>> _listeners;

    public void addListener(CollectionChangesListener<T> listener) {
        if (_listeners == null)
            _listeners = new ArrayList<>();

        _listeners.add(listener);
    }

    public void removeListener(CollectionChangesListener<T> listener) {
        if (_listeners != null)
            _listeners.remove(listener);
    }

    @Override
    public boolean add(T item) {
        boolean success = super.add(item);
        if (success && _listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.added(item);
            }
        }
        return success;
    }

    @Override
    public void add(int index, T object) {
        super.add(index, object);
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.added(index, object);
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean success = super.addAll(collection);
        if (success && _listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.addedAll(collection);
            }
        }
        return success;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        boolean success = super.addAll(index, collection);
        if (success && _listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.addedAll(index, collection);
            }
        }
        return success;
    }

    @Override
    public T set(int index, T object) {
        T item = super.set(index, object);
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.set(index, object);
            }
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object item) {
        boolean success = super.remove(item);
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.removed((T)item);
            }
        }
        return success;
    }

    @Override
    public T remove(int index) {
        T item = super.remove(index);
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.removed(index);
            }
        }
        return item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NonNull Collection<?> collection){
        boolean success = super.removeAll(collection);
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.removedAll((Collection<? extends T>) collection);
            }
        }
        return success;
    }

    @Override
    public void clear() {
        super.clear();
        if (_listeners != null) {
            // Notify any listeners
            for (CollectionChangesListener<T> listener: _listeners) {
                listener.cleared();
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
        void removed(T item);
        void removed(int index);
        void removedAll(Collection<? extends T> collection);
        void cleared();
    }
}
