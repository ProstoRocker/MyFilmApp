package com.ilyadev.moviesearch.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.data.model.Reminder
import com.ilyadev.moviesearch.databinding.ItemReminderBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Адаптер для списка напоминаний ("Посмотреть позже").
 *
 * Отображает:
 * - Название фильма
 * - Время напоминания
 * - Кнопки: Редактировать / Отменить
 *
 * Использует ListAdapter + DiffUtil для эффективного обновления.
 */
class ReminderAdapter(
    private val onEdit: (Reminder) -> Unit,
    private val onCancel: (Reminder) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reminder: Reminder) {
            binding.tvMovieTitle.text = reminder.movieTitle
            binding.tvReminderTime.text = formatTime(reminder.reminderTimeMillis)

            binding.btnEdit.setOnClickListener { onEdit(reminder) }
            binding.btnCancel.setOnClickListener { onCancel(reminder) }
        }

        private fun formatTime(millis: Long): String {
            val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
            return sdf.format(Date(millis))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(old: Reminder, new: Reminder): Boolean =
            old.movieId == new.movieId

        override fun areContentsTheSame(old: Reminder, new: Reminder): Boolean = old == new
    }
}