package com.ilyadev.moviesearch

import androidx.activity.OnBackPressedCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка данных фильма
        val movieId = arguments?.getInt("movie_id") ?: return
        val movie = createMockMovies().find { it.id == movieId } ?: return

        binding.collapsingToolbar.title = movie.title
        binding.ivBackdrop.setImageResource(movie.backdropResId)
        binding.tvTitle.text = movie.title
        binding.tvYear.text = movie.year
        binding.tvDescription.text = movie.description

        // Кнопки
        binding.btnFavorite.setOnClickListener {
            Snackbar.make(it, "Добавлено в избранное", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnWatchLater.setOnClickListener {
            Snackbar.make(it, "Добавлено в «Посмотреть позже»", Snackbar.LENGTH_SHORT).show()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Метод  DetailFragment.newInstance(movie.id)
    companion object {
        fun newInstance(movieId: Int): DetailFragment {
            val args = Bundle().apply {
                putInt("movie_id", movieId)
            }
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Создаёт mock-данные о фильмах
     * В реальном приложении это будет репозиторий
     */
    private fun createMockMovies(): List<Movie> {
        return listOf(
            Movie(
                id = 1,
                title = "Матрица",
                year = "1999",
                rating = 8.7,
                genre = "Научная фантастика",
                posterResId = R.drawable.poster_matrix,
                backdropResId = R.drawable.backdrop_matrix,
                description = "Нео узнаёт, что реальность — это иллюзия, созданная машинами."
            ),
            Movie(
                id = 2,
                title = "Оппенгеймер",
                year = "2023",
                rating = 8.3,
                genre = "Биография, Драма, История",
                posterResId = R.drawable.poster_oppenheimer,
                backdropResId = R.drawable.backdrop_oppenheimer,
                description = "История Роберта Оппенгеймера, отца атомной бомбы, и моральных дилемм, связанных с научным прогрессом."
            ),
            Movie(
                id = 3,
                title = "Интерстеллар",
                year = "2014",
                rating = 8.6,
                genre = "Драма",
                posterResId = R.drawable.poster_interstellar,
                backdropResId = R.drawable.backdrop_interstellar,
                description = "Астронавты ищут новую планету для человечества."
            ),
            Movie(
                id = 4,
                title = "Тёмный рыцарь",
                year = "2008",
                rating = 9.0,
                genre = "Экшен",
                posterResId = R.drawable.poster_dark_knight,
                backdropResId = R.drawable.backdrop_dark_knight,
                description = "Бэтмен против Джокера в битве за душу Готэма."
            ),
            Movie(
                id = 5,
                title = "Форрест Гамп",
                year = "1994",
                rating = 8.8,
                genre = "Драма",
                posterResId = R.drawable.poster_forrest_gump,
                backdropResId = R.drawable.backdrop_forrest_gump,
                description = "Жизнь простого человека в эпоху великих перемен."
            ),
            Movie(
                id = 6,
                title = "Побег из Шоушенка",
                year = "1994",
                rating = 9.3,
                genre = "Драма",
                posterResId = R.drawable.poster_shawshank,
                backdropResId = R.drawable.backdrop_shawshank,
                description = "Надежда и дружба в тюрьме, лишённой свободы."
            ),
            Movie(
                id = 7,
                title = "Крёстный отец",
                year = "1972",
                rating = 9.2,
                genre = "Криминал",
                posterResId = R.drawable.poster_godfather,
                backdropResId = R.drawable.backdrop_godfather,
                description = "Семья мафии и путь превращения в крёстного отца."
            )
        )
    }
}