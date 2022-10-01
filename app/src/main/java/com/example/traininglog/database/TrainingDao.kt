package com.example.traininglog.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {

    @Insert
    suspend fun insert(trainingEntity: TrainingEntity): Long

    @Update
    suspend fun update(trainingEntity: TrainingEntity)

    @Delete
    suspend fun delete(trainingEntity: TrainingEntity)

    @Transaction
    @Query("SELECT * FROM trainings")
    fun getTrainingsWithExercises(): Flow<List<TrainingWithExercises>>
}