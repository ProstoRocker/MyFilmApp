package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.di.AppApplication
import com.ilyadev.moviesearch.shared.MoviePagingAdapter
import com.ilyadev.moviesearch.utils.circularReveal
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

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

        // === Создание ViewModel через Dagger 2 ===
        val appComponent = (requireActivity().application as AppApplication).appComponent
        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return when (modelClass) {
                    PagingHomeViewModel::class.java -> {
                        val apiService = appComponent.provideApiService()
                        PagingHomeViewModel(apiService, requireContext()) as T
                    }

                    else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
                }
            }
        }

        viewModel = ViewModelProvider(this, factory)[PagingHomeViewModel::class.java]

        // === Настройка RecyclerView ===
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())

        adapter = MoviePagingAdapter(
            onClick = { movie ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("movie_id", movie.id)
                startActivity(intent)
            },
            onLongClick = { movie ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val result = viewModel.downloadPosterSuspend(movie)
                        if (result.isSuccess) {
                            val uri = result.getOrNull()
                            if (uri != null) {
                                Snackbar.make(
                                    binding.root,
                                    "Постер сохранён в галерее!",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val e = result.exceptionOrNull()
                            val message = when {
                                e is IOException -> "Ошибка сети: не удалось загрузить постер"
                                e is SecurityException -> "Нет доступа к хранилищу"
                                else -> "Не удалось сохранить изображение"
                            }
                            Snackbar.make(
                                binding.root,
                                message,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Snackbar.make(
                            binding.root,
                            "Произошла ошибка при сохранении",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )

        binding.recyclerMovies.adapter = adapter

        // === Подписка на состояния UI ===
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }

        // === Пагинация ===
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        // === Обработка состояний пагинации ===
        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> binding.progressBar.isVisible = true
                is LoadState.Error -> {
                    binding.progressBar.isVisible = false
                    val error = (loadState.refresh as LoadState.Error).error
                    viewModel.postErrorMessage(error.message ?: "Ошибка загрузки фильмов")
                }

                is LoadState.NotLoading -> binding.progressBar.isVisible = false
            }

            val hasNoData =
                adapter.itemCount == 0 && loadState.source.refresh is LoadState.NotLoading
            binding.emptyText.isVisible = hasNoData
        }

        // === Анимация появления экрана ===
        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}