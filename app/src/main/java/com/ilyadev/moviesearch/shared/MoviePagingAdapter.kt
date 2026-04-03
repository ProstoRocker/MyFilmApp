package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.ItemMovieBinding
import com.ilyadev.moviesearch.model.MovieDto

class MoviePagingAdapter(
    private val onClick: (MovieDto) -> Unit,
    private val onLongClick: (MovieDto) -> Unit = {}
) : PagingDataAdapter<MovieDto, MoviePagingAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int, item: MovieDto) {
        holder.bind(item, onClick, onLongClick)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movie: MovieDto,
            onClick: (MovieDto) -> Unit,
            onLongClick: (MovieDto) -> Unit
        ) {
            binding.apply {
                movieTitle.text = movie.title
                movieYear.text = movie.releaseDate.take(4).ifEmpty { "--" }
                ratingText.text = "%.1f".format(movie.voteAverage)

                // Загрузка постера через Glide
                if (movie.posterPath != null) {
                    val imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                    GlideApp.with(itemView)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(posterImage)
                } else {
                    posterImage.setImageResource(R.drawable.ic_placeholder)
                }

                itemView.setOnClickListener { onClick(movie) }
                itemView.setOnLongClickListener {
                    onLongClick(movie)
                    true // consumed
                }
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

class MovieDiffCallback : DiffUtil.ItemCallback<MovieDto>() {
    override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean {
        return oldItem == newItem
    }
}