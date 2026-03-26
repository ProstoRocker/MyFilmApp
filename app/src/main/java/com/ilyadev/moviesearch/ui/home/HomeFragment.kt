package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.model.MovieDto
import com.ilyadev.moviesearch.shared.MoviePagingAdapter
import com.ilyadev.moviesearch.utils.circularReveal
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PagingHomeViewModel
    private lateinit var adapter: MoviePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        adapter = MoviePagingAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.recyclerMovies.adapter = adapter
        val testMovie = MovieDto(
            id = 1,
            title = "Тестовый фильм",
            releaseDate = "2024-01-01",
            rating = 8.5,
            genreIds = listOf(28),
            posterPath = null,
            backdropPath = null
        )

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.submitData(PagingData.from(listOf(testMovie)))
            }
        }

        viewModel = PagingHomeViewModel()

        // ✅ Корректный способ: collectLatest внутри launch + repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    // Убедимся, что адаптер и binding ещё существуют
                    if (isAdded && _binding != null) {
                        adapter.submitData(pagingData)
                    }
                }
            }
        }

        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    private fun safeBind(block: FragmentHomeBinding.() -> Unit) {
        _binding?.let { it.block() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}