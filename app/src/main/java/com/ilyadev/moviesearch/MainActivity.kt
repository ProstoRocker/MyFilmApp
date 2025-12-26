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
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.favorites.FavoritesFragment
import com.ilyadev.moviesearch.shared.MovieAdapterVertical
import com.ilyadev.moviesearch.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим RecyclerView для вертикального списка фильмов
        val recycler = findViewById<RecyclerView>(R.id.recycler_movies_vertical)

        // Создаём список фильмов
        val movies = MovieRepository.getAllMovies()

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

        // --- ОБРАБОТЧИКИ КЛИКОВ ---

        // Кнопка "Главная"
        navHome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Кнопка "Избранное"
        navFavorites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoritesFragment())
                .commit()
        }

        // Кнопка "Настройки"
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

}