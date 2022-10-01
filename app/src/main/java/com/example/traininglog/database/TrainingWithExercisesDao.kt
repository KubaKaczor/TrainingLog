package com.example.traininglog.database

import androidx.room.*

@Dao
interface TrainingWithExercisesDao {

    @Insert
    suspend fun insert(trainingWithExercises: TrainingExercisesCrossRef)

    @Update
    suspend fun update(trainingWithExercises: TrainingExercisesCrossRef)

    @Delete
    suspend fun delete(trainingWithExercises: TrainingExercisesCrossRef)

}