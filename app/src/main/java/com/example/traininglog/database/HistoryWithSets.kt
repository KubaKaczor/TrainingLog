package com.example.traininglog.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryWithSets(
    @Embedded val historyTraining: FinishedTrainingEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "trainingId"
    )
    val sets: List<SetEntity>
): Parcelable
