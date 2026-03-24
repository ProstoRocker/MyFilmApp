package com.ilyadev.moviesearch.ui.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ilyadev.moviesearch.databinding.FragmentCollectionsBinding
import com.ilyadev.moviesearch.utils.circularReveal
import kotlin.math.hypot

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
            view.post {
                binding.root.circularReveal(800)
            }
            isRevealed = true
        }
    }

    private fun createCircularReveal(view: View) {
        val cx = view.width / 2
        val cy = view.height / 2
        val finalRadius = hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        anim.duration = 800

        view.alpha = 0f
        view.visibility = View.VISIBLE

        anim.start()
        view.animate().alpha(1f).setDuration(600).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}