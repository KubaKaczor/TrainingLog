package com.example.traininglog.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(historyTrainingEntity: FinishedTrainingEntity): Long

    @Insert
    suspend fun insert(sets: SetEntity)

    @Delete
    suspend fun delete(historyTrainingEntity: FinishedTrainingEntity)

    @Update
    suspend fun update(historyTrainingEntity: FinishedTrainingEntity)

    @Query("SELECT * FROM histories")
    fun getTrainingsHistory(): Flow<List<HistoryWithSets>>
}