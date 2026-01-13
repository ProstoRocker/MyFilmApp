package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.android.material.appbar.MaterialToolbar
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.favorites.FavoritesFragment
import com.ilyadev.moviesearch.shared.MovieAdapterVertical
import com.ilyadev.moviesearch.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView        // ✅ Новая переменная
    private lateinit var recycler: RecyclerView
    private var isSearchVisible = true                // ✅ Для отслеживания видимости

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим RecyclerView
        recycler = findViewById(R.id.recycler_movies_vertical)

        // Находим SearchView
        searchView = findViewById(R.id.search_view)     // ✅ Добавлено

        // Создаём адаптер
        val adapter = MovieAdapterVertical { movie ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        // Передаём все фильмы в адаптер
        adapter.submitList(MovieRepository.getAllMovies())
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)

        // ➕ Настраиваем обработчик текста в поиске
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Можно выполнить действие при нажатии "Enter"
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies(newText.orEmpty())
                return true
            }
        })

        // --- 🔺 АНИМАЦИЯ СКРЫТИЯ ПРИ СКРОЛЛЕ 🔺 ---
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && isSearchVisible) {
                    // Прокрутка вниз → скрываем SearchView
                    searchView.animate().alpha(0f).setDuration(200).withEndAction {
                        searchView.visibility = View.GONE
                    }.start()
                    isSearchVisible = false
                } else if (dy < 0 && !isSearchVisible) {
                    // Прокрутка вверх → показываем SearchView
                    searchView.visibility = View.VISIBLE
                    searchView.animate().alpha(1f).setDuration(200).start()
                    isSearchVisible = true
                }
            }
        })


        // Находим Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Находим кнопки нижней навигации
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        // Обработчики кликов
        navHome.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        navFavorites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoritesFragment())
                .commit()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем обработку кнопки "Назад"
        setupOnBackPressed()
    }

    /**
     * Фильтрует фильмы по названию и жанру
     */
    private fun filterMovies(query: String) {
        val filteredList = MovieRepository.getAllMovies()
            .filter { movie ->
                movie.title.contains(query, ignoreCase = true) ||
                        movie.genre.contains(query, ignoreCase = true)
            }
        (recycler.adapter as? MovieAdapterVertical)?.submitList(filteredList)
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
}