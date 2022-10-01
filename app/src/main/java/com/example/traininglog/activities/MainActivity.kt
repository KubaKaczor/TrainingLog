package com.example.traininglog.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.traininglog.R
import com.example.traininglog.database.PartEntity
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_history, R.id.navigation_training, R.id.navigation_exercises
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //addBodyParts()
    }

    private fun addBodyParts(){
        val partDao = (application as TrainingLogApp).db.partDao()

        val backPart = PartEntity(name = "Plecy", icon = "back")
        val chestPart = PartEntity(name = "Klatka piersiowa", icon = "chest")
        val shoulderPart = PartEntity(name = "Barki", icon = "shoulder")
        val armPart = PartEntity(name = "Ramiona", icon = "arms")
        val absPart = PartEntity(name = "Brzuch", icon = "abs")
        val legsPart = PartEntity(name = "Nogi", icon = "legs")

        lifecycleScope.launch {
            partDao.insert(backPart, chestPart, shoulderPart, armPart, absPart, legsPart)
        }
    }
}