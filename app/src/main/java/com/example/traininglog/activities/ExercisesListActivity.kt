package com.example.traininglog.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.BodyPartsWithExercisesListAdapter
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.ActivityExercisesListBinding
import kotlinx.coroutines.launch

class ExercisesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExercisesListBinding

    var selectedExercises: ArrayList<ExerciseEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExercisesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.SELECTED_EXERCISES)){
            selectedExercises = intent.getParcelableArrayListExtra(Constants.SELECTED_EXERCISES)!!
        }
        setUpActionBar()
        loadExercises()
    }

    private fun setUpActionBar(){
        val toolbar = binding.toolbarExercisesList

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadExercises(){

        val exerciseDao = (application as TrainingLogApp).db.exerciseDao()

        lifecycleScope.launch {

            exerciseDao.getPartsWithExercises().collect{

                val exercises = it

                if(exercises.isNotEmpty()){

                    val adapter = BodyPartsWithExercisesListAdapter(exercises, selectedExercises,this@ExercisesListActivity)

                    binding.rvAllExercises.layoutManager = LinearLayoutManager(this@ExercisesListActivity)
                    binding.rvAllExercises.adapter = adapter

                    adapter.onSelectedExerciseListener = object: BodyPartsWithExercisesListAdapter.OnSelectedExercise{
                        override fun selectExercise(exercise: ExerciseEntity) {
                            if(selectedExercises.contains(exercise)) {
                                selectedExercises.remove(exercise)
                                binding.toolbarExercisesList.title = "Wybrano: ${selectedExercises.size}"
                            }
                            else {
                                selectedExercises.add(exercise)
                                binding.toolbarExercisesList.title = "Wybrano: ${selectedExercises.size}"
                            }
                        }
                    }

                    binding.rvAllExercises.visibility = View.VISIBLE
                    //binding.noAddedExercises.visibility = View.GONE
                }else{
                    //binding.rvAddedExercises.visibility = View.GONE
                    //binding.noAddedExercises.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.select_exercises_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addExerciseItem ->{
                intent.putParcelableArrayListExtra(Constants.SELECTED_EXERCISES, selectedExercises)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}