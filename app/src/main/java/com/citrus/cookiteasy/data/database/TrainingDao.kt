package com.citrus.cookiteasy.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {
    @Insert
    suspend fun insert(training: Training): Long

    @Query("SELECT * FROM trainings")
    fun getAllTrainings(): Flow<List<Training>>

    @Query("DELETE FROM trainings")
    suspend fun deleteAll()

}

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Query("SELECT * FROM exercises WHERE location = :location")
    suspend fun getExercisesByLocation(location: String): List<Exercise>

    @Query("DELETE FROM exercises")
    suspend fun deleteAll()
}

@Dao
interface EquipmentDao {
    @Insert
    suspend fun insert(equipment: Equipment): Long

    @Query("SELECT * FROM equipment WHERE equipment_id = :id")
    suspend fun getById(id: Int): Equipment?

    @Query("DELETE FROM equipment")
    suspend fun deleteAll()
}

