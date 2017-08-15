package com.deya.hospital.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventMgr<T> {
    public interface INotifyCallback<T> {
        void onNotify(T listener);
    }

    private ReferenceQueue<T> mListenerReferenceQueue = new ReferenceQueue<T>();
    private ConcurrentLinkedQueue<WeakReference<T>> mListenerList =
        new ConcurrentLinkedQueue<WeakReference<T>>();

    public void register(T listener) {
        if (listener == null) {
            return;
        }

        synchronized (this) {
            Reference<? extends T> releaseListener = null;
            while ((releaseListener = mListenerReferenceQueue.poll()) != null) {
                mListenerList.remove(releaseListener);
            }

            for (WeakReference<T> weakListener : mListenerList) {
                T listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            WeakReference<T> weakListener = new WeakReference<T>(listener, mListenerReferenceQueue);
            mListenerList.add(weakListener);
        }
    }

    public void unregister(T listener) {
        if (listener == null) {
            return;
        }

        synchronized (this) {
            for (WeakReference<T> weakListener : mListenerList) {
                T listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    mListenerList.remove(weakListener);
                    return;
                }
            }
        }
    }

    public void startNotify(INotifyCallback callback) {
        synchronized (this) {
            for (Iterator<WeakReference<T>> iterator = mListenerList.iterator(); iterator
                .hasNext();) {
                T listener = iterator.next().get();
                if (listener == null) {
                    iterator.remove();
                } else {
                    callback.onNotify(listener);
                }
            }
        }
    }

    public void clear() {
        mListenerList.clear();
    }

    public int size() {
        return mListenerList.size();
    }
}
