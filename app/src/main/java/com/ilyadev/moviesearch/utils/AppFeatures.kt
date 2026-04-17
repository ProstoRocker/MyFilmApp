package com.ilyadev.moviesearch.utils

import com.ilyadev.moviesearch.BuildConfig

object AppFeatures {

    /**
     * Проверяет, доступен ли экран "Избранное"
     *
     * Доступно если:
     * - Это платная версия (flavor == "paid")
     * - ИЛИ пробный период ещё активен
     */
    suspend fun isFavoritesAvailable(): Boolean =
        BuildConfig.FLAVOR == "paid" || TrialManager.isTrialActive()

    /**
     * Проверяет, доступен ли экран "Подборки"
     */
    suspend fun isCollectionsAvailable(): Boolean =
        BuildConfig.FLAVOR == "paid" || TrialManager.isTrialActive()
}
