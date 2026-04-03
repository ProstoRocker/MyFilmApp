package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.shared.MoviePagingAdapter
import com.ilyadev.moviesearch.utils.circularReveal
import com.ilyadev.moviesearch.di.AppApplication
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

        // Получаем AppComponent из Application
        val appComponent = (requireActivity().application as AppApplication).appComponent

        // Создаём фабрику для ViewModel
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                if (modelClass == PagingHomeViewModel::class.java) {
                    val apiService = appComponent.provideApiService()
                    return PagingHomeViewModel(apiService, requireContext()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // Создаём ViewModel
        viewModel = ViewModelProvider(this, factory)[PagingHomeViewModel::class.java]

        // Настройка RecyclerView
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        adapter = MoviePagingAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.recyclerMovies.adapter = adapter

        // ==============================
        // 🔹 Слушаем состояние загрузки (ProgressBar)
        // ==============================

        // Если вы используете LiveData isLoading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Если вы используете errorMessage
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }

        // ==============================
        // 🔹 Основная пагинация
        // ==============================

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        // ==============================
        // 🔹 Обработка ошибок пагинации + кэш
        // ==============================

        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    // При первом запуске — может быть Loading
                    binding.progressBar.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    val error = loadState.refresh as LoadState.Error
                    val message = error.error.message ?: "Не удалось загрузить фильмы"
                    // Показываем SnackBar с ошибкой
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()

                    // Дополнительно можно обновить состояние в ViewModel
                    // viewModel.onNetworkError(message)
                }

                is LoadState.NotLoading -> {
                    binding.progressBar.visibility = View.GONE
                }
            }

            // Если есть данные — скрываем emptyText
            val isLoading = loadState.source.refresh is LoadState.Loading
            val hasNoData = adapter.itemCount == 0 && !isLoading
            binding.emptyText.visibility = if (hasNoData) View.VISIBLE else View.GONE
        }

        // ==============================
        // 🔹 Анимация появления экрана
        // ==============================

        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}