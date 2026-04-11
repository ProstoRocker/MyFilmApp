package com.ilyadev.moviesearch.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.di.AppApplication
import com.ilyadev.moviesearch.model.MovieDto
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var backdropImage: AppCompatImageView
    private lateinit var titleText: AppCompatTextView
    private lateinit var yearText: AppCompatTextView
    private lateinit var descriptionText: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupToolbar()
        bindViews()
        initViewModel()

        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            Toast.makeText(this, "Ошибка: неверный ID фильма", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadMovie(movieId)
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun bindViews() {
        backdropImage = findViewById(R.id.iv_backdrop)
        titleText = findViewById(R.id.tv_title)
        yearText = findViewById(R.id.tv_year)
        descriptionText = findViewById(R.id.tv_description)
    }

    private fun initViewModel() {
        val appComponent = (application as AppApplication).appComponent
        val apiService = appComponent.apiService()
        viewModel = DetailViewModel(apiService)
        observeViewModel()
    }

    private fun loadMovie(id: Int) {
        viewModel.loadMovie(id)
    }

    private fun observeViewModel() {
        viewModel.movie.observe(this) { movie ->
            if (movie != null) {
                showMovie(movie)
            } else {
                Toast.makeText(this, "Фильм не найден", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.isLoading.observe(this) { /* можно показать прогресс */ }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, "Ошибка: $error", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun showMovie(movie: MovieDto) {
        findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).title = movie.title

        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.backdropPath}"
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_error)
            .fit()
            .centerCrop()
            .into(backdropImage)

        titleText.text = movie.title
        yearText.text = movie.releaseDate.take(4)
        descriptionText.text = movie.overview ?: "Описание отсутствует"

        setupButtons(movie)
    }

    private fun setupButtons(movie: MovieDto) {
        val btnFavorite = findViewById<android.widget.Button>(R.id.btn_favorite)
        updateFavoriteButton(btnFavorite, movie.id) // ✅ Передаём ID

        btnFavorite.setOnClickListener {
            if (viewModel.isFavorite(movie.id)) { // ✅ Только ID
                viewModel.removeFromFavorites(movie)
                Snackbar.make(it, "Удалено из избранного", Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.addToFavorites(movie)
                Snackbar.make(it, "Добавлено в избранное", Snackbar.LENGTH_SHORT).show()
            }
            updateFavoriteButton(btnFavorite, movie.id)
        }

        findViewById<android.widget.Button>(R.id.btn_share).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Смотри фильм: ${movie.title} — отличный выбор!")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
        }

        findViewById<android.widget.Button>(R.id.btn_watch_later).setOnClickListener {
            viewModel.addWatchLater(movie)
            Snackbar.make(it, "Добавлено в «Посмотреть позже»", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteButton(button: android.widget.Button, movieId: Int) {
        if (viewModel.isFavorite(movieId)) {
            button.text = "Удалить из избранного"
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.red_500))
        } else {
            button.text = "В избранное"
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}