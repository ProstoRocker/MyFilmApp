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
                    // Используем компонент Dagger для получения зависимостей
                    val apiService = appComponent.provideApiService()
                    return PagingHomeViewModel(apiService) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        // Создаём ViewModel через фабрику
        viewModel = ViewModelProvider(this, factory)[PagingHomeViewModel::class.java]

        // Настройка RecyclerView
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        adapter = MoviePagingAdapter { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.recyclerMovies.adapter = adapter

        // Обработка состояния загрузки
        adapter.addLoadStateListener { loadState ->
            val isLoading = loadState is LoadState.Loading
            val hasNoData = adapter.itemCount == 0 && !isLoading

            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.emptyText.visibility = if (hasNoData) View.VISIBLE else View.GONE
        }

        // Загрузка данных через Paging
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movies.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        // Анимация появления экрана
        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}