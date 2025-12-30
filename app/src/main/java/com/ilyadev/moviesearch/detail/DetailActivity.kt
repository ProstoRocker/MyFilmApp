package com.ilyadev.moviesearch.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.MovieRepository
import com.ilyadev.moviesearch.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Получаем ID фильма
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            Toast.makeText(this, "Ошибка загрузки фильмы", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Находим фильм
        val movie = MovieRepository.getMovieById(movieId)
        if (movie == null) {
            Toast.makeText(this, "Фильм не найден", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Устанавливаем данные
        findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).title = movie.title
        findViewById<AppCompatImageView>(R.id.iv_backdrop)
            .setImageResource(movie.backdropResId)
        findViewById<AppCompatTextView>(R.id.tv_title).text = movie.title
        findViewById<AppCompatTextView>(R.id.tv_year).text = movie.year
        findViewById<AppCompatTextView>(R.id.tv_description).text = movie.description

        // --- КНОПКА «В ИЗБРАННОЕ» ---
        val btnFavorite = findViewById<Button>(R.id.btn_favorite)

        // Обновляем внешний вид кнопки в зависимости от состояния
        updateFavoriteButton(btnFavorite, movie.id)

        btnFavorite.setOnClickListener {
            if (MovieRepository.isFavorite(movie.id)) {
                MovieRepository.removeFromFavorites(movie.id)
                Snackbar.make(it, "Удалено из избранного", Snackbar.LENGTH_SHORT).show()
            } else {
                MovieRepository.addToFavorites(movie)
                Snackbar.make(it, "Добавлено в избранное", Snackbar.LENGTH_SHORT).show()
            }
            // Обновляем кнопку
            updateFavoriteButton(btnFavorite, movie.id)
        }
        // --- КОНЕЦ КНОПКИ «В ИЗБРАННОЕ» ---

        // --- КНОПКА «ПОДЕЛИТЬСЯ» ---
        findViewById<Button>(R.id.btn_share).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Смотри фильм: ${movie.title} — отличный выбор!")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
        }
        // --- КОНЕЦ КНОПКИ «ПОДЕЛИТЬСЯ» ---

        // --- КНОПКА «ПОСМОТРЕТЬ ПОЗЖЕ» ---
        findViewById<Button>(R.id.btn_watch_later).setOnClickListener {
            Snackbar.make(it, "Добавлено в «Посмотреть позже»", Snackbar.LENGTH_SHORT).show()
        }
        // --- КОНЕЦ КНОПКИ «ПОСМОТРЕТЬ ПОЗЖЕ» ---
    }

    /**
     * Обновляет текст и цвет кнопки "В избранное" в зависимости от состояния
     */
    private fun updateFavoriteButton(button: Button, movieId: Int) {
        if (MovieRepository.isFavorite(movieId)) {
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
