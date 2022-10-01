package com.example.traininglog.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.StartedExercisesAdapter
import com.example.traininglog.database.*
import com.example.traininglog.databinding.ActivityStartedTrainingActivityBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class StartedTrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartedTrainingActivityBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var trainingDetails: TrainingWithExercises? = null

    private var mCurrentExercisePosition = 0

    private var setsList: ArrayList<SetEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartedTrainingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        registerOnActivityFinishExerciseForResult()

        if(intent.hasExtra(Constants.EXERCISE_DETAILS)){
            trainingDetails = intent.getParcelableExtra(Constants.EXERCISE_DETAILS)!!
        }

        setUpActionBar()
        loadTrainingDetails()

        binding.btnEndTraining.setOnClickListener {
            showFinishTrainingDialog()
        }

    }

    private fun setUpActionBar(){
        val toolbar = binding.toolbarTrainingDetails

        if(toolbar != null){
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

            toolbar.setNavigationOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Na pewno chcesz przerwać trening?")
                    .setPositiveButton("Tak",
                        DialogInterface.OnClickListener { _, _ ->
                            onBackPressed()
                        })
                    .setNegativeButton("Nie",
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.dismiss()
                        })
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }
    }

    private fun loadTrainingDetails(){

        binding.tvStartedTrainingName.text = trainingDetails!!.training.trainingName

        val adapter  = StartedExercisesAdapter(trainingDetails!!.exercises, this, mCurrentExercisePosition)
        binding.rvActiveSets.layoutManager = LinearLayoutManager(this)
        binding.rvActiveSets.adapter = adapter


        adapter.onClickListener = object: StartedExercisesAdapter.OnClickListener{
            override fun onClick(exercise: ExerciseEntity, position: Int) {
                val intent = Intent(this@StartedTrainingActivity, CurrentExerciseActivity::class.java)
                intent.putExtra(Constants.EXERCISE_DETAILS, exercise)

                var exerciseSets: ArrayList<SetEntity> = ArrayList()
                exerciseSets = setsList.filter { s ->s.exerciseName == exercise.exerciseName } as ArrayList<SetEntity>
                if(exerciseSets.isNotEmpty())
                    intent.putExtra(Constants.EXERCISE_SETS, exerciseSets)

                if(position < mCurrentExercisePosition){
                    intent.putExtra(Constants.HIDE_MENU, true)
                }
                resultLauncher.launch(intent)
            }
        }
    }


    private fun registerOnActivityFinishExerciseForResult(){
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mCurrentExercisePosition++
                loadTrainingDetails()
                val exerciseSets: ArrayList<SetEntity> = result.data!!.getParcelableArrayListExtra(Constants.EXERCISE_SETS)!!
                //setsList.removeIf { s -> s.exerciseName == exerciseSets[0].exerciseName }
                setsList = (setsList + exerciseSets) as ArrayList<SetEntity>
            }
        }
    }

    private fun showFinishTrainingDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Na pewno chcesz zakończyć trening?")
            .setPositiveButton("Tak",
                DialogInterface.OnClickListener { _, _ ->
                    finishTraining()
                })
            .setNegativeButton("Nie",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun finishTraining(){
        val trainingName = binding.tvStartedTrainingName.text.toString()
        val time = binding.tvTrainingTime.text.toString()
        val note = binding.etNote.text.toString()
        val date = Calendar.getInstance().timeInMillis

        if(validateForm(trainingName, setsList.size)){
            val historyDao = (application as TrainingLogApp).db.historyDao()

            lifecycleScope.launch {

                val finishedTraining = FinishedTrainingEntity(date = date, trainingName = trainingName, note = note)
                val trainingId = historyDao.insert(finishedTraining)

                for(set in setsList){
                    set.trainingId = trainingId
                    historyDao.insert(set)
                }
                showCompleteSnackBar("Trening ukończony")
                finish()
            }
        }
    }

    private fun validateForm(name: String, setListSize: Int): Boolean{
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Podaj nazwę treningu")
                false
            }
            setListSize == 0 -> {
                showErrorSnackBar("Dodaj serie do treningu")
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