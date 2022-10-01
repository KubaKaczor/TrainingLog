package com.example.traininglog.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parts")
data class PartEntity(
    @PrimaryKey(autoGenerate = true)
    val partId: Int = 0,
    val name: String,
    val icon: String

)
