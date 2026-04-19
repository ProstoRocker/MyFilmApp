package com.ilyadev.moviesearch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilyadev.moviesearch.databinding.FragmentSearchBinding
import com.ilyadev.moviesearch.shared.MoviePagingAdapter
import com.ilyadev.moviesearch.utils.circularReveal
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Экран поиска.
 *
 * Поддерживает:
 * - Live search (с debounce 300ms)
 * - Бесконечную прокрутку (Paging 3)
 * - Анимацию появления
 *
 * Поиск — реактивный: при каждом изменении текста создаётся новый поток.
 */

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: MoviePagingAdapter
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // === ViewModel ===
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        // === RecyclerView ===
        adapter = MoviePagingAdapter(
            onClick = { movie ->
                // Перейти к деталям фильма
            },
            onLongClick = { movie ->
                // Сохранить постер — как в HomeFragment
            }
        )
        binding.recyclerResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerResults.adapter = adapter

        // === Поиск по мере ввода (debounce 300ms) ===
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                // Задержка 300 мс перед запросом
                kotlinx.coroutines.delay(300)
                if (text != null && text.length >= 2) {
                    // Подписка на новый поток
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.searchResults(text.toString()).collectLatest { pagingData ->
                            adapter.submitData(pagingData)
                        }
                    }
                } else {
                    adapter.submitData(PagingData.empty())
                }
            }
        }

        // === Обработка состояний ===
        binding.recyclerResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                val lastVisible = layoutManager?.findLastVisibleItemPosition() ?: 0
                val total = layoutManager?.itemCount ?: 0
                if (lastVisible == total - 1) {
                    // Бесконечная прокрутка — Paging 3 делает это автоматически через nextKey
                }
            }
        })

        // === Анимация появления ===
        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchJob?.cancel()
    }
}