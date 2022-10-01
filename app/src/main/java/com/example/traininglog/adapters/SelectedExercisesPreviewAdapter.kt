package com.example.traininglog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.databinding.SelectedExercisePreviewItemBinding

class SelectedExercisesPreviewAdapter(private val exercises: ArrayList<ExerciseEntity>): RecyclerView.Adapter<SelectedExercisesPreviewAdapter.MySelectedExercisesViewHolder>() {

    inner class MySelectedExercisesViewHolder(binding: SelectedExercisePreviewItemBinding): RecyclerView.ViewHolder(binding.root){
        val exerciseName = binding.tvSelectedExerciseName
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MySelectedExercisesViewHolder {
        val view = SelectedExercisePreviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySelectedExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MySelectedExercisesViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = "${position + 1}. ${exercise.exerciseName}"
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}