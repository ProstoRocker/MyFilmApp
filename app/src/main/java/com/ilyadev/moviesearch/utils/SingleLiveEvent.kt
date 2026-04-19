package com.ilyadev.moviesearch.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * LiveData, которая отправляет события только один раз.
 *
 * Решает проблему повторного показа Snackbar при повороте экрана.
 *
 * Используется в ViewModel для ошибок.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    override fun setValue(value: T) {
        if (pending.compareAndSet(false, true)) super.setValue(value)
    }

    override fun postValue(value: T) {
        if (pending.compareAndSet(false, true)) super.postValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
}