package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.ExerciseEntity
import com.example.traininglog.database.PartWithExercises
import com.example.traininglog.databinding.PartExercisesItemBinding

class BodyPartsWithExercisesListAdapter(val exercises: List<PartWithExercises>, private val selectedExercises: ArrayList<ExerciseEntity>,val context: Context): RecyclerView.Adapter<BodyPartsWithExercisesListAdapter.MyBodyPartExercisesViewHolder>() {

    var onSelectedExerciseListener: OnSelectedExercise? = null

    inner class MyBodyPartExercisesViewHolder(binding: PartExercisesItemBinding): RecyclerView.ViewHolder(binding.root){
        val partName = binding.tvBodyPartName
        val rvExercises = binding.rvExercisesSublist
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBodyPartExercisesViewHolder {
        val view = PartExercisesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBodyPartExercisesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyBodyPartExercisesViewHolder, position: Int) {
        val model = exercises[position]

        holder.partName.text = model.part.name

        if(model.exercises.isNotEmpty()){

            val adapter = SelectExerciseListAdapter(model.exercises, context, selectedExercises)
            holder.rvExercises.layoutManager = LinearLayoutManager(context)
            holder.rvExercises.adapter = adapter

            adapter.onClickListener = object: SelectExerciseListAdapter.OnClickListener{
                override fun onClick(exercise: ExerciseEntity) {
                    onSelectedExerciseListener?.selectExercise(exercise)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    interface OnSelectedExercise{
        fun selectExercise(exercise: ExerciseEntity)
    }
}