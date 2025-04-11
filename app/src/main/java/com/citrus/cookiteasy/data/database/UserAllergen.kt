package com.citrus.cookiteasy.data.database

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "allergenId"])
data class UserAllergen(
    val userId: Long,
    val allergenId: Long
)