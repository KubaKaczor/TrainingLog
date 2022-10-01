package com.example.traininglog.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.FinishedSetItemBinding

class FinishedSetsAdapter(private val finishedExercises: Map<String, List<SetEntity>>): RecyclerView.Adapter<FinishedSetsAdapter.MyFinishedSetsViewHolder>() {

    inner class MyFinishedSetsViewHolder(binding: FinishedSetItemBinding): RecyclerView.ViewHolder(binding.root){
        val llHeader = binding.llHeader
        val setsNumber = binding.tvSetsNumber
        val exerciseName = binding.tvExerciseName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFinishedSetsViewHolder {
        val view = FinishedSetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFinishedSetsViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyFinishedSetsViewHolder, position: Int) {
        if(position > 0)
            holder.llHeader.visibility = View.GONE

        val exercise = finishedExercises.keys.elementAt(position)
        val sets = finishedExercises.getValue(exercise)

        holder.setsNumber.text = "${sets.size} x"
        holder.exerciseName.text = exercise



    }

    override fun getItemCount(): Int {
        return finishedExercises.size
    }
}