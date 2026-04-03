package com.ilyadev.moviesearch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ilyadev.moviesearch.databinding.ActivityMainBinding
import com.ilyadev.moviesearch.ui.collections.CollectionsFragment
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.home.HomeFragment
import com.ilyadev.moviesearch.ui.watchlater.WatchLaterFragment


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        // Настройка Toolbar
        setSupportActionBar(binding.toolbar)

        // Нижняя навигация — теперь через binding
        binding.navHome.setOnClickListener {
            replaceFragment(HomeFragment(), R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }

        binding.navFavorites.setOnClickListener {
            replaceFragment(
                FavoritesFragment(),
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
        }

        binding.navWatchLater.setOnClickListener {
            replaceFragment(
                WatchLaterFragment(),
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left
            )
        }

        binding.navCollections.setOnClickListener {
            replaceFragment(
                CollectionsFragment(),
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            )
        }

        binding.navSettings.setOnClickListener {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }

        setupOnBackPressed()
    }

    private fun replaceFragment(fragment: Fragment, enterAnimation: Int, exitAnimation: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

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