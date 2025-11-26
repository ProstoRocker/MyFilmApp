package com.ilyadev.moviesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemMovieBinding

/**
 * Адаптер для отображения списка фильмов в RecyclerView.
 * Использует View Binding и DiffUtil для оптимального обновления.
 */
class MovieAdapter(
    private val onItemClick: (Movie) -> Unit  // Функция-колбэк при клике
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    /**
     * Вьюхолдер, хранящий ссылки на View одного элемента
     */
    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Создаёт новый ViewHolder из макета item_movie.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    /**
     * Привязывает данные фильма к UI
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position) // ListAdapter сам управляет списком

        holder.binding.apply {
            posterImage.setImageResource(movie.posterResId)
            movieTitle.text = movie.title
            movieYear.text = movie.year

            // Обработчик клика по карточке
            root.setOnClickListener {
                onItemClick(movie) // Вызываем переданный колбэк
            }
        }
    }
}

/**
 * Класс для сравнения фильмов при обновлении списка
 * Нужен для эффективной работы ListAdapter + DiffUtil
 */
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    /**
     * Сравнивает, являются ли два объекта одним и тем же фильмом (по ID)
     */
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Сравнивает, изменилось ли содержимое фильма
     */
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}