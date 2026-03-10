package com.ilyadev.moviesearch.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.ilyadev.moviesearch.data.repository.MovieRepository
import com.ilyadev.moviesearch.R
import com.ilyadev.moviesearch.databinding.FragmentHomeBinding
import com.ilyadev.moviesearch.detail.DetailActivity
import com.ilyadev.moviesearch.shared.MovieAdapterVertical
import kotlin.math.hypot

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var isRevealed = false

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

        Log.d("HomeFragment", "Фильмов: ${MovieRepository.getAllMovies().size}")

        // 🔥 Плавное появление экрана через Circular Reveal
        if (!isRevealed) {
            view.post {
                createCircularReveal(binding.root)
            }
            isRevealed = true
        }

        // Настройка списка фильмов
        val adapter = MovieAdapterVertical { movie ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }

        adapter.submitList(MovieRepository.getAllMovies())
        binding.recyclerMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        // Подключаем поиск
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.orEmpty()
                val filtered = MovieRepository.getAllMovies()
                    .filter { it.title.contains(query, ignoreCase = true) }
                adapter.submitList(filtered)
                return true
            }
        })
    }

    /**
     * Круговая анимация появления экрана
     */
    private fun createCircularReveal(view: View) {
        val cx = view.width / 2      // центр X
        val cy = view.height / 2     // центр Y
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // Начинаем с нуля
        val reveal = ViewAnimationUtils.createCircularReveal(
            view,
            cx,
            cy,
            0f,
            finalRadius
        )

        view.alpha = 0f
        view.visibility = View.VISIBLE

        reveal.duration = 800
        reveal.start()

        // Одновременно плавно показываем весь контент
        view.animate().alpha(1f).setDuration(600).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}