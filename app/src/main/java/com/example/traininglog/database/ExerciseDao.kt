package com.example.traininglog.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert
    suspend fun insert(exercise: ExerciseEntity)

    @Update
    suspend fun update(exercise: ExerciseEntity)

    @Delete
    suspend fun delete(exercise: ExerciseEntity)

    @Query("SELECT * from exercises where bodyPartId=:partId")
    fun getExercisesByPart(partId: Int): Flow<List<ExerciseEntity>>

    @Transaction
    @Query("SELECT * FROM parts")
    fun getPartsWithExercises(): Flow<List<PartWithExercises>>
}