package com.ilyadev.moviesearch

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ilyadev.moviesearch.databinding.ActivityMainBinding
import com.ilyadev.moviesearch.ui.collections.CollectionsFragment
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.home.HomeFragment
import com.ilyadev.moviesearch.ui.watchlater.WatchLaterFragment
import com.ilyadev.moviesearch.utils.AppFeatures
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // === Нижняя навигация с проверкой доступа ===

        binding.navHome.setOnClickListener {
            replaceFragment(HomeFragment(), R.anim.slide_in_from_right, R.anim.slide_out_to_left)
        }

        binding.navFavorites.setOnClickListener {
            lifecycleScope.launch {
                if (AppFeatures.isFavoritesAvailable()) {
                    replaceFragment(
                        FavoritesFragment(),
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right
                    )
                } else {
                    showUpgradeDialog("«Избранное» доступно в платной версии")
                }
            }
        }

        binding.navWatchLater.setOnClickListener {
            replaceFragment(
                WatchLaterFragment(),
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left
            )
        }

        binding.navCollections.setOnClickListener {
            lifecycleScope.launch {
                if (AppFeatures.isCollectionsAvailable()) {
                    replaceFragment(
                        CollectionsFragment(),
                        R.anim.slide_in_from_left,
                        R.anim.slide_out_to_right
                    )
                } else {
                    showUpgradeDialog("«Подборки» доступны в платной версии")
                }
            }
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

    /**
     * Показывает диалог при попытке открыть заблокированную функцию.
     */
    private fun showUpgradeDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ограничение функционала")
            .setMessage("$message\nПопробуйте платную версию или дождитесь окончания пробного периода.")
            .setPositiveButton("OK", null)
            .show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}