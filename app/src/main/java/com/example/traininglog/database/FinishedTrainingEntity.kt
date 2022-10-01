package com.example.traininglog.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "histories")
data class FinishedTrainingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val trainingName: String,
    val note: String = "",
    val time: Long = 0L
): Parcelable

