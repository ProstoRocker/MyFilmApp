package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.shared.MovieAdapterVertical

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        // Создаём адаптер
        val adapter = MovieAdapterVertical { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        // Показываем все фильмы
        adapter.submitList(MovieRepository.getAllMovies())
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.adapter = adapter

        // Получаем SearchView из MainActivity
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view)

        // Настраиваем фильтрацию
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty()
                val filtered = if (query.isEmpty()) {
                    MovieRepository.getAllMovies()
                } else {
                    MovieRepository.getAllMovies()
                        .filter { movie ->
                            movie.title.contains(query, ignoreCase = true) ||
                                    movie.genre.contains(query, ignoreCase = true)
                        }
                }
                adapter.submitList(filtered)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}