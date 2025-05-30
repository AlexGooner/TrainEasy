package com.citrus.cookiteasy.data.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrainingRepository(private val context: Context) {
    fun getKnowledgeBase(): KnowledgeBase {
        val json = context.assets.open("initial_data.json")
            .bufferedReader().use { it.readText() }
        return Gson().fromJson(json, KnowledgeBase::class.java)
    }

    fun getTrainings(): List<Training> {
        return getKnowledgeBase().trainings.map { it.toDomainModel() }
    }

    fun getExercisesForTraining(trainingId: Int): List<Exercise> {
        val knowledge = getKnowledgeBase()
        val training = knowledge.trainings.find { it.id == trainingId }
        return knowledge.exercises
            .filter { exercise -> training?.exercises?.contains(exercise.id) == true }
            .map { it.toDomainModel() }
    }
}