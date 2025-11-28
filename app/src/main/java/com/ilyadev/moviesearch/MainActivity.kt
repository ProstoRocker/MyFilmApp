package com.ilyadev.moviesearch

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.home.HomeFragment
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
    class MainActivity : AppCompatActivity() {

        private lateinit var bottomNav: BottomNavigationView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            bottomNav = findViewById(R.id.bottom_navigation)

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }

            setupBottomNavigation()

            // Настраиваем обработку кнопки "Назад"
            setupOnBackPressed()
        }

        private fun setupBottomNavigation() {
            bottomNav.setOnItemSelectedListener { item ->
                val fragment = when (item.itemId) {
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

        private fun setupOnBackPressed() {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    } else {
                        showExitDialog()
                    }
                }
            }
            onBackPressedDispatcher.addCallback(this, callback)
        }

        private fun showExitDialog() {
            AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите закрыть приложение?")
                .setPositiveButton("Да") { _, _ -> finish() }
                .setNegativeButton("Нет", null)
                .show()
        }
    }
}