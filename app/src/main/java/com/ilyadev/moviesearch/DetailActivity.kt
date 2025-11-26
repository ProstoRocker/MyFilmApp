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
        findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.tv_description).text =
            movie.description

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
                posterResId = R.drawable.poster_matrix,
                backdropResId = R.drawable.backdrop_matrix,
                description = "Нео узнаёт, что реальность — это иллюзия, созданная машинами."
            ),
            Movie(
                id = 2,
                title = "Оппенгеймер",
                year = "2023",
                rating = 8.3,
                genre = "Биография, Драма, История",
                posterResId = R.drawable.poster_oppenheimer,
                backdropResId = R.drawable.backdrop_oppenheimer,
                description = "История Роберта Оппенгеймера, отца атомной бомбы, и моральных дилемм, связанных с научным прогрессом."
            ),
            Movie(
                id = 3,
                title = "Интерстеллар",
                year = "2014",
                rating = 8.6,
                genre = "Драма",
                posterResId = R.drawable.poster_interstellar,
                backdropResId = R.drawable.backdrop_interstellar,
                description = "Астронавты ищут новую планету для человечества."
            ),
            Movie(
                id = 4,
                title = "Тёмный рыцарь",
                year = "2008",
                rating = 9.0,
                genre = "Экшен",
                posterResId = R.drawable.poster_dark_knight,
                backdropResId = R.drawable.backdrop_dark_knight,
                description = "Бэтмен против Джокера в битве за душу Готэма."
            ),
            Movie(
                id = 5,
                title = "Форрест Гамп",
                year = "1994",
                rating = 8.8,
                genre = "Драма",
                posterResId = R.drawable.poster_forrest_gump,
                backdropResId = R.drawable.backdrop_forrest_gump,
                description = "Жизнь простого человека в эпоху великих перемен."
            ),
            Movie(
                id = 6,
                title = "Побег из Шоушенка",
                year = "1994",
                rating = 9.3,
                genre = "Драма",
                posterResId = R.drawable.poster_shawshank,
                backdropResId = R.drawable.backdrop_shawshank,
                description = "Надежда и дружба в тюрьме, лишённой свободы."
            ),
            Movie(
                id = 7,
                title = "Крёстный отец",
                year = "1972",
                rating = 9.2,
                genre = "Криминал",
                posterResId = R.drawable.poster_godfather,
                backdropResId = R.drawable.backdrop_godfather,
                description = "Семья мафии и путь превращения в крёстного отца."
            )
        )
    }
}