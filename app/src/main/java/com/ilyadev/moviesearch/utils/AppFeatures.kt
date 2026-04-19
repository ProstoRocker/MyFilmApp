package com.ilyadev.moviesearch.utils

import com.ilyadev.moviesearch.BuildConfig

/**
 * Централизованная логика доступа к функциям приложения.
 *
 * Определяет, доступны ли платные функции:
 * - "Избранное"
 * - "Подборки"
 *
 * Зависит от:
 * - Текущего flavor (free / paid)
 * - Статуса пробного периода (через TrialManager)
 */
object AppFeatures {

    /**
     * Проверяет, можно ли использовать экран "Избранное".
     *
     * Доступно если:
     * - Это платная версия (`paid`)
     * - ИЛИ пробный период ещё активен
     *
     * @return true — доступ разрешён, false — показать диалог
     */
    suspend fun isFavoritesAvailable(): Boolean =
        BuildConfig.FLAVOR == "paid" || TrialManager.isTrialActive()

    /**
     * Проверяет доступ к экрану "Подборки".
     *
     * Аналогично isFavoritesAvailable().
     */
    suspend fun isCollectionsAvailable(): Boolean =
        BuildConfig.FLAVOR == "paid" || TrialManager.isTrialActive()
}