package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.ItemMovieVerticalBinding
import com.ilyadev.moviesearch.model.Movie

class MovieAdapterVertical(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapterVertical.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(val binding: ItemMovieVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieVerticalBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.binding.apply {
            // --- 1. Загрузка данных ---
            posterImage.setImageResource(movie.posterResId)
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieDescription.text = movie.description

            // --- 2. Установка рейтинга ---
            tvRating.text = movie.rating.toString()

            // 🔥 Здесь — обработка клика по всей карточке
            root.setOnClickListener {
                onItemClick(movie)  // ← Вызывает переданный callback
            }

            // Сброс прогресса
            progressBar.progress = 0
            progressBar.alpha = 0f

            // Анимация появления
            progressBar.animate()
                .alpha(1f)
                .setDuration(400)
                .start()

            // Анимация заполнения
            android.animation.ValueAnimator.ofInt(0, (movie.rating * 10).toInt()).apply {
                duration = 800
                addUpdateListener { animator ->
                    progressBar.progress = animator.animatedValue as Int
                }
                start()
            }

            // --- 3. Анимация появления карточки ---
            root.startAnimation(
                AnimationUtils.loadAnimation(root.context, R.anim.slide_in_from_bottom)
            )

            // --- 4. Клик по карточке ---
            root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }
}

// DiffUtil Callback
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}