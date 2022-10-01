package com.example.traininglog.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.ExerciseSetsAdapter
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.ActivityCurrentExerciseBinding

class CurrentExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurrentExerciseBinding

    private var mExercise: ExerciseEntity? = null

    private var exerciseSets: ArrayList<SetEntity> = ArrayList()

    private var hideMenu = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.EXERCISE_DETAILS)){
            mExercise = intent.getParcelableExtra(Constants.EXERCISE_DETAILS)
        }

        if(intent.hasExtra(Constants.EXERCISE_SETS)){
            exerciseSets = intent.getParcelableArrayListExtra(Constants.EXERCISE_SETS)!!
        }

        if(intent.hasExtra(Constants.HIDE_MENU)){
            hideMenu = intent.getBooleanExtra(Constants.HIDE_MENU, false)
        }
        invalidateOptionsMenu()

        setUpActionBar()
        loadExerciseDetails()
    }

    private fun setUpActionBar(){
        val toolbar = binding.toolbarCurrentExercise

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadExerciseDetails(){
        binding.tvCurrentExerciseName.text = mExercise!!.exerciseName

        if(exerciseSets.isEmpty()){
            val set = SetEntity(exerciseName = mExercise!!.exerciseName)
            exerciseSets.add(set)
        }

        val adapter = ExerciseSetsAdapter(exerciseSets)
        binding.rvActiveSets.layoutManager = LinearLayoutManager(this)
        binding.rvActiveSets.adapter = adapter

        exerciseSets = adapter.sets
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.current_exercise_menu, menu)
        if(hideMenu)
            menu?.getItem(0)?.isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.current_exercise_finish ->{
                intent.putExtra(Constants.EXERCISE_SETS, exerciseSets)

                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}