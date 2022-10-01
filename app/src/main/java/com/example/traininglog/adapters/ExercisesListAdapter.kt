package com.example.traininglog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.databinding.ExerciseItemBinding

class ExercisesListAdapter(private val exercises: List<ExerciseEntity>): RecyclerView.Adapter<ExercisesListAdapter.MyExerciseViewHolder>() {

    inner class MyExerciseViewHolder(binding: ExerciseItemBinding): RecyclerView.ViewHolder(binding.root){
        val tvExerciseName = binding.tvExerciseName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExerciseViewHolder {
        val view = ExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyExerciseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.tvExerciseName.text = exercise.exerciseName
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun findExercise(position: Int): ExerciseEntity{
        val exercise = exercises[position]
        return exercise
    }

}