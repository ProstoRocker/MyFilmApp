package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.ilyadev.moviesearch.ui.home.HomeFragment
import com.ilyadev.moviesearch.ui.watchlater.WatchLaterFragment
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.collections.CollectionsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настройка Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Нижняя навигация
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navWatchLater = findViewById<ImageButton>(R.id.nav_watch_later)
        val navCollections = findViewById<ImageButton>(R.id.nav_collections)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        // Переходы с анимацией
        navHome.setOnClickListener {
            replaceFragment(HomeFragment(), R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }

        navFavorites.setOnClickListener {
            replaceFragment(
                FavoritesFragment(),
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
        }

        navWatchLater.setOnClickListener {
            replaceFragment(
                WatchLaterFragment(),
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left
            )
        }

        navCollections.setOnClickListener {
            replaceFragment(
                CollectionsFragment(),
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
        }

        navSettings.setOnClickListener {
            android.widget.Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }

        // Обработка кнопки "Назад"
        setupOnBackPressed()
    }

    /**
     * Универсальная функция для замены фрагмента с анимацией
     */
    private fun replaceFragment(
        fragment: Fragment,
        enterAnimation: Int,
        exitAnimation: Int
    ) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Диалог выхода из приложения
     */
    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Закрыть приложение?")
            .setPositiveButton("Да") { _, _ -> finish() }
            .setNegativeButton("Нет", null)
            .show()
    }
}