package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.HistoryWithSets
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.FragmentHistoryBinding
import com.example.traininglog.databinding.HistoryTrainingItemBinding
import java.text.SimpleDateFormat
import java.util.*

class FinishedTrainingsAdapter(private val histories: List<HistoryWithSets>, private val context: Context): RecyclerView.Adapter<FinishedTrainingsAdapter.MyHistoryViewHolder>() {

    var onClickListener: OnClick? = null

    inner class MyHistoryViewHolder(binding: HistoryTrainingItemBinding): RecyclerView.ViewHolder(binding.root){
        val trainingName = binding.tvTrainingName
        val date = binding.tvDate
        val rvFinishedExercises = binding.rvFinishedExercises

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHistoryViewHolder {
        val view = HistoryTrainingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyHistoryViewHolder, position: Int) {
        val training = histories[position]

        val date = getDateFromMils(training.historyTraining.date)

        holder.trainingName.text = training.historyTraining.trainingName
        holder.date.text = "Data treningu: ${date}"

        val list = training.sets.groupBy { s -> s.exerciseName }

        val adapter = FinishedSetsAdapter(list)

        holder.rvFinishedExercises.layoutManager = LinearLayoutManager(context)
        holder.rvFinishedExercises.adapter = adapter

        holder.itemView.setOnClickListener {
            if(onClickListener != null)
                onClickListener!!.onClick(training)
        }
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    private fun getDateFromMils(timeInMils: Long): String {

        val formatter = SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(Date(timeInMils))
    }

    interface OnClick{
        fun onClick(training: HistoryWithSets)
    }
}