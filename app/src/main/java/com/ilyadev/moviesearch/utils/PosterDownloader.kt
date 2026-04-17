package com.ilyadev.moviesearch.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

object PosterDownloader {

    private val client = OkHttpClient()

    /**
     * Асинхронно загружает постер и сохраняет в галерею.
     *
     * @param context Activity
     * @param posterUrl URL постера
     * @param title Название фильма (для имени файла)
     * @return Result<Uri> — URI сохранённого файла
     */
    suspend fun downloadAndSaveToGallery(
        context: Context,
        posterUrl: String,
        title: String
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(posterUrl).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("HTTP ${response.code}")

            val bytes = response.body?.bytes() ?: throw Exception("Empty body")

            // Создаём файл
            val fileName = "${title.replace("[^a-zA-Z0-9]", "_")}_${System.currentTimeMillis()}.jpg"
            val file = File(context.getExternalFilesDir(null), fileName)

            FileOutputStream(file).use { it.write(bytes) }

            // Получаем безопасный URI
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            Result.success(uri)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}