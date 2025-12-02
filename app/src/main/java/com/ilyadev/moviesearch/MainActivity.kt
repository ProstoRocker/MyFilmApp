package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим RecyclerView для вертикального списка фильмов
        val recycler = findViewById<RecyclerView>(R.id.recycler_movies_vertical)

        // Создаём список фильмов
        val movies = createMockMovies()

        // Создаём адаптер с обработчиком клика
        val adapter = MovieAdapterVertical { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        // Передаём данные в адаптер
        adapter.submitList(movies)

        // Настраиваем RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // Находим Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Находим кнопки нижней навигации
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        // Обработчики кликов по нижней панели
        navHome.setOnClickListener {
            Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show()
        }

        navFavorites.setOnClickListener {
            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем обработку кнопки "Назад"
        setupOnBackPressed()
    }

    /**
     * Настраивает поведение при нажатии кнопки "Назад"
     */
    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    /**
     * Показывает диалог подтверждения выхода из приложения
     */
    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выйти из приложения?")
            .setMessage("Вы действительно хотите закрыть приложение?")
            .setPositiveButton("Да") { _, _ -> finish() }
            .setNegativeButton("Нет", null)
            .show()
    }

    /**
     * Возвращает список мок-данных о фильмах
     */
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