package com.example.traininglog.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [
    PartEntity::class,
    ExerciseEntity::class,
    TrainingEntity::class,
    TrainingExercisesCrossRef::class,
    SetEntity::class,
    FinishedTrainingEntity::class
                      ], version = 4, exportSchema = true, autoMigrations = [AutoMigration (from = 3, to = 4)]
)
abstract  class LocalDatabase : RoomDatabase(){

    abstract fun partDao(): PartDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun trainingDao(): TrainingDao
    abstract fun trainingWithExercisesDao(): TrainingWithExercisesDao
    abstract fun historyDao(): HistoryDao

    companion object{

        @Volatile
        private var INSTANCE : LocalDatabase? = null

        fun getInstance(context : Context): LocalDatabase{

            synchronized(this){
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "workoutLog-database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}