package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Настраиваем Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 2. Инициализируем RecyclerView
        val recycler = findViewById<RecyclerView>(R.id.recycler_movies)

        // Создаём список фильмов
        val movies = createMockMovies()

        // Создаём адаптер с обработкой клика
        val adapter = MovieAdapter { movie ->
            // При клике на фильм — переходим в DetailActivity
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        // Передаём данные в адаптер
        adapter.submitList(movies)

        // Устанавливаем LayoutManager и Adapter
        recycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recycler.adapter = adapter

        // 3. Нижняя панель навигации (ImageButton)
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navCollections = findViewById<ImageButton>(R.id.nav_collections)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        navHome.setOnClickListener {
            Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show()
        }

        navFavorites.setOnClickListener {
            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
        }

        navCollections.setOnClickListener {
            Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Возвращает список тестовых фильмов (mock data)
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