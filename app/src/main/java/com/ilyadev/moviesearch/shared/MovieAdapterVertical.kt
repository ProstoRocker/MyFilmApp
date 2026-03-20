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
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Адаптер для вертикального списка фильмов
 */
class MovieAdapterVertical(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapterVertical.MovieViewHolder>(MovieDiffCallback()) {

    /**
     * ViewHolder, хранящий ссылки на View одного элемента
     */
    inner class MovieViewHolder(val binding: ItemMovieVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Создаёт новый ViewHolder из макета item_movie_vertical.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieVerticalBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    /**
     * Привязывает данные фильма к UI
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.binding.apply {
            // --- 1. Загрузка данных ---
            posterImage.setImageResource(movie.posterResId)
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieDescription.text = movie.description

            // --- 2. Установка рейтинга ---
            val ratingText = root.findViewById<TextView>(R.id.tv_rating)
            val progressBar = root.findViewById<ProgressBar>(R.id.progress_bar)

            if (ratingText != null && progressBar != null) {
                val progress = (movie.rating * 10).toInt().coerceIn(0, 100)
                ratingText.text = movie.rating.toString()

                // Сначала 0
                progressBar.progress = 0
                progressBar.alpha = 0f

                // Анимация появления + заполнения
                progressBar.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .start()

                // Анимация заполнения
                android.animation.ValueAnimator.ofInt(0, progress).apply {
                    duration = 800
                    addUpdateListener { animator ->
                        progressBar.progress = animator.animatedValue as Int
                    }
                    start()
                }
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

/**
 * Класс для сравнения фильмов при обновлении списка (DiffCallback)
 */
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}