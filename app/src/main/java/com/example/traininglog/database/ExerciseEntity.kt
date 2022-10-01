package com.example.traininglog.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "exercises", foreignKeys = [
    ForeignKey(entity = PartEntity::class,
        parentColumns = ["partId"],
        childColumns = ["bodyPartId"],
        onDelete = ForeignKey.CASCADE
    )])
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseName: String,
    val bodyPartId: Int
): Parcelable
