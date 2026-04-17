package com.ilyadev.moviesearch.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyadev.moviesearch.data.repository.MovieRepository
import com.ilyadev.moviesearch.databinding.FragmentFavoritesBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.shared.MovieAdapterVertical

/**
 * Экран "Избранное".
 *
 * Показывает фильмы, добавленные пользователем.
 * Использует мок-репозиторий MovieRepository.
 *
 * В продакшене:
 * - Данные берутся из Room
 * - Состояние isFavorite хранится в БД
 */
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAdapterVertical { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        val favorites = MovieRepository.favorites
        adapter.submitList(favorites)

        binding.recyclerFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}