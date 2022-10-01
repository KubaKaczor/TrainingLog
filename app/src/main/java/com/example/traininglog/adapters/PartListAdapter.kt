package com.example.traininglog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traininglog.database.PartEntity
import com.example.traininglog.databinding.ExerciseCategoryItemBinding

class PartListAdapter(val partsList: List<PartEntity>, val context: Context): RecyclerView.Adapter<PartListAdapter.PartViewHolder>() {

    var onClickListener: OnClickListener? = null

    inner class PartViewHolder(binding: ExerciseCategoryItemBinding): RecyclerView.ViewHolder(binding.root){
        val name = binding.tvPartName
        val icon = binding.ivPartIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = ExerciseCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        val part = partsList[position]

        holder.name.text = part.name
        val resId = context.resources.getIdentifier("${part.icon}", "drawable", context.packageName)
        holder.icon.setImageResource(resId)

        if(onClickListener != null){
            holder.itemView.setOnClickListener {
                onClickListener!!.onClick(part.partId, part.name)
            }
        }
    }

    override fun getItemCount(): Int {
        return partsList.size
    }

    interface OnClickListener{
        fun onClick(partId: Int, partName: String)
    }

}