package com.citrus.cookiteasy.data.database

import androidx.room.Entity

@Entity(primaryKeys = ["recipeId","ingredientId"])
data class RecipeIngredient(
    val recipeId: Long,
    val ingredientId: Long,
    val amount: String
)