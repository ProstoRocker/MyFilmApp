pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Включить (если используете libs.versions.toml)
// enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "MovieSearch"

// Подключаем модуль приложения
include(":app")