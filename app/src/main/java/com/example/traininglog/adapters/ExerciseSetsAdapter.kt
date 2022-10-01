package com.example.traininglog.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.SetEntity
import com.example.traininglog.databinding.ExerciseSetItemBinding

class ExerciseSetsAdapter(var sets: ArrayList<SetEntity>): RecyclerView.Adapter<ExerciseSetsAdapter.MySetsViewHolder>() {

    inner class MySetsViewHolder(binding: ExerciseSetItemBinding): RecyclerView.ViewHolder(binding.root){
        val btnAddSet = binding.btnAddSet
        val weight = binding.etWeight
        val reps = binding.etReps
        val deleteSet = binding.ivDeleteSet

        fun setListener(){
            weight.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    var weight = 0F
                    if(text!!.isNotEmpty())
                        weight = text!!.toString().toFloat()
                    //listener.onWeightChange(sets[adapterPosition], weight)

                    sets[adapterPosition].weight = weight

                }
            })

            reps.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(text: Editable?) {
                    var reps = 0
                    if(text!!.isNotEmpty())
                        reps = text!!.toString().toInt()
                    //listener.onRepsChange(sets[adapterPosition], reps)

                    sets[adapterPosition].reps = reps

                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySetsViewHolder {
        val view = ExerciseSetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val myViewHolder = MySetsViewHolder(view)
        myViewHolder.setListener()
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MySetsViewHolder, position: Int) {
        val set = sets[holder.adapterPosition]
        val exerciseName = sets[0].exerciseName

        if(set.weight > 0F)
            holder.weight.setText("${set.weight}")
        else{
            holder.weight.setText("")
        }

        if(set.reps > 0)
            holder.reps.setText("${set.reps}")
        else{
            holder.reps.setText("")
        }

        if(position == 0)
            holder.deleteSet.visibility = View.GONE
        else{
            holder.deleteSet.visibility = View.VISIBLE
        }

        if(position == sets.size - 1 )
            holder.btnAddSet.visibility = View.VISIBLE
        else
            holder.btnAddSet.visibility = View.GONE

        holder.btnAddSet.setOnClickListener {

            val newSet = SetEntity(0, exerciseName)
            sets.add(newSet)
            notifyDataSetChanged()
        }

        holder.deleteSet.setOnClickListener {
            sets.remove(set)
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
        return sets.size
    }

    interface OnTextChange{
        fun onWeightChange(set: SetEntity, weight: Float)
        fun onRepsChange(set: SetEntity, reps: Int)
    }

}