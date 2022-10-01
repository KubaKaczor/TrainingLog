package com.example.traininglog.database

import android.app.Application

class TrainingLogApp: Application() {
    val db by lazy {
        LocalDatabase.getInstance(this)
    }
}