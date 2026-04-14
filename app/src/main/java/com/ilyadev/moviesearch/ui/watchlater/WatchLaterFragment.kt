package com.ilyadev.moviesearch.ui.watchlater

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ilyadev.moviesearch.data.model.Reminder
import com.ilyadev.moviesearch.data.repository.ReminderRepository
import com.ilyadev.moviesearch.databinding.FragmentWatchLaterBinding
import com.ilyadev.moviesearch.shared.ReminderAdapter
import com.ilyadev.moviesearch.utils.circularReveal
import com.ilyadev.moviesearch.worker.MovieReminderWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class WatchLaterFragment : Fragment() {

    private var _binding: FragmentWatchLaterBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReminderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = "Посмотреть позже"

        adapter = ReminderAdapter(
            onEdit = { reminder -> editReminder(reminder) },
            onCancel = { reminder -> cancelReminder(reminder) }
        )
        binding.recyclerReminders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReminders.adapter = adapter

        // Подписка на изменения
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ReminderRepository.getAllRemindersFlow(requireContext())
                    .collectLatest { reminders ->
                        if (reminders.isEmpty()) {
                            binding.emptyText.visibility = View.VISIBLE
                            binding.recyclerReminders.visibility = View.GONE
                        } else {
                            binding.emptyText.visibility = View.GONE
                            binding.recyclerReminders.visibility = View.VISIBLE
                            adapter.submitList(reminders)
                        }
                    }
            }
        }

        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    private fun editReminder(reminder: Reminder) {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = reminder.reminderTimeMillis
        }

        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val newTimeMillis = calendar.timeInMillis

                if (newTimeMillis <= System.currentTimeMillis()) {
                    Toast.makeText(requireContext(), "Выберите будущее время", Toast.LENGTH_SHORT)
                        .show()
                    return@TimePickerDialog
                }

                // 🚀 Перепланировать напоминание
                lifecycleScope.launch {
                    // 1. Отменяем старую задачу
                    WorkManager.getInstance(requireContext())
                        .cancelUniqueWork("reminder_${reminder.movieId}")

                    // 2. Удаляем старую запись из DataStore
                    ReminderRepository.removeReminder(requireContext(), reminder.movieId)

                    // 3. Создаём новую запись с новым временем
                    val newReminder = Reminder(
                        id = Random.nextInt(),
                        movieId = reminder.movieId,
                        movieTitle = reminder.movieTitle,
                        reminderTimeMillis = newTimeMillis,
                        isScheduled = true
                    )
                    ReminderRepository.addReminder(requireContext(), newReminder)

                    // 4. Запускаем новую задачу
                    val data = Data.Builder()
                        .putInt("movie_id", reminder.movieId)
                        .putString("movie_title", reminder.movieTitle)
                        .build()

                    val workRequest = OneTimeWorkRequestBuilder<MovieReminderWorker>()
                        .setInitialDelay(
                            newTimeMillis - System.currentTimeMillis(),
                            TimeUnit.MILLISECONDS
                        )
                        .setInputData(data)
                        .addTag("reminder_${reminder.movieId}")
                        .build()

                    WorkManager.getInstance(requireContext())
                        .enqueueUniqueWork(
                            "reminder_${reminder.movieId}",
                            ExistingWorkPolicy.REPLACE,
                            workRequest
                        )

                    Toast.makeText(
                        requireContext(),
                        "✅ Время обновлено на ${formatTime(newTimeMillis)}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).also { dialog ->
            dialog.setTitle("Изменить время напоминания")
            dialog.show()
        }
    }

    private fun formatTime(millis: Long): String {
        val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    private fun cancelReminder(reminder: Reminder) {
        context?.let { ctx ->
            androidx.work.WorkManager.getInstance(ctx)
                .cancelUniqueWork("reminder_${reminder.movieId}")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            ReminderRepository.removeReminder(requireContext(), reminder.movieId)
            Toast.makeText(requireContext(), "Напоминание отменено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}