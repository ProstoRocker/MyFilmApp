package com.ilyadev.moviesearch.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object PosterDownloader {

    private val client = OkHttpClient()

    /**
     * Загружает постер по URL и сохраняет в галерею через MediaStore (Android 10+)
     *
     * @param context Контекст Activity/Fragment
     * @param posterUrl URL постера (например, "https://image.tmdb.org/t/p/w500/...")
     * @param title Название фильма (для имени файла)
     * @return Result<Uri> — URI сохранённого файла или ошибка
     */
    suspend fun downloadAndSaveToGallery(
        context: Context,
        posterUrl: String,
        title: String
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            // 1. Загрузка байтов
            val request = Request.Builder().url(posterUrl).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("HTTP ${response.code}"))
            }

            val bytes = response.body?.bytes()
                ?: return@withContext Result.failure(IOException("Empty response"))

            // 2. Подготовка имени файла
            val fileName = "${title.replace("[^a-zA-Z0-9]", "")}_${System.currentTimeMillis()}.jpg"
            val mimeType = "image/jpeg"

            // 3. Сохранение через MediaStore
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/MovieSearch")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return@withContext Result.failure(IOException("Failed to insert into MediaStore"))

            // 4. Запись данных
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(bytes)
            } ?: return@withContext Result.failure(IOException("Cannot open output stream"))

            // 5. Снимаем флаг IS_PENDING (Android Q+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            }

            Result.success(uri)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}