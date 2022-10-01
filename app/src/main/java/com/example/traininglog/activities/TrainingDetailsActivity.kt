package com.example.traininglog.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.ExercisesListAdapter
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.database.TrainingWithExercises
import com.example.traininglog.databinding.ActivityTrainingDetailsBinding
import kotlinx.coroutines.launch

class TrainingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingDetailsBinding

    private var trainingDetails: TrainingWithExercises? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.EXERCISE_DETAILS)){
            trainingDetails = intent.getParcelableExtra(Constants.EXERCISE_DETAILS)!!
        }

        setUpActionBar()

        if(trainingDetails != null){
            loadTrainingDetails()
        }

        binding.btnStartTraining.setOnClickListener {
            val intent = Intent(this, StartedTrainingActivity::class.java)
            intent.putExtra(Constants.EXERCISE_DETAILS, trainingDetails)
            startActivity(intent)
        }
    }

    private fun setUpActionBar(){
        val toolbar = binding.toolbarTrainingDetails

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadTrainingDetails(){

        binding.tvNameTrainingDetails.text = trainingDetails!!.training.trainingName
        binding.tvDayTrainingDetails.text = trainingDetails!!.training.dayOfWeek

        val adapter = ExercisesListAdapter(trainingDetails!!.exercises)

        binding.rvExercisesTrainingDetails.layoutManager = LinearLayoutManager(this)
        binding.rvExercisesTrainingDetails.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.training_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.details_edit ->{
                val intent = Intent(this, AddTrainingActivity::class.java)
                intent.putExtra(Constants.EXERCISE_DETAILS, trainingDetails)
                startActivity(intent)
                return true
            }
            R.id.details_delete ->{
                showDeleteDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog(){
        //val trainingWithExercisesDao = (application as TrainingLogApp).db.trainingWithExercisesDao()
        val trainingDao = (application as TrainingLogApp).db.trainingDao()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Na pewno chcesz usunąć trening?")
            .setPositiveButton("Tak",
                DialogInterface.OnClickListener { _, _ ->
                    lifecycleScope.launch {
                        trainingDao.delete(trainingDetails!!.training)
                        Toast.makeText(this@TrainingDetailsActivity, "Usunięto trening", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                })
            .setNegativeButton("Nie",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        val alertDialog = builder.create()
        alertDialog.show()
    }
}