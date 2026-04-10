package com.ilyadev.moviesearch.di

import android.app.Application

class AppApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // Передаём контекст через .create(), так как Application — это Context
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}