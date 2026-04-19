package com.ilyadev.moviesearch

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemPosterBinding
import android.view.animation.OvershootInterpolator

/**
 * Адаптер для отображения списка постеров (например, в демо-экране).
 *
 * Отвечает за:
 * - Привязку данных к элементам списка (ImageView)
 * - Визуальную анимацию при нажатии
 * - Построение ViewHolder через ViewBinding
 *
 * 🔔 Обратите внимание: используется только для статичного списка R.drawable.
 * В реальных экранах (Home, Search) используется MoviePagingAdapter — он работает с данными из API и поддерживает пагинацию.
 */
class PosterAdapter(private val posters: List<Int>) :
    RecyclerView.Adapter<PosterAdapter.ViewHolder>() {

    // === ViewHolder — хранит ссылки на View одного элемента ===
    inner class ViewHolder(val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Создаёт новый ViewHolder.
     * Вызывается, когда RecyclerView нуждается в новом ViewHolder (не для каждого элемента!).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Инфлейтим layout для одного элемента списка
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPosterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    /**
     * Привязывает данные к конкретному ViewHolder.
     * Вызывается при появлении элемента на экране.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Устанавливаем изображение из ресурсов (R.drawable.id)
        holder.binding.posterImage.setImageResource(posters[position])

        // Добавляем анимацию масштабирования при клике
        holder.binding.root.setOnClickListener {
            // Анимация изменения scaleX и scaleY
            val scaleX = ObjectAnimator.ofFloat(holder.binding.posterImage, "scaleX", 1f, 0.9f, 1f)
            val scaleY = ObjectAnimator.ofFloat(holder.binding.posterImage, "scaleY", 1f, 0.9f, 1f)

            // Группируем анимации
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.duration = 300L
            animatorSet.interpolator = OvershootInterpolator() // эффект "пружины"
            animatorSet.start()
        }
    }

    /**
     * Возвращает количество элементов в списке.
     * Необходимо для работы RecyclerView.
     */
    override fun getItemCount(): Int = posters.size
}