package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
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

        // Настройка RecyclerView
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())

        // Создаём адаптер с обработкой клика
        adapter = MoviePagingAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.recyclerMovies.adapter = adapter

        // Инициализация ViewModel
        viewModel = PagingHomeViewModel()

        // Загрузка данных через Paging
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    // Скрываем индикатор после первой загрузки
                    binding.progressBar.visibility = View.GONE
                    adapter.submitData(pagingData)
                }
            }
        }

        // Анимация появления экрана (если нужно)
        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}