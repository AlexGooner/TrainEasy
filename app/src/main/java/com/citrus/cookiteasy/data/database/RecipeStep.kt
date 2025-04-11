package com.citrus.cookiteasy.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RecipeStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String? = null
)