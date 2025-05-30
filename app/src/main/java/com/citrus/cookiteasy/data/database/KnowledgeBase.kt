package com.citrus.cookiteasy.data.database

data class KnowledgeBase(
    val equipment: List<EquipmentJson>,
    val exercises: List<ExerciseJson>,
    val trainings: List<TrainingJson>
)