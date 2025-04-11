package com.citrus.cookiteasy.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: Int, // в минутах
    val difficulty: String, // "легко", "средне", "сложно"
    val category: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "ingredients_json") val ingredientsJson: String, // JSON списка ингредиентов с количествами
    @ColumnInfo(name = "steps_json") val stepsJson: String
) {

    data class RecipeIngredient(
        val ingredientId: Int,
        val amount: Float,
        val name: String = "",
        val unit: String = "г"
    )

}

data class NutritionFacts(
    val calories: Int,
    val proteins: Float,
    val fats: Float,
    val carbs: Float
)