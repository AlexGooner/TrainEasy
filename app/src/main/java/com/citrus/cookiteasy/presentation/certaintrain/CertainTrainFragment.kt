package com.citrus.cookiteasy.presentation.certaintrain

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.citrus.cookiteasy.R
import com.citrus.cookiteasy.data.database.Exercise
import com.citrus.cookiteasy.data.database.TrainingRepository
import com.citrus.cookiteasy.databinding.FragmentCertainTrainBinding
import com.citrus.cookiteasy.presentation.ColorsFromRes
import com.google.android.material.bottomnavigation.BottomNavigationView


class CertainTrainFragment : Fragment() {
    private lateinit var binding: FragmentCertainTrainBinding
    private lateinit var repository: TrainingRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCertainTrainBinding.inflate(inflater, container, false)
        repository = TrainingRepository(requireContext())
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trainingId = arguments?.getInt("trainingId") ?: run {
            Log.e("CertainTrain", "trainingId is null!")
            return
        }
        val difficulty = arguments?.getInt("difficulty")

        if (difficulty != null) {
            if (difficulty <= 2) {
                binding.certainTrainDifficultyFirstBtn.setBackgroundColor(ColorsFromRes.green)
            } else if (difficulty.toInt() > 2 && difficulty.toInt() <= 4) {
                binding.certainTrainDifficultyFirstBtn.setBackgroundColor(ColorsFromRes.yellow)
                binding.difficultySecondBtn.setBackgroundColor(ColorsFromRes.yellow)
            } else {
                binding.certainTrainDifficultyFirstBtn.setBackgroundColor(ColorsFromRes.red)
                binding.difficultySecondBtn.setBackgroundColor(ColorsFromRes.red)
                binding.difficultyThirdBtn.setBackgroundColor(ColorsFromRes.red)
            }
        }

        Log.d("CertainTrain", "Loading training ID: $trainingId")

        // Загружаем тренировку
        val training = repository.getTrainings().find { it.id == trainingId }
        if (training == null) {
            Log.e("CertainTrain", "Training not found for ID: $trainingId")
            return
        }

        // Загружаем упражнения
        val exercises = repository.getExercisesForTraining(trainingId)
        Log.d("CertainTrain", "Exercises loaded: ${exercises.size}")

        // Обновляем UI
        binding.certainTrainTitleTextView.text = training.title
        binding.certainTrainLocationTextView.text = training.location
        binding.certainTrainDurationTextView.text = "${training.duration} мин"
        binding.certainTrainCaloriesTextView.text = "${training.calories} ккал"
        binding.certainTrainDescriptionTextView.text = training.description

        // Настройка RecyclerView
        val adapter = ExerciseAdapter(exercises)
        binding.recyclerExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerExercises.adapter = adapter
    }
}