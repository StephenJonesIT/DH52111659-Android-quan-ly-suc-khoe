package vn.edu.stu.tranthanhsang.healthy_app.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private var pending = AtomicBoolean(false)
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            // Log warning if multiple active observers are registered, as only one will receive the event.
            // This is typically for events that are 'consumed'.
            // If you need multiple observers, consider a regular LiveData or SharedFlow/StateFlow.
            Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of new events.")
        }

        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call(){
        value = null
    }
}