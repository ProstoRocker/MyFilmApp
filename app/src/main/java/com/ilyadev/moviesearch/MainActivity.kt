package com.ilyadev.moviesearch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilyadev.moviesearch.ui.home.HomeFragment
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // Загружаем HomeFragment при старте
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Обработка нажатий в нижней навигации
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_favorites -> FavoritesFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> return@setOnItemSelectedListener false
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            true
        }
    }

    /**
     * Переопределение кнопки "Назад"
     * Если есть фрагменты в стеке — назад
     * Если на главном экране — спрашиваем, хочет ли пользователь выйти
     */
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager

        // Если в стеке есть фрагменты (например, DetailFragment) — просто возвращаемся назад
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
        // Если это корневой уровень — спрашиваем подтверждение
        else {
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выйти из приложения?")
            .setMessage("Вы действительно хотите закрыть приложение?")
            .setPositiveButton("Да") { _, _ ->
                finish() // Закрываем активити и приложение
            }
            .setNegativeButton("Нет", null) // Просто закрываем диалог
            .setCancelable(true)
            .show()
    }
}