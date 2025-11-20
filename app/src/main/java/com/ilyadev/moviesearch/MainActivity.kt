package com.ilyadev.moviesearch

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Настраиваем Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 2. Инициализируем RecyclerView для постеров
        val recycler = findViewById<RecyclerView>(R.id.recycler_posters)

        // Список изображений (постеры)
        val posters = listOf(
            R.drawable.poster_1,
            R.drawable.poster_2,
            R.drawable.poster_3,
            R.drawable.poster_4,
            R.drawable.poster_5
        )

        // Создаём адаптер
        val adapter = PosterAdapter(posters)

        // Анимация появления: каждый постер "вплывает"
        recycler.post {
            for (i in 0 until recycler.childCount) {
                val child = recycler.getChildAt(i)
                child.alpha = 0f
                child.translationY = 50f
                child.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500L)
                    .setStartDelay(i * 100L)
                    .start()
            }
        }

        // Устанавливаем LayoutManager и Adapter
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        // 3. Нижняя панель навигации (ImageButton)
        val navHome = findViewById<ImageButton>(R.id.nav_home)
        val navFavorites = findViewById<ImageButton>(R.id.nav_favorites)
        val navCollections = findViewById<ImageButton>(R.id.nav_collections)
        val navSettings = findViewById<ImageButton>(R.id.nav_settings)

        navHome.setOnClickListener {
            Toast.makeText(this, "Главная", Toast.LENGTH_SHORT).show()
        }

        navFavorites.setOnClickListener {
            Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
        }

        navCollections.setOnClickListener {
            Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
        }

        navSettings.setOnClickListener {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}