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

/**
 * Адаптер для отображения фильмов в вертикальной карточке (например, в демо).
 *
 * Использует:
 * - ListAdapter + DiffUtil — эффективное обновление списка
 * - Встроенную анимацию: прогресс-рейтинга, появления
 * - Callback onClick для реакции на клик
 *
 * ❌ Не используется в основных экранах.
 * ✅ Только для статичного UI / моковых данных.
 */
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
            posterImage.setImageResource(movie.posterResId)
            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieDescription.text = movie.description

            tvRating.text = movie.rating.toString()

            // Анимация заполнения рейтинга
            android.animation.ValueAnimator.ofInt(0, (movie.rating * 10).toInt()).apply {
                duration = 800
                addUpdateListener { animator ->
                    progressBar.progress = animator.animatedValue as Int
                }
                start()
            }

            // Анимация появления карточки
            root.startAnimation(
                AnimationUtils.loadAnimation(root.context, R.anim.slide_in_from_bottom)
            )

            // Клик
            root.setOnClickListener { onItemClick(movie) }
        }
    }
}

// === DiffUtil ===
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}