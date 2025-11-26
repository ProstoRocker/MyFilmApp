package com.ilyadev.moviesearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ilyadev.moviesearch.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    // Приватное свойство для binding
    private var _binding: FragmentDetailBinding? = null
    // Публичный доступ к binding (будет падать, если вызвать после onDestroyView)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Инициализируем binding
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем ID фильма из аргументов
        val movieId = arguments?.getInt("movie_id") ?: run {
            // Если нет ID — выходим
            requireActivity().onBackPressed()
            return
        }

        // Находим фильм по ID
        val movie = createMockMovies().find { it.id == movieId }
        if (movie == null) {
            Snackbar.make(view, "Фильм не найден", Snackbar.LENGTH_LONG).show()
            requireActivity().onBackPressed()
            return
        }

        // Устанавливаем данные
        binding.collapsingToolbar.title = movie.title
        binding.ivBackdrop.setImageResource(movie.backdropResId)
        binding.tvTitle.text = movie.title
        binding.tvYear.text = movie.year
        binding.tvDescription.text = movie.description

        // Обработка кликов по кнопкам
        binding.btnFavorite.setOnClickListener { view ->
            Snackbar.make(view, "Добавлено в избранное", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnWatchLater.setOnClickListener { view ->
            Snackbar.make(view, "Добавлено в «Посмотреть позже»", Snackbar.LENGTH_SHORT).show()
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Смотри фильм: ${movie.title}")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Освобождаем ссылку на binding
        _binding = null
    }

    companion object {
        /**
         * Фабричный метод для создания фрагмента с аргументами
         */
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