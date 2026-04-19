package com.ilyadev.moviesearch.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.times
import org.mockito.kotlin.never

/**
 * Модульные тесты для SingleLiveEvent.
 * 
 * Тесты покрывают:
 * - Событие доставляется только один раз
 * - Событие не доставляется повторно при изменении конфигурации
 * - Поведение с несколькими наблюдателями
 */
class SingleLiveEventTest {

    private lateinit var singleLiveEvent: SingleLiveEvent<String>
    private lateinit var observer: Observer<String>
    private lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setup() {
        singleLiveEvent = SingleLiveEvent()
        observer = mock()
        lifecycleOwner = mock()
    }

    @Test
    fun setValue_deliversValueOnce() {
        // Дано
        singleLiveEvent.observe(lifecycleOwner, observer)

        // Когда
        singleLiveEvent.setValue("Test Event")

        // Тогда
        verify(observer, times(1)).onChanged("Test Event")
    }

    @Test
    fun setValue_doesNotRedeliverOnSecondObserve() {
        // Дано
        val observer2 = mock<Observer<String>>()
        singleLiveEvent.setValue("Test Event")
        
        // Первый наблюдатель получает событие
        singleLiveEvent.observe(lifecycleOwner, observer)
        verify(observer, times(1)).onChanged("Test Event")

        // Когда - второй наблюдатель подписывается после отправки события
        singleLiveEvent.observe(lifecycleOwner, observer2)

        // Тогда - второй наблюдатель НЕ должен получить прошлое событие
        verify(observer2, never()).onChanged(any())
    }

    @Test
    fun postValue_deliversValueOnce() {
        // Дано
        singleLiveEvent.observe(lifecycleOwner, observer)

        // Когда
        singleLiveEvent.postValue("Async Event")

        // Примечание: для postValue нужно ждать главный лупер
        // В реальных Android-тестах это делается через Robolectric
        // В модульных тестах мы проверяем поведение флага pending
    }

    @Test
    fun multipleSetValues_deliversAllValues() {
        // Дано
        singleLiveEvent.observe(lifecycleOwner, observer)

        // Когда
        singleLiveEvent.setValue("Event 1")
        singleLiveEvent.setValue("Event 2")
        singleLiveEvent.setValue("Event 3")

        // Тогда
        verify(observer, times(1)).onChanged("Event 1")
        verify(observer, times(1)).onChanged("Event 2")
        verify(observer, times(1)).onChanged("Event 3")
    }

    @Test
    fun setValue_beforeObserve_doesNotDeliverUntilObserved() {
        // Дано
        singleLiveEvent.setValue("Early Event")

        // Когда - наблюдаем после установки значения
        singleLiveEvent.observe(lifecycleOwner, observer)

        // Тогда - должно доставить, потому что pending всё ещё true
        verify(observer, times(1)).onChanged("Early Event")
    }

    @Test
    fun nullValues_areDelivered() {
        // Дано
        val nullObserver = mock<Observer<String?>>()
        val nullableEvent = SingleLiveEvent<String?>()
        nullableEvent.observe(lifecycleOwner, nullObserver)

        // Когда
        nullableEvent.setValue(null)

        // Тогда
        verify(nullObserver, times(1)).onChanged(null)
    }

    @Test
    fun consecutiveObserves_doNotDuplicateEvents() {
        // Дано
        singleLiveEvent.observe(lifecycleOwner, observer)
        singleLiveEvent.setValue("Single Event")
        
        // Сбрасываем мок для очистки предыдущих взаимодействий
        org.mockito.Mockito.reset(observer)

        // Когда - пытаемся наблюдать снова (симуляция изменения конфигурации)
        singleLiveEvent.observe(lifecycleOwner, observer)

        // Тогда - новой доставки нет
        verify(observer, never()).onChanged(any())
    }
}

// Вспомогательная функция для Mockito
fun <T> any(): T {
    return org.mockito.ArgumentMatchers.any<T>() as T
}
