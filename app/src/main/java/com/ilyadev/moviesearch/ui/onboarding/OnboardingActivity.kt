package com.ilyadev.moviesearch.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ilyadev.moviesearch.MainActivity
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.utils.FirstLaunchManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

/**
 * Приветственный экран, показывается при первом запуске.
 *
 * Логика:
 * - Проверяет FirstLaunchManager.isFirstLaunch()
 * - Загружает промо-изображение из Firebase Remote Config
 * - При клике — открывает DetailActivity
 * - После просмотра переходит в MainActivity
 */

class OnboardingActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var progressBar: CircularProgressIndicator
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        imageView = findViewById(R.id.iv_promo_poster)
        progressBar = findViewById(R.id.progress_circular)

        remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().build())
            setDefaultsAsync(R.xml.remote_config_defaults) // опционально
        }

        loadPromoImage()
    }

    private fun loadPromoImage() {
        showLoading(true)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val imageUrl = remoteConfig.getString("promo_poster_url")
                    val movieId = remoteConfig.getLong("promo_movie_id").toInt()

                    if (imageUrl.isNotEmpty()) {
                        imageView.isVisible = true
                        Picasso.get()
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_error)
                            .into(imageView)

                        // Клик → переход на фильм
                        imageView.setOnClickListener {
                            val intent = Intent(this, DetailActivity::class.java)
                            intent.putExtra("movie_id", movieId)
                            startActivity(intent)
                        }
                    } else {
                        finishMainFlow()
                    }
                } else {
                    Toast.makeText(this, "Не удалось загрузить конфиг", Toast.LENGTH_SHORT).show()
                    finishMainFlow()
                }
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.isVisible = show
        imageView.isVisible = !show
    }

    private fun finishMainFlow() {
        lifecycleScope.launch {
            FirstLaunchManager.markAsLaunched(this@OnboardingActivity)
            startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        // Нельзя вернуться назад — только просмотреть или пропустить
        finishMainFlow()
    }
}