package pl.wat.nutpromobile.features.service;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public interface ServiceManager{
    void release();
    void resume();
    void bindService();
    void unbindService();
}
