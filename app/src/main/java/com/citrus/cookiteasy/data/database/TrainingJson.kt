package com.citrus.cookiteasy.data.database

data class TrainingJson(
    val id: Int,
    val title: String,
    val type: String,
    val duration: Int,
    val difficulty: Int,
    val location: String,
    val calories: Int,
    val description: String,
    val restBetweenExercises: Int,
    val targetMuscles: List<String>,
    val exercises: List<Int>
) {
    fun toDomainModel() = Training(
        id = id,
        title = title,
        type = type,
        duration = duration,
        difficulty = difficulty,
        location = location,
        calories = calories,
        description = description,
        restBetweenExercises = restBetweenExercises,
        isFavorite = false,
        targetMuscles = targetMuscles.joinToString(","),
        exercises = exercises
    )
}

data class ExerciseJson(
    val id: Int,
    val name: String,
    val sets: Int,
    val reps: Int,
    val durationSec: Int,
    val restAfterSec: Int,
    val description: String,
    val location: List<String>,
    val imageUrl: String?
) {
    fun toDomainModel() = Exercise(
        id = id,
        title = name,
        sets = sets,
        reps = reps,
        durationSec = durationSec,
        restAfterSec = restAfterSec,
        description = description,
        location = location.joinToString(","),
        imageUrl = imageUrl
    )
}

data class EquipmentJson(
    val id: Int,
    val title: String
) {
    fun toDomainModel() = Equipment(
        id = id,
        title = title
    )
}