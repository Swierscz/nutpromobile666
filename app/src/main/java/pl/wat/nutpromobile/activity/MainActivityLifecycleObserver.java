package pl.wat.nutpromobile.activity;


import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;


public class MainActivityLifecycleObserver implements LifecycleObserver {
    public final static String TAG = MainActivityLifecycleObserver.class.getSimpleName();
    Lifecycle lifecycle;

    public MainActivityLifecycleObserver(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void logStart() {
        Log.i(TAG, "Start");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void logPause() {
        Log.i(TAG, "Pause");
    }

    void doSth() {
        if (lifecycle.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            //do sth

        }
    }
}