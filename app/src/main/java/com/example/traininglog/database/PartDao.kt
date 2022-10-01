package com.example.traininglog.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PartDao {

    @Insert
    suspend fun insert(vararg partEntity: PartEntity)

    @Update
    suspend fun update(partEntity: PartEntity)

    @Delete
    suspend fun delete(partEntity: PartEntity)

    @Query("SELECT * from parts")
    fun getPartsList(): Flow<List<PartEntity>>
}