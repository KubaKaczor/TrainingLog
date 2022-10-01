package com.example.traininglog.database

import androidx.room.Embedded
import androidx.room.Relation

data class PartWithExercises(
    @Embedded val part: PartEntity,
    @Relation(
        parentColumn = "partId",
        entityColumn = "bodyPartId"
    )
    val exercises: List<ExerciseEntity>
)
