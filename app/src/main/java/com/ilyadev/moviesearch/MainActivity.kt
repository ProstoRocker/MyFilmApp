package com.ilyadev.moviesearch

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.ilyadev.moviesearch.favorites.FavoritesFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Находим Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Находим кнопки нижней навигации
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        // --- ПЕРЕХОДЫ МЕЖДУ ФРАГМЕНТАМИ С АНИМАЦИЕЙ ---
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

        navSettings.setOnClickListener {
            android.widget.Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем обработку кнопки "Назад"
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
     * Обработка кнопки "Назад"
     */
    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    /**
     * Диалог выхода из приложения
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