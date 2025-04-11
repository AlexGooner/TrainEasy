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
import com.citrus.cookiteasy.data.database.CategoryItem
import com.citrus.cookiteasy.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
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

        navController= findNavController()

        val categories = listOf(
            CategoryItem(R.drawable.breakfast, "Завтраки"),
            CategoryItem(R.drawable.lunch, "Обеды"),
            CategoryItem(R.drawable.dinner, "Ужины"),
            CategoryItem(R.drawable.brunch, "Бранчи"),
            CategoryItem(R.drawable.vegan, "Веганские"),
            CategoryItem(R.drawable.nonalcohol, "Напитки"),
            CategoryItem(R.drawable.alcohol, "Коктейли"),
            CategoryItem(R.drawable.hotdrink, "Горячие напитки")
        )
        val adapter = CategoryAdapter()

        binding.categoryRecyclerView.adapter = adapter
        adapter.submitList(categories)

    }

}