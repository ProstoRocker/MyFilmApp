package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemMovieVerticalBinding
import com.ilyadev.moviesearch.model.MovieDto

class MoviePagingAdapter(
    private val onItemClick: (MovieDto) -> Unit
) : PagingDataAdapter<MovieDto, MoviePagingAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    inner class MovieViewHolder(val binding: ItemMovieVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieVerticalBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position) ?: return
        holder.binding.apply {
            movieTitle.text = movie.title
            movieYear.text = movie.releaseDate.take(4).ifEmpty { "—" }
            tvRating.text = movie.rating.toString()

            root.setOnClickListener { onItemClick(movie) }
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