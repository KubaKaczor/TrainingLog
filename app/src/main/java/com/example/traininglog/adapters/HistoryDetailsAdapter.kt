package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.FinishedSetItemBinding
import com.example.traininglog.databinding.HistoryExerciseItemBinding

class HistoryDetailsAdapter(private val finishedExercises: Map<String, List<SetEntity>>, private val context: Context): RecyclerView.Adapter<HistoryDetailsAdapter.MyFinishedSetsViewHolder>() {

    inner class MyFinishedSetsViewHolder(binding: HistoryExerciseItemBinding): RecyclerView.ViewHolder(binding.root){
        val exerciseName = binding.tvExerciseName
        val rvSets = binding.rvExercisesSublist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFinishedSetsViewHolder {
        val view = HistoryExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFinishedSetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyFinishedSetsViewHolder, position: Int) {
        val exercise = finishedExercises.keys.elementAt(position)
        val sets = finishedExercises.getValue(exercise)

        holder.exerciseName.text = exercise

        val adapter = SetsDetailsAdapter(sets)
        holder.rvSets.layoutManager = LinearLayoutManager(context)
        holder.rvSets.adapter = adapter


    }

    override fun getItemCount(): Int {
        return finishedExercises.size
    }
}