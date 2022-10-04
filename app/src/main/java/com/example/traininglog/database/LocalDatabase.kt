package com.example.traininglog.database

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.traininglog.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                        .addCallback(object: RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)

                                val backPart = PartEntity(name = "Plecy", icon = "back")
                                val chestPart = PartEntity(name = "Klatka piersiowa", icon = "chest")
                                val shoulderPart = PartEntity(name = "Barki", icon = "shoulder")
                                val armPart = PartEntity(name = "Ramiona", icon = "arms")
                                val absPart = PartEntity(name = "Brzuch", icon = "abs")
                                val legsPart = PartEntity(name = "Nogi", icon = "legs")

                                GlobalScope.launch {
                                    instance?.partDao()?.insert(backPart, chestPart, shoulderPart, armPart, absPart, legsPart)
                                }
                            }
                        })
                        .build()

                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}