// === ПЛАГИНЫ: что собирает и как компилирует Gradle ===
plugins {
    alias(libs.plugins.android.application)  // Подключает Android-приложение (из libs.versions.toml)
    alias(libs.plugins.kotlin.android)     // Включает Kotlin для Android
    alias(libs.plugins.kotlin.kapt)        // Аннотационный процессор (для Dagger, Glide)
    alias(libs.plugins.google.gms.google.services) // Подключает Google Services (Firebase)
}

android {
    namespace = "com.ilyadev.moviesearch"  // Уникальный ID пакета
    compileSdk = 34                        // Версия SDK для компиляции

    // Основные настройки сборки
    defaultConfig {
        applicationId = "com.ilyadev.moviesearch"
        minSdk = 30                          // Минимальная поддерживаемая версия Android
        targetSdk = 34                       // Целевая версия Android
        versionCode = 1                      // Номер версии (внутренний)
        versionName = "1.0"                  // Отображаемое имя версии
    }

    // === FLAVORS: бесплатная и платная версии ===
    // Позволяет собирать два APK: free и paid
    flavorDimensions.add("version")
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"      // com.ilyadev.moviesearch.free
            versionNameSuffix = "-free"         // отображается как "1.0-free"
        }
        create("paid") {
            dimension = "version"
            applicationIdSuffix = ".paid"       // com.ilyadev.moviesearch.paid
            versionNameSuffix = "-paid"
        }
    }

    // Типы сборки
    buildTypes {
        getByName("debug") {
            isDebuggable = true               // Разрешает отладку
        }
        getByName("release") {
            isMinifyEnabled = false           // Не минифицируем (можно включить)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Настройки Java/Kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"                     // Bytecode для JVM 17
    }

    // Включенные фичи
    buildFeatures {
        viewBinding = true                   // Автогенерация ViewBinding
        dataBinding = true                  // Поддержка Data Binding
        buildConfig = true                  // Генерация BuildConfig (FLAVOR, DEBUG и т.д.)
    }
}

dependencies {

    // === 🔥 FIREBASE REMOTE CONFIG (динамический контент) ===
    // Используется для показа промо-экрана без обновления приложения
    implementation(platform("com.google.firebase:firebase-bom:33.0.0")) // BOM — управление версиями
    implementation("com.google.firebase:firebase-config-ktx")          // Remote Config

    // === 🧱 WORKMANAGER (отложенные задачи) ===
    // Для напоминаний "Посмотреть позже"
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // === 💾 DATASTORE (хранилище) ===
    // Хранит состояние: первый запуск, пробный период, настройки
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // === 🖼 PICASSO (загрузка изображений) ===
    // Альтернатива Glide, используется для загрузки постеров
    implementation("com.squareup.picasso:picasso:2.8")

    // === ⚙️ LIFECYCLE (жизненный цикл) ===
    // Для корректной работы с жизненным циклом Activity/Fragment
    implementation("androidx.lifecycle:lifecycle-runtime:2.8.5")
    implementation("androidx.lifecycle:lifecycle-process:2.8.5") // ProcessLifecycleOwner

    // === 🔄 RXJAVA 3 (реактивное программирование) ===
    // Работа с потоками данных: API → UI
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")

    // === 🟣 KOTLIN COROUTINES (асинхронность) ===
    // Современная альтернатива RxJava для коротких задач
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // === 🗄 ROOM (локальная БД) ===
    // Хранение фильмов, избранного, кэша
    val room_version = "2.7.0-alpha11"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")           // Kotlin-расширения
    implementation("androidx.room:room-rxjava3:$room_version")      // RxJava поддержка
    kapt("androidx.room:room-compiler:$room_version")                // Кодогенерация

    // === 🔌 DAGGER (внедрение зависимостей) ===
    // Управляет созданием объектов: Retrofit, Room, Repository
    implementation("com.google.dagger:dagger:2.52")
    kapt("com.google.dagger:dagger-compiler:2.52")

    // === 🌐 RETROFIT (сетевые запросы) ===
    // HTTP-клиент для взаимодействия с TMDB API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")     // JSON → объекты
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")   // RxJava адаптер
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Логирование запросов

    // === 🖼 GLIDE (загрузка и кэширование изображений) ===
    // Используется в DetailActivity
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")                // Кодогенерация

    // === 🌀 LIFECYCLE + VIEWMODEL + LIVE DATA ===
    // Архитектурные компоненты
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")

    // === 📄 PAGING 3 (постраничная загрузка) ===
    // Для бесконечной прокрутки списков (popular, top rated)
    implementation("androidx.paging:paging-runtime:3.2.1")
    implementation("androidx.paging:paging-common:3.2.1")

    // === 🛠 ОСТАЛЬНЫЕ ЗАВИСИМОСТИ ===
    implementation("androidx.core:core-ktx:1.13.1")                   // Kotlin расширения
    implementation("androidx.appcompat:appcompat:1.6.1")              // Совместимость
    implementation("com.google.android.material:material:1.12.0")      // Material Design
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Верстка
    implementation("androidx.recyclerview:recyclerview:1.3.2")          // Списки
    implementation("androidx.preference:preference:1.2.0")            // Настройки
    implementation("androidx.core:core-splashscreen:1.0.1")          // Современный splash screen
    implementation("io.coil-kt:coil:2.4.0")                           // Альтернатива Glide/Picasso
    implementation("com.google.code.gson:gson:2.13.2")               // JSON парсинг

    // Тестирование
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}