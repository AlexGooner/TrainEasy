package com.citrus.cookiteasy.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @ColumnInfo(name = "calories_per_100g") val caloriesPer100g: Int,
    @ColumnInfo(name = "proteins_per_100g") val proteinsPer100g: Float,
    @ColumnInfo(name = "fats_per_100g") val fatsPer100g: Float,
    @ColumnInfo(name = "carbs_per_100g") val carbsPer100g: Float,
    val type: String,
    @ColumnInfo(name = "allergen_ids") val allergenIds: List<Int> = emptyList()
)

