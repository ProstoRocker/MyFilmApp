package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.ItemMovieVerticalBinding
import com.ilyadev.moviesearch.model.MovieDto
import com.bumptech.glide.Glide

class MoviePagingAdapter(
    private val onItemClick: (MovieDto) -> Unit  // ← Добавляем параметр
) : PagingDataAdapter<MovieDto, MoviePagingAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieVerticalBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie, onItemClick)
        }
    }

    inner class MovieViewHolder(private val binding: ItemMovieVerticalBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto, clickListener: (MovieDto) -> Unit) {
            binding.movieTitle.text = movie.title
            binding.movieYear.text = movie.releaseDate.take(4).ifEmpty { "—" }
            binding.tvRating.text = movie.rating.toString()

            val imageUrl = movie.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
            if (imageUrl != null) {
                Glide.with(binding.root)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(binding.posterImage)
            } else {
                binding.posterImage.setImageResource(R.drawable.ic_placeholder)
            }

            binding.root.setOnClickListener { clickListener(movie) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieDto>() {
            override fun areItemsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: MovieDto, newItem: MovieDto): Boolean =
                oldItem == newItem
        }
    }
}