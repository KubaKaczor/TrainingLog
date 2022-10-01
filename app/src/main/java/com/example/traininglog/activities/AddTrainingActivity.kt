package com.example.traininglog.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.BodyPartsWithExercisesListAdapter
import com.example.traininglog.adapters.SelectedExercisesPreviewAdapter
import com.example.traininglog.database.*
import com.example.traininglog.databinding.ActivityAddTrainingBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTrainingBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var mDayOfWeek: String = Constants.days[0]

    private var selectedExercises: ArrayList<ExerciseEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerOnActivityExercisesForResult()
        loadData()

        binding.ivAddExercise.setOnClickListener {
            val intent = Intent(this, ExercisesListActivity::class.java)
            intent.putParcelableArrayListExtra(Constants.SELECTED_EXERCISES, selectedExercises)
            resultLauncher.launch(intent)
        }
    }

    private fun registerOnActivityExercisesForResult(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedExercises = result.data?.getParcelableArrayListExtra(Constants.SELECTED_EXERCISES)!!
                loadExerciseList()
            }
        }
    }

    private fun setUpActionBar(){
        val toolbar = binding.toolbarAddTraining

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadData(){

        setUpActionBar()
        loadExerciseList()
        loadSpinner()
    }

    private fun loadExerciseList(){

        if(selectedExercises.isNotEmpty()){

            val adapter = SelectedExercisesPreviewAdapter(selectedExercises)

            binding.rvAddedExercises.layoutManager = LinearLayoutManager(this@AddTrainingActivity)
            binding.rvAddedExercises.adapter = adapter

            binding.rvAddedExercises.visibility = View.VISIBLE
            binding.noAddedExercises.visibility = View.GONE
        }else{
            binding.rvAddedExercises.visibility = View.GONE
            binding.noAddedExercises.visibility = View.VISIBLE
        }
    }

    private fun loadSpinner(){
        val dropdown = binding.spinnerDay

        val days = Constants.days
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        dropdown.adapter = adapter
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mDayOfWeek = days[position]
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_training_menu, menu!!)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addTrainingItem ->{
                addTraining()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addTraining(){

        val trainingDao = (application as TrainingLogApp).db.trainingDao()
        val trainingExercisesDao = (application as TrainingLogApp).db.trainingWithExercisesDao()

        val trainingName = binding.etTrainingName.text.toString()

        if(validateForm(trainingName, selectedExercises.size)){

            lifecycleScope.launch {
                val training = TrainingEntity(trainingName = trainingName, dayOfWeek = mDayOfWeek)
                val trainingId = trainingDao.insert(training)

                for(exercise in selectedExercises){
                    val trainingWithExercises = TrainingExercisesCrossRef(trainingId = trainingId, id = exercise.id)
                    trainingExercisesDao.insert(trainingWithExercises)
                }

                showCompleteSnackBar("Dodano trening")
                finish()
            }
        }

    }

    private fun validateForm(trainingName: String, listSize: Int): Boolean{
        return when {
            TextUtils.isEmpty(trainingName) -> {
                showErrorSnackBar("Podaj nazwę treningu")
                false
            }
            listSize == 0 -> {
                showErrorSnackBar("Wybierz ćwiczenia")
                false
            }
            else -> true
        }

    }

    fun showErrorSnackBar(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))

        snackBar.show()
    }

    fun showCompleteSnackBar(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_complete_color))

        snackBar.show()
    }
}