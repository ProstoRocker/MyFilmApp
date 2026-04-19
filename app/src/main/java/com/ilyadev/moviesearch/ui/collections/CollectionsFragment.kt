package com.ilyadev.moviesearch.ui.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilyadev.moviesearch.databinding.FragmentCollectionsBinding
import com.ilyadev.moviesearch.utils.circularReveal

/**
 * Экран "Подборки" — демонстрационный.
 *
 * Сейчас показывает только заголовок с анимацией.
 * В будущем можно добавить:
 * - Категории: "По жанрам", "По рейтингу", "Классика"
 * - Карусель фильмов
 *
 * Использует circularReveal для плавного появления.
 */
class CollectionsFragment : Fragment() {

    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!

    private var isRevealed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = "Подборки"

        if (!isRevealed) {
            view.post { binding.root.circularReveal(800) }
            isRevealed = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}