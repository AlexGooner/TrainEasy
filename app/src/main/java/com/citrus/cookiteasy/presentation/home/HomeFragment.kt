package com.citrus.cookiteasy.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Goal
import com.citrus.cookiteasy.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE

        navController = findNavController()


        val goals = listOf(
            Goal("Набор массы", R.drawable.ic_muscle),
            Goal("Похудение", R.drawable.ic_weight_loss),
            Goal("Поддержание формы", R.drawable.ic_fitness),
            Goal("Увеличение выносливости", R.drawable.ic_endurance),
            Goal("Увеличение силы", R.drawable.ic_strength),
            Goal("Гибкость и растяжка", R.drawable.ic_flexibility),
            Goal("Функциональный тренинг", R.drawable.ic_crossfit),
            Goal("Беговые тренировки", R.drawable.ic_running),
            Goal("Реабилитация после травм", R.drawable.ic_recovery),
            Goal("Улучшение осанки", R.drawable.ic_posture)
        )

        val adapter = GoalAdapter(goals, navController)

        binding.categoryRecyclerView.adapter = adapter


    }

}