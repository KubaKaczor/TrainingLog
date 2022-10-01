package com.example.traininglog.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingWithExercises(
    @Embedded val training: TrainingEntity,
    @Relation(
        parentColumn = "trainingId",
        entityColumn = "id",
        associateBy = Junction(TrainingExercisesCrossRef::class)
    )
    val exercises: List<ExerciseEntity>
): Parcelable

@Entity(primaryKeys = ["trainingId", "id"])
data class TrainingExercisesCrossRef(
    val trainingId: Long,
    val id: Int
)
