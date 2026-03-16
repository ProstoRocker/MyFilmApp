package com.ilyadev.moviesearch.utils

import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.view.isVisible

fun View.circularReveal(
    duration: Long = 600,
    startRadius: Float = 0f,
    endRadius: Float = kotlin.math.hypot(width.toDouble(), height.toDouble()).toFloat()
) {
    val cx = width / 2
    val cy = height / 2

    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, startRadius, endRadius)
    anim.duration = duration

    // Скрываем до анимации
    isVisible = false

    // Запускаем анимацию
    anim.start()

    // Через duration показываем view (без слушателя — безопасно)
    postDelayed({
        isVisible = true
    }, duration)
}