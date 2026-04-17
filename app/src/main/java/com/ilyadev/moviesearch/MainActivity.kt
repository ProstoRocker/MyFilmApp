package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ilyadev.moviesearch.databinding.ActivityMainBinding
import com.ilyadev.moviesearch.ui.collections.CollectionsFragment
import com.ilyadev.moviesearch.ui.favorites.FavoritesFragment
import com.ilyadev.moviesearch.ui.home.HomeFragment
import com.ilyadev.moviesearch.ui.onboarding.OnboardingActivity
import com.ilyadev.moviesearch.ui.watchlater.WatchLaterFragment
import com.ilyadev.moviesearch.utils.AppFeatures
import kotlinx.coroutines.launch

/**
 * Основной экран приложения — точка входа после Splash и Onboarding.
 *
 * Отвечает за:
 * - Проверку первого запуска (переход в OnboardingActivity)
 * - Управление навигацией через нижнее меню
 * - Блокировку функций в бесплатной версии
 * - Обработку выхода из приложения
 *
 * Использует современные практики:
 * - repeatOnLifecycle + lifecycleScope для безопасных корутин
 * - ViewBinding для доступа к UI
 * - Fragment Transactions с анимациями
 */
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем: это первый запуск?
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (com.ilyadev.moviesearch.utils.FirstLaunchManager.isFirstLaunch(this@MainActivity)) {
                    // Переходим на экран приветствия
                    startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                    finish()
                    return@repeatOnLifecycle
                }

                // Если не первый запуск — создаём основной интерфейс
                initializeUI()
            }
        }
    }

    /**
     * Инициализирует UI: биндинг, toolbar, обработчики кликов.
     * Вызывается только после проверки первого запуска.
     */
    private fun initializeUI() {
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

    /**
     * Заменяет текущий фрагмент с анимацией.
     * @param fragment — новый фрагмент
     * @param enterAnimation — анимация появления
     * @param exitAnimation — анимация исчезновения
     */
    private fun replaceFragment(fragment: Fragment, enterAnimation: Int, exitAnimation: Int) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimation, exitAnimation)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Показывает диалог с сообщением о блокировке функции.
     * Используется при попытке открыть недоступный экран в бесплатной версии.
     */
    private fun showUpgradeDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Ограничение функционала")
            .setMessage("$message\nПопробуйте платную версию или дождитесь окончания пробного периода.")
            .setPositiveButton("OK", null)
            .show()
    }

    /**
     * Настройка обработчика нажатия кнопки "Назад"
     * Показывает диалог подтверждения выхода.
     */
    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
    }

    /**
     * Диалог подтверждения выхода из приложения.
     */
    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Закрыть приложение?")
            .setPositiveButton("Да") { _, _ -> finish() }
            .setNegativeButton("Нет", null)
            .show()
    }

    /**
     * Очистка ссылки на ViewBinding при уничтожении Activity.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}