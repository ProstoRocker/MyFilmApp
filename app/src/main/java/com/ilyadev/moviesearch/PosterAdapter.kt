package com.ilyadev.moviesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.ItemPosterBinding

class PosterAdapter(private val posters: List<Int>) : RecyclerView.Adapter<PosterAdapter.ViewHolder>() {

    // Внутренний класс, хранящий ссылки на View одного элемента
    inner class ViewHolder(val binding: ItemPosterBinding) : RecyclerView.ViewHolder(binding.root)

    // Вызывается, когда RecyclerView создаёт новый элемент
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPosterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Вызывается, чтобы заполнить данные в одном элементе
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val posterResId = posters[position]
        holder.binding.posterImage.setImageResource(posterResId)

        // Анимация при нажатии (пример)
        holder.binding.root.setOnClickListener {
            holder.binding.posterImage.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .withEndAction {
                    holder.binding.posterImage.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .start()
                }
                .start()
        }
    }

    // Сколько всего элементов?
    override fun getItemCount(): Int = posters.size
}