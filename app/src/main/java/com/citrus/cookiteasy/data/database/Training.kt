package com.citrus.cookiteasy.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter

// Тренировка
@Entity(tableName = "trainings")
data class Training(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "training_id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val title: String,

    @ColumnInfo(name = "type")
    val type: String, // "Набор массы", "Похудение" и т.д.

    @ColumnInfo(name = "duration_minutes")
    val duration: Int,

    @ColumnInfo(name = "difficulty_level")
    val difficulty: Int, // 1-5

    @ColumnInfo(name = "location")
    val location: String, // "зал", "дом", "улица"

    @ColumnInfo(name = "estimated_calories")
    val calories: Int,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "rest_between_exercises_sec")
    val restBetweenExercises: Int?,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "target_muscles")
    val targetMuscles: String? ,// Например: "ноги,спина,пресс"

    @ColumnInfo(name = "exercises")
    val exercises: List<Int>
)

// Упражнение
@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val title: String,

    @ColumnInfo(name = "sets")
    val sets: Int,

    @ColumnInfo(name = "reps")
    val reps: Int?,

    @ColumnInfo(name = "duration_sec")
    val durationSec: Int?,

    @ColumnInfo(name = "rest_after_sec")
    val restAfterSec: Int?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "location")
    val location: String?, // "зал", "дом", "улица"

    val imageUrl: String?
)

// Инвентарь
@Entity(tableName = "equipment")
data class Equipment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "equipment_id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val title: String
)
