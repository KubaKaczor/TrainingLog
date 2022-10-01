package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.database.TrainingWithExercises
import com.example.traininglog.databinding.TrainingItemBinding

class TrainingsListAdapter(private val trainings: List<TrainingWithExercises>, private val context: Context): RecyclerView.Adapter<TrainingsListAdapter.MyTrainingsViewHolder>() {

    var onClickListener: OnClickListener? = null

    inner class MyTrainingsViewHolder(binding: TrainingItemBinding): RecyclerView.ViewHolder(binding.root){

        val trainingName = binding.tvTrainingName
        val dayOfWeek = binding.tvDayOfWeek
        val rvExercises = binding.rvTrainingExercises
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTrainingsViewHolder {
        val view = TrainingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyTrainingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyTrainingsViewHolder, position: Int) {
        val training = trainings[position]

        holder.trainingName.text = training.training.trainingName
        holder.dayOfWeek.text = training.training.dayOfWeek

        val adapter = SelectedExercisesPreviewAdapter(training.exercises as ArrayList<ExerciseEntity>)
        holder.rvExercises.layoutManager = LinearLayoutManager(context)
        holder.rvExercises.adapter = adapter

        holder.itemView.setOnClickListener {
            if(onClickListener != null){
                onClickListener!!.onClick(training)
            }
        }

    }

    override fun getItemCount(): Int {
        return trainings.size
    }

    interface OnClickListener{
        fun onClick(training: TrainingWithExercises)
    }
}