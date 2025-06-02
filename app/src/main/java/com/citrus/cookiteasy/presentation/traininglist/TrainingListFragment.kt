package com.citrus.cookiteasy.presentation.traininglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Training
import com.citrus.cookiteasy.data.database.TrainingRepository
import com.citrus.cookiteasy.databinding.FragmentTrainingListBinding
import com.citrus.cookiteasy.presentation.InformationAboutTrains
import com.google.android.material.bottomnavigation.BottomNavigationView


class TrainingListFragment : Fragment() {

    private var _binding: FragmentTrainingListBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var repository: TrainingRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrainingListBinding.inflate(inflater)
        repository = TrainingRepository(requireContext())
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE



        navController = findNavController()


        val title = arguments?.getString("title")

        binding.trainListTitleTextView.text = title

        when (title) {
            "Набор массы" -> binding.trainListInformationTextView.text = InformationAboutTrains.gainingMass
            "Похудение" -> binding.trainListInformationTextView.text = InformationAboutTrains.weightLoss
            "Поддержание формы" -> binding.trainListInformationTextView.text = InformationAboutTrains.keepingFit
            "Увеличение выносливости" -> binding.trainListInformationTextView.text = InformationAboutTrains.increasedStamina
            "Увеличение силы" -> binding.trainListInformationTextView.text = InformationAboutTrains.increasedStrengh
            "Гибкость и растяжка" -> binding.trainListInformationTextView.text = InformationAboutTrains.flexibilityAndStretching
            "Функциональный тренинг" -> binding.trainListInformationTextView.text = InformationAboutTrains.functionalTraining
            "Беговые тренировки" -> binding.trainListInformationTextView.text = InformationAboutTrains.runningTraining
            "Реабилитация после травм" -> binding.trainListInformationTextView.text = InformationAboutTrains.rehabilitationAfterInjuries
            "Улучшение осанки" -> binding.trainListInformationTextView.text = InformationAboutTrains.improvedPosture
        }

        val trainings = repository.getTrainings()
        val adapter = TrainingListAdapter(trainings, navController)
        binding.trainListRecyclerView.adapter = adapter



    }
}