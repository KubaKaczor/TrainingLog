package com.example.traininglog.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.R
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.databinding.SelectExerciseItemBinding

class SelectExerciseListAdapter(val exercises: List<ExerciseEntity>, val context: Context, private val selectedExercises: ArrayList<ExerciseEntity>): RecyclerView.Adapter<SelectExerciseListAdapter.MySelectExerciseViewHolder>() {

    var onClickListener: OnClickListener? = null

    inner class MySelectExerciseViewHolder(binding: SelectExerciseItemBinding): RecyclerView.ViewHolder(binding.root){
        val exerciseName = binding.tvExerciseName
        val ivSelected = binding.ivSelectedExercise
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectExerciseViewHolder {
        val view = SelectExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySelectExerciseViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MySelectExerciseViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.exerciseName

        holder.itemView.isSelected = selectedExercises.contains(exercise)

        holder.itemView.setOnClickListener {
            if(!holder.itemView.isSelected) {
                holder.itemView.isSelected = true
                holder.exerciseName.setBackgroundColor(context.getColor(R.color.light_yellow))
                holder.ivSelected.setColorFilter(context.getColor(R.color.snackbar_complete_color))
                holder.ivSelected.setBackgroundColor(context.getColor(R.color.light_yellow))
            }
            else{
                holder.itemView.isSelected = false
                holder.exerciseName.setBackgroundColor(context.getColor(R.color.white))
                holder.ivSelected.setColorFilter(context.getColor(R.color.gray))
                holder.ivSelected.setBackgroundColor(context.getColor(R.color.white))
            }
            onClickListener?.onClick(exercise)
        }

        if(holder.itemView.isSelected) {
            holder.exerciseName.setBackgroundColor(context.getColor(R.color.light_yellow))
            holder.ivSelected.setColorFilter(context.getColor(R.color.snackbar_complete_color))
            holder.ivSelected.setBackgroundColor(context.getColor(R.color.light_yellow))
        }else{
            holder.exerciseName.setBackgroundColor(context.getColor(R.color.white))
            holder.ivSelected.setColorFilter(context.getColor(R.color.gray))
            holder.ivSelected.setBackgroundColor(context.getColor(R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }


    interface OnClickListener{
        fun onClick(exercise: ExerciseEntity)
    }
}