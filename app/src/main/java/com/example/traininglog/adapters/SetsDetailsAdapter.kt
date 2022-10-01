package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.ExerciseSetItemBinding
import com.example.traininglog.databinding.HistoryExerciseItemBinding

class SetsDetailsAdapter(private val sets: List<SetEntity>): RecyclerView.Adapter<SetsDetailsAdapter.MyFinishedSetsViewHolder>() {

    inner class MyFinishedSetsViewHolder(binding: ExerciseSetItemBinding): RecyclerView.ViewHolder(binding.root){
        val etReps = binding.etReps
        val etWeight = binding.etWeight
        val ivDelete = binding.ivDeleteSet
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFinishedSetsViewHolder {
        val view = ExerciseSetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFinishedSetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyFinishedSetsViewHolder, position: Int) {
        val set = sets[position]

        holder.ivDelete.visibility = View.GONE

        holder.etReps.setText(set.reps.toString())
        holder.etWeight.setText(set.weight.toString())
    }

    override fun getItemCount(): Int {
        return sets.size
    }
}