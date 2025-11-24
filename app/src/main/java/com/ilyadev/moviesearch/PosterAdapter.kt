package com.ilyadev.moviesearch

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemPosterBinding
import android.view.animation.OvershootInterpolator

class PosterAdapter(private val posters: List<Int>) : RecyclerView.Adapter<PosterAdapter.ViewHolder>() {

    // Внутренний класс для хранения ссылок на View
    inner class ViewHolder(val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPosterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Устанавливаем изображение постера
        holder.binding.posterImage.setImageResource(posters[position])

        // Анимация при нажатии — ObjectAnimator (второй подход)
        holder.binding.root.setOnClickListener {
            val scaleX = ObjectAnimator.ofFloat(holder.binding.posterImage, "scaleX", 1f, 0.9f, 1f)
            val scaleY = ObjectAnimator.ofFloat(holder.binding.posterImage, "scaleY", 1f, 0.9f, 1f)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY)
            animatorSet.duration = 300L
            animatorSet.interpolator = OvershootInterpolator() // эффект "пружины"
            animatorSet.start()
        }
    }

    override fun getItemCount(): Int = posters.size
}