package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.ItemMovieBinding
import com.ilyadev.moviesearch.model.MovieDto
import com.bumptech.glide.Glide

/**
 * Стандартный адаптер для списка фильмов.
 *
 * Отличается от PagingDataAdapter тем, что:
 * - Работает с обычным List<MovieDto>
 * - Используется в избранном, подборках и т.п.
 *
 * Поддерживает:
 * - Клик и долгий клик
 * - Загрузку постера через Glide
 * - Форматирование даты и рейтинга
 */
class MovieListAdapter(
    private val onClick: (MovieDto) -> Unit,
    private val onLongClick: (MovieDto) -> Unit = {}
) : ListAdapter<MovieDto, MovieListAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick, onLongClick)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto, onClick: (MovieDto) -> Unit, onLongClick: (MovieDto) -> Unit) {
            binding.movieTitle.text = movie.title
            binding.movieYear.text = movie.releaseDate.take(4).ifEmpty { "--" }
            binding.ratingText.text = "%.1f".format(movie.voteAverage)

            if (movie.posterPath != null) {
                val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                Glide.with(binding.root)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(binding.posterImage)
            } else {
                binding.posterImage.setImageResource(R.drawable.ic_placeholder)
            }

            binding.root.setOnClickListener { onClick(movie) }
            binding.root.setOnLongClickListener {
                onLongClick(movie)
                true
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieDto>() {
            override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
                return oldItem == newItem
            }
        }
    }
}