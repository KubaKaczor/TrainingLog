package com.example.traininglog.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.R
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.databinding.ActiveExerciseItemBinding

class StartedExercisesAdapter(private val exercises: List<ExerciseEntity>, val context: Context, val exercisePosition: Int): RecyclerView.Adapter<StartedExercisesAdapter.MyStartedExercisesViewHolder>() {

    var onClickListener: OnClickListener? = null

    inner class MyStartedExercisesViewHolder(binding: ActiveExerciseItemBinding): RecyclerView.ViewHolder(binding.root){
        val exerciseName = binding.tvActiveExerciseName
        val ivOpenExercise = binding.ivOpenExercise
        val ivFinishedExercise = binding.ivFinishedExercise
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyStartedExercisesViewHolder {
        val view = ActiveExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyStartedExercisesViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyStartedExercisesViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.exerciseName.text = exercise.exerciseName

        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener!!.onClick(exercise, holder.adapterPosition)
            }
        }

        if(position == exercisePosition){
            holder.ivFinishedExercise.visibility = View.GONE
            holder.exerciseName.setBackgroundColor(context.getColor(R.color.white))
            holder.ivOpenExercise.setBackgroundColor(context.getColor(R.color.white))
        }


        if(position < exercisePosition){
            holder.ivFinishedExercise.visibility = View.VISIBLE
            holder.exerciseName.setBackgroundColor(context.getColor(R.color.light_green))
            holder.ivFinishedExercise.setBackgroundColor(context.getColor(R.color.light_green))
            holder.ivFinishedExercise.setColorFilter(context.getColor(R.color.snackbar_complete_color))
            holder.ivOpenExercise.setBackgroundColor(context.getColor(R.color.light_green))
        }

        if(position > exercisePosition){
            holder.exerciseName.setBackgroundColor(context.getColor(R.color.gray))
            holder.ivFinishedExercise.setBackgroundColor(context.getColor(R.color.gray))
            holder.ivOpenExercise.setBackgroundColor(context.getColor(R.color.gray))
            holder.ivFinishedExercise.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    interface OnClickListener{
        fun onClick(exercise: ExerciseEntity, position: Int)
    }
}