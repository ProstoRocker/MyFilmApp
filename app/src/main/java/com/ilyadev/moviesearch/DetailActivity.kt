package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Получаем ID фильма
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            finish()
            return
        }

        // Находим фильм по ID
        val movie = createMockMovies().find { it.id == movieId }
        if (movie == null) {
            finish()
            return
        }

        // Устанавливаем данные
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbar.title = movie.title

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).title = movie.title

        findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.iv_backdrop)
            .setImageResource(movie.backdropResId)

        findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_title).text = movie.title
        findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_year).text = movie.year
        findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_description).text = movie.description

        // Кнопки
        findViewById<Button>(R.id.btn_favorite).setOnClickListener {
            Snackbar.make(it, "Добавлено в избранное", Snackbar.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_watch_later).setOnClickListener {
            Snackbar.make(it, "Добавлено в «Посмотреть позже»", Snackbar.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_share).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Смотри фильм: ${movie.title}")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться"))
        }
    }

    private fun createMockMovies(): List<Movie> {
        return listOf(
            Movie(
                id = 1,
                title = "Матрица",
                year = "1999",
                rating = 8.7,
                genre = "Научная фантастика",
                posterResId = R.drawable.poster_1,
                backdropResId = R.drawable.poster_3,
                description = "Нео узнаёт, что реальность — это иллюзия, созданная машинами."
            ),
            Movie(
                id = 2,
                title = "Начало",
                year = "2010",
                rating = 8.8,
                genre = "Триллер",
                posterResId = R.drawable.poster_2,
                backdropResId = R.drawable.poster_4,
                description = "Команда внедряется в сны, чтобы украсть или внедрить идею."
            ),
            // ... остальные фильмы
        )
    }
}