package com.ilyadev.moviesearch.utils

import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.view.isVisible

/**
 * Расширяющий метод для View.
 *
 * Добавляет красивую анимацию появления — круговая раскрывающаяся анимация.
 *
 * Используется во всех фрагментах.
 */
fun View.circularReveal(
    duration: Long = 600,
    startRadius: Float = 0f,
    endRadius: Float = kotlin.math.hypot(width.toDouble(), height.toDouble()).toFloat()
) {
    val cx = width / 2
    val cy = height / 2

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, startRadius, endRadius)
    anim.duration = duration

    isVisible = false
    anim.start()

    postDelayed({ isVisible = true }, duration)
}