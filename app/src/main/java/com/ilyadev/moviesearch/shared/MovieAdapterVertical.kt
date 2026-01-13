package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemMovieVerticalBinding
import com.ilyadev.moviesearch.model.Movie

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
            posterImage.setImageDrawable(null) // или .setImageResource(0)
            posterImage.setImageResource(movie.posterResId)

            movieTitle.text = movie.title
            movieYear.text = movie.year
            movieDescription.text = movie.description

            root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }
}

/**
 * Класс для сравнения фильмов при обновлении списка
 */
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}