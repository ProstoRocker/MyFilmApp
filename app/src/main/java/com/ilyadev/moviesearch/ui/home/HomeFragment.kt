package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.shared.MovieAdapterVertical
import com.ilyadev.moviesearch.model.Movie
import com.ilyadev.moviesearch.utils.circularReveal

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: MovieAdapterVertical

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

        // 1. Настройка RecyclerView
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        adapter = MovieAdapterVertical { movie ->

            //  Переход по клику на карточку
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.recyclerMovies.adapter = adapter

        // 2. Инициализация ViewModel
        viewModel = HomeViewModel()

        // 3. Подписка на данные — с безопасным доступом
        viewModel.setOnMoviesLoaded { movies ->
            safeBind {
                adapter.submitList(movies)
            }
        }

        viewModel.setOnLoadingChanged { isLoading ->
            safeBind {
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        // 4. Поиск
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.search(newText.orEmpty())
                return true
            }
        })

        // 5. Анимация
        binding.root.post {
            binding.root.circularReveal(800)
        }
    }

    private fun safeBind(block: FragmentHomeBinding.() -> Unit) {
        _binding?.let { binding ->
            binding.block()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}