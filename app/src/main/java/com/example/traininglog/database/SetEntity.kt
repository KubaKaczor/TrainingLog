package com.example.traininglog.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sets")
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,
    val exerciseName: String,
    var weight: Float = 0F,
    var reps: Int = 0,
    var trainingId: Long = 0
) : Parcelable
