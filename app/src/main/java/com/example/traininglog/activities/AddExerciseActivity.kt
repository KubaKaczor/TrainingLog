package com.example.traininglog.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyorganizer.utils.SwipeToDeleteCallback
import com.example.moneyorganizer.utils.SwipeToEditCallback
import com.example.traininglog.Constants
import com.example.traininglog.R
import com.example.traininglog.adapters.ExercisesListAdapter
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.database.TrainingLogApp
import com.example.traininglog.databinding.ActivityAddExerciseBinding
import com.example.traininglog.databinding.DialogAddExerciseBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExerciseBinding

    private var mPartId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.BODY_PART_ID)){

            mPartId = intent.getIntExtra(Constants.BODY_PART_ID, -1)

            if(mPartId != -1){
                loadExercisesList()
            }
        }

        if(intent.hasExtra(Constants.PART_NAME)){
            val partName = intent.getStringExtra(Constants.PART_NAME)
            setUpActionBar(partName!!)
        }

        binding.fabAddExercise.setOnClickListener {
            showAddExerciseDialog()
        }

    }

    private fun setUpActionBar(partName: String){

        val toolbar = binding.toolbarExercises

        if(toolbar != null){
            setSupportActionBar(toolbar)
            toolbar.title = "Ćwiczenia: ${partName}"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(getDrawable(R.drawable.ic_baseline_arrow_back_24))

            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun loadExercisesList(){

        val exerciseDao = (application as TrainingLogApp).db.exerciseDao()

        lifecycleScope.launch {

            exerciseDao.getExercisesByPart(mPartId).collect{

                val exercises = it

                if(exercises.isNotEmpty()){
                    binding.rvExerciseList.visibility = View.VISIBLE
                    binding.tvNoExercises.visibility = View.GONE

                    val adapter = ExercisesListAdapter(exercises)
                    binding.rvExerciseList.layoutManager = LinearLayoutManager(this@AddExerciseActivity)
                    binding.rvExerciseList.adapter = adapter

                }else{
                    binding.rvExerciseList.visibility = View.GONE
                    binding.tvNoExercises.visibility = View.VISIBLE
                }
            }
        }

        val editSwipeHandler = object: SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvExerciseList.adapter as ExercisesListAdapter
                val exercise = adapter.findExercise(viewHolder.adapterPosition)

                showAddExerciseDialog(exercise)
                //this.clearView(binding.rvHistory, viewHolder)
                loadExercisesList()

            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvExerciseList)


        val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvExerciseList.adapter as ExercisesListAdapter
                val exercise = adapter.findExercise(viewHolder.adapterPosition)

                val exerciseDao = (application as TrainingLogApp).db.exerciseDao()
                lifecycleScope.launch {

                    exerciseDao.delete(exercise)
                    showCompleteSnackBar("Pomyślnie usunięto")
                }
            }
        }

        val deleteItemTouchHandler = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHandler.attachToRecyclerView(binding.rvExerciseList)
    }

    private fun showAddExerciseDialog(exercise: ExerciseEntity? = null){
        val dialog = Dialog(this)

        val dialogBinding = DialogAddExerciseBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialogBinding.btnAddExercise.setOnClickListener {
            val exerciseName = dialogBinding.etExerciseName.text.toString()
            if(exercise == null)
                addExercise(exerciseName, dialog)
            else
                editExercise(exercise, exerciseName, dialog)
        }

        //editing

        if(exercise != null){
            dialogBinding.etExerciseName.setText(exercise.exerciseName)
            dialogBinding.btnAddExercise.setText("Edytuj")
        }

        dialog.show()
    }

    private fun addExercise(exerciseName: String, dialog: Dialog){

        if(TextUtils.isEmpty(exerciseName)){
            showErrorSnackBar("Podaj nazwę")
        }else{
            val exerciseDao = (application as TrainingLogApp).db.exerciseDao()

            lifecycleScope.launch {
                val exercise = ExerciseEntity(exerciseName = exerciseName, bodyPartId = mPartId)
                exerciseDao.insert(exercise)
                showCompleteSnackBar("Pomyślnie dodano")
                dialog.dismiss()
            }
        }
    }

    private fun editExercise(exercise: ExerciseEntity, exerciseName: String, dialog: Dialog){

        if(TextUtils.isEmpty(exerciseName)){
            showErrorSnackBar("Podaj nazwę")
        }else{
            val exerciseDao = (application as TrainingLogApp).db.exerciseDao()

            lifecycleScope.launch {
                val exercise = ExerciseEntity(id = exercise.id ,exerciseName = exerciseName, bodyPartId = mPartId)
                exerciseDao.update(exercise)
                showCompleteSnackBar("Pomyślnie edytowano")
                dialog.dismiss()
            }
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